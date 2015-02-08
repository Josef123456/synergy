package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import synergy.models.PhotoTag;

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

	public void create(PhotoTag photoTag) throws Exception{
		photoTagDao.createIfNotExists (photoTag);
	}

	public QueryBuilder<PhotoTag, Integer> getQueryBuilder() {
		return photoTagDao.queryBuilder ();
	}
}
