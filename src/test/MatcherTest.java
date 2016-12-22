package test;

import org.junit.Test;
import uzman.oguz.Matcher;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by oguz on 15/12/2016.
 */
public class MatcherTest {
    @Test
    public void match() throws Exception {

        int numOfAttributes = 8;


        //first num if transaction, second one is the pattern
        matchCheck(
                0b00000000,
                0b00000000,
                numOfAttributes,
                true);
        matchCheck(
                0b11111111,
                0b00000000,
                numOfAttributes,
                true);
        matchCheck(
                0b00000000,
                0b00000001,
                numOfAttributes,
                false);
        matchCheck(
                0b00000001,
                0b00010001,
                numOfAttributes,
                false);
        matchCheck(
                0b00000000,
                0b10000000,
                numOfAttributes,
                false);
        matchCheck(
                0b11100000,
                0b10000000,
                numOfAttributes,
                true);
        matchCheck(
                0b10001000,
                0b11111000,
                numOfAttributes,
                false);
        matchCheck(
                0b10010000,
                0b01100000,
                numOfAttributes,
                false);
        matchCheck(
                0b10010000,
                0b11110000,
                numOfAttributes,
                false);
        matchCheck(
                0b10000000,
                0b01100000,
                numOfAttributes,
                false);
        matchCheck(
                0b10000111,
                0b10000000,
                numOfAttributes,
                true);
        matchCheck(
                0b10000000,
                0b00000111,
                numOfAttributes,
                false);
        matchCheck(
                0b11000000,
                0b11100000,
                numOfAttributes,
                false);
        matchCheck(
                0b11110000,
                0b11000000,
                numOfAttributes,
                true);
        matchCheck(
                0b11111111,
                0b00000000,
                numOfAttributes,
                true);
        matchCheck(
                0b00000000,
                0b11111111,
                numOfAttributes,
                false);
        matchCheck(
                0b10011000,
                0b00011000,
                numOfAttributes,
                true);


    }


    private void matchCheck(int tran, int patt, int numOfAttributes, boolean correctVal) {

        byte[] patternByte = new byte[]{(byte) patt};
        byte[] transactionnByte = new byte[]{(byte) tran};

        BitSet pattern = BitSet.valueOf(patternByte);
        BitSet transaction = BitSet.valueOf(transactionnByte);

        boolean foundVal = Matcher.match(transaction, pattern, numOfAttributes);
        if (correctVal)
            assertTrue(foundVal);
        else
            assertFalse(foundVal);

    }

    private void subsetCheck(int superset, int subset, int numOfAttributes, boolean correctVal) {

        byte[] supersetByte = new byte[]{(byte) superset};
        byte[] subsetByte = new byte[]{(byte) subset};

        BitSet superB = BitSet.valueOf(supersetByte);
        BitSet sub = BitSet.valueOf(subsetByte);

        boolean foundVal = Matcher.subset(sub, superB, numOfAttributes);
        if (correctVal)
            assertTrue(foundVal);
        else
            assertFalse(foundVal);

    }

    @Test
    public void findMatches() throws Exception {

        BitSet[] database = new BitSet[]
                {
                        BitSet.valueOf(new byte[]{0b0000}),//0
                        BitSet.valueOf(new byte[]{0b0001}),//1
                        BitSet.valueOf(new byte[]{0b0010}),//2
                        BitSet.valueOf(new byte[]{0b0011}),//3
                        BitSet.valueOf(new byte[]{0b0100}),//4
                        BitSet.valueOf(new byte[]{0b0101}),//5
                        BitSet.valueOf(new byte[]{0b0110}),//6
                        BitSet.valueOf(new byte[]{0b0111}),//7
                        BitSet.valueOf(new byte[]{0b1000}),//8
                        BitSet.valueOf(new byte[]{0b1001}),//9
                        BitSet.valueOf(new byte[]{0b1010}),//10
                        BitSet.valueOf(new byte[]{0b1011}),//11
                        BitSet.valueOf(new byte[]{0b1100}),//12
                        BitSet.valueOf(new byte[]{0b1101}),//13
                        BitSet.valueOf(new byte[]{0b1110}),//14
                        BitSet.valueOf(new byte[]{0b1111}) //15
                };
        List<Integer> allIndices = addHelper(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        int numAttributes = 4;

        BitSet pattern = makePattern(0b0000);
        List<Integer> expectedMatches = addHelper(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Integer> actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

        pattern = makePattern(0b0001);
        expectedMatches = addHelper(1, 3, 5, 7, 9, 11, 13, 15);
        actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

        pattern = makePattern(0b0011);
        expectedMatches = addHelper(3, 7, 11, 15);
        actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

        pattern = makePattern(0b1111);
        expectedMatches = addHelper(15);
        actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

        pattern = makePattern(0b1110);
        expectedMatches = addHelper(14, 15);
        actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

        pattern = makePattern(0b1100);
        expectedMatches = addHelper(12, 13, 14, 15);
        actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

        pattern = makePattern(0b1000);
        expectedMatches = addHelper(8, 9, 10, 11, 12, 13, 14, 15);
        actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

        pattern = makePattern(0b0010);
        expectedMatches = addHelper(2, 3, 6, 7, 10, 11, 14, 15);
        actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

        pattern = makePattern(0b0110);
        expectedMatches = addHelper(6, 7, 14, 15);
        actualMatches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        assertEquals(expectedMatches, actualMatches);

    }

    private BitSet makePattern(int patt) {
        return BitSet.valueOf(new byte[]{(byte) patt});
    }

    private List<Integer> addHelper(int... indices) {
        List<Integer> list = new ArrayList<>();
        for (int index :
                indices) {
            list.add(index);
        }
        return list;
    }

    @Test
    public void close() throws Exception {

        //This one must work, in order to close to work, so we test it.
        findMatches();
        BitSet[] database = new BitSet[]
                {
                        BitSet.valueOf(new byte[]{0b0001}),//0
                        BitSet.valueOf(new byte[]{0b0111}),//1
                        BitSet.valueOf(new byte[]{0b1110}),//2
                        BitSet.valueOf(new byte[]{0b1100}),//3
                        BitSet.valueOf(new byte[]{0b1111}),//4
                        BitSet.valueOf(new byte[]{0b0110}) //5
                };
        int numAttributes = 4;
        BitSet pattern;
        BitSet closedForm;
        BitSet expectedPattern;
        List<Integer> matches;
        List<Integer> allIndices = addHelper(0, 1, 2, 3, 4);

        pattern         = makePattern(0b0000);
        expectedPattern = makePattern(0b0000);
        matches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        closedForm = Matcher.close(matches, database, numAttributes);
        assertTrue(closedForm.equals(expectedPattern));

        pattern         = makePattern(0b0001);
        expectedPattern = makePattern(0b0001);
        matches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        closedForm = Matcher.close(matches, database, numAttributes);
        assertTrue(closedForm.equals(expectedPattern));

        pattern         = makePattern(0b0011);
        expectedPattern = makePattern(0b0111);
        matches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        closedForm = Matcher.close(matches, database, numAttributes);
        assertTrue(closedForm.equals(expectedPattern));

        pattern         = makePattern(0b1000);
        expectedPattern = makePattern(0b1100);
        matches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        closedForm = Matcher.close(matches, database, numAttributes);
        assertTrue(closedForm.equals(expectedPattern));

        pattern         = makePattern(0b1111);
        expectedPattern = makePattern(0b1111);
        matches = Matcher.findMatches(database, pattern, allIndices, numAttributes);
        closedForm = Matcher.close(matches, database, numAttributes);
        assertTrue(closedForm.equals(expectedPattern));


    }

    @Test
    public void findExtraItemsInClosedForm() throws Exception {

        BitSet pattern, closedPattern, expectedExtraItems, foundExtraItems;

        int numAttributes = 4;


        pattern             = makePattern(0b0000);
        closedPattern       = makePattern(0b0011);
        expectedExtraItems  = makePattern(0b0011);
        foundExtraItems     = Matcher.findExtraItemsInClosedForm(pattern, closedPattern, numAttributes);
        assertTrue(foundExtraItems.equals(expectedExtraItems));

        pattern             = makePattern(0b0000);
        closedPattern       = makePattern(0b0111);
        expectedExtraItems  = makePattern(0b0111);
        foundExtraItems     = Matcher.findExtraItemsInClosedForm( pattern, closedPattern, numAttributes);
        assertTrue(foundExtraItems.equals(expectedExtraItems));

        pattern             = makePattern(0b0011);
        closedPattern       = makePattern(0b0111);
        expectedExtraItems  = makePattern(0b0100);
        foundExtraItems     = Matcher.findExtraItemsInClosedForm( pattern, closedPattern, numAttributes);
        assertTrue(foundExtraItems.equals(expectedExtraItems));

        pattern             = makePattern(0b0111);
        closedPattern       = makePattern(0b0111);
        expectedExtraItems  = makePattern(0b0000);
        foundExtraItems     = Matcher.findExtraItemsInClosedForm( pattern, closedPattern, numAttributes);
        assertTrue(foundExtraItems.equals(expectedExtraItems));

        pattern             = makePattern(0b1001);
        closedPattern       = makePattern(0b1111);
        expectedExtraItems  = makePattern(0b0110);
        foundExtraItems     = Matcher.findExtraItemsInClosedForm( pattern, closedPattern, numAttributes);
        assertTrue(foundExtraItems.equals(expectedExtraItems));

        pattern             = makePattern(0b0000);
        closedPattern       = makePattern(0b1111);
        expectedExtraItems  = makePattern(0b1111);
        foundExtraItems     = Matcher.findExtraItemsInClosedForm( pattern, closedPattern, numAttributes);
        assertTrue(foundExtraItems.equals(expectedExtraItems));





    }

    @Test
    public void subset() throws Exception {
        int numOfAttributes = 8;


        //first num if transaction, second one is the pattern
        subsetCheck(
                0b00000000,
                0b00000000,
                numOfAttributes,
                true);
        subsetCheck(
                0b11111111,
                0b00000000,
                numOfAttributes,
                true);
        subsetCheck(
                0b00000000,
                0b00000001,
                numOfAttributes,
                false);
        subsetCheck(
                0b00000001,
                0b00010001,
                numOfAttributes,
                false);
        subsetCheck(
                0b00000000,
                0b10000000,
                numOfAttributes,
                false);
        subsetCheck(
                0b11100000,
                0b10000000,
                numOfAttributes,
                true);
        subsetCheck(
                0b10001000,
                0b11111000,
                numOfAttributes,
                false);
        subsetCheck(
                0b10010000,
                0b01100000,
                numOfAttributes,
                false);
        subsetCheck(
                0b10010000,
                0b11110000,
                numOfAttributes,
                false);
        subsetCheck(
                0b10000000,
                0b01100000,
                numOfAttributes,
                false);
        subsetCheck(
                0b10000111,
                0b10000000,
                numOfAttributes,
                true);
        subsetCheck(
                0b10000000,
                0b00000111,
                numOfAttributes,
                false);
        subsetCheck(
                0b11000000,
                0b11100000,
                numOfAttributes,
                false);
        subsetCheck(
                0b11110000,
                0b11000000,
                numOfAttributes,
                true);
        subsetCheck(
                0b11111111,
                0b00000000,
                numOfAttributes,
                true);
        subsetCheck(
                0b00000000,
                0b11111111,
                numOfAttributes,
                false);
        subsetCheck(
                0b10011000,
                0b00011000,
                numOfAttributes,
                true);
    }
}