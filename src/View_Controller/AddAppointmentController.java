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
import java.time.*;
import java.util.ResourceBundle;
import static Model.DateTimeConversion.userLocalTZtoUTC;
import static Model.Alerts.apptErrorAlert;

/** Controls add appointment scene*/
public class AddAppointmentController implements Initializable {
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

    private ObservableList<Appointment> apptsTable = FXCollections.observableArrayList();

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
     * Runs input validation checks, then adds appointment if passes
     * @param event
     * @throws IOException
     */
    @FXML
    private void addButton(ActionEvent event) throws IOException {
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
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        int contactID = contactComboBox.getValue().getContactID();
        int custID = customerComboBox.getValue().getCustomerID();
        int user = userComboBox.getValue().getUserID();

        // Alert if there are overlapping appointments
        if (isOverlappingAppt(custID, startDT, endDT)) {
            apptErrorAlert("The customer is already booked for that time.\nPlease try a different time.");
            return;
        }

        // Add appointment and refresh table
        DBAppointments.addAppointment(title, description, location, type, contactID, custID, startDateTime, endDateTime, user);
        refreshApptTable();
        clearButton(event);
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
        // Program keeps timing out when I set "end" to 23:45
        // Does not time out adding last 15 min increment outside of while loop
        timeList.add(LocalTime.of(23, 45));
        return timeList;
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
     * Populates appointments table, combo boxes, and date pickers
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

        // Populate datePickers
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
    }
}
