package synergy.metadata;

import junit.framework.TestCase;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.junit.Test;
import synergy.models.Photo;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Amit on 26/02/2015.
 */
public class DateTest {

	protected final static String DIR_PATH = "/Users/alexstoick/Dropbox/edited photos/";
	protected final static String FILE_PATH = DIR_PATH + "20150124-_DSC0107.jpg";

    @Test public void testDateChange() throws ImageWriteException, ImageReadException, IOException {
        Date date = new Date();

        // //"C:\Users\Amit\Pictures\testcamimage.jpg"

        Photo photo = new Photo(FILE_PATH);
	    System.out.println(photo);
	    Date.getDate (FILE_PATH);
//        String replacementDate = "2018:01:23";
//        date.changeDate(FILE_PATH,replacementDate);

//        assertEquals(Date.getDate (FILE_PATH),replacementDate);
    }
}
