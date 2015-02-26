package synergy.MetaData;

import junit.framework.TestCase;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import synergy.models.Photo;

import java.io.IOException;

/**
 * Created by Amit on 26/02/2015.
 */
public class DateTest extends TestCase {

    public void testDateChange() throws ImageWriteException, ImageReadException, IOException {
        Date date = new Date();

        Photo testPhoto = new Photo("C:\\Users\\Amit\\Pictures\\inputfile.jpg");
        String replacementDate = "2015:01:23";
        date.changeDate(testPhoto,replacementDate);

        assertEquals(date.replacementDate,replacementDate);
        assertNotNull(date.metadata);
    }
}
