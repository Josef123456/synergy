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
	@DatabaseField(columnName = COLUMN_TYPE, uniqueCombo = true)
	private TagType type;

	@DatabaseField(columnName = COLUMN_VALUE, uniqueCombo = true)
	private String value;

	public static final String _ID = "id";
	public static final String COLUMN_TYPE = "tag_type";
	public static final String COLUMN_VALUE = "value";

	public enum TagType {
		    KID, PLACE, EXTRA
	}

	public Tag(){
	}

	public int getID () {
		return ID;
	}

	@Override
	public String toString () {
		return "Tag{" +
				"ID=" + ID +
				", type=" + type +
				", value='" + value + '\'' +
				'}';
	}

	@Override
	public boolean equals (Object o) {
		if ( this == o ) return true;
		if ( !(o instanceof Tag) ) return false;

		Tag tag = (Tag) o;

		if ( ID != tag.ID ) return false;
		if ( type != tag.type ) return false;
		if ( !value.equals (tag.value) ) return false;

		return true;
	}

	public Tag (TagType type, String value) {
		this.type = type;
		this.value = value;
	}
}
