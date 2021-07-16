package Database;

import Model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Database.DBConnection.getConnection;

/*
 * Returns ObservableList of first level divisions
 * @param String country name is passed in
 */
public class DBFirstLevelDivision {
    public ObservableList<FirstLevelDivision> getDivisionsByCountry(String countryName) {
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        String getDivisionsByCountry = "SELECT f.Division FROM first_level_divisions f " +
                "INNER JOIN countries c ON c.Country_ID = f.COUNTRY_ID WHERE c.Country = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getDivisionsByCountry);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FirstLevelDivision division = new FirstLevelDivision();
                division.setDivision(rs.getString("f.Division"));

                divisions.add(division);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return divisions;
    }
}
