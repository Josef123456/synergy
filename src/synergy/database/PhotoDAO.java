package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;
import synergy.models.Photo;
import synergy.models.PhotoTags;
import synergy.models.Tag;

import java.sql.SQLException;

/**
 * Created by alexstoick on 2/7/15.
 */
public class PhotoDao {
	private static PhotoDao ourInstance = new PhotoDao ();

	public static PhotoDao getInstance () {
		return ourInstance;
	}

	private Dao<Photo, Integer> photoDao;

	private static JdbcConnectionSource connection ;

	private PhotoDao () {
		try {
			connection = new DatabaseConnection ().getConnection ();
			photoDao = DaoManager.createDao (connection, Photo.class);
			TableUtils.createTableIfNotExists (connection, Photo.class);
		} catch ( Exception e ) {
			System.err.println ( e.toString () ) ;
		}
	}

	public PreparedQuery<Tag> makeTagsForPhotoQuery() throws SQLException {
		QueryBuilder<PhotoTags, Integer>  photoTagsQueryBuilder = PhotoTagsDao.getInstance ().getQueryBuilder ();
		photoTagsQueryBuilder.selectColumns (PhotoTags.COLUMN_PHOTO_ID);
		SelectArg postSelectArg = new SelectArg ();
		photoTagsQueryBuilder.where().eq(PhotoTags.COLUMN_PHOTO_ID, postSelectArg);

		QueryBuilder<Tag, Integer> tagQueryBuilder = TagDao.getInstance().getQueryBuilder ();
		tagQueryBuilder.where().in(Tag._ID, photoTagsQueryBuilder);

		return tagQueryBuilder.prepare();
	}
}
