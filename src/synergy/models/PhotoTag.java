package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import synergy.database.PhotoTagDao;

/**
 * Created by alexstoick on 2/7/15.
 */
@DatabaseTable(tableName = "photoTags")
public class PhotoTag {
	@DatabaseField(generatedId = true, columnName = _ID)
	private int ID;

	@DatabaseField(foreign = true, columnName = COLUMN_PHOTO_ID, uniqueCombo = true, foreignAutoRefresh = true)
	Photo photo ;

	@DatabaseField(foreign = true, columnName =  COLUMN_TAG_ID, uniqueCombo = true, foreignAutoRefresh = true)
	Tag tag;

	public static final String _ID = "id";
	public static final String COLUMN_PHOTO_ID = "photo_id";
	public static final String COLUMN_TAG_ID = "tag_id";

	public PhotoTag () {
	}

	public Photo getPhoto () {
		return photo;
	}

	public Tag getTag () {
		return tag;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getID () {
		return ID;
	}

	public void destroy() {
		try {
			System.out.println(this);
			PhotoTagDao.getInstance ().destroy (this);
		} catch ( Exception e ) {
			System.err.println (e);
			e.printStackTrace ();
		}
	}

	public void save() {
		try {
			PhotoTagDao.getInstance ().createOrUpdate (this);
		} catch ( Exception e ) {
			System.err.println(e);
			e.printStackTrace ();
		}
	}

	@Override
	public String toString () {
		return "\nPhotoTag{" +
				"ID=" + ID +
				", photo=" + photo +
				", tag=" + tag +
				"}\n";
	}

	public PhotoTag (Photo photo, Tag tag) {
		this.photo = photo;
		this.tag = tag;
	}
}
