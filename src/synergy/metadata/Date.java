package synergy.metadata;

import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import synergy.models.Photo;
import org.apache.commons.imaging.*;

import java.io.*;
import java.util.List;

/**
 * Created by Amit on 25/02/2015.
 */
public class Date {

    public String replacementDate;
    public String originalTime, name, values;
    private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
    public IImageMetadata metadata = null;

    public void changeDate(Photo photo, String newDate) throws IOException, ImageReadException, ImageWriteException {

        replacementDate = newDate;
        File inputFile = new File(photo.getPath ());
        OutputStream os = null;
        TiffOutputSet tiffOutputSet = null;
        IImageMetadata metaData = Imaging.getMetadata(inputFile);
        JpegImageMetadata jpegMetadata = (JpegImageMetadata) metaData;

        if (null != jpegMetadata) {
            final TiffImageMetadata exif = jpegMetadata.getExif();
            if (null != exif) {
                tiffOutputSet = exif.getOutputSet();
            }
        }
        if (null == tiffOutputSet) {
            tiffOutputSet = new TiffOutputSet();
        }

        if (metaData instanceof JpegImageMetadata) {
            final TiffOutputDirectory exifDirectory = tiffOutputSet.getOrCreateExifDirectory();
            final List<IImageMetadata.IImageMetadataItem> items = jpegMetadata.getItems();

            for (int i = 0; i < items.size(); i++) {
                final IImageMetadata.IImageMetadataItem item = items.get(i);
                name = item.toString().substring(0, item.toString().indexOf(":"));
                values = item.toString().substring(0);
                if (name.contains("DateTimeOriginal")) {
                    String parts[] = values.split(" ");
                    originalTime = parts[2].substring(0,parts[2].length()-1);
                  //System.out.println(values);
                  //System.out.println(originalTime);

                }
            }
        } else {
            System.out.println("Not a jpg file");
        }

        final TiffOutputDirectory exifDirectory = tiffOutputSet.getOrCreateExifDirectory();
        exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
        exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL,newDate+" "+originalTime);
        File outputFile = new File("C:\\Users\\Amit\\Pictures\\Output\\output.jpg");
        os = new FileOutputStream(outputFile);
        os = new BufferedOutputStream(os);
        new ExifRewriter().updateExifMetadataLossless(inputFile, os,tiffOutputSet);

        //Rename output file to input file
        File temp = inputFile;
        inputFile.delete();
        outputFile.renameTo(temp);
    }

}
