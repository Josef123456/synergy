package synergy.database;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import synergy.models.Photo;
import synergy.models.PhotoTag;
import synergy.models.Tag;

import java.util.*;

/**
 * Created by alexstoick on 2/4/15.
 */
public class DatabaseConnectionTest {

	private final static String DIR_PATH = "/Users/alexstoick/Dropbox/edited photos/";
	private final static String FILE_PATH = DIR_PATH + "20150213-DSC01999.jpg";

	@Before
	public void emptyDatabaseTables() throws Exception {
		PhotoDao.getInstance ().dropTable ();
		TagDao.getInstance ().dropTable ();
		PhotoTagDao.getInstance ().dropTable ();
	}

	@Test public void testUniqueDates() throws Exception {
		Photo photo = new Photo(FILE_PATH);
		photo.save();
		photo = new Photo(DIR_PATH + "20150209-_DSC0727.jpg");
		photo.save();
		Date[] dates = Photo.getUniqueDates();
		assertEquals (2, dates.length);
	}

	@Test public void testPhotoCreation() throws Exception {
		Photo photo = new Photo (FILE_PATH);
		photo.save ();
		assertNotSame (-1, photo.getID ());
	}

	@Test public void testTagCreation() throws Exception {
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();
		assertNotSame (-1, tag.getID ());
	}

	@Test public void testPhotoTagCreation() throws Exception {
		Photo photo = new Photo();
		Tag tag = new Tag();
		PhotoTag photoTag = new PhotoTag (photo,tag);
		photoTag.save();
		assertNotSame (-1, photoTag.getID ());
	}

	@Test public void testPhotoGetTags() throws Exception {
		Photo photo = new Photo (FILE_PATH);
		photo.save ();
		assertEquals (0, photo.getTags ().length);
	}

	@Test public void testPhotoAddTag() throws Exception {
		Photo photo = new Photo (FILE_PATH);
		photo.save ();
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();
		photo.addTag (tag);
		assertEquals (1, photo.getTags ().length);
		assertEquals (tag, photo.getTags ()[0]);
	}

	@Test public void testPhotoRemoveTag() throws Exception{
		Photo photo = new Photo (FILE_PATH);
		photo.save ();
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();
		photo.addTag (tag);
		assertEquals (1, photo.getTags ().length);
		assertEquals (tag, photo.getTags ()[0]);
		photo.removeTag (tag);
		assertEquals (0, photo.getTags ().length);
	}


	@Test public void testDatabaseConnection() throws Exception {
		Photo photo = new Photo (FILE_PATH);
		photo.save ();

		System.out.println(photo);

		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();

		System.out.println(tag);

		Tag tag2 = new Tag(Tag.TagType.EXTRA, "playing with block");
		tag2.save();

		photo.addTag (tag);
		photo.addTag (tag2);

		Tag[] tags = photo.getTags ();
		assertEquals(2, tags.length);
		assertEquals(tag, tags[0]);
		assertEquals(tag2, tags[1] );

		photo.removeTag (tag);
		tags = photo.getTags ();
		assertEquals(1, tags.length);
		assertEquals(tag2, tags[0] );
	}
}
