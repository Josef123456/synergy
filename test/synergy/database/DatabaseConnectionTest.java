package synergy.database;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by alexstoick on 2/4/15.
 */
public class DatabaseConnectionTest extends TestCase {

	@Test
	public void testDatabaseConnection() throws Exception {
		DatabaseConnection db =  DatabaseConnection.getInstance ();
		assertEquals(db.hashCode (),2);
	}

}
