package uzman.oguz;

import java.util.BitSet;

/**
 * Created by oguz on 20/11/2016.
 */
public class PatternQualityPair {
    private final double quality;
    private final BitSet pattern;

    public PatternQualityPair( BitSet pattern, double quality) {
        this.quality = quality;
        this.pattern = pattern;
    }

    public double getQuality() {
        return quality;
    }

    public BitSet getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        String format = "{quality = %.3f, pattern = %s}";
        return String.format(format, quality, pattern);
    }
}
