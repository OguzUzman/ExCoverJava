package uzman.oguz;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by Oguz Uzman on 18/11/2016.
 */
public class Matcher {
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
}
