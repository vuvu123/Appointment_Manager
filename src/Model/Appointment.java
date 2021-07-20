package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.ZonedDateTime;

public class Appointment {
    private final IntegerProperty appointmentID;
    private final IntegerProperty customerID;
    private final IntegerProperty userID;
    private final IntegerProperty contactID;
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty location;
    private final StringProperty type;
    private ZonedDateTime start;
    private ZonedDateTime end;

    private final StringProperty custName;
    private final StringProperty contactName;
    private final StringProperty userName;
    private final StringProperty startDate;
    private final StringProperty startTime;
    private final StringProperty endDate;
    private final StringProperty endTime;

    public Appointment() {
        this.appointmentID = new SimpleIntegerProperty();
        this.customerID = new SimpleIntegerProperty();
        this.userID = new SimpleIntegerProperty();
        this.contactID = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.custName = new SimpleStringProperty();
        this.contactName = new SimpleStringProperty();
        this.userName = new SimpleStringProperty();
        this.startDate = new SimpleStringProperty();
        this.startTime = new SimpleStringProperty();
        this.endDate = new SimpleStringProperty();
        this.endTime = new SimpleStringProperty();
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

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getCustName() {
        return custName.get();
    }

    public StringProperty custNameProperty() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName.set(custName);
    }

    public String getContactName() {
        return contactName.get();
    }

    public StringProperty contactNameProperty() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName.set(contactName);
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public StringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.appointmentID + " | " + this.title + " | " + this.description + " | " + this.location + " | ");
        sb.append(this.custName + " | " + this.contactName + " | " + this.type + " | ");
        sb.append("Start Date/Time: " + this.startDate + " " + this.startTime + " | End Date/Time: " + this.endDate + " " + this.endTime);
        sb.append(" | " + this.userName);
        return sb.toString();
    }
}
