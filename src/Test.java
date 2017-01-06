import org.apache.commons.cli.*;

/**
 * Created by oguz on 14/12/2016.
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        System.out.println((Runtime.getRuntime().availableProcessors()));
        Options options = new Options();
        options.addOption( "i", "input", true, "Input dataset folder name" );
        options.addOption( "s", "score", true, "Score measure -s=f for F-Score" );
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);
        cmd.getArgList();
    }
}
