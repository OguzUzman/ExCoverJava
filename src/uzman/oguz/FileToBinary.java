package uzman.oguz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by oguz on 17/11/2016.
 */
public class FileToBinary {
    public static void main(String[] args) {
        String inputFilePath = args[0];
        File inputFile = new File(inputFilePath);
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                // process the line.
            }
        } catch (Exception e){
            System.out.println(e);
            return;
        }
    }
}
