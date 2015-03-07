package synergy.engines.suggestion;

import org.junit.Test;
import synergy.models.Photo;
import synergy.models.Tag;

import java.util.List;

/**
 * Created by alexstoick on 2/25/15.
 */
public class EngineTest {

	@Test public void testSomeFunction() throws Exception {
		//do some shit
		//assertEquals(0, 1);
        Photo p1 = new Photo("/home/sari/Photostest/1.jpg");
        Photo p2 = new Photo("/home/sari/Photostest/2.jpg");
        Photo p3 = new Photo("/home/sari/Photostest/3.jpg");
        Photo p4 = new Photo("/home/sari/Photostest/4.jpg");
        p1.addTag(new Tag(Tag.TagType.KID,"Codrin"));
        p3.addTag(new Tag(Tag.TagType.KID,"Alex"));

        p4.addTag(new Tag(Tag.TagType.KID,"Codrin"));

        p1.save();
        System.out.println(p1);
        p2.save();
        p3.save();
        p4.save();
        Engine.prepare();

        List<Tag> suggested = p2.getSuggestedTags();
        System.out.println(suggested.get(0).getValue());




	}
}
