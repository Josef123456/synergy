package synergy.MetaData;

import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import synergy.models.Photo;
import org.apache.commons.imaging.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Amit on 25/02/2015.
 */
public class Date {

    public String photoPath;
    public String dateToReplace;
    public IImageMetadata metadata = null;

    public void changeDate(Photo photo, String newDate) throws IOException, ImageReadException {
        File inputFile = new File(photo.getPath ());
        OutputStream os = null;
        TiffOutputSet tiffOutputSet = null;

        IImageMetadata metaData = Imaging.getMetadata(inputFile);
        JpegImageMetadata jpegMetaData = (JpegImageMetadata) metaData;


    }
}
