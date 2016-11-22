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
    private ArrayList<Integer>[] transactionPatternMapping;

    /**
     * A list of patterns, which contain pattern and its quality
     */
    private ArrayList<PatternQualityPair> patternQualityPairs;


    public L(int numOfTransactions) {
        transactionPatternMapping = new ArrayList[numOfTransactions];

        for (int i = 0, length = transactionPatternMapping.length; i < length; i++) {
            transactionPatternMapping[i] = new ArrayList<>();
        }

        patternQualityPairs = new ArrayList<PatternQualityPair>();
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
            if (!firstPart) {
                BitSet posTransaction = positiveDatabase[xSubPatternMatches.get(i)];
                boolean match = Matcher.match(posTransaction, patternXPrime, attributeCount);
                if(match){
                    if (transactionPatternMapping[i].size() > 0) {
                        firstPart = true;
                        double firstPaternQality = patternQualityPairs.get(transactionPatternMapping[i].get(0)).getQuality();
                        currentMin = currentMin > firstPaternQality ? firstPaternQality : currentMin;
                    }
                }
            }
        }

        if(firstPart){
            secondPart = upperBoundForXPrime < currentMin;
        }

        return firstPart && secondPart;
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
            } else if(qualityOfX == arbitraryQuality){
                boolean isASubPattern = false;

                ArrayList<Integer> patternsIndexForATransaction = transactionPatternMapping[t];

                for (int i = 0; i < transactionPatternMapping.length; i++) {

                    BitSet pattern = patternQualityPairs.get(patternsIndexForATransaction.get(i)).getPattern();
                    if(Matcher.subset(patternX, pattern, numOfAttributes)){
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