package synergy.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;


/**
 * Created by alexstoick on 2/6/15.
 */
public class DatabaseConnection {
    private JdbcConnectionSource connection = null;
    public final static String DATABASE_URL = "jdbc:h2:~/Desktop/synergy/db/h2.db";
//    public final static String DATABASE_URL = "jdbc:h2:mem:db";

    public JdbcConnectionSource getConnection() {
        return connection;
    }

    public DatabaseConnection() {
        try {
            Class.forName("org.h2.Driver");
            connection = new JdbcConnectionSource(DATABASE_URL);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
