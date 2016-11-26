package uzman.oguz.quality;

import uzman.oguz.Matcher;

import java.util.BitSet;
import java.util.List;

import static uzman.oguz.Matcher.*;

/**
 * Created by oguz on 18/11/2016.
 */
public class FScore extends QualityFunction{
    @Override
    public double quality(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern, int numAttributes) {
        List<Integer> positiveMatches = findMatches(positiveDatabase, pattern, null, numAttributes);
        List<Integer> negativeMatches = findMatches(negativeDatabase, pattern, null, numAttributes);

        return quality(positiveMatches.size(), negativeMatches.size(), positiveDatabase.length,
                negativeDatabase.length);
    }

    @Override
    public double quality(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern,
                          List<Integer> positiveMatches, List<Integer> negativeMatches, int numAttributes) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public double quality(int positiveMatchCount, int negativeMatchCount, int positiveDatabaseSize, int negativeDatabaseSize) {
        double pCandX = positiveMatchCount;
        double pX = positiveMatchCount + negativeMatchCount;

        double pCgivenX = pCandX/pX;

        //p(x|c) =p(x,c)/p(c)
        double pXandC = pCandX;
        double pC = positiveDatabaseSize;
        double pXgivenC = pXandC/pC;

        //Calculate F score using precision and recall
        double score = 2*pCgivenX*pXgivenC/(pXgivenC + pCgivenX);
        return Double.isNaN(score) ? 0: score;
    }

    @Override
    public double upperBound(int positiveMatchCount, int negativeMatchCount, int positiveDatabaseSize, int negativeDatabaseSize) {
        //negative match count, negativeDatabaseSize not used
        double pCandX = positiveMatchCount;
        //p(x|c) =p(x,c)/p(c)
        double pXandC = pCandX;
        double pC = positiveDatabaseSize;
        double pXgivenC = pXandC/pC;
        return 2*(pXgivenC)/(1+pXgivenC);
    }

    @Override
    public double upperBound(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern, int numAttributes) {
        return 0;
    }

    @Override
    public double upperBound(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern,
                             List<Integer> positiveMatches, List<Integer> negativeMatches, int numAttributes) {
        return 0;
    }
}
