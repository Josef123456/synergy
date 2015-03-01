package synergy.models;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;

/**
 * Created by alexstoick on 3/1/15.
 */
public class TagTest extends BaseTest {

	@Test
	public void testTagCreation() throws Exception {
		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		tag.save();
		assertNotSame (-1, tag.getID ());
	}

}
