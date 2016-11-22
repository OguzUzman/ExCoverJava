package uzman.oguz.quality;

import java.util.BitSet;
import java.util.List;

/**
 * Created by oguz on 18/11/2016.
 */
public abstract class QualityFunction {
    public abstract double quality(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern, int numAttributes);

    public abstract double quality(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern,
                                   List<Integer> positiveMatches, List<Integer> negativeMatches, int numAttributes);

    public abstract double quality(int positiveMatchCount, int negativeMatchCount, int positiveDatabaseSize, int negativeDatabaseSize);

    public abstract double upperBound(int positiveMatchCount, int negativeMatchCount, int positiveDatabaseSize, int negativeDatabaseSize);


    public abstract double upperBound(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern, int numAttributes);

    public abstract double upperBound(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern,
                                      List<Integer> positiveMatches, List<Integer> negativeMatches, int numAttributes);

    public double probXGivenC(BitSet[] positiveDatabase, List<Integer> positiveMatches){
        double pCAndX = positiveMatches.size();
        double pC = positiveDatabase.length;
        return pCAndX / pC;
    }

    public double probXGivenNotC(BitSet[] negativeDatabase, List<Integer> negativeMatches){
        double pCAndX = negativeMatches.size();
        double pX = negativeDatabase.length;
        return pCAndX / pX;
    }
}
