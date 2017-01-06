package uzman.oguz;

/**
 * Created by oguz on 17/11/2016.
 */

import org.apache.commons.cli.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
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
        Option logOption = Option.builder("l").optionalArg(true).argName("l").longOpt("Log output file location").hasArg(true)
                .desc("Set to y if you want the logs to be stored, else set to n, default is n").build();
        Option threadOption = Option.builder("t").optionalArg(false).argName("t").longOpt("threads").hasArg(true).
                desc("Number of threads. E.g. -t=4").build();
        options.addOption(threadOption);
        options.addOption(logOption);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            String path = cmd.getOptionValue("i");
            String score = cmd.getOptionValue("s");
            String strNumOfThreads = cmd.getOptionValue("t");
            String logText = cmd.getOptionValue("l");
            boolean logToFile = logText != null  && logText.equals("y") ;
            int numOfThreads = strNumOfThreads == null ? 1 : Integer.parseInt(strNumOfThreads);


            String positiveClassesPath = Paths.get("inputs", path, "binary positive output.txt").toString();
            String negativeClassesPath = Paths.get("inputs", path, "binary negative output.txt").toString();

            String logPath = "logs";
            File logFolder = new File(logPath);

            if(logToFile)
                logFolder.mkdirs();


            ExCoverAlgorithm exCoverAlgorithm = new ExCoverAlgorithm(positiveClassesPath, negativeClassesPath, score);

            long algorithmStart = System.currentTimeMillis();


            //g = null;
            exCoverAlgorithm.run(numOfThreads);
            StringBuilder outputSaver = new StringBuilder();


            String algorithmTime = "Algorithma took " + (System.currentTimeMillis() - algorithmStart) + " milis";
            System.out.println(algorithmTime);
            outputSaver.append(algorithmTime);
            outputSaver.append("\n");

            outputSaver.append("Number of threads at a time = "+ numOfThreads + "\n");
            System.out.println("Best patterns are: ");
            outputSaver.append("Best patterns are: \n");

            List<PatternQualityPair> outputs = exCoverAlgorithm.getOutputPatterns();

            for (PatternQualityPair pattern :
                    outputs) {
                outputSaver.append(pattern+"\n");
                System.out.println(pattern);
            }

            String msg = null;
            if (exCoverAlgorithm.allPositivesAreCovered()) {
                msg = "All positive transactions are covered";
            } else {
                msg = "NOT all positive transactions are covered";
            }
            System.out.println(msg);
            outputSaver.append(msg + "\n");

            if(logToFile) {

                try {
                    String logFilePath = Paths.get("logs", path+"_t="+numOfThreads+".txt").toString();
                    File logFile = new File(logFilePath);
                    BufferedWriter bwr = new BufferedWriter(new FileWriter(logFile));

                    //write contents of StringBuffer to a file
                    bwr.write(outputSaver.toString());

                    //flush the stream
                    bwr.flush();

                    //close the stream
                    bwr.close();

                    System.out.println("Output written to file: " +logFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (ParseException e) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("ExCover", options);
        } catch (SecurityException e){
            e.printStackTrace();
        }

    }
}
