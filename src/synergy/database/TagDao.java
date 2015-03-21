package synergy.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;
import synergy.models.Photo;
import synergy.models.PhotoTag;
import synergy.models.Tag;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by alexstoick on 2/7/15.
 */
public class TagDao {
	private static TagDao ourInstance = new TagDao ();
	private static PreparedQuery<Photo> photosForTagQuery = null;
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

	public void dropTable() throws SQLException {
		TableUtils.dropTable (connection, Tag.class, true );
		TableUtils.createTableIfNotExists (connection, Tag.class);
	}

	public void createOrUpdate(Tag tag) throws Exception {
		List<Tag> tags = tagWithTypeAndValue (tag.getType(), tag.getValue());
		if ( tags.size() > 0 ) {
			tag.setID (tags.get (0).getID ());
		}
		tagDao.createOrUpdate (tag);
	}

	public List<Tag> query(PreparedQuery<Tag> query) throws SQLException{
		return tagDao.query (query);
	}

	public QueryBuilder<Tag, Integer> getQueryBuilder() {
		return tagDao.queryBuilder ();
	}

	public List<Tag> tagWithValueLike(String value) throws SQLException {
		QueryBuilder<Tag, Integer> qb = tagDao.queryBuilder ();
		qb.where().raw ( "UPPER(" + Tag.COLUMN_VALUE + ") LIKE UPPER('" + value + "%')");
		qb.orderBy ( Tag.COLUMN_VALUE, true );
		return tagDao.query (qb.prepare ());
	}

	private List<Tag> tagWithTypeAndValue(Tag.TagType type, String value) throws SQLException{
		QueryBuilder<Tag, Integer> qb = tagDao.queryBuilder ();
		qb.where().eq (Tag.COLUMN_TYPE, type);
		qb.where().eq (Tag.COLUMN_VALUE, value);
		return tagDao.query(qb.prepare ());
	}

	private PreparedQuery<Photo> makePhotosForTagQuery() throws SQLException{

		QueryBuilder<PhotoTag, Integer> photoTagsQueryBuilder = PhotoTagDao.getInstance()
				.getQueryBuilder();
		photoTagsQueryBuilder.selectColumns(PhotoTag.COLUMN_PHOTO_ID);
		SelectArg postSelectArg = new SelectArg();
		photoTagsQueryBuilder.where().eq(PhotoTag.COLUMN_TAG_ID, postSelectArg);

		QueryBuilder<Photo, Integer> photoQueryBuilder = PhotoDao.getInstance().getQueryBuilder();
		photoQueryBuilder .where().in(Photo._ID, photoTagsQueryBuilder);

		return photoQueryBuilder.prepare();
	}

	public List<Tag> getAllChildrenTags() throws SQLException {
		QueryBuilder<Tag, Integer> qb = tagDao.queryBuilder ();
		qb.where().eq(Tag.COLUMN_TYPE, Tag.TagType.KID);
		return tagDao.query (qb.prepare ());
	}

    public List<Tag> getAllPlacesTags() throws SQLException{
        QueryBuilder<Tag, Integer> qb = tagDao.queryBuilder();
        qb.where().eq(Tag.COLUMN_TYPE,Tag.TagType.PLACE);
        return tagDao.query(qb.prepare());
    }

	public List<Photo> getPhotosForTag(Tag tag) throws SQLException {
		if (photosForTagQuery == null ) {
			photosForTagQuery = makePhotosForTagQuery();
		}
		photosForTagQuery.setArgumentHolderValue(0, tag);
		return PhotoDao.getInstance().query(photosForTagQuery);
	}
}
