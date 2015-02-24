package synergy.MetaData;

/**
 * Created by Amit on 07/02/2015.
 */
import java.io.*;
import java.util.List;


import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.common.IImageMetadata.IImageMetadataItem;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.util.IoUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import synergy.Utilities.TagEncoder;
import synergy.models.Photo;
import synergy.models.Tag;

public class MetaData {

    public IImageMetadata metadata = null;
    public String name, values, userComment;

    public void getMetaData(File f) {
        try {
            metadata = Imaging.getMetadata(f);
        } catch (ImageReadException | IOException e) {
            System.out.println("File not found");
        }
        if (metadata instanceof JpegImageMetadata) {
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            final List<IImageMetadataItem> items = jpegMetadata.getItems();

            for (int i = 0; i < items.size(); i++) {
                final IImageMetadataItem item = items.get(i);
                name = item.toString().substring(0, item.toString().indexOf(":"));
                values = item.toString();
                //Only print out 'UserComment' tag and its values
                //Remove string to return all meta-data tags
                if (values.contains("UserComment")) {
                    userComment = values;
                    System.out.println(userComment);

                }
            }
        } else {
            System.out.println("Not a jpg file");
        }

    }

    /*
    Modified the example method from apache.commons.imagining library
     */
    public void changeExifMetadata(Photo photo)
            throws IOException, ImageReadException, ImageWriteException {
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

	private String encodeTags(Tag[] tags) {
		String result = "";
		for( Tag tag: tags) {
			result += tag.getType () + " ";
		}
		return "";
	}

}
