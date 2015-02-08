package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by alexstoick on 2/7/15.
 */
@DatabaseTable(tableName = "photoTags")
public class PhotoTag {
	@DatabaseField(generatedId = true, columnName = _ID)
	private int ID;

	@DatabaseField(foreign = true, columnName = COLUMN_PHOTO_ID)
	Photo photo ;

	@DatabaseField(foreign = true, columnName =  COLUMN_TAG_ID)
	Tag tag;

	public static final String _ID = "id";
	public static final String COLUMN_PHOTO_ID = "photo_id";
	public static final String COLUMN_TAG_ID = "tag_id";

	public PhotoTag () {
	}

	@Override
	public String toString () {
		return "PhotoTag{" +
				"photo=" + photo.getID() +
				", tag=" + tag +
				'}';
	}

	public PhotoTag (Photo photo, Tag tag) {
		this.photo = photo;
		this.tag = tag;
	}
}
