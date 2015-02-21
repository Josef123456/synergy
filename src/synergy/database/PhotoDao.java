package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import synergy.models.Photo;
import synergy.models.PhotoTag;
import synergy.models.Tag;

/**
 * Created by alexstoick on 2/7/15.
 */
public class PhotoDao {
    private static PhotoDao ourInstance = new PhotoDao ();
    private static PreparedQuery<Tag> tagsForPhotoQuery = null;

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

	public void dropTable() throws SQLException {
		TableUtils.dropTable (connection, Photo.class, true );
		TableUtils.createTableIfNotExists (connection, Photo.class);
	}

	public List<Photo> getUniqueDates() throws SQLException {
		QueryBuilder<Photo, Integer> qb = photoDao.queryBuilder ();
		qb.selectColumns (Photo.COLUMN_DATE);
		qb.groupBy (Photo.COLUMN_DATE);
		return photoDao.query(qb.prepare ());
	}

    public void createOrUpdate(Photo photo) throws Exception {
	    List<Photo> photos = photoWithPath (photo.getPath ());
	    if(photos.size() > 0 ) {
		    photo.setID (photos.get (0).getID ());
	    }
	    photoDao.createOrUpdate (photo);
    }

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
