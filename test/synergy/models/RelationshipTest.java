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

        Tag tag3 = new Tag(Tag.TagType.KID,"KID3");
        Tag tag4 = new Tag(Tag.TagType.KID,"KID4");
		Relationship r = new Relationship (tag1, tag2);
        Relationship r2 = new Relationship(tag1,tag3);

        assertEquals(1, r.getOccurrences());
        assertEquals(1, r2.getOccurrences());
		assertNotEquals(-1, r.getID());
		assertEquals(1, r.getID());
		r = new Relationship (tag2, tag1);
		r.save();
		assertEquals(1, r.getID());
		assertEquals ( 2 , r.getOccurrences ());
        r = new Relationship(tag1, tag2);
        assertEquals(3,r.getOccurrences());
        r = new Relationship(tag2, tag1);
        assertEquals(4,r.getOccurrences());
        r = new Relationship(tag1,tag2);
        assertEquals(5,r.getOccurrences());
        System.out.println(r.getOccurrences());

	}

	@Test
	public void testViataLuSari() throws Exception {


	}

}
