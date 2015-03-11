package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import synergy.models.Relationship;
import synergy.models.Tag;

import java.sql.SQLException;
import java.util.List;


public class RelationshipDao {
    private static RelationshipDao ourInstance = new RelationshipDao ();
    private static PreparedQuery<Tag> tagsForPhotoQuery = null;

    public static RelationshipDao getInstance() {
        return ourInstance;
    }

    private Dao<Relationship, Integer> relationshipDao;

    private static JdbcConnectionSource connection;

    private RelationshipDao () {
        try {
            connection = new DatabaseConnection().getConnection();
            relationshipDao = DaoManager.createDao(connection, Relationship.class);
            TableUtils.createTableIfNotExists (connection, Relationship.class);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void dropTable() throws SQLException {
        TableUtils.dropTable (connection, Relationship.class, true );
        TableUtils.createTableIfNotExists (connection, Relationship.class);
    }

    public void createOrUpdate(Relationship relationship) throws Exception {
        relationshipDao.createOrUpdate(relationship);
    }

    public List<Relationship> getAllRelationships() throws SQLException {
        return relationshipDao.queryForAll();
    }

    public List<Relationship> query(PreparedQuery<Relationship> preparedQuery) throws SQLException {
        return relationshipDao.query(preparedQuery);
    }

    public QueryBuilder<Relationship,Integer> getQueryBuilder() {
        return relationshipDao.queryBuilder();
    }


}
