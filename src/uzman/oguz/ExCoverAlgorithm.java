package uzman.oguz;

import uzman.oguz.quality.FScore;
import uzman.oguz.quality.QualityFunction;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

/**
 * Created by Oguz Uzman on 18/11/2016.
 */
public class ExCoverAlgorithm {

    /**
     * Positive and negative databases; kept in different files
     */
    private BitSet[] positiveTransactionDatabase, negativeTransactionDatabase;


    private int numOfAttributes = 0;

    public static String FSCORE = "-f";

    QualityFunction qualityFunction;

    /**
     * Candidate table L, as described in the paper
     * It should take the size of positive trasaction database
     * An array of Lists
     */
    private ArrayList<Integer>[] L ;

    public ExCoverAlgorithm(String positiveFilePath, String negativeFilePath, String score){

        readDatabasesFromFiles(positiveFilePath, negativeFilePath);

        //Choose the quality score
        if(score == FSCORE){
            qualityFunction = new FScore();
        } else if(score.equals(FSCORE)) {
            qualityFunction = new FScore();
        } else {
            qualityFunction = new FScore();
        }

        calculateAndSortScoresForAttributes();

    }


    /**
     * Start algorithm
     */
    public void run(){

        /**
         * ExCover Lines 1, 2
         */
        L = (ArrayList<Integer>[])new ArrayList[positiveTransactionDatabase.length];

        /**
         * Initial empty pattern ExCover 1-2
         */
        BitSet initialPattern = new BitSet(numOfAttributes);

    }

    /**
     * GROW function described in the paper's 11th page.
     */
    private void grow(BitSet patternX, BitSet[] positiveDataset, ArrayList<Integer> conditionalDatabaseForX){

    }

    /**
     * Finds matches for a pattern in a database
     * @param database the database, array of bitsets
     * @param patternX the pattern, an array of Bitsets
     * @return
     */
    private ArrayList<Integer> findMatchesForDatabase(BitSet[] database, BitSet patternX){
        return null;
    }

    /**
     * Finds matches for a pattern in a database, using a subpattern matches.
     * @param database the database, array of BitSets
     * @param pattern the patern, a BitSet
     * @param subPatternMatches, this can be used to speed up the search. A list of indices for the matches of a
     *                           subpattern of a pattrn
     * @return
     */
    private ArrayList<Integer> findMatchesForDatabaseFast(BitSet[] database, BitSet pattern, List<Integer> subPatternMatches){
        return null;
    }

    /**
     * Read databases from given file paths
     * @param positiveClassesPath
     * @param negativeClassesPath
     */
    private void readDatabasesFromFiles(String positiveClassesPath, String negativeClassesPath){
        positiveTransactionDatabase = readDatabaseFromFile(positiveClassesPath);
        negativeTransactionDatabase = readDatabaseFromFile(negativeClassesPath);
    }


    /**
     * Read database from file and set the parameters for number of attributes and transactions.
     * @param filePath
     * @return
     */
    private BitSet[] readDatabaseFromFile(String filePath){

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

            for(int row = 0; row < numOfTransactions; row++){
                line = br.readLine();
                String[] attributes = line.split(" ");

                BitSet transaction = new BitSet(numOfAttributes);
                for(int attributeIndex = 0; attributeIndex < numOfAttributes; attributeIndex++){
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
    private void calculateAndSortScoresForAttributes(){

        //Empty array for quality scores
        double[] qualities = new double[numOfAttributes];

        for(int attribute = 0; attribute < numOfAttributes; attribute++){
            BitSet pattern = new BitSet(numOfAttributes);
            pattern.set(attribute);
            double qualityForAttribute =
                    qualityFunction.quality(positiveTransactionDatabase, negativeTransactionDatabase, pattern,
                            numOfAttributes);
            qualities[attribute] = qualityForAttribute;
        }

        ArrayIndexComparator comparator = new ArrayIndexComparator(qualities);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);
        System.out.println("Sorted");
    }

}
