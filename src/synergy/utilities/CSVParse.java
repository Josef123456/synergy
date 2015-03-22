package synergy.utilities;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that uses CSVParser to parse a csv file.
 */
public class CSVParse {
    /**
     * Parses CSV file.
     * @param file Input CSV file.
     * @return A list of entries, with every entry containing a list of data.
     */
    public static List<List<String>> parseCSVFile(File file){
        File csvData = file;
        CSVParser csvParser;
        List<List<String>> toReturn = new ArrayList<>();
        List<CSVRecord> listOfRecords = new ArrayList<>();
        try {
            csvParser = CSVParser.parse(FileReader.readFile(csvData), CSVFormat.MYSQL);
            listOfRecords = csvParser.getRecords();

        } catch (IOException e) {
            e.printStackTrace();
        }


        for(int i = 1;i<listOfRecords.size();i++){
            String[] split = listOfRecords.get(i).get(0).split(",");
            List<String> thisRecord = new ArrayList<>();
            System.out.println(split.length);
            if(split.length>=3) {
                thisRecord.add(split[0]);thisRecord.add(split[1]);thisRecord.add(split[2]);
                toReturn.add(thisRecord);
            }
        }

        return toReturn;
    }

}
