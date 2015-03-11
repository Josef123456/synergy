package synergy.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by alexstoick on 3/11/15.
 */
public class RelationshipTest extends BaseTest {

	@Test
	public void testRelationshipCreation() throws Exception {
		Tag tag1 = new Tag(Tag.TagType.KID, "Sari");
		Tag tag2 = new Tag(Tag.TagType.KID, "Alex");
		Relationship r = new Relationship (tag1, tag2);
		r.save();
		assertNotEquals (-1, r.getID ());
		assertEquals ( 1 , r.getID ());
		r = new Relationship (tag2, tag1);
		r.save();
		assertEquals ( 1 , r.getID ());
		assertEquals ( 0 , r.getOccurrences ());
		r.increaseOccurences ();
		assertEquals( 1 , r.getOccurrences ());
	}

	@Test
	public void testViataLuSari() throws Exception {
		assertEquals ( 1 , 0 );
	}

}
