package synergy.metadata;


import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.junit.Test;
import synergy.models.BaseTest;
import synergy.models.Photo;
import java.io.IOException;

/**
 * Created by Amit on 19/02/2015.
 */
public class MetaDataTest extends BaseTest {

    @Test
    public void testMetaData() throws ImageWriteException, ImageReadException, IOException {
        MetaData image = new MetaData();

	    new Photo (FILE_PATH).save();

	    System.out.println ("$$" + image.getTagsForFile (FILE_PATH));
    }
}
