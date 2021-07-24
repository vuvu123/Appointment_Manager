package View_Controller;

import Database.DBAppointments;
import Database.DBContacts;
import Database.DBCustomers;
import Database.DBUsers;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Alerts.apptErrorAlert;
import static Model.Alerts.noApptSelected;
import static Model.DateTimeConversion.userLocalTZtoUTC;

/** Controls modify appointment screen */
public class ModifyAppointmentController implements Initializable {
    @FXML private TableView<Appointment> appointmentsTableView;

    @FXML private TableColumn<Appointment, Integer> apptIDTableColumn;
    @FXML private TableColumn<Appointment, String> titleTableColumn;
    @FXML private TableColumn<Appointment, String> descriptionTableColumn;
    @FXML private TableColumn<Appointment, String> locationTableColumn;
    @FXML private TableColumn<Appointment, String> custNameTableColumn;
    @FXML private TableColumn<Appointment, String> contactTableColumn;
    @FXML private TableColumn<Appointment, String> typeTableColumn;
    @FXML private TableColumn<Appointment, String> startDateTableColumn;
    @FXML private TableColumn<Appointment, String> startTimeTableColumn;
    @FXML private TableColumn<Appointment, String> endDateTableColumn;
    @FXML private TableColumn<Appointment, String> endTimeTableColumn;
    @FXML private TableColumn<Appointment, String> userTableColumn;

    @FXML private TextField apptIDTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField typeTextField;

    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<User> userComboBox;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    @FXML private Button deleteButton;

    @FXML private Label messageLabel;

    private ObservableList<Appointment> apptsTable = FXCollections.observableArrayList();
    private static Appointment selectedAppt;

    /**
     * Returns to MainScreen
     * @param event
     * @throws IOException
     */
    @FXML
    private void cancelButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Clears input fields except datePickers
     * @param event
     * @throws IOException
     */
    @FXML
    private void clearButton(ActionEvent event) throws IOException {
        apptIDTextField.clear();
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        typeTextField.clear();

        contactComboBox.getSelectionModel().clearSelection();
        startTimeComboBox.getSelectionModel().clearSelection();
        endTimeComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
        userComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Handles modify button. Pulls data of selected row into input fields.
     * @param event
     * @throws IOException
     */
    @FXML
    private void modifyButton(ActionEvent event) throws IOException {
        selectedAppt = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {
            noApptSelected("modify", "appointment");
            return;
        }
            enableFields();
            deleteButton.setDisable(true);

            apptIDTextField.setText(String.valueOf(selectedAppt.getAppointmentID()));
            titleTextField.setText(selectedAppt.getTitle());
            descriptionTextField.setText(selectedAppt.getDescription());
            locationTextField.setText(selectedAppt.getLocation());
            typeTextField.setText(selectedAppt.getType());
            contactComboBox.setValue(lookUpContact(selectedAppt.getContactName()));
            customerComboBox.setValue(lookUpCustomer(selectedAppt.getCustName()));
            userComboBox.setValue(lookUpUser(selectedAppt.getUserName()));
            startDatePicker.setValue(DateTimeConversion.convertZDTtoLocalDate(selectedAppt.getStart()));
            endDatePicker.setValue(DateTimeConversion.convertZDTtoLocalDate(selectedAppt.getEnd()));
            startTimeComboBox.setValue(DateTimeConversion.convertZDTtoLocalTime(selectedAppt.getStart()));
            endTimeComboBox.setValue(DateTimeConversion.convertZDTtoLocalTime(selectedAppt.getEnd()));
    }

    /**
     * Runs input validation checks, then updates appointment if pass
     * @param event
     * @throws IOException
     */
    @FXML
    private void saveButton(ActionEvent event) throws IOException {
        selectedAppt = appointmentsTableView.getSelectionModel().getSelectedItem();

        // Checks if an appointment is selected
        if (selectedAppt == null) {
            noApptSelected("modify", "appointment");
            return;
        }

        // Error alert if any fields are empty
        if (areFieldsEmpty()) {
            apptErrorAlert("Please fill out all input fields.");
            return;
        }

        // Get DateTime from input fields
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startTimeComboBox.getValue();

        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endTimeComboBox.getValue();

        // Convert to LocalDateTime
        LocalDateTime startDT = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(endDate, endTime);

        // Alert if end is scheduled before start
        if (endDT.isBefore(startDT)) {
            apptErrorAlert("Appointment end time can't be before start time.");
            return;
        }

        // Alert if start and end are equal
        if (endDT.equals(startDT)) {
            apptErrorAlert("Appointment start and end time can't be equal!");
            return;
        }

        // Alert if appointment scheduled outside of business hours
        if (!isDuringBusinessHours(startDT, endDT)) {
            apptErrorAlert("Appointment must be during business hours!");
            return;
        }

        // Convert system default time zone to UTC time zone (for DB query)
        String startDateTime = DateTimeConversion.convertLocalDateLocalTimetoUTCString(startDate, startTime);
        String endDateTime = DateTimeConversion.convertLocalDateLocalTimetoUTCString(endDate, endTime);

        // Get input field values
        int apptID = Integer.parseInt(apptIDTextField.getText());
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        int contactID = contactComboBox.getValue().getContactID();
        int custID = customerComboBox.getValue().getCustomerID();
        int userID = userComboBox.getValue().getUserID();

        // Alert if there are overlapping appointments
        if (isOverlappingAppt(custID, startDT, endDT)) {
            apptErrorAlert("The customer is already booked for that time.\nPlease try a different time.");
            return;
        }

        // Update appointment and refresh table
        DBAppointments.updateAppointment(apptID, title, description, location, type, custID, userID, contactID, startDateTime, endDateTime);
        refreshApptTable();

        // Displays message on screen with what appointment and type were deleted.
        messageLabel.setText("Appointment ID [" + apptID + "] of type [" + type + "] saved.");
        messageLabel.setVisible(true);

        // Clear and disable appropriate fields
        clearButton(event);
        disableFields();
        deleteButton.setDisable(false);
    }

    /**
     * Handles deleting appointments, then displays a message of AppointmentID and type deleted.
     * @param event
     * @throws IOException
     */
    @FXML
    private void deleteButton(ActionEvent event) throws IOException {
        selectedAppt = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {
            noApptSelected("delete", "appointment");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Confirm Delete Appointment");
            alert.setContentText("Are you sure you want to delete Appointment ID [" + selectedAppt.getAppointmentID() + "]?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                int apptID = selectedAppt.getAppointmentID();
                DBAppointments.deleteApptByApptID(apptID);
                refreshApptTable();
                messageLabel.setText("Appointment ID [" + selectedAppt.getAppointmentID() +
                        "] of type [" + selectedAppt.getType() + "] deleted.");
                messageLabel.setVisible(true);
                disableFields();
                clearButton(event);
            } else {
                messageLabel.setText("Delete canceled of AppointmentID [" + selectedAppt.getAppointmentID() + "]");
                messageLabel.setVisible(true);
                System.out.println("Delete canceled of AppointmentID [" + selectedAppt.getAppointmentID() + "]");
            }
        }
    }

    /**
     * Checks for appointment overlap for specified customer
     * @param custID Passed in through customerComboBox
     * @param newStart startDateTime from input fields
     * @param newEnd startEndTime from input fields
     * @return true if is overlapping, false if no overlap
     */
    private boolean isOverlappingAppt(int custID, LocalDateTime newStart, LocalDateTime newEnd) {
        for (Appointment a : apptsTable) {
            if (a.getCustomerID() == custID) {
                LocalDateTime existStart = a.getStart().toLocalDateTime();
                LocalDateTime existEnd = a.getEnd().toLocalDateTime();

//                System.out.println(a.getCustName() + ": Checking new " + newStart + " - " + newEnd + " against existing " + existStart + " - " + existEnd);
                if (newStart.isAfter(existStart) && newStart.isBefore(existEnd) || newEnd.isAfter(existStart) && newEnd.isBefore(existEnd)
                        || newStart.equals(existStart) && newEnd.equals(existEnd) || newStart.equals(existStart) && newEnd.isAfter(existStart)
                        || newEnd.equals(existEnd) && newStart.isBefore(existEnd)) {
                    System.out.println("There is an overlapping appointment!");
                    return true;
                }
            }
        }
        System.out.println("There were no overlapping appointments!");
        return false;
    }

    /**
     * Check if passed in LocalDateTime range is within business hours of
     * 12:00 - 02:00 UTC or 08:00 - 20:00 EST
     * @param start LocalDateTime from input fields
     * @param end LocalDateTime from input fields
     * @return true if during business hours, false if not
     */
    private boolean isDuringBusinessHours(LocalDateTime start, LocalDateTime end) {
        // Convert from local time to UTC
        LocalDateTime utcStart = userLocalTZtoUTC(start);
        LocalDateTime utcEnd = userLocalTZtoUTC(end);

        LocalTime utcStartTime = utcStart.toLocalTime();
        LocalTime utcEndTime = utcEnd.toLocalTime();

        System.out.println("UTC start: " + utcStartTime + "| UTC end: " + utcEndTime);

        if ((utcStartTime.isAfter(LocalTime.of(11,59)) || (utcStartTime.isBefore(LocalTime.of(2,0,1)) && (utcEndTime.isAfter(LocalTime.of(11,59)) || utcEndTime.isBefore(LocalTime.of(2, 0,1)))))){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if any input fields are empty
     * @return true if empty, false if not
     */
    private boolean areFieldsEmpty(){
        boolean isContactEmpty = contactComboBox.getValue() == null;
        boolean isCustEmpty = customerComboBox.getValue() == null;
        boolean isStartDateEmpty = startDatePicker.getValue() == null;
        boolean isStartTimeEmpty = startTimeComboBox.getValue() == null;
        boolean isEndDateEmpty = endDatePicker.getValue() == null;
        boolean isEndTimeEmpty = endTimeComboBox.getValue() == null;

        if (titleTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty() ||
                locationTextField.getText().isEmpty() || typeTextField.getText().isEmpty() ||
                isContactEmpty || isCustEmpty || isStartDateEmpty || isStartTimeEmpty ||
                isEndDateEmpty || isEndTimeEmpty) {
            return true;
        }
        return false;
    }

    /**
     * Generates times for time combo boxes in 15 minute increments
     * @return ObservableList of LocalTime objects ranging from 0:00 - 23:45
     */
    private ObservableList<LocalTime> fillTimeComboBox() {
        ObservableList<LocalTime> timeList = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 30);

        while (start.isBefore(end.plusSeconds(1))) {
            timeList.add(start);
            start = start.plusMinutes(15);
        }
        // Program keeps timing out when I set "end" to LocalTime.of(23,45)
        // Does not crash adding last 15 min increment outside of while loop
        timeList.add(LocalTime.of(23, 45));
        return timeList;
    }

    /**
     * Enables input fields
     */
    private void enableFields() {
        titleTextField.setDisable(false);
        descriptionTextField.setDisable(false);
        locationTextField.setDisable(false);
        typeTextField.setDisable(false);
        contactComboBox.setDisable(false);
        startDatePicker.setDisable(false);
        startTimeComboBox.setDisable(false);
        endDatePicker.setDisable(false);
        endTimeComboBox.setDisable(false);
        customerComboBox.setDisable(false);
        userComboBox.setDisable(false);
    }

    /**
     * Disables input fields
     */
    private void disableFields() {
        titleTextField.setDisable(true);
        descriptionTextField.setDisable(true);
        locationTextField.setDisable(true);
        typeTextField.setDisable(true);
        contactComboBox.setDisable(true);
        startDatePicker.setDisable(true);
        startTimeComboBox.setDisable(true);
        endDatePicker.setDisable(true);
        endTimeComboBox.setDisable(true);
        customerComboBox.setDisable(true);
        userComboBox.setDisable(true);
    }

    /**
     * Updates apptsTable from SQL query observableList and refreshes TableView
     */
    private void refreshApptTable() {
        apptsTable = DBAppointments.getAllAppointments();
        appointmentsTableView.setItems(apptsTable);
        appointmentsTableView.refresh();
    }

    /**
     * Looks up contact from contactComboBox
     * @param contactName used to search for contact
     * @return Selected contact object from comboBox
     */
    private Contact lookUpContact(String contactName) {
        for (Contact c : contactComboBox.getItems()) {
            if (c.getContactName().equals(contactName)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Looks up customer from custComboBox
     * @param custName used to search for customer
     * @return Selected customer object from comboBox
     */
    private Customer lookUpCustomer(String custName) {
        for (Customer cu : customerComboBox.getItems()) {
            if (cu.getName().equals(custName)) {
                return cu;
            }
        }
        return null;
    }

    /**
     * Looks up user from userComboBox
     * @param userName used to search for user
     * @return Selected user object from comboBox
     */
    private User lookUpUser(String userName) {
        for (User u : userComboBox.getItems()) {
            if (u.getUserName().equals(userName)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Populates appointments Table, comboboxes, and hides message label
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apptsTable = DBAppointments.getAllAppointments();

        // Populate Appointments TableView
        apptIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        custNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("custName"));
        contactTableColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        userTableColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        appointmentsTableView.setItems(apptsTable);

        // Populate combo boxes
        contactComboBox.setItems(DBContacts.getAllContacts());
        customerComboBox.setItems(DBCustomers.getAllCustIDandName());
        userComboBox.setItems(DBUsers.getAllUsers());
        startTimeComboBox.setItems(fillTimeComboBox());
        endTimeComboBox.setItems(fillTimeComboBox());

        // Hides message label
        messageLabel.setVisible(false);
    }

}
