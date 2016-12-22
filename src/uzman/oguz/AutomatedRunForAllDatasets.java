package uzman.oguz;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * Created by oguz on 22/12/2016.
 */
public class AutomatedRunForAllDatasets {
    public static void main(String[] args) {
        File file = new File("inputs");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for (String directory: directories
             ) {
            ExCover.main(new String[]{String.format("-i=%s", directory),"-s=f", "-t=8", "-l=y" });
        }
    }
}
