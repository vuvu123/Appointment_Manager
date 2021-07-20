package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Appointment {
    private IntegerProperty appointmentID;
    private IntegerProperty customerID;
    private IntegerProperty userID;
    private IntegerProperty contactID;
    private StringProperty title;
    private StringProperty description;
    private StringProperty location;
    private StringProperty type;
    private LocalDateTime start;
    private LocalDateTime end;

    private String custName;
    private String contactName;
    private String userName;

    public Appointment() {
        this.appointmentID = new SimpleIntegerProperty();
        this.customerID = new SimpleIntegerProperty();
        this.userID = new SimpleIntegerProperty();
        this.contactID = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
    }

    public int getAppointmentID() {
        return appointmentID.get();
    }

    public IntegerProperty appointmentIDProperty() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID.set(appointmentID);
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

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public int getContactID() {
        return contactID.get();
    }

    public IntegerProperty contactIDProperty() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID.set(contactID);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
