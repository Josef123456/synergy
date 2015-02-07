package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import synergy.models.Tag;

/**
 * Created by alexstoick on 2/7/15.
 */
public class TagDao {
	private static TagDao ourInstance = new TagDao ();

	public static TagDao getInstance () {
		return ourInstance;
	}

	private Dao<Tag, Integer> tagDao;
	private static JdbcConnectionSource connection;

	private TagDao () {
		try {
			connection = new DatabaseConnection ().getConnection ();
			tagDao = DaoManager.createDao (connection, Tag.class);
			TableUtils.createTableIfNotExists (connection, Tag.class);
		} catch ( Exception e ) {
			System.err.println ( e.toString () ) ;
		}
	}

	public QueryBuilder<Tag, Integer> getQueryBuilder() {
		return tagDao.queryBuilder ();
	}
}
