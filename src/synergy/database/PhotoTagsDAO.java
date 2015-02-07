package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import synergy.models.PhotoTags;

/**
 * Created by alexstoick on 2/7/15.
 */
public class PhotoTagsDao {
	private static PhotoTagsDao ourInstance = new PhotoTagsDao ();

	public static PhotoTagsDao getInstance () {
		return ourInstance;
	}

	private Dao<PhotoTags, Integer> photoTagsDao;

	private static JdbcConnectionSource connection ;

	private PhotoTagsDao () {
		try {
			connection = new DatabaseConnection ().getConnection ();
			photoTagsDao = DaoManager.createDao (connection, PhotoTags.class);
			TableUtils.createTableIfNotExists (connection, PhotoTags.class);
		} catch ( Exception e ) {
			System.err.println ( e.toString () ) ;
		}
	}

	public QueryBuilder<PhotoTags, Integer> getQueryBuilder() {
		return photoTagsDao.queryBuilder ();
	}
}
