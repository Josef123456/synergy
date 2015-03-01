package synergy.models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Created by alexstoick on 3/1/15.
 */
public class TagTest extends BaseTest {

	@Test public void testTagCreation() throws Exception {
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();
		assertNotSame (-1, tag.getID ());
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
