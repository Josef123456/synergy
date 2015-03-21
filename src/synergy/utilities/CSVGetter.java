package synergy.utilities;

import synergy.models.Tag;

import java.util.List;

/**
 * Created by sari on 21/03/15.
 */
public class CSVGetter {

    public static void getCSVData(String path){

        List<List<String>> parsedData = CSVParse.parseCSVFile(path);

        for(List<String> entry : parsedData){
            new Tag(Tag.TagType.KID,entry.get(0)+" "+entry.get(1)).save();
            new Tag(Tag.TagType.PLACE,entry.get(2)).save();
        }
    }
}
