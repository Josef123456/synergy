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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alexstoick on 2/6/15.
 */
@DatabaseTable(tableName = "photos")
public class Photo {

	@DatabaseField(generatedId=true, columnName = _ID)
	private int ID = -1;
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
			FileTime createdAt = attr.creationTime ();
			this.date = new Date(createdAt.toMillis ());
		} catch ( Exception e ) {
			System.err.println(e);
			e.printStackTrace ();
		}
	}

	public void save() {
		try {
			PhotoDao.getInstance ().createOrUpdate (this);
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

	public void setID (int ID) {
		this.ID = ID;
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
		tag.save();
		PhotoTag photoTag = new PhotoTag (this, tag);
		photoTag.save();
		System.out.println(photoTag);
	}

	public void removeTag(Tag tag){
		PhotoTag photoTag = new PhotoTag (this, tag);
		photoTag.save();
		photoTag.destroy();
	}

	public static Date[] getUniqueDates() {
		try {
			List<Photo> photos = PhotoDao.getInstance ().getUniqueDates ();
			Set<Date> datesSet = new HashSet<> ();
			for(Photo tmp: photos) {
				Date date = tmp.getDate ();
				System.out.println (date);
				Date tmpDate =new Date (date.getYear (), date.getMonth (), date.getDate ());
				System.out.println (tmpDate);
				datesSet.add (tmpDate);
			}
			return datesSet.toArray (new Date[datesSet.size ()]);
		} catch ( Exception e ) {
			System.err.println (e) ;
			e.printStackTrace ();
		}
		return new Date[0];
	}

	public static Photo[] getPhotosForDate(Date date){
		//TODO: impelement this
		return null;
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
		return "\nPhoto{" +
				"ID=" + ID +
				", path='" + path + '\'' +
				", date=" + date +
				"}\n";
	}
}
