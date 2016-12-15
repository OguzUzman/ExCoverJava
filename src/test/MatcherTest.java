package test;

import org.junit.Test;
import uzman.oguz.Matcher;

import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * Created by oguz on 15/12/2016.
 */
public class MatcherTest {
    @Test
    public void match() throws Exception {
        BitSet pattern = new BitSet();
        BitSet transaction = new BitSet();
        assertTrue(Matcher.match(transaction, pattern, 4));
        transaction.set(0);
        assertTrue(Matcher.match(transaction, pattern, 4));
        pattern.set(0);
        assertTrue(Matcher.match(transaction, pattern, 4));
        pattern.set(1);
        assertFalse(Matcher.match(transaction, pattern, 4));
        transaction.set(1);
        assertTrue(Matcher.match(transaction, pattern, 4));
    }

    @Test
    public void findMatches() throws Exception {

    }

    @Test
    public void close() throws Exception {

    }

    @Test
    public void findExtraItemsInClosedForm() throws Exception {

    }

    @Test
    public void subset() throws Exception {

    }
}