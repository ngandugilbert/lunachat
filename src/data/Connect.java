package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static Connection connection;
    private static final String url = "jdbc:mysql://localhost:3306/lunachat";
    private static final String user = "root";
    private static final String password = "";

    public static boolean connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            return true;
        } catch (SQLException e) {
            // Handle this exception
        }

        return false;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() {
        try {
            // Close the database connection
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
