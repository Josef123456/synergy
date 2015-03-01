package synergy.models;

import org.junit.Before;
import org.junit.Test;
import synergy.database.PhotoDao;
import synergy.database.PhotoTagDao;
import synergy.database.TagDao;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Created by alexstoick on 3/1/15.
 */
public class PhotoTest extends BaseTest{


	@Test
	public void testUniqueDates() throws Exception {
		Photo photo = new Photo(FILE_PATH);
		photo.save();
		photo = new Photo(DIR_PATH + "20150209-_DSC0727.jpg");
		photo.save();
		ArrayList<Date> dates = Photo.getUniqueDates();
		assertEquals (2, dates.size());
	}

	@Test public void testPhotoCreation() throws Exception {
		Photo photo = new Photo (FILE_PATH);
		photo.save ();
		assertNotSame (-1, photo.getID ());
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

}
