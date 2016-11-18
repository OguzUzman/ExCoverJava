package uzman.oguz;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.stream.IntStream;

/**
 * Created by oguz on 18/11/2016.
 */
public class ImmutableBitSet {
    BitSet instance;

    public static BitSet valueOf(long[] longs) {
        return BitSet.valueOf(longs);
    }

    public boolean get(int bitIndex) {
        return instance.get(bitIndex);
    }

    public boolean intersects(BitSet set) {
        return instance.intersects(set);
    }

    public int size() {
        return instance.size();
    }

    public int previousClearBit(int fromIndex) {
        return instance.previousClearBit(fromIndex);
    }

    public int cardinality() {
        return instance.cardinality();
    }

    public static BitSet valueOf(LongBuffer lb) {
        return BitSet.valueOf(lb);
    }

    public IntStream stream() {
        return instance.stream();
    }

    public void flip(int bitIndex) {
        instance.flip(bitIndex);
    }

    public void or(BitSet set) {
        instance.or(set);
    }

    public BitSet get(int fromIndex, int toIndex) {
        return instance.get(fromIndex, toIndex);
    }

    public int nextClearBit(int fromIndex) {
        return instance.nextClearBit(fromIndex);
    }

    public void xor(BitSet set) {
        instance.xor(set);
    }

    public long[] toLongArray() {
        return instance.toLongArray();
    }

    public static BitSet valueOf(byte[] bytes) {
        return BitSet.valueOf(bytes);
    }

    public byte[] toByteArray() {
        return instance.toByteArray();
    }

    public int previousSetBit(int fromIndex) {
        return instance.previousSetBit(fromIndex);
    }

    public void and(BitSet set) {
        instance.and(set);
    }

    public void set(int bitIndex) {
        instance.set(bitIndex);
    }

    public boolean isEmpty() {
        return instance.isEmpty();
    }

    public void clear() {
        instance.clear();
    }

    public void set(int fromIndex, int toIndex) {
        instance.set(fromIndex, toIndex);
    }

    public void flip(int fromIndex, int toIndex) {
        instance.flip(fromIndex, toIndex);
    }

    public void set(int fromIndex, int toIndex, boolean value) {
        instance.set(fromIndex, toIndex, value);
    }

    public void andNot(BitSet set) {
        instance.andNot(set);
    }

    public static BitSet valueOf(ByteBuffer bb) {
        return BitSet.valueOf(bb);
    }

    public int nextSetBit(int fromIndex) {
        return instance.nextSetBit(fromIndex);
    }

    public void clear(int bitIndex) {
        instance.clear(bitIndex);
    }

    public void clear(int fromIndex, int toIndex) {
        instance.clear(fromIndex, toIndex);
    }

    public int length() {
        return instance.length();
    }

    public void set(int bitIndex, boolean value) {
        instance.set(bitIndex, value);
    }
}