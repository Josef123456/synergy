package synergy.database;

import com.sun.javafx.tools.packager.Log;

import java.sql.*;

/**
 * Created by alexstoick on 2/6/15.
 */
public class DatabaseConnection {
	private static DatabaseConnection ourInstance = new DatabaseConnection ();

	public static DatabaseConnection getInstance () {
		return ourInstance;
	}

	private static Connection connection = null ;

	public Boolean executeUpdateStatement( String SQLQuery ){
		try {
			connection.createStatement ().executeUpdate (SQLQuery);
			return true;
		} catch ( SQLException e ) {
			Log.debug( e.toString () );
			return false ;
		}
	}

	public ResultSet executeQueryStatement ( String SQLQuery ) {
		try {
			ResultSet resultSet = connection.createStatement ().executeQuery (SQLQuery);
			return resultSet;
		} catch ( SQLException e ) {
			Log.debug( e.toString () );
		}
		return null;
	}

	private DatabaseConnection () {
		try {
			connection = DriverManager.getConnection ("jdbc:sqlite:db/sample.db");
		} catch (SQLException e) {
			System.err.println (e.getMessage ());
		}
	}
}
