package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import synergy.models.Tag;

import java.sql.SQLException;
import java.util.List;

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

	public void create(Tag tag) throws Exception {
		tagDao.createIfNotExists (tag);
	}

	public List<Tag> query(PreparedQuery<Tag> query) throws SQLException{
		return tagDao.query (query);
	}

	public QueryBuilder<Tag, Integer> getQueryBuilder() {
		return tagDao.queryBuilder ();
	}
}