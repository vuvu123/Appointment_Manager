package Database;

import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Database.DBConnection.getConnection;

public class DBCountries {
    /**
     * Returns all country names from countries table
     * @return ObservableList of Country objects
     */
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        String getAllCountries = "SELECT Country_ID, Country from countries";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllCountries);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Country country = new Country();
                country.setCountryID(rs.getInt("Country_ID"));
                country.setCountry(rs.getString("Country"));

                allCountries.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allCountries;
    }
}
