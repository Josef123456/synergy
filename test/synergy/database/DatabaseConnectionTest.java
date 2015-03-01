package synergy.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alexstoick on 2/4/15.
 */
public class DatabaseConnectionTest {
	@Test public void testDatabaseConnection() throws Exception {
		DatabaseConnection databaseConnection = new DatabaseConnection ();
		JdbcConnectionSource connection = databaseConnection.getConnection ();
		assertNotNull (connection);
	}
}
