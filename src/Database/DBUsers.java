package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Database.DBConnection.getConnection;

public class DBUsers {
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

}
