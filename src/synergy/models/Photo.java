package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import synergy.database.PhotoDao;
import synergy.database.PhotoTagDao;
import synergy.database.TagDao;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.List;

/**
 * Created by alexstoick on 2/6/15.
 */
@DatabaseTable(tableName = "photos")
public class Photo {

	@DatabaseField(generatedId=true, columnName = _ID)
	private int ID;
	@DatabaseField(canBeNull = false, columnName =  COLUMN_PATH, unique = true)
	private String path;
	@DatabaseField(canBeNull = false, columnName = COLUMN_DATE)
	private Date date;

	public static final String COLUMN_PATH = "path";
	public static final String COLUMN_DATE = "date";
	public static final String _ID = "ID";

	public Photo(){ }

	public Photo (String path) {
		this.path = path;
		try {
			Path p = Paths.get (path);
			BasicFileAttributes attr = Files.readAttributes (p, BasicFileAttributes.class);
			System.out.println ("creationTime: " + attr.creationTime ());
			FileTime createdAt = attr.creationTime ();
			this.date = new Date(createdAt.toMillis ());
		} catch ( Exception e ) {
			System.err.println(e);
			e.printStackTrace ();
		}
	}

	public String getPath () {
		return path;
	}

	public int getID () {
		return ID;
	}

	public Date getDate () {
		return date;
	}

	public Tag[] getTags () {
		try {
			List<Tag> tags = PhotoDao.getInstance ().getTagsForPhoto (this);
			return tags.toArray (new Tag[tags.size ()]);
		} catch ( Exception e ) {
			System.err.println (e);
			e.printStackTrace ();
		}
		return null;
	}

	public void addTag(Tag tag) {
		try {
			TagDao.getInstance ().create (tag);
			PhotoTag photoTag = new PhotoTag (this, tag);
			PhotoTagDao.getInstance ().create(photoTag);
		} catch ( Exception e ) {
			System.err.println(e);
			e.printStackTrace ();
		}
	}

	public void removeTag(Tag tag){
		try {
			PhotoTag photoTag = new PhotoTag (this, tag);
			PhotoTagDao.getInstance ().destroy(photoTag);
		} catch ( Exception e ) {
			System.err.println (e);
			e.printStackTrace ();
		}
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

}
