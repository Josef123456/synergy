package synergy.MetaData;

import junit.framework.TestCase;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import synergy.MetaData.MetaData;

import java.io.File;
import java.io.IOException;

/**
 * Created by Amit on 19/02/2015.
 */
public class MetaDataTest extends TestCase {

    public void testMetaData() throws ImageWriteException, ImageReadException, IOException {

        MetaData image = new MetaData();

        //Original file used for modifying
        File inputFile = new File("C:\\Users\\Amit\\Pictures\\testcamimage.jpg");
        //Create a new output file of the modified image.
        File outputFile = new File ("C:\\Users\\Amit\\Pictures\\temp.jpg");

        image.changeExifMetadata(inputFile,outputFile);
        image.getMetaData(inputFile);

        assertNotNull(image.values);
        assertNotNull(image.metadata);

    }


}
