package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Holds all country objects variables */
public class Country {
    private int countryID;
    private String country;

    private ObservableList<FirstLevelDivision> assocDivisions = FXCollections.observableArrayList();

    public Country() {
    }

    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ObservableList<FirstLevelDivision> getAssocDivisions() {
        return assocDivisions;
    }

    public void setAssocDivisions(ObservableList<FirstLevelDivision> assocDivisions) {
        this.assocDivisions = assocDivisions;
    }

    @Override
    public String toString() {
        return this.country;
    }
}
