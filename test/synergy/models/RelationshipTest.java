package synergy.models;

import org.junit.Test;
import synergy.utilities.RelationshipMerger;

import java.util.List;

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
	public void testMerging() throws Exception {
        Photo p1 = new Photo("/home/sari/Photostest/1.jpg");
        Photo p2 = new Photo("/home/sari/Photostest/2.jpg");
        Photo p3 = new Photo("/home/sari/Photostest/3.jpg");
        Photo p4 = new Photo("/home/sari/Photostest/4.jpg");
        //Sari Serban Alia Jeff Mama Tata Nicu
        p1.addTag(new Tag(Tag.TagType.KID,"Sari"));
        p1.addTag(new Tag(Tag.TagType.KID,"Serban"));
        p2.addTag(new Tag(Tag.TagType.KID,"Sari"));
        p2.addTag(new Tag(Tag.TagType.KID,"Alia"));
        p3.addTag(new Tag(Tag.TagType.KID,"Sari"));
        p3.addTag(new Tag(Tag.TagType.KID,"Alia"));
        p3.addTag(new Tag(Tag.TagType.KID,"Serban"));
        p4.addTag(new Tag(Tag.TagType.KID,"Serban"));
        p4.addTag(new Tag(Tag.TagType.KID,"Jeff"));
        p4.addTag(new Tag(Tag.TagType.KID,"Alia"));
        p4.addTag(new Tag(Tag.TagType.KID,"Sari"));
        System.out.println("ASTA E:"+p1.getRelationshipsForAllTags());
        List<Relationship> mergedList;
        mergedList = RelationshipMerger.mergeNLists(p1.getRelationshipsForAllTags());

        for(Relationship r:mergedList){
            System.out.println(r);
            System.out.println("BAAAAAAAAAAAAAAA"+r);
        }

        mergedList = RelationshipMerger.mergeNLists(p2.getRelationshipsForAllTags());

        for(Relationship r:mergedList){
            System.out.println(r);
            System.out.println("BAAAAAAAAAAAAAAA"+r);
        }

        mergedList = RelationshipMerger.mergeNLists(p3.getRelationshipsForAllTags());

        for(Relationship r:mergedList){
            System.out.println(r);
            System.out.println("BAAAAAAAAAAAAAAA"+r);
        }

        mergedList = RelationshipMerger.mergeNLists(p4.getRelationshipsForAllTags());

        for(Relationship r:mergedList){
            System.out.println(r);
            System.out.println("BAAAAAAAAAAAAAAA"+r);
        }

	}

}
