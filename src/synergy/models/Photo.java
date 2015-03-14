package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import synergy.database.PhotoDao;
import synergy.engines.suggestion.Engine;

import java.util.*;

/**
 * Created by alexstoick on 2/6/15.
 */
@DatabaseTable(tableName = "photos")
public class Photo {

    @DatabaseField(generatedId = true, columnName = _ID)
    private int ID = -1;
    @DatabaseField(canBeNull = false, columnName = COLUMN_PATH, unique = true)
    private String path;
    @DatabaseField(canBeNull = false, columnName = COLUMN_DATE)
    private Date date;

    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_DATE = "date";
    public static final String _ID = "ID";

    public Photo() {
    }

    public Photo(String path) {
        this.path = path;
        try {
            String[] dateParts = synergy.metadata.Date.getDate (path).split(":");
	        int year = Integer.parseInt (dateParts[0]);
	        int month = Integer.parseInt(dateParts[1]);
	        int day = Integer.parseInt(dateParts[2]);
	        String[] timeParts = synergy.metadata.Date.getTime (path).split(":");
	        int hour = Integer.parseInt(timeParts[0]);
	        int minute = Integer.parseInt(timeParts[1]);
	        int second = Integer.parseInt(timeParts[2]);
            this.date = new Date(year, month-1, day, hour, minute, second);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            PhotoDao.getInstance().createOrUpdate(this);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public int getID() {
        return ID;
    }

    public Date getDate() {
        return date;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

	public List<Tag> getTags () {
		try {
			List<Tag> tags = PhotoDao.getInstance ().getTagsForPhoto (this);
			return tags;
		} catch ( Exception e ) {
			System.err.println (e);
			e.printStackTrace ();
		}
		return null;
	}

	public List<Tag> getSuggestedTags() {
		return Engine.suggest(this);
	}

    public List<List<Relationship>> getRelationshipsForAllTags(){
        List<List<Relationship>> toReturn = new ArrayList<>();
        List<Tag> tagsForPhoto = this.getChildTags();

        if(tagsForPhoto.equals(null))
            return null;

        for(Tag t: tagsForPhoto){
                toReturn.add(t.getRelationshipsForTagSortedByOccurrences());
        }

        return toReturn;
    }

    public List<Tag> getLocationTags() {
        List<Tag> allTags = getTags ();
        List<Tag> locationTags = new ArrayList<> ();
        System.out.println(allTags.size ());
        for(Tag tag:allTags) {
            if ( tag.getType () == Tag.TagType.PLACE )
                locationTags.add (tag);
        }
        return locationTags;
    }

    public List<Tag> getChildTags() {
        List<Tag> allTags = getTags ();
        List<Tag> childTags = new ArrayList<> ();
        for(Tag tag:allTags) {
            if ( tag.getType () == Tag.TagType.KID )
                childTags.add(tag);
        }
        return childTags;
    }

    public static List<Photo> getAllPhotos() {
        try {
            return PhotoDao.getInstance ().getAllPhotos ();
        } catch ( Exception e ) {
            System.err.println(e);
            e.printStackTrace ();
        }
        return null;
    }

	public void addTag(Tag tag) {
        if(!this.getChildTags().isEmpty() && tag.getType() == Tag.TagType.KID){
            for(Tag t:this.getChildTags()){
                Relationship r = new Relationship(tag,t);
            }
        }
        this.save ();
		tag.save();
		PhotoTag photoTag = new PhotoTag (this, tag);
		photoTag.save();
		System.out.println(photoTag);

	}

    public void removeTag(Tag tag) {
	    this.save();
        tag.save();
        PhotoTag photoTag = new PhotoTag(this, tag);
        photoTag.save();
        photoTag.destroy();
    }

    public static ArrayList<Date> getUniqueDates() {
        try {
            List<Photo> photos = PhotoDao.getInstance().getUniqueDates();
            Set<Date> datesSet = new HashSet<>();
            for (Photo tmp : photos) {
                Date date = tmp.getDate();
                System.out.println(date);
                Date tmpDate = new Date(date.getYear(), date.getMonth(), date.getDate());
                System.out.println(tmpDate);
                datesSet.add(tmpDate);
            }
            return new ArrayList<Date>(Arrays.asList(datesSet.toArray(new Date[datesSet.size()
                    ])));
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public static Photo[] getPhotosForDate(Date date) {
        try {
            List<Photo> photos = PhotoDao.getInstance().getPhotosForDate(date);
            return photos.toArray(new Photo[photos.size()]);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return new Photo[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        if (ID != photo.ID) return false;
        if (path != null ? !path.equals(photo.path) : photo.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "\nPhoto{" +
                "ID=" + ID +
                ", path='" + path + '\'' +
                ", date=" + date +
                "}\n";
    }
}
