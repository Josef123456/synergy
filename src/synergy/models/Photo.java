package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import synergy.database.PhotoDao;
import synergy.engines.suggestion.Engine;
import synergy.metadata.MetaData;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
            String[] dateParts = synergy.metadata.Date.getDate(path).split(":");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            String[] timeParts = synergy.metadata.Date.getTime(path).split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            int second = Integer.parseInt(timeParts[2]);
            this.date = new Date(year - 1900, month - 1, day, hour, minute, second);
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

    public List<Tag> getTags() {
        try {
            List<Tag> tags = PhotoDao.getInstance().getTagsForPhoto(this);
            return tags;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public List<Tag> getSuggestedTags() {
        return Engine.suggest(this);
    }

    public List<List<Relationship>> getRelationshipsForAllTags() {
        List<Tag> tagsForPhoto = this.getChildTags();

	    return tagsForPhoto.stream ().map (Tag::getRelationshipsForTagSortedByOccurrences).collect (Collectors.toList ());
    }

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

    public List<Tag> getChildTags() {
        List<Tag> allTags = getTags();
        return allTags.stream ().filter (tag -> tag.getType () == Tag.TagType.KID).collect (Collectors.toList ());
    }

    public static List<Photo> getAllPhotos() {
        try {
            List<Photo> photos = PhotoDao.getInstance().getAllPhotos();
            List<Photo> photosToRemove = new ArrayList<>();

            for(Photo p : photos){
                if(! new File(p.getPath()).exists()){
                    photosToRemove.add(p);
                    p.delete();
                }
            }
            photos.removeAll(photosToRemove);
            return photos;
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

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

    public void removeTag(Tag tag) {
        this.save();
        tag.save();
        PhotoTag photoTag = new PhotoTag(this, tag);
        photoTag.save();
        photoTag.destroy();
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

    public static List<Photo> getPhotosForDate(Date date) {
        try {
            return PhotoDao.getInstance().getPhotosForDate(date);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public static List<Photo> getPhotosForDatesAndRoomAndKid(LocalDate fromDate, LocalDate
            toDate, Tag room, Tag kid) {
        if (room != null) {
            room.save();
        }
        if (kid != null) {
            kid.save();
        }
        try {
            return PhotoDao.getInstance().getPhotosForDatesAndRoomAndKid(fromDate, toDate, room,
                    kid);
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public void delete() {
        try {
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
