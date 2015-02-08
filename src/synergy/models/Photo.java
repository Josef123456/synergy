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

	public int getID () {
		return ID;
	}

	@Override
	public boolean equals (Object o) {
		if ( this == o ) return true;
		if ( !(o instanceof Photo) ) return false;

		Photo photo = (Photo) o;

		if ( ID != photo.ID ) return false;
		if ( path != null ? !path.equals (photo.path) : photo.path != null ) return false;

		return true;
	}

	@Override
	public int hashCode () {
		return 0;
	}

	@Override
	public String toString () {
		return "Photo{" +
				"path='" + path + '\'' +
				'}';
	}

	public Photo (String path) {
		this.path = path;
	}
}
