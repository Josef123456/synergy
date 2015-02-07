package synergy.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.sun.javafx.tools.packager.Log;
import org.h2.jdbc.JdbcConnection;

import java.sql.*;


/**
 * Created by alexstoick on 2/6/15.
 */
public class DatabaseConnection {
	private JdbcConnectionSource connection = null;
	private final static String DATABASE_URL = "jdbc:h2:db/sample.db";

	public JdbcConnectionSource getConnection () {
		return connection;
	}

	public DatabaseConnection() {
		try {
			Class.forName("org.h2.Driver");
			connection = new JdbcConnectionSource (DATABASE_URL);
		} catch (Exception e) {
			System.err.println (e.getMessage ());
		}
	}
}
