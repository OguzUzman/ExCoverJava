package test;

import uzman.oguz.ExCoverAlgorithm;
import uzman.oguz.PatternQualityPair;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by oguz on 15/12/2016.
 */
public class ExCoverAlgorithmTest {

    @org.junit.Test
    public void run() throws Exception {
        String positiveFilePath = "inputs/tic-tac-toe/binary positive output.txt";
        String negativeFilePath = "inputs/tic-tac-toe/binary negative output.txt";

        for (int numOfThreads = 0; numOfThreads < 8; numOfThreads++) {
            ExCoverAlgorithm exCoverAlgorithm = new ExCoverAlgorithm(positiveFilePath, negativeFilePath, "f");
            exCoverAlgorithm.run(numOfThreads);
        }
    }

    @org.junit.Test
    public void allPositivesAreCovered() throws Exception {


        for (int numOfThreads = 0; numOfThreads < 8; numOfThreads++) {

            String positiveFilePath = "inputs/tic-tac-toe/binary positive output.txt";
            String negativeFilePath = "inputs/tic-tac-toe/binary negative output.txt";

            List<BitSet> correctPatterns = new ArrayList<>();
            BitSet first = new BitSet();
            first.set(14);
            correctPatterns.add(first);
            BitSet set = new BitSet();
            set.set(2);
            correctPatterns.add(set);
            set = new BitSet();
            set.set(8);
            correctPatterns.add(set);
            set = new BitSet();
            set.set(20);
            correctPatterns.add(set);
            set = new BitSet();
            set.set(26);
            correctPatterns.add(set);

            ExCoverAlgorithm exCoverAlgorithm = new ExCoverAlgorithm(positiveFilePath, negativeFilePath, "f");
            exCoverAlgorithm.run(numOfThreads);
            List<PatternQualityPair> outputPatterns = exCoverAlgorithm.getOutputPatterns();

            assertTrue("Inequal size of outputs", outputPatterns.size() != correctPatterns.size());

            for (int i = 0; i < outputPatterns.size(); i++) {
                boolean isCorrect = false;
                for (int j = 0; j < correctPatterns.size(); j++) {
                    if(correctPatterns.get(j).equals(outputPatterns.get(i).getPattern()))
                        isCorrect = true;
                }
                assertTrue("One of the outputs is not correct output.", isCorrect);
            }
        }
    }

}