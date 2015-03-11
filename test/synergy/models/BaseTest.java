package synergy.models;

import org.junit.Before;
import synergy.database.PhotoDao;
import synergy.database.PhotoTagDao;
import synergy.database.RelationshipDao;
import synergy.database.TagDao;

/**
 * Created by alexstoick on 3/1/15.
 */
public class BaseTest {
	protected final static String DIR_PATH = "/Users/alexstoick/Dropbox/edited photos/";
	protected final static String FILE_PATH = DIR_PATH + "20150213-DSC01999.jpg";

	@Before
	public void emptyDatabaseTables() throws Exception {
		PhotoDao.getInstance ().dropTable ();
		TagDao.getInstance ().dropTable ();
		PhotoTagDao.getInstance ().dropTable ();
		RelationshipDao.getInstance ().dropTable ();
	}

}
