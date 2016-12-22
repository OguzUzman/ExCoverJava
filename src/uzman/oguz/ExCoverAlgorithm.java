package uzman.oguz;

import uzman.oguz.quality.FScore;
import uzman.oguz.quality.QualityFunction;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static uzman.oguz.Matcher.findMatches;

/**
 * Created by Oguz Uzman on 18/11/2016.
 */
public class ExCoverAlgorithm {

    public static String FSCORE = "-f";
    Integer[] sortedAttributes = null;
    QualityFunction qualityFunction;
    /**
     * Positive and negative databases; kept in different files
     */
    private BitSet[] positiveTransactionDatabase, negativeTransactionDatabase;
    private int numOfAttributes = 0;

    /**
     * Candidate table L, as described in the paper
     * It should take the size of positive transaction database
     * An array of Lists
     */
    private L l;


    public ExCoverAlgorithm(String positiveFilePath, String negativeFilePath, String score) {

        readDatabasesFromFiles(positiveFilePath, negativeFilePath);

        //Choose the quality score
        if (score == FSCORE) {
            qualityFunction = new FScore();
        } else if (score.equals(FSCORE)) {
            qualityFunction = new FScore();
        } else {
            qualityFunction = new FScore();
        }

        calculateAndSortScoresForAttributes();

        /**
         * ExCover Lines 1, 2
         * Initial empty pattern covers all transactions so we define quality wrt that.
         */
        double emptyPatternQuality = qualityFunction.quality
                (positiveTransactionDatabase.length, negativeTransactionDatabase.length,
                        positiveTransactionDatabase.length, negativeTransactionDatabase.length);
        l = new L(positiveTransactionDatabase.length, emptyPatternQuality, numOfAttributes);

    }


    /**
     * Start algorithm
     */
    public void run(int nunOfThreads) {



        //If there are more threads than the number of attributes, set number of threads to number of attributes.
        nunOfThreads = nunOfThreads > numOfAttributes ? nunOfThreads : numOfAttributes;

        /**
         * Initial empty pattern ExCover 1-2
         */
        BitSet initialPattern = new BitSet(numOfAttributes);

        List<Integer> positiveT = new ArrayList<>();
        List<Integer> negativeT = new ArrayList<>();

        /**
         * Empty pattern matches all so we set all of them
         */
        for (int i = 0; i < positiveTransactionDatabase.length; i++) {
            positiveT.add(i);
        }
        for (int i = 0; i < negativeTransactionDatabase.length; i++) {
            negativeT.add(i);
        }
        startMultiThreaded(nunOfThreads, initialPattern, positiveT, negativeT);

        System.out.println("finished");

    }

    private void startMultiThreaded(int numOfThreads, BitSet patternX, List<Integer> positiveT,
                                    List<Integer> negativeT) {

        List<Integer> B = new ArrayList<>();

        int lastAddedCoreItemInOrder = -1;
        if (lastAddedCoreItemInOrder == -1) {
            for (int i = 0; i < numOfAttributes; i++) {
                B.add(i);
            }
        }


        int partitionSize = (int) Math.ceil(((double)B.size()) / ((double)numOfThreads));

        Thread[] threads = new Thread[numOfThreads];
        long[] times = new long[numOfAttributes];
        int[] totalLeafs = new int[numOfAttributes];
        Arrays.fill(totalLeafs, 0);

        ExecutorService taskExecutor = Executors.newFixedThreadPool(numOfThreads);

        for(int i = 0 ; i < numOfAttributes;i++){

            final int finalI = i;
             Runnable leafStart = new Runnable() {
                @Override
                public void run() {

                    Thread.currentThread().setName("ExCover thread: " + finalI);
                    int from = finalI;
                    int to = finalI;
                    long start = System.currentTimeMillis();
                    totalLeafs[finalI] = grow(patternX, positiveT, negativeT, -1, from, to);
                    long end = System.currentTimeMillis();
                    times[finalI] = end - start;
                    System.out.printf("Thread %d is finished in %d ms\n", finalI, end-start);
                }
            };
            taskExecutor.execute(leafStart);
        }

        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("There's a problem with multi threading.");
            e.printStackTrace();
        }



        for (int i = 0; i < numOfThreads; i++) {
            System.out.printf("Thread %d, with % d leafs, is finished in %d ms\n", i, totalLeafs[i], times[i]);
        }


        System.out.println("Threads finished");

    }

    /**
     * GROW function described in the paper's 11th page.
     *
     * @param patternX
     * @param positiveT                current matches for pattern x in positive T
     * @param negativeT
     * @param lastAddedCoreItemInOrder
     * @param maxInOrder Only used when lastAddedCoreItemInOrder == -1
     * @param minOrder Only used when lastAddedCoreItemInOrder == -1
     */
    private int grow(BitSet patternX, List<Integer> positiveT, List<Integer> negativeT, int lastAddedCoreItemInOrder,
                     int minOrder, int maxInOrder) {


        /**
         * Definition of B {x ∈ X |x not∈ x and patternX is a predecessor of the core item lastly added into x}
         * GROW Line 1
         */
        List<Integer> B = new ArrayList<>();

        if (lastAddedCoreItemInOrder == -1) {
            for (int i = minOrder; i <= maxInOrder && i < numOfAttributes; i++) {
                B.add(i);
            }
        } else {
            for (int i = 0; i < lastAddedCoreItemInOrder; i++) {
                if (patternX.get(sortedAttributes[i])) {
                    //x ∈ X
                } else {
                    B.add(i);
                }
            }
        }
        int total = 1;
        /**
         * Line 2
         */
        for (int i = 0; i < B.size(); i++) {

            /**
             * Line 3
             * Don't use 'i' ever again
             */
            int index = B.get(i);
            int sortedIndex = sortedAttributes[index];
            BitSet xPrime = (BitSet) patternX.clone();
            xPrime.set(sortedIndex);
            //System.out.println(xPrime);

            int lastAddedCoreItemInOrderXPrime = index;

            List<Integer> positiveMatchesXPrime = Matcher.findMatches(positiveTransactionDatabase, xPrime, positiveT,
                    numOfAttributes);
            double upperBound = qualityFunction.upperBound(positiveMatchesXPrime.size(), -1/* Irrelevant for fscore*/,
                    positiveTransactionDatabase.length, -1/* irrelevant*/);

            //Line 4 GROW
            if (l.line4GrowAlgorithm(xPrime, positiveMatchesXPrime, positiveTransactionDatabase, numOfAttributes, upperBound)) {
                continue;// pruned
            }

            //Line 5 GROW Close x to xClosed
            BitSet xClosed = Matcher.close(positiveMatchesXPrime, positiveTransactionDatabase, numOfAttributes);
            //System.out.println("Closed: ");
            //System.out.println(xClosed);
            //Line 6 GROW
            if (!preservesSuffix(xPrime, xClosed, lastAddedCoreItemInOrderXPrime)) {
                continue;
            }

            List<Integer> tStarPositive = positiveMatchesXPrime;
            List<Integer> tStarNegative =
                    findMatches(negativeTransactionDatabase, xClosed, negativeT, numOfAttributes);

            double qualityXStar = qualityFunction.quality(tStarPositive.size(), tStarNegative.size(),
                    positiveTransactionDatabase.length, negativeTransactionDatabase.length);

            double xStarGivenC = qualityFunction.probXGivenC(positiveTransactionDatabase, tStarPositive);

            double xStarGivenNotC = qualityFunction.probXGivenNotC(negativeTransactionDatabase, tStarNegative);

            if (xStarGivenC >= xStarGivenNotC) {
                synchronized (this) {
                    add(tStarPositive, xClosed, qualityXStar, numOfAttributes);
                }
            }
            total += grow(xClosed, tStarPositive, tStarNegative, lastAddedCoreItemInOrderXPrime, 0, 0);

        }
        return total;
    }

    private void add(List<Integer> positiveMatches, BitSet patternX, double qualityOfX, int numOfAttributes) {

        l.add(positiveMatches, patternX, qualityOfX, numOfAttributes);

    }

    /**
     * Read databases from given file paths
     *
     * @param positiveClassesPath
     * @param negativeClassesPath
     */
    private void readDatabasesFromFiles(String positiveClassesPath, String negativeClassesPath) {
        positiveTransactionDatabase = readDatabaseFromFile(positiveClassesPath);
        negativeTransactionDatabase = readDatabaseFromFile(negativeClassesPath);
    }


    /**
     * Read database from file and set the parameters for number of attributes and transactions.
     *
     * @param filePath
     * @return
     */
    private BitSet[] readDatabaseFromFile(String filePath) {

        File dataFile = new File(filePath);
        BitSet[] database = null;
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            //First line which consists of number of transactions and attributes.

            line = br.readLine();
            String[] nums = line.split(" ");


            //Get the number of attributes and positive db size
            numOfAttributes = Integer.valueOf(nums[1]);
            int numOfTransactions = Integer.valueOf(nums[0]);
            database = new BitSet[numOfTransactions];

            for (int row = 0; row < numOfTransactions; row++) {
                line = br.readLine();
                String[] attributes = line.split(" ");

                BitSet transaction = new BitSet(numOfAttributes);
                for (int attributeIndex = 0; attributeIndex < numOfAttributes; attributeIndex++) {
                    transaction.set(attributeIndex, attributes[attributeIndex].equals("1"));
                }
                database[row] = transaction;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return database;


    }


    /**
     * Will calculate all quality scores
     */
    private void calculateAndSortScoresForAttributes() {

        //Empty array for quality scores
        double[] qualities = new double[numOfAttributes];

        //
        for (int attribute = 0; attribute < numOfAttributes; attribute++) {
            BitSet pattern = new BitSet(numOfAttributes);
            pattern.set(attribute);
            double qualityForAttribute =
                    qualityFunction.quality(positiveTransactionDatabase, negativeTransactionDatabase, pattern,
                            numOfAttributes);
            qualities[attribute] = qualityForAttribute;
        }

        //Sort the attributes indices using the comparator
        ArrayIndexComparator comparator = new ArrayIndexComparator(qualities);
        sortedAttributes = comparator.createIndexArray();
        Arrays.sort(sortedAttributes, comparator);
    }

    /**
     * Means that a pattern can't have attributes which is earlier in the order through the closure.
     *
     * @param patternX
     * @param closedPatternX
     * @return
     */
    private boolean preservesSuffix(BitSet patternX, BitSet closedPatternX, int lastAddedCoreItemIndex) {
        BitSet closedAdds = Matcher.findExtraItemsInClosedForm(patternX, closedPatternX, numOfAttributes);

        for (int i = lastAddedCoreItemIndex + 1; i < numOfAttributes; i++)
            if (closedAdds.get(sortedAttributes[i]))
                return false;
        return true;
    }


    /**
     * Checks if all positive transactions are covered
     * @return
     */
    public boolean allPositivesAreCovered(){
        ArrayList<Integer>[] transactionPatternMapping = l.getTransactionPatternMapping();
        for (int i = 0; i < transactionPatternMapping.length; i++) {
            if(transactionPatternMapping[i].size() == 0){
                return false;
            }
        }
        return true;
    }


    public ArrayList<Integer>[] getTransactionPatternMapping() {
        return l.getTransactionPatternMapping();
    }

    public ArrayList<PatternQualityPair> getPatternQualityPairs() {
        return l.getPatternQualityPairs();
    }

    public List<PatternQualityPair> getOutputPatterns(){

        List<PatternQualityPair> outputPatterns = new ArrayList<>();

        ArrayList<PatternQualityPair> patternQualityPairs = getPatternQualityPairs();
        ArrayList<Integer>[] transactionPatternMapping = getTransactionPatternMapping();

        ArrayList<Integer> patternQualityIndices = new ArrayList<>();
        for (int i = 0; i < transactionPatternMapping.length; i++) {
            ArrayList<Integer> mapping = transactionPatternMapping[i];
            for (Integer integer : mapping) {
                patternQualityIndices.add(integer);
            }
        }
        Set<Integer> uniqKeys = new TreeSet<Integer>();
        uniqKeys.addAll(patternQualityIndices);

        Iterator iter = uniqKeys.iterator();
        while (iter.hasNext()) {
            outputPatterns.add(patternQualityPairs.get((Integer) iter.next()));
        }

        sortPatterns(outputPatterns);


        return outputPatterns;
    }

    /**
     * Sorts the patterns by their quality, first has the highest quality score
     * @param patternsToSort
     */
    private void sortPatterns(List<PatternQualityPair> patternsToSort){
        Collections.sort(patternsToSort, new Comparator<PatternQualityPair>() {
            @Override
            public int compare(PatternQualityPair o1, PatternQualityPair o2) {
                if(o1.getQuality() == o2.getQuality())
                    return 0;
                else if(o1.getQuality() > o2.getQuality())
                    return -1;
                return 1;
            }
        });
    }

    public BitSet[] getPositiveTransactionDatabase() {
        return positiveTransactionDatabase;
    }

    public BitSet[] getNegativeTransactionDatabase() {
        return negativeTransactionDatabase;
    }
}