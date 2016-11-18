package uzman.oguz;

/**
 * Created by oguz on 17/11/2016.
 */

import java.io.*;
import java.util.BitSet;

/**
 * This is an implementation of ExCover algorithm described by
 */
public class ExCover {


    public static void main(String[] args) {

        if(args.length < 2){
            System.out.println(
                    "Please enter parameters for the file paths of positive transactions and negative transactions");
            return;
        }

        String positiveClassesPath = args[0];
        String negativeClassesPath = args[1];
        String score = args[2];

        ExCoverAlgorithm exCoverAlgorithm = new ExCoverAlgorithm(positiveClassesPath, negativeClassesPath, score);

        exCoverAlgorithm.run();

    }





}
