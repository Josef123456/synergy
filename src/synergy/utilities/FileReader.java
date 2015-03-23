package synergy.utilities;

import java.io.BufferedReader;
import java.io.File;

/**
 * Class to make reading from a file easier.
 */
public class FileReader {

    /**
     * The file is read and its contents returned.
     * @param file The file to be read.
     * @return The contents of the file as a {@link java.lang.String}.
     */
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
