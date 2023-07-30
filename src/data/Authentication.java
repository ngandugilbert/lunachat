package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Models.User;

import java.sql.Connection;

public class Authentication {
    private static User loggedUser;

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static boolean login(String username, String password) {
        if (Connect.connect()) {
            try {
                // create a new user object
                loggedUser = new User();

                Connection conn = Connect.getConnection();
                // Prepare the SQL statement for login validation
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement statement = conn.prepareStatement(sql);

                statement.setString(1, username);
                statement.setString(2, password);

                // Execute the SQL statement
                ResultSet resultSet = statement.executeQuery();

                // Check if there is a matching record
                boolean success = resultSet.next();

                setUserDetails(resultSet);

                // Close the database resources
                resultSet.close();
                statement.close();

                return success;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            } finally {
                // Connect.disconnect();
            }
        }

        return false;
    }

    public static boolean createAccount(String username, String password, String display_name) {
        if (Connect.connect() && !login(username, password)) {

            Connection conn = Connect.getConnection();
            try {
                // Prepare the SQL statement for account creation
                String sql = "INSERT INTO users (username, password, display_name) VALUES (?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(sql);

                // statement.setInt(1, user_id);
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, display_name);

                // Execute the SQL statement
                statement.executeUpdate();

                if(login(username, password))
                    return true;
                return false; // account is created but failed to login
            } catch (SQLException error) {
                // Handle any errors that occur during the account creation process
                return false;
            } finally {
                Connect.disconnect();
            }
        }

        return false;
    }

    // set user details for the logged in user
    private static void setUserDetails(ResultSet result) {
        try {
            loggedUser.setName(result.getString("display_name"));
            loggedUser.setUsername(result.getString("username"));
            loggedUser.setUserId(result.getInt("user_id"));
        } catch (SQLException e) {
            // Handle this event bro
        }
    }
}
