package Database;

import Model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Database.DBConnection.getConnection;

public class DBFirstLevelDivision {
    /**
     * Query to return list of first level divisions by country ID
     * @param countryID
     * @return ObservableList of FirstLevelDivision objects
     */
    public static ObservableList<FirstLevelDivision> getDivisionsByCountry(int countryID) {
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        String getDivisionsByCountry = "SELECT Division_ID, Division FROM first_level_divisions WHERE COUNTRY_ID = ?";

        try {

            PreparedStatement ps = getConnection().prepareStatement(getDivisionsByCountry);
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FirstLevelDivision division = new FirstLevelDivision();
                division.setDivision(rs.getString("Division"));
                division.setDivisionID(rs.getInt("Division_ID"));

                divisions.add(division);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return divisions;
    }
}
