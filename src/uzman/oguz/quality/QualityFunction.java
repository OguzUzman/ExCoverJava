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


    public abstract double upperBound(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern, int numAttributes);

    public abstract double upperBound(BitSet[] positiveDatabase, BitSet[] negativeDatabase, BitSet pattern,
                                      List<Integer> positiveMatches, List<Integer> negativeMatches, int numAttributes);
}
