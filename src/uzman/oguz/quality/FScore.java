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
        //p(c|x) = (p(c,x)/p(x))
        double pCandX = positiveMatches.size();
        double pX = positiveMatches.size() + negativeMatches.size();

        double pCgivenX = pCandX/pX;

        //p(x|c) =p(x,c)/p(c)
        double pXandC = pCandX;
        double pC = positiveMatches.size();
        double pXgivenC = pXandC/pC;

        //Calculate F score using precision and recall
        return 2*pCgivenX*pXgivenC/(pXgivenC + pCgivenX);
    }

    @Override
    public double quality(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern,
                          List<Integer> positiveMatches, List<Integer> negativeMatches, int numAttributes) {
        return 0;
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
