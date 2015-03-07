package synergy.engines.suggestion;

import org.junit.Test;
import synergy.models.BaseTest;
import synergy.models.Photo;
import synergy.models.Tag;

import java.util.List;

/**
 * Created by alexstoick on 2/25/15.
 */
public class EngineTest extends BaseTest {

	@Test public void testSomeFunction() throws Exception {
		//do some shit
		//assertEquals(0, 1);
        Photo p1 = new Photo("/home/sari/Photostest/1.jpg");
        Photo p2 = new Photo("/home/sari/Photostest/2.jpg");
        Photo p3 = new Photo("/home/sari/Photostest/3.jpg");
        Photo p4 = new Photo("/home/sari/Photostest/4.jpg");
        p1.addTag(new Tag(Tag.TagType.KID,"Codrin"));
        //.save();
        p2.addTag(new Tag(Tag.TagType.KID,"Alex"));
        p3.save();
        p4.addTag(new Tag(Tag.TagType.KID,"Codrin"));

        Engine.prepare();
        System.out.println(p1.getChildTags());
        List<Tag> suggested = p3.getSuggestedTags();
        System.out.println(suggested);
//



	}
}
