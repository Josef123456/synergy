package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import synergy.models.PhotoTag;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by alexstoick on 2/7/15.
 */
public class PhotoTagDao {
	private static PhotoTagDao ourInstance = new PhotoTagDao ();

	public static PhotoTagDao getInstance () {
		return ourInstance;
	}

	private Dao<PhotoTag, Integer> photoTagDao;

	private static JdbcConnectionSource connection ;

	private PhotoTagDao () {
		try {
			connection = new DatabaseConnection ().getConnection ();
			photoTagDao = DaoManager.createDao (connection, PhotoTag.class);
			TableUtils.createTableIfNotExists (connection, PhotoTag.class);
		} catch ( Exception e ) {
			System.err.println ( e.toString () ) ;
			e.printStackTrace ();
		}
	}

	public void createOrUpdate(PhotoTag photoTag) throws Exception {
		List<PhotoTag> photoTags = photoTagsWithPhotoIdAndTagId(
				photoTag.getPhoto ().getID (),
				photoTag.getTag ().getID ()
		);
		if ( photoTags.size () > 0 ) {
			photoTag.setID(photoTags.get (0).getID());
		}
		photoTagDao.createOrUpdate (photoTag);

	}

	public void dropTable() throws SQLException {
		TableUtils.dropTable (connection, PhotoTag.class, true );
		TableUtils.createTableIfNotExists (connection, PhotoTag.class);
	}

	public void destroy(PhotoTag photoTag) throws Exception{
		photoTagDao.delete (photoTag);
	}

	public QueryBuilder<PhotoTag, Integer> getQueryBuilder() {
		return photoTagDao.queryBuilder ();
	}

	private List<PhotoTag> photoTagsWithPhotoIdAndTagId(int photoID, int tagID) throws SQLException {
		QueryBuilder<PhotoTag, Integer> qb = photoTagDao.queryBuilder ();
		qb.where ().eq(PhotoTag.COLUMN_PHOTO_ID, photoID);
		qb.where ().eq(PhotoTag.COLUMN_TAG_ID, tagID);
		return photoTagDao.query(qb.prepare ());
	}
}
