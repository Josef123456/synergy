package synergy.metadata;

import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class allows the 'date' field of a jpeg image
 * to read and written to.
 *
 * @author Amit Patel
 * @author Alexandru Stoica
 */
public class Date {

	/**
	 * This method is used to retrieve the date and time of
	 * a given file by passing the path of the file to it.
	 *
	 * @param path Path of the file to be read from
	 * @return String This returns a formatted version of the date and time
	 * @throws IOException input error
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	private static String getDateAndTime(String path) throws IOException, ImageReadException, ImageWriteException {

		File inputFile = new File(path);
		JpegImageMetadata jpegMetadata = (JpegImageMetadata) Imaging.getMetadata(inputFile);
		if ( jpegMetadata != null ) {

			List<IImageMetadata.IImageMetadataItem> items = jpegMetadata.getItems ();
            for (final IImageMetadata.IImageMetadataItem item : items) {
                String name = item.toString().substring(0, item.toString().indexOf(":"));
                String values = item.toString().substring(0);
                if (name.contains("DateTimeOriginal")) {
                    return values.split("\'")[1];
                }
            }
		}

		String s = LocalDateTime.now ().toString ().replace ("T", " ").replace ("-", ":");
		s = s.substring (0, s.length () - 4);
		return s;
	}

	/**
	 * This method is used to return only the time that a photo was taken.
	 *
	 * @param path Path of the file
	 * @return String returns time of the file
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	public static String getTime(String path) throws IOException, ImageReadException, ImageWriteException {
		return getDateAndTime (path).split (" ")[1];
	}

	/**
	 * This method is used to retrieve the date of a photo
	 *
	 * @param path Path of the file
	 * @return String date of the file
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 */
	public static String getDate(String path) throws IOException, ImageReadException, ImageWriteException {
		return getDateAndTime (path).split (" ")[0];
	}

	/**
	 * This method change's the date of a given file, with
	 * the date that is passed as a parameter.
	 * @param path Path of the file
	 * @param newDate The date that is to be set for the file
	 * @throws IOException
	 * @throws ImageReadException
	 * @throws ImageWriteException
	 * @return void
	 */
    public static void changeDate(String path, String newDate) throws IOException, ImageReadException, ImageWriteException {

        OutputStream os;
	    File inputFile = new File(path);
	    JpegImageMetadata jpegMetadata = (JpegImageMetadata) Imaging.getMetadata(inputFile);
	    TiffOutputSet tiffOutputSet = null;
	    if (null != jpegMetadata) {
		    TiffImageMetadata exif = jpegMetadata.getExif();
		    if (null != exif) {
			    tiffOutputSet = exif.getOutputSet();
		    }
	    }

        final TiffOutputDirectory exifDirectory = tiffOutputSet.getOrCreateExifDirectory();
        exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
	    String originalTime = getTime(path);
        exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL,newDate+" "+originalTime);
        File outputFile = new File("tmp.jgp");
        os = new FileOutputStream(outputFile);
        os = new BufferedOutputStream(os);
        new ExifRewriter().updateExifMetadataLossless(inputFile, os,tiffOutputSet);

        //Rename output file to input file
        File temp = inputFile;
        inputFile.delete();
        outputFile.renameTo(temp);
    }

}
