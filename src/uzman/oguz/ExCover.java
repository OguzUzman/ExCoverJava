package uzman.oguz;

/**
 * Created by oguz on 17/11/2016.
 */

import org.apache.commons.cli.*;

import java.util.*;

/**
 * This is an implementation of ExCover algorithm described by
 */
public class ExCover {


    public static void main(String[] args)  {

        Options options = new Options();
        options.addOption("i", "input", true, "Input dataset folder name. E.g. -i=mushroom");
        options.addOption("s", "score", true, "Score measure -s=f for F-Score");
        options.addOption("h", "help", false, "Put input datasets into input folder of root folder.");
        Option threadOption = Option.builder("t").optionalArg(false).argName("t").longOpt("threads").hasArg(true).
                desc("Number of threads. E.g. -t=4").build();
        options.addOption(threadOption);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            String path = cmd.getOptionValue("i");
            String score = cmd.getOptionValue("s");
            String strNumOfThreads = cmd.getOptionValue("t");


            int numOfThreads = strNumOfThreads == null ? 1 : Integer.parseInt(strNumOfThreads);


            String positiveClassesPath = "inputs/" + path + "/binary positive output.txt";
            String negativeClassesPath = "inputs/" + path + "/binary negative output.txt";



            ExCoverAlgorithm exCoverAlgorithm = new ExCoverAlgorithm(positiveClassesPath, negativeClassesPath, score);

            long algorithmStart = System.currentTimeMillis();


            //g = null;
            exCoverAlgorithm.run(numOfThreads);

            System.out.println("Algorithm took " + (System.currentTimeMillis() - algorithmStart) + " milis");
            System.out.println("Best patterns are: ");

            List<PatternQualityPair> outputs = exCoverAlgorithm.getOutputPatterns();

            for (PatternQualityPair pattern :
                    outputs) {
                System.out.println(pattern);
            }

            if (exCoverAlgorithm.allPositivesAreCovered()) {
                System.out.println("All positive transactions are covered");
            } else {
                System.out.println("NOT all positive transactions are covered");
            }

        } catch (ParseException e) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("ExCover", options);
        }
    }
}
