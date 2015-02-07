package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by alexstoick on 2/6/15.
 */
@DatabaseTable(tableName = "photos")
public class Photo {

	@DatabaseField(generatedId=true, columnName = _ID)
	private int ID;
	@DatabaseField(canBeNull = false, columnName =  COLUMN_PATH)
	private String path;

	private static final String TABLE_NAME = "photos";
	public static final String COLUMN_PATH = "path";
	public static final String _ID = "ID";

	public String getPath () {
		return path;
	}

	public Photo(){

	}

	public Photo (String path) {
		this.path = path;
	}
}
