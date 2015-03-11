package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
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

	public List<Relationship> getRelationshipForTags( Tag tag1 , Tag tag2 ) throws SQLException {
		QueryBuilder<Relationship, Integer> qb = relationshipDao.queryBuilder ();
		Where where = qb.where();
		Where<Relationship, Integer> where1 = where.and (
				where.eq (Relationship.COLUMN_KID1_ID, tag1.getID ()),
				where.eq (Relationship.COLUMN_KID2_ID, tag2.getID ())
		);
		Where<Relationship, Integer> where2 = where.and (
				where.eq (Relationship.COLUMN_KID2_ID, tag1.getID ()),
				where.eq (Relationship.COLUMN_KID1_ID, tag2.getID ())
		);
		where.or(where1, where2);
		return relationshipDao.query (qb.prepare ());
	}

	public List<Relationship> getRelationshipForTagSortedByOccurences(Tag tag) throws SQLException {
		QueryBuilder<Relationship, Integer> qb = relationshipDao.queryBuilder ();
		qb.where().eq(Relationship.COLUMN_KID1_ID, tag.getID ()).or ().
				eq (Relationship.COLUMN_KID2_ID, tag.getID ());
		qb.orderBy (Relationship.COLUMN_OCCURRENCES, true);
		return relationshipDao.query (qb.prepare ());
	}

    public void dropTable() throws SQLException {
        TableUtils.dropTable (connection, Relationship.class, true );
        TableUtils.createTableIfNotExists (connection, Relationship.class);
    }

    public void createOrUpdate(Relationship relationship) throws Exception {
	    List<Relationship> relationships = getRelationshipForTags (relationship.getKid1 (), relationship.getKid2 ());
	    if ( relationships.size () > 0 ) {
		    relationship.setID( relationships.get (0).getID ());
		    if ( relationship.getOccurrences () == -1 ) {
			    relationship.setOccurrences (relationships.get (0).getOccurrences ());
		    }
	    } else {
		    relationship.setOccurrences (0);
	    }

        relationshipDao.createOrUpdate(relationship);
    }

    public List<Relationship> getAllRelationships() throws SQLException {
        return relationshipDao.queryForAll();
    }
}
