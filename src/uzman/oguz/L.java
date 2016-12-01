package uzman.oguz;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by oguz on 20/11/2016.
 */
public class L {
    /**
     * If a bit is true, there is a pattern at that index in @patternQualityPair.
     */
    volatile private ArrayList<Integer>[] transactionPatternMapping;

    /**
     * A list of patterns, which contain pattern and its quality
     */
    volatile private ArrayList<PatternQualityPair> patternQualityPairs;


    public L(int numOfTransactions, double initialScoreForEmptyPattern, int numOfAttributes) {
        transactionPatternMapping = new ArrayList[numOfTransactions];

        for (int i = 0, length = transactionPatternMapping.length; i < length; i++) {
            ArrayList<Integer> patternMappings = new ArrayList<>();
            //patternMappings.add(0);
            transactionPatternMapping[i] = patternMappings;
        }

        patternQualityPairs = new ArrayList<PatternQualityPair>();

        //PatternQualityPair emptyPattern = new PatternQualityPair(new BitSet(numOfAttributes), initialScoreForEmptyPattern);
        //patternQualityPairs.add(emptyPattern);
    }


    /**
     * Implements the first part from 4th line in GROW algorithm
     *
     * @return
     */
    public synchronized boolean line4GrowAlgorithm(BitSet patternXPrime, List<Integer> xSubPatternMatches,
                                      BitSet[] positiveDatabase, int attributeCount, double upperBoundForXPrime) {
        boolean firstPart = false;
        boolean secondPart = true;
        double currentMin = Double.MAX_VALUE;
        for (int i = 0; i < xSubPatternMatches.size(); i++) {
            int transactionIndexInPositiveDB = xSubPatternMatches.get(i);
            BitSet posTransaction = positiveDatabase[transactionIndexInPositiveDB];
            boolean match = Matcher.match(posTransaction, patternXPrime, attributeCount);
            if(match){

                //Means first part of line 4 is true
                if (transactionPatternMapping[transactionIndexInPositiveDB].size() > 0) {
                    try {
                        firstPart = true;
                        double arbitraryPatternQuality = patternQualityPairs.get(
                                transactionPatternMapping[transactionIndexInPositiveDB].get(0)).getQuality();
                        currentMin = currentMin > arbitraryPatternQuality ? arbitraryPatternQuality : currentMin;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        if(firstPart){
            secondPart = upperBoundForXPrime < currentMin;
        }

        return firstPart && secondPart;
        //return false;
    }


    public synchronized void add(List<Integer> positiveMatches,
                                 BitSet patternX, double qualityOfX, int numOfAttributes){
        patternQualityPairs.add(new PatternQualityPair(patternX, qualityOfX));
        int patternIndex = patternQualityPairs.size()-1;
        /**
         * Line 1 in algorithm: ADD
         */
        for (int positiveMatchIndex = 0; positiveMatchIndex < positiveMatches.size(); positiveMatchIndex++) {


            // This is the t in the algorithm as an index
            int t = positiveMatches.get(positiveMatchIndex);

            //L[t]
            List<Integer> Lt = transactionPatternMapping[t];

            // Line 2 arbitraryQuality = r: ADD If there are no patterns, just return 0
            double arbitraryQuality = Lt.size() > 0 ? patternQualityPairs.get(Lt.get(0)).getQuality() : 0;



            //Line 3: ADD
            if(qualityOfX > arbitraryQuality){
                setToPattern(patternIndex, t);
            }
            //Line 5: ADD
            else if(qualityOfX == arbitraryQuality){
                boolean isASubPattern = false;


                for (int i = 0; i < Lt.size(); i++) {

                    BitSet patternZ = patternQualityPairs.get(i).getPattern();
                    // If X is a superset of pattern z
                    if(Matcher.subset(patternZ, patternX, numOfAttributes)){
                        isASubPattern = true;
                        break;
                    }

                }
                if(!isASubPattern){
                    addPattern(patternIndex, positiveMatchIndex);
                }
            }

        }

    }

    private synchronized void setToPattern(int patternIndex, int positiveTransactionIndex){
        /**
         * Set it to map to the pattern
         */

        PatternQualityPair pair = patternQualityPairs.get(patternIndex);

        transactionPatternMapping[positiveTransactionIndex].clear();
        transactionPatternMapping[positiveTransactionIndex].add(patternIndex);

    }

    private synchronized void addPattern(int patternIndex, int positiveTransactionIndex){

        /**
         * Add to the existing patterns for a transaction
         */
        transactionPatternMapping[positiveTransactionIndex].add(patternIndex);

    }


    public ArrayList<Integer>[] getTransactionPatternMapping() {
        return transactionPatternMapping;
    }

    public ArrayList<PatternQualityPair> getPatternQualityPairs() {
        return patternQualityPairs;
    }
}