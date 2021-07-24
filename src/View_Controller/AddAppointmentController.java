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
import static Model.DateTimeConversion.convertLocalDateLocalTimetoUTCString;
import static Database.DBAppointments.checkOverlappingAppts;

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

    @FXML
    private void cancelButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void clearButton(ActionEvent event) throws IOException {
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        typeTextField.clear();

        startDatePicker.setValue(null);
        endDatePicker.setValue(null);

        contactComboBox.getSelectionModel().clearSelection();
        startTimeComboBox.getSelectionModel().clearSelection();
        endTimeComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
        userComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void addButton(ActionEvent event) throws IOException {
        // Error alert if any fields are empty
        if (areFieldsEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to add appointment.");
            alert.setContentText("Please fill out all input fields.");
            alert.showAndWait();

            return;
        }

        // Date time
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startTimeComboBox.getValue();

        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endTimeComboBox.getValue();

        LocalDateTime startDT = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(endDate, endTime);


        // Alert if end is scheduled before start
        if (endDT.isBefore(startDT)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to add appointment.");
            alert.setContentText("Appointment end time can't be before start time.");
            alert.showAndWait();

            return;
        }

        // Alert if start and end are equal
        if (endDT.equals(startDT)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to add appointment.");
            alert.setContentText("Appointment start and end time can't be equal!");
            alert.showAndWait();

            return;
        }

        // Alert if appointment scheduled outside of business hours
        if (!isDuringBusinessHours(startDT, endDT)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to add appointment.");
            alert.setContentText("Appointment must be during business hours!");
            alert.showAndWait();

            return;
        }

        // Convert system default time zone to UTC time zone (for DB query)
        String startDateTime = DateTimeConversion.convertLocalDateLocalTimetoUTCString(startDate, startTime);
        String endDateTime = DateTimeConversion.convertLocalDateLocalTimetoUTCString(endDate, endTime);

        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        int contactID = contactComboBox.getValue().getContactID();
        int custID = customerComboBox.getValue().getCustomerID();
        int user = userComboBox.getValue().getUserID();

        // Business hours (5:00 - 19:00 PST) -> (08:00 - 22:00 EST) -> (12:00 - 02:00 UTC)
        // Add time validation to make sure appointment times don't overlap for selected customer
        if (!checkOverlappingAppts(custID,convertLocalDateLocalTimetoUTCString(startDate, startTime), convertLocalDateLocalTimetoUTCString(endDate, endTime))) {
            DBAppointments.addAppointment(title, description, location, type, contactID, custID, startDateTime, endDateTime, user);
            refreshApptTable();
            clearButton(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to add appointment.");
            alert.setContentText("The customer is already booked for that time.\nPlease try a different time.");
            alert.showAndWait();
        }
    }

    private boolean isDuringBusinessHours(LocalDateTime start, LocalDateTime end) {
        // Convert from local time to UTC
        LocalDateTime utcStart = userLocalTZtoUTC(start);
        LocalDateTime utcEnd = userLocalTZtoUTC(end);

        LocalTime utcStartTime = utcStart.toLocalTime();
        LocalTime utcEndTime = utcEnd.toLocalTime();

        System.out.println("UTC start: " + utcStartTime + "| UTC end: " + utcEndTime);

        if ((utcStartTime.isAfter(LocalTime.of(11,59)) || (utcStartTime.isBefore(LocalTime.of(2,0)) && (utcEndTime.isAfter(LocalTime.of(11,59)) || utcEndTime.isBefore(LocalTime.of(2, 0)))))){
            return true;
        } else {
            return false;
        }
    }

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

    private ObservableList<LocalTime> fillTimeComboBox() {
        ObservableList<LocalTime> timeList = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 30);

        while (start.isBefore(end.plusSeconds(1))) {
            timeList.add(start);
            start = start.plusMinutes(15);
        }
        // Program keeps timing out when I set "end" to 23:45
        // Does not crash adding last 15 min increment outside of while loop
        timeList.add(LocalTime.of(23, 45));
        return timeList;
    }

    private void refreshApptTable() {
        apptsTable = DBAppointments.getAllAppointments();
        appointmentsTableView.setItems(apptsTable);
        appointmentsTableView.refresh();
    }

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
