package synergy.models;

import javafx.scene.chart.PieChart;
import synergy.database.DatabaseConnection;
import synergy.database.DatabaseException;
import synergy.database.DatabaseFunctions;

/**
 * Created by alexstoick on 2/6/15.
 */
public class Photo extends DatabaseObject implements DatabaseFunctions {
	private String path;
	Tag[] tags;

	private static final String TABLE_NAME = "photos";
	private static final String COLUMN_PATH = "path";
	private static final String _ID = "ID";

	public String getPath () {
		return path;
	}

	@Override
	public void createTable () throws DatabaseException {
		String SQL =
				"CREATE TABLE " + TABLE_NAME +"(" +
				COLUMN_PATH + "TEXT " +
				_ID + " INTEGER PRIMARY KEY AUTOINCREMENT)" ;
		DatabaseConnection.getInstance ().executeUpdateStatement (SQL);
	}

	@Override
	public int insertObject (DatabaseObject object) throws DatabaseException {
		if (! (object instanceof Photo)) {
			throw new DatabaseException ("Object of wrong type");
		}
		Photo photo = (Photo) object;
		String SQL = "INSERT INTO " + TABLE_NAME + "(" +
				COLUMN_PATH + ") VALUES (" + photo.getPath() + ")";
		return 0;
	}

	@Override
	public int deleteObject (DatabaseObject object) throws DatabaseException {
		return 0;
	}
}
