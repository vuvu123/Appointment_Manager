package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    private IntegerProperty customerID;
    private StringProperty name;
    private StringProperty address;
    private StringProperty postalCode;
    private StringProperty phoneNumber;
    private StringProperty firstLevelDivision;
    private StringProperty country;
    private int divisionID, countryID;

    public Customer() {
        this.customerID = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.postalCode = new SimpleStringProperty();
        this.phoneNumber = new SimpleStringProperty();
        this.firstLevelDivision = new SimpleStringProperty();
        this.country = new SimpleStringProperty();
    }

    public int getCustomerID() {
        return customerID.get();
    }

    public IntegerProperty customerIDProperty() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getFirstLevelDivision() {
        return firstLevelDivision.get();
    }

    public StringProperty firstLevelDivisionProperty() {
        return firstLevelDivision;
    }

    public void setFirstLevelDivision(String firstLevelDivision) {
        this.firstLevelDivision.set(firstLevelDivision);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    @Override
    public String toString() {
        return "(" + this.getCustomerID() + ") " + this.getName();
    }
}
