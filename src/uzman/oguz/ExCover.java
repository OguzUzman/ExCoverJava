package uzman.oguz;

/**
 * Created by oguz on 17/11/2016.
 */

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;
import org.jgrapht.ListenableGraph;
import org.jgrapht.alg.PrimMinimumSpanningTree;
import uzman.oguz.visualize.GraphAdapter;

import javax.swing.*;
import java.util.*;

/**
 * This is an implementation of ExCover algorithm described by
 */
public class ExCover {


    public static void main(String[] args) {

        BitSet s = new BitSet(16);


        s.set(5);

        if(args.length < 1){
            System.out.println(
                    "Please enter parameters for the file paths of positive transactions and negative transactions");
            return;
        }

        String path = args[0];
        String positiveClassesPath = "inputs/"+path+"/binary positive output.txt";
        String negativeClassesPath = "inputs/"+path+"/binary negative output.txt";

        String score = args[1];

        ExCoverAlgorithm exCoverAlgorithm = new ExCoverAlgorithm(positiveClassesPath, negativeClassesPath, score);

        if(false) {
            GraphAdapter graphAdapter = new GraphAdapter();
            graphAdapter.init();
            ListenableGraph g = graphAdapter.g;
        }
        long algorithmStart = System.currentTimeMillis();


        //g = null;
        exCoverAlgorithm.run(1);

        System.out.println("Algorithm took " + (System.currentTimeMillis()- algorithmStart)+ " milis");
        System.out.println("Best patterns are: ");




        ArrayList<PatternQualityPair> patternQualityPairs = exCoverAlgorithm.getPatternQualityPairs();
        ArrayList<Integer>[] transactionPatternMapping = exCoverAlgorithm.getTransactionPatternMapping();

        ArrayList<Integer> patternQualityIndices = new ArrayList<>();
        for (int i = 0; i < transactionPatternMapping.length; i++) {
            ArrayList<Integer> mapping = transactionPatternMapping[i];
            for (Integer integer: mapping) {
                patternQualityIndices.add(integer);
            }
        }
        Set<Integer> uniqKeys = new TreeSet<Integer>();
        uniqKeys.addAll(patternQualityIndices);

        Iterator iter = uniqKeys.iterator();
        while (iter.hasNext()) {
            System.out.println(patternQualityPairs.get((Integer) iter.next()));
        }

    }
}
