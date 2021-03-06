package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import synergy.models.Photo;
import synergy.models.PhotoTag;
import synergy.models.Tag;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Data access object class for {@link synergy.models.Photo}
 */
public class PhotoDao {
    private static PhotoDao ourInstance = new PhotoDao ();
    private static PreparedQuery<Tag> tagsForPhotoQuery = null;

    /**
     * Get the instance of the singleton.
     * @return The instance of this class.
     */
    public static PhotoDao getInstance() {
        return ourInstance;
    }

    private Dao<Photo, Integer> photoDao;

    private static JdbcConnectionSource connection;

    private PhotoDao () {
        try {
            connection = new DatabaseConnection().getConnection();
            photoDao = DaoManager.createDao(connection, Photo.class);
            TableUtils.createTableIfNotExists (connection, Photo.class);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Drops the database table.
     * @throws SQLException
     */
	public void dropTable() throws SQLException {
		TableUtils.dropTable (connection, Photo.class, true );
		TableUtils.createTableIfNotExists (connection, Photo.class);
	}

    /**
     * Get all the dates on which photos are present.
     * @return A {@link java.util.List} list of {@link synergy.models.Photo}
     * @throws SQLException
     */
	public List<Photo> getUniqueDates() throws SQLException {
		QueryBuilder<Photo, Integer> qb = photoDao.queryBuilder ();
		qb.selectColumns (Photo.COLUMN_DATE);
		qb.groupBy (Photo.COLUMN_DATE);
		return photoDao.query(qb.prepare ());
	}

    /**
     * Gets all the photos found at a {@link java.util.Date}.
     * @param date The date we want the photos from.
     * @return All the photos on the date.
     * @throws SQLException
     */
	public List<Photo> getPhotosForDate(Date date) throws SQLException {
		QueryBuilder<Photo, Integer> qb = photoDao.queryBuilder ();
		Date startOfDay = new Date(date.getYear (), date.getMonth (), date.getDate (),0,0,0);
		Date endOfDay = new Date(date.getYear (), date.getMonth (), date.getDate (),23,59,59);
		qb.where ().between (Photo.COLUMN_DATE, startOfDay, endOfDay);
		return photoDao.query (qb.prepare ());
	}

    /**
     * Creates or updates a {@link synergy.models.Photo} in the database.
     * @param photo Photo to create or update.
     * @throws Exception
     */
    public void createOrUpdate(Photo photo) throws Exception {
	    List<Photo> photos = photoWithPath (photo.getPath ());
	    if(photos.size() > 0 ) {
		    photo.setID (photos.get (0).getID ());
	    }
	    photoDao.createOrUpdate (photo);
    }

    /**
     * Deletes a photo from the database.
     * @param photo Photo to be deleted.
     * @throws SQLException
     */
	public void delete(Photo photo) throws SQLException {
		photoDao.delete (photo);
	}

    /**
     * Gets all photos mactching the date interval with the specific room and kids.
     * @param fromDate Start of the date interval.
     * @param toDate End of the date interval.
     * @param room The room tag that must be contained.
     * @param kids The kid tags that must be contained.
     * @return A {@link java.util.List} of all photos matching the criteria.
     * @throws SQLException
     */
	public List<Photo>getPhotosForDatesAndRoomAndKid(LocalDate fromDate, LocalDate toDate, Tag room, List<Tag> kids)
			throws SQLException {
		QueryBuilder<Photo, Integer> qb = photoDao.queryBuilder ();
		Where where = qb.where ();
		int count = 0 ;
		int roomTag = 0 ;
		int kidTag = 0;
		if ( toDate != null && fromDate != null ) {
			Date startOfDay = new Date(fromDate.getYear ()-1900, fromDate.getMonthValue ()-1, fromDate.getDayOfMonth (),0,0,0);
			Date endOfDay = new Date(toDate.getYear ()-1900, toDate.getMonthValue ()-1, toDate.getDayOfMonth(),23,59,59);
			System.out.println(fromDate);
			System.out.println ("photo dao; init date " + startOfDay);
			System.out.println ("photo dao; end date " + endOfDay);
			where.between (Photo.COLUMN_DATE, startOfDay, endOfDay);
			++ count ;
			roomTag = kidTag = 2;
		}
		if ( room != null ) {
			makePhotoForTagQuery (where);
			++ count ;
			kidTag ++ ;
		}
		if ( kids != null ) {
			for ( int i = 0 ; i < kids.size (); ++ i ) {
				makePhotoForTagQuery (where);
			}
			count += kids.size();
		}
		System.out.println(where);
		System.out.println(count);
		if ( count == 0 ) {
			return photoDao.queryForAll ();
		} else {
			PreparedQuery preparedQuery = where.and (count).prepare ();
			if ( room != null ) {
				preparedQuery.setArgumentHolderValue (roomTag, room);
			}
			if ( kids != null ) {
				for(Tag kid: kids) {
					preparedQuery.setArgumentHolderValue (kidTag++, kid);
				}
			}
			System.out.println ("PHOTO DAO: QUERY:::" + qb.prepareStatementString());
			return photoDao.query (preparedQuery);
		}
	}

    /**
     * Gets all photos in the database.
     * @return Returns a list of all photos found in the database.
     * @throws SQLException
     */
	public List<Photo> getAllPhotos() throws SQLException {
		return photoDao.queryForAll ();
	}

    /**
     * Returns photos based on a query.
     * @param preparedQuery The query we want to use.
     * @return Photos from query.
     * @throws SQLException
     */
	public List<Photo> query(PreparedQuery<Photo> preparedQuery) throws SQLException {
		return photoDao.query (preparedQuery);
	}

    /**
     * Builds a query.
     * @return
     */
	public QueryBuilder<Photo,Integer> getQueryBuilder() {
		return photoDao.queryBuilder ();
	}

    /**
     * Get tags for a specific photo.
     * @param photo Photo we want tags for.
     * @return Tags from specified photo.
     * @throws SQLException
     */
    public List<Tag> getTagsForPhoto(Photo photo) throws SQLException {
        if (tagsForPhotoQuery == null) {
            tagsForPhotoQuery = makeTagsForPhotoQuery();
        }
        tagsForPhotoQuery.setArgumentHolderValue(0, photo);
        return TagDao.getInstance().query(tagsForPhotoQuery);
    }

	private List<Photo> photoWithPath(String path) throws SQLException {
		QueryBuilder<Photo, Integer> qb = photoDao.queryBuilder ();
		qb.where ().eq (Photo.COLUMN_PATH, path);
		return photoDao.query(qb.prepare ());
	}

	private Where makePhotoForTagQuery(Where where) throws SQLException {
		QueryBuilder<PhotoTag, Integer> photoTagsQueryBuilder = PhotoTagDao.getInstance()
				.getQueryBuilder();
		photoTagsQueryBuilder.selectColumns(PhotoTag.COLUMN_PHOTO_ID);
		SelectArg postSelectArg = new SelectArg();
		photoTagsQueryBuilder.where().eq(PhotoTag.COLUMN_TAG_ID, postSelectArg);

		return where.in (Photo._ID, photoTagsQueryBuilder);
	}

    private PreparedQuery<Tag> makeTagsForPhotoQuery() throws SQLException {
        QueryBuilder<PhotoTag, Integer> photoTagsQueryBuilder = PhotoTagDao.getInstance()
                .getQueryBuilder();
        photoTagsQueryBuilder.selectColumns(PhotoTag.COLUMN_TAG_ID);
        SelectArg postSelectArg = new SelectArg();
        photoTagsQueryBuilder.where().eq(PhotoTag.COLUMN_PHOTO_ID, postSelectArg);

        QueryBuilder<Tag, Integer> tagQueryBuilder = TagDao.getInstance().getQueryBuilder();
        tagQueryBuilder.where().in(Tag._ID, photoTagsQueryBuilder);

        return tagQueryBuilder.prepare();
    }
}
