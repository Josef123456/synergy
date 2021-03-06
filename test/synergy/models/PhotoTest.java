package synergy.models;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		List<Photo> photos = Photo.getPhotosForDate (date);
		assertEquals (1, photos.size());
	}

	@Test public void testPhotoWithoutDate() throws Exception {
		Photo photo = new Photo("/Users/alexstoick/Desktop/ss.png");
		photo.save();
	}

	@Test public void testGetPhotosForDatesAndRoomAndKid() throws Exception {
		Photo photo = new Photo(FILE_PATH);
		photo.save();

		Date photoDate = photo.getDate();
		// Becase of weird formatting of Date ...
		LocalDate toDate = LocalDate.of(photoDate.getYear ()+1900, photoDate.getMonth ()+1, photoDate.getDate ());
		LocalDate fromDate = LocalDate.of (photoDate.getYear ()+1900, photoDate.getMonth ()+1, photoDate.getDate ());

		System.out.println(photoDate + " %%% " );
		List<Photo> photos = Photo.getPhotosForDatesAndRoomAndKids (toDate, fromDate, null, null);
		assertEquals (1 , photos.size ());

		Tag t = new Tag(Tag.TagType.KID, "Sari");
		Tag t1 = new Tag(Tag.TagType.PLACE, "RoomA");

		photo.addTag (t);

		List<Tag> tags = new ArrayList<> ();
		tags.add(t);

		photos = Photo.getPhotosForDatesAndRoomAndKids (toDate, fromDate, null, tags);
		assertEquals (1 , photos.size ());

		photos = Photo.getPhotosForDatesAndRoomAndKids (null, null, null, tags);
		assertEquals (1 , photos.size ());

		photo.removeTag (t);

		photos = Photo.getPhotosForDatesAndRoomAndKids (toDate, fromDate, null, tags);
		assertEquals (0, photos.size ());

		photo.addTag (t);
		photo.addTag (t1);

		photos = Photo.getPhotosForDatesAndRoomAndKids (null, null, t1, tags);
		assertEquals (1 , photos.size ());

		photos = Photo.getPhotosForDatesAndRoomAndKids (null, null, t1, null);
		assertEquals (1 , photos.size ());

		Tag t2 = new Tag(Tag.TagType.KID, "Alex");
		photo.addTag (t2);

		photos = Photo.getPhotosForDatesAndRoomAndKids (null, null, null, tags);
		assertEquals(1, photos.size());
	}

}
