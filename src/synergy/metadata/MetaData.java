package synergy.metadata;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.common.IImageMetadata.IImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.util.IoUtils;
import synergy.models.Photo;
import synergy.models.Tag;
import synergy.utilities.TagEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows manipulation and reading of MetaData from
 * images of the JPEG format and that format only.
 *
 * @author Amit Patel
 */

public class MetaData {

	/**
     * Attempts to retrieve & list the metadata attributes of a given File
     * @param file File object that is to be read from
     * @return void
     */
    private static String[] getMetaData(File file) {
	    IImageMetadata metadata = null;
        try {
            metadata = Imaging.getMetadata(file);
        } catch (ImageReadException | IOException e) {
            System.out.println("File not found");
        }
        if (metadata instanceof JpegImageMetadata) {
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            final List<IImageMetadataItem> items = jpegMetadata.getItems();

            for (final IImageMetadataItem item : items) {
	            String x = item.toString ();
	            String name = x.substring (0, x.indexOf (":"));
	            if ( name.contains ("UserComment")) {
		            return x.substring(x.indexOf(":")+3, x.length ()).split("\\|");
                }
            }
        } else {
            System.out.println("Not a jpg file");
        }
	    return new String[0];
    }

	public static List<Tag> getTagsForFile(String path) {
		File file = new File(path);
		String[] stringTags = getMetaData (file);
		List<Tag> tags = new ArrayList<> ();
		for ( int i = 0 ; i < stringTags.length -1 ; ++ i) {
			String[] parts = stringTags[i].split(":");
			System.out.println (parts[0] + " " + parts[1]);
			if ( parts[0].equals("PLACE") ) {
				tags.add( new Tag(Tag.TagType.PLACE, parts[1]));
			}
			if ( parts[0].equals("KID") ) {
				tags.add( new Tag(Tag.TagType.KID, parts[1]));
			}
		}
		return tags;
	}

    /**
     *This method is used to change an 'Exif Tag' of a specific photo object
     *the photo must be of JPEG format in order to have this tag
     *
     *
     * @param photo
     * @throws IOException on input error
     * @throws ImageReadException failed to read the image
     * @throws ImageWriteException failed to write to the image
     * @return void
     */
    public static void changeExifMetadata(Photo photo) throws IOException, ImageReadException, ImageWriteException {
	    final File inputFile = new File(photo.getPath ());
        OutputStream os = null;
        boolean canThrow = false;
        try {
            TiffOutputSet outputSet = null;

            // metadata can be null if none is attached to the jpg
            final IImageMetadata metadata = Imaging.getMetadata(inputFile);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                // exif data can be null if none is found
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
            }

            // if file does not contain any exif metadata, we create an empty
            // set of exif metadata.
            if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }

            // TagInfo constants often contain a description of what
            // directories are associated with a given tag.
            final TiffOutputDirectory exifDirectory = outputSet
                    .getOrCreateExifDirectory();
            //Remove old field and replace with it with a new one.
            exifDirectory.removeField(ExifTagConstants.EXIF_TAG_USER_COMMENT);
            exifDirectory.add(ExifTagConstants.EXIF_TAG_USER_COMMENT, TagEncoder.encodeTagArray (photo.getTags()));

            File outputFile = new File("tmp2.jpg");
            os = new FileOutputStream(outputFile);
            os = new BufferedOutputStream(os);

            new ExifRewriter().updateExifMetadataLossless(inputFile, os,
                    outputSet);

            //Rename output file to input file
            File temp = inputFile;
            inputFile.delete();
            outputFile.renameTo(temp);

            canThrow = true;
        } finally {
            IoUtils.closeQuietly(canThrow, os);
        }

    }
}
