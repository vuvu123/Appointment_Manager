package Database;

import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Database.DBConnection.getConnection;

/** All SQL queries associated with Users */
public class DBUsers {
    /**
     * Verifies username and password with database
     * @param username
     * @param password
     * @return boolean
     */
    public static boolean verifyCredentials(String username, String password) {
        String checkCredentials = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(checkCredentials);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Queries all users for User_ID and User_Name
     * @return ObservableList of User objects
     */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String getAllUsersQuery = "SELECT User_ID, User_Name FROM users ORDER BY User_ID";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllUsersQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("User_ID"));
                user.setUserName(rs.getString("User_Name"));

                users.add(user);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Looks up user by userName
     * @param username
     * @return User object
     */
    public static User lookUpUser(String username) {
        String getUserByNameQuery = "SELECT User_ID, User_Name FROM users WHERE User_Name = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getUserByNameQuery);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            rs.next();
            User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"));
            return user;
        } catch (Exception e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;
    }

}
