package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by alexstoick on 2/6/15.
 */
@DatabaseTable(tableName = "tags")
public class Tag {
	@DatabaseField(generatedId = true, columnName = _ID)
	private int ID;
	@DatabaseField(columnName = COLUMN_TYPE)
	private TagType type;

	public static final String _ID = "id";
	public static final String COLUMN_TYPE = "tag_type" ;

	public enum TagType {
		    KID, PLACE, EXTRA
	}

	public Tag(){
	}
}
