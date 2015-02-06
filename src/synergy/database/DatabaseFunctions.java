package synergy.database;

import synergy.models.DatabaseObject;

/**
 * Created by alexstoick on 2/6/15.
 */
public interface DatabaseFunctions {
	void createTable() throws DatabaseException;
	int insertObject(DatabaseObject object) throws DatabaseException;
	int deleteObject(DatabaseObject object) throws DatabaseException;

}
