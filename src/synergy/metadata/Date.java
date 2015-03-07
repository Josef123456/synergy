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

	private static String getDateAndTime(String path) throws IOException, ImageReadException, ImageWriteException {

		File inputFile = new File(path);
		JpegImageMetadata jpegMetadata = (JpegImageMetadata) Imaging.getMetadata(inputFile);
		List<IImageMetadata.IImageMetadataItem> items = jpegMetadata.getItems ();
		for ( int i = 0 ; i < items.size () ; i++ ) {
			final IImageMetadata.IImageMetadataItem item = items.get (i);
			String name = item.toString ().substring (0, item.toString ().indexOf (":"));
			String values = item.toString ().substring (0);
			if ( name.contains ("DateTimeOriginal") ) {
				String dateAndTime = values.split ("\'")[ 1 ];
				return dateAndTime;
			}
		}
		return null;
	}

	public static String getTime(String path) throws IOException, ImageReadException, ImageWriteException {
		return getDateAndTime (path).split (" ")[1];
	}
	public static String getDate(String path) throws IOException, ImageReadException, ImageWriteException {
		return getDateAndTime (path).split (" ")[0];
	}

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
