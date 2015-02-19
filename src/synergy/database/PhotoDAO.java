package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

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

    public void create(Photo photo) throws Exception {
        photoDao.createIfNotExists(photo);
    }

    public List<Tag> getTagsForPhoto(Photo photo) throws SQLException {
        if (tagsForPhotoQuery == null) {
            tagsForPhotoQuery = makeTagsForPhotoQuery();
        }
        tagsForPhotoQuery.setArgumentHolderValue(0, photo);
        return TagDao.getInstance().query(tagsForPhotoQuery);
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
