package synergy.models;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;

/**
 * Created by alexstoick on 3/1/15.
 */
public class PhotoTagTest {
	@Test
	public void testPhotoTagCreation() throws Exception {
		Photo photo = new Photo();
		Tag tag = new Tag();
		PhotoTag photoTag = new PhotoTag (photo,tag);
		photoTag.save();
		assertNotSame (-1, photoTag.getID ());
	}

}
