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
		assertEquals (0, photo.getTags ().size ());
	}

	@Test public void testPhotoAddTag() throws Exception {
		Photo photo = new Photo (FILE_PATH);
		photo.save ();
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();
		photo.addTag (tag);
		assertEquals (1, photo.getTags ().size());
		assertEquals (tag, photo.getTags ().get(0));
	}

	@Test public void testPhotoRemoveTag() throws Exception{
		Photo photo = new Photo (FILE_PATH);
		photo.save ();
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();
		photo.addTag (tag);
		assertEquals (1, photo.getTags ().size());
		assertEquals (tag, photo.getTags ().get(0));
		photo.removeTag (new Tag (Tag.TagType.PLACE, "main room"));
		assertEquals (0, photo.getTags ().size());
	}

	@Test public void testPhotosForDate() throws Exception {
		Photo photo = new Photo(FILE_PATH);
		photo.save ();
		System.out.println(photo);
		Date date = new Date(115,1,21);
		System.out.println(date);
		Photo[] photos = Photo.getPhotosForDate (date);
		assertEquals (1, photos.length);
	}


	@Test public void testDatabaseConnection() throws Exception {
		Photo photo = new Photo (FILE_PATH);
		photo.save ();

		Photo photo2 = new Photo(DIR_PATH+"20150209-_DSC0727.jpg");
		photo2.save();

		System.out.println(photo);

		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		Tag tag2 = new Tag(Tag.TagType.EXTRA, "playing with block");

		photo.addTag (tag);
		photo.addTag (tag2);

		List<Tag> tags = photo.getTags ();
		assertEquals(2, tags.size());
		assertEquals(tag, tags.get(0));
		assertEquals(tag2, tags.get(1) );

		assertEquals (0, photo2.getTags ().size ());
	}
}
