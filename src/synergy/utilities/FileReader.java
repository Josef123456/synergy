package synergy.utilities;

import java.io.BufferedReader;
import java.io.File;

/**
 * Created by sari on 21/03/15.
 */
public class FileReader {


    public static String readFile(File file){
        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return  sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
