package synergy.database;

import junit.framework.TestCase;
import org.junit.Test;
import synergy.models.Photo;
import synergy.models.PhotoTag;
import synergy.models.Tag;

import java.util.List;

/**
 * Created by alexstoick on 2/4/15.
 */
public class DatabaseConnectionTest extends TestCase {

	@Test
	public void testDatabaseConnection() throws Exception {

		Photo photo = new Photo ("/users/alexstoick/photos/test.jpg");
		PhotoDAO photoDao = PhotoDAO.getInstance() ;
		photoDao.create (photo);

		Tag tag = new Tag (Tag.TagType.PLACE, "main room");
		TagDao tagDao = TagDao.getInstance ();
		tagDao.create(tag);

		Tag tag2 = new Tag(Tag.TagType.EXTRA, "playing with block");
		tagDao.create (tag2);

		PhotoTagDao photoTagDao = PhotoTagDao.getInstance ();
		PhotoTag photoTag = new PhotoTag (photo, tag);
		photoTagDao.create(photoTag);

		PhotoTag photoTag2 = new PhotoTag (photo, tag2);
		photoTagDao.create(photoTag2) ;

		System.out.println ( photoTag) ;
		System.out.println ( photoTag2);

		List<Tag> tags = photoDao.getTagsForPhoto(photo);
		System.out.println(tags.get(0));
		assertEquals(tags.size(), 2);
		assertEquals(tags.get(0), tag);
		assertEquals(tags.get(1), tag2);
	}
}
