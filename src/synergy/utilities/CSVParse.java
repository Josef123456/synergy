package synergy.utilities;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sari on 21/03/15.
 */
public class CSVParse {

    public static List<List<String>> parseCSVFile(String path){
        File csvData = new File(path);
        CSVParser csvParser = null;
        List<List<String>> toReturn = new ArrayList<>();
        try {
            csvParser = CSVParser.parse(FileReader.readFile(csvData), CSVFormat.MYSQL);
            //list = csvParser.getRecords();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(CSVRecord csvRecord:csvParser){
            String[] split = csvRecord.get(0).split(",");
            List<String> thisRecord = new ArrayList<>();
            System.out.println(split.length);
            if(split.length>=3) {
                System.out.println(split[0] + " " + split[1] + " " + split[2]);
                thisRecord.add(split[0]);thisRecord.add(split[1]);thisRecord.add(split[2]);
                toReturn.add(thisRecord);
            }
        }

        return toReturn;
    }

}
