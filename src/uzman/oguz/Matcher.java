package uzman.oguz;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by Oguz Uzman on 18/11/2016.
 */
public class Matcher {
    /**
     * Finds matches for a transaction db
     * @param database transaction db
     * @param pattern
     * @param existingMatches existing matches for a sub pattern
     * @param attributeCount
     * @return
     */
    public static List<Integer> findMatches(BitSet[] database, BitSet pattern, List<Integer> existingMatches,
                                            int attributeCount){

        List<Integer> matches = new ArrayList<>();

        if(existingMatches == null){
            // No information about previous existing matches, will check whole database

            for(int dbRow = 0; dbRow < database.length; dbRow++){
                if(match(database[dbRow], pattern, attributeCount)){
                    matches.add(dbRow);
                }
            }

        } else{

            // Check only matches in existing matches since the sub pattern matches them.
            for (int i = 0; i < existingMatches.size(); i++){
                if(match(database[existingMatches.get(i)], pattern, attributeCount))
                    matches.add(existingMatches.get(i));
            }
        }
        return matches;
    }

    public static boolean match(BitSet transaction, BitSet pattern, int attributeCounter){
        // We perform the boolean operation T + ( ¬P ), then the sum should be equal to the number of attributes.
        //((BitSet)transaction.clone()).or(((BitSet) pattern.clone()).no);
        // OR this
        BitSet flippedPattern = ((BitSet) pattern.clone());// Clone P

        flippedPattern.flip(0, attributeCounter);// Negate P

        flippedPattern.or(transaction);// Sum them

        return flippedPattern.cardinality() == attributeCounter; // They are equal
    }

    public static BitSet close(List<Integer> matches, BitSet[] positiveDatabase, int numOfAttributes){
        //Starting from a state where it has all the closed ones
        BitSet closedForm = new BitSet(numOfAttributes);
        closedForm.flip(0, numOfAttributes);
        for (int matchIndex = 0; matchIndex < matches.size(); matchIndex++) {
            closedForm.and(positiveDatabase[matches.get(matchIndex)]);
        }
        return closedForm;
    }

    public static BitSet findExtraItemsInClosedForm(BitSet patternX, BitSet closedPatternX, int numOfAttributes){
        //We want to preserve the original
        BitSet result = (BitSet) patternX.clone();
        //The result should be (not X ) AND XClosed
        //Not X
        result.flip(0, numOfAttributes);
        //AND C
        result.and(closedPatternX);
        return result;
    }

    /**
     * Returns true if subPattern is truly a subPattern of superPattern
     * @param subPattern
     * @param superPattern
     * @param numberOfAttributes
     * @return
     */
    public static boolean subset(BitSet subPattern, BitSet superPattern, int numberOfAttributes){

        // ((not @subPattern) OR superPattern) gives 1 for a bit if subpattern is truly a subpattern of superpattern.
        // In the end, the sum of 1 bits must be equal to number of Attributes

        BitSet cloneSubPattern = (BitSet) subPattern.clone();
        cloneSubPattern.flip(0, numberOfAttributes);
        cloneSubPattern.or(superPattern);
        if(cloneSubPattern.cardinality() == numberOfAttributes){
            //It is a subpattern, return true
            return  true;
        }
        return false;
    }
}
