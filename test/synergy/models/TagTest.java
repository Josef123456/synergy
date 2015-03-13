package synergy.models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by alexstoick on 3/1/15.
 */
public class TagTest extends BaseTest {

	@Test public void testTagCreation() throws Exception {
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();
		assertNotSame (-1, tag.getID ());
	}

	@Test public void testUniqueTag() throws Exception {

		Tag tag = new Tag(Tag.TagType.KID, "Sari");
		tag.save ();
		assertNotEquals (-1, tag.getID ());
		assertEquals (1, tag.getID ());
		tag = new Tag(Tag.TagType.KID, "Sari");
		tag.save();
		assertEquals (1, tag.getID ());
	}

	@Test public void testSuggestedTagsForString() throws Exception {
		Tag tag = new Tag(Tag.TagType.KID, "Sari");
		tag.save ();
		tag = new Tag(Tag.TagType.KID, "Josef");
		tag.save();

		tag = new Tag(Tag.TagType.KID, "Safdj");
		tag.save();

		List<Tag> suggestions = Tag.getSuggestedTagsForString("sa");
		assertNotNull (suggestions);
		assertEquals(2,suggestions.size ());
		assertEquals(tag, suggestions.get (0));
	}

	@Test public void testPhotosForTag() throws Exception {
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		Photo photo = new Photo(FILE_PATH);
		photo.addTag (tag);
		List<Photo> photos = Tag.getPhotosForTag (tag);
		assertEquals (1, photos.size ());
		assertEquals (photo, photos.get (0));
	}
}
