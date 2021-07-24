package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Starts, Maintains, and closes connection to database */
public class DBConnection {
    // JDBC URL parts
    private static final String ipAddress = "jdbc:mysql://wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ07aEM";

    // JDBC URL
    private static final String jdbcURL = ipAddress + dbName;

    // Driver interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U07aEM";
    private static final String password = "53688972450";

    /**
     * Starts connection to database
     * @return Connection object
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);

            System.out.println("Connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * Gets current connection to database
     * @return Connection object
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     * Closes connection to database
     */
    public static void closeConnection() {
        try {
            conn.close();

            System.out.println("Connection closed.");
        } catch (Exception e) {
            // do nothing
        }
    }

}
