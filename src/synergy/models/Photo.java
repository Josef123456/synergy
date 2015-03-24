package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import synergy.database.PhotoDao;
import synergy.engines.suggestion.Engine;
import synergy.metadata.MetaData;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the main model of the application. This is tightly connected to {@link synergy.database.PhotoDao}
 * and it abstracts away the database layer. This facilitates an easy integration for the front-end team that only
 * works with this object.
 * The class has a few annotations that work with our database library ormLite. The {@value #COLUMN_PATH},
 * {@value #COLUMN_DATE}, {@value #_ID} are the names of the column in the database. There is also a unique
 * constraint on the path, which makes sure that we can't import the same photo twice.
 *
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

	/**
	 * Instantiates a photo using the path. It also sets the date of the photo by opening the file
	 * and parsing the date the photo was taken at.
	 * @param path the path of the file we want to instantiate.
	 */
    public Photo(String path) {
        this.path = path;
        try {
            String[] dateParts = synergy.metadata.Date.getDate(path).split(":");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            String[] timeParts = synergy.metadata.Date.getTime(path).split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            int second = Integer.parseInt(timeParts[2]);
            this.date = new Date(year - 1900, month - 1, day, hour, minute, second);
	        List<Tag> tags = MetaData.getTagsForFile(path);
	        tags.forEach (t -> this.addTag (t));
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

	/**
	 * This method pushes the object into the database. After calling this, the object is saved and the {@link #ID}
	 * field is populated.
	 */
    public void save() {
        try {
            PhotoDao.getInstance().createOrUpdate(this);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

	/**
	 *
	 * @return {@link java.lang.String} with the path of the photo.
	 */
    public String getPath() {
        return path;
    }

	/**
	 *
	 * @return {@link int} with the database ID of the photo.
	 */
    public int getID() {
        return ID;
    }

	/**
	 *
	 * @return {@link java.util.Date} of the current photo.
	 */
    public Date getDate() {
        return date;
    }

	/**
	 * Sets the database ID of this photo.
	 * @param ID
	 */
    public void setID(int ID) {
        this.ID = ID;
    }

	/**
	 *
	 * @return an array of {@link synergy.models.Tag} that are associated with this photo.
	 */
    public List<Tag> getTags() {
        try {
            return PhotoDao.getInstance().getTagsForPhoto(this);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * Using the {@link synergy.engines.suggestion.Engine} we generate suggestions for this photo.
	 * @return an array of suggested {@link synergy.models.Tag}
	 */
    public List<Tag> getSuggestedTags() {
        return Engine.suggest(this);
    }

	/**
	 * This returns the {@link synergy.models.Relationship} for each {@link synergy.models.Tag} that is associated with
	 * this photo.
	 * @return a list of {@link synergy.models.Relationship}
	 */
    public List<List<Relationship>> getRelationshipsForAllTags() {
        List<Tag> tagsForPhoto = this.getChildTags();

	    return tagsForPhoto.stream ().map (Tag::getRelationshipsForTagSortedByOccurrences).collect (Collectors.toList ());
    }

	/**
	 * Get the location associated with this photo. (RoomA/RoomB)
	 * @return a {@link synergy.models.Tag} that represents the location
	 */
    public Tag getLocationTag() {
        List<Tag> allTags = getTags();
        Tag locationTag = null;
        for (Tag tag : allTags) {
            if (tag.getType() == Tag.TagType.PLACE) {
                locationTag = tag;
                break;
            }
        }
        return locationTag;
    }

	/**
	 *
	 * @return a {@link java.util.List} of {@link synergy.models.Tag} that are the kids tagged in the photo
	 */
    public List<Tag> getChildTags() {
        List<Tag> allTags = getTags();
        return allTags.stream ().filter (tag -> tag.getType () == Tag.TagType.KID).collect (Collectors.toList ());
    }

	/**
	 * This method is used to display all of the available photos in the database.
	 * @return a {@link java.util.List} of {@link synergy.models.Photo}
	 */
    public static List<Photo> getAllPhotos() {
        try {
            List<Photo> photos = PhotoDao.getInstance().getAllPhotos();
            List<Photo> photosToRemove = new ArrayList<>();

            photos.stream().filter(p -> !new File(p.getPath()).exists()).forEach(p -> {
                photosToRemove.add(p);
                p.delete();
            });
            photos.removeAll(photosToRemove);
            return photos;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * Add the {@link synergy.models.Tag} to the list of tags associated with the photo. This also
	 * creates a new {@link synergy.models.PhotoTag} that marks the relationship.
	 * @param tag the tag that has to be added to the photo.
	 */
    public void addTag(Tag tag) {
        this.save();
        tag.save();
        PhotoTag photoTag = new PhotoTag(this, tag);
        photoTag.save();
        System.out.println("adding: " + photoTag);
	    try {
		    MetaData.changeExifMetadata (this);
	    } catch ( Exception e ) {
		    System.err.println (e);
		    e.printStackTrace ();
	    }
        if (!this.getChildTags().isEmpty() && tag.getType() == Tag.TagType.KID) {
            for (Tag t : this.getChildTags()) {
                new Relationship(tag, t);
            }
        }
    }

	/**
	 * Removes the {@link synergy.models.Tag} from this photo. Also removes the {@link synergy.models.PhotoTag} that
	 * is associated with this.
	 * @param tag
	 */
    public void removeTag(Tag tag) {
        this.save();
        tag.save();
        PhotoTag photoTag = new PhotoTag(this, tag);
        photoTag.save();
        photoTag.destroy();
	    try {
		    MetaData.changeExifMetadata (this);
	    } catch ( Exception e ) {
		    System.err.println (e);
		    e.printStackTrace ();
	    }
        List<Relationship> relList = tag.getRelationshipsForTagSortedByOccurrences();
	    relList.stream ().filter (r -> this.getChildTags ().contains (r.getPartner (tag))).forEach (r -> {
		    System.out.println ("Occurrences before: " + r);
		    r.decreaseOccurrences ();
		    System.out.println ("Occurrences after: " + r);
	    });
    }

    public static ArrayList<Date> getUniqueDates() {
        try {
            List<Photo> photos = PhotoDao.getInstance().getUniqueDates();
            Set<Date> datesSet = new HashSet<>();
            for (Photo tmp : photos) {
                Date date = tmp.getDate();
                Date tmpDate = new Date(date.getYear(), date.getMonth(), date.getDate());
                datesSet.add(tmpDate);
            }
            return new ArrayList<>(Arrays.asList(datesSet.toArray(new Date[datesSet.size()
                    ])));
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

	public static Object[] getUniqueYears() {
		List<Date> uniqueDates = getUniqueDates ();
		Set<String> uniqueYears = new HashSet ();
		for ( Date uniqueDate : uniqueDates ) {
			uniqueYears.add (new SimpleDateFormat ("yyyy").format (uniqueDate));
		}
		return uniqueYears.toArray ();
	}

    public static List<Photo> getPhotosForDate(Date date) {
        try {
            return PhotoDao.getInstance().getPhotosForDate(date);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public static List<Photo> getPhotosForDatesAndRoomAndKids (LocalDate fromDate, LocalDate
		    toDate, Tag room, List<Tag> kids) {
        if (room != null) {
            room.save();
        }
        if (kids != null) {
	        kids.forEach (synergy.models.Tag::save);
        }
        try {
            return PhotoDao.getInstance().getPhotosForDatesAndRoomAndKid(fromDate, toDate, room,
		            kids);
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public void delete() {
        try {
            this.getChildTags().forEach(this::removeTag);
            PhotoDao.getInstance().delete(this);
//			File f = new File(path);
//			f.delete();
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        return ID == photo.ID && !(path != null ? !path.equals(photo.path) : photo.path != null);

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
