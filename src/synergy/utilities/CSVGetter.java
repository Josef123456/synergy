package synergy.utilities;

import synergy.models.Tag;

import java.io.File;
import java.util.List;

/**
 * Class that contains the method to update the internal database with entries found in a CSV file.
 */
public class CSVGetter {
    /**
     * Creates new {@link synergy.models.Tag} for each entry in the CSV, after parsing.
     * @param file CSV file imported from.
     */
    public static void getCSVData(File file){

        List<List<String>> parsedData = CSVParse.parseCSVFile(file);

        for(List<String> entry : parsedData){
            new Tag(Tag.TagType.KID,entry.get(0)+" "+entry.get(1)).save();
            new Tag(Tag.TagType.PLACE,entry.get(2)).save();
        }
    }
}
