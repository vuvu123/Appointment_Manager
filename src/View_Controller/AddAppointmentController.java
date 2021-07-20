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
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

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
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        int contactID = contactComboBox.getValue().getContactID();
        int custID = customerComboBox.getValue().getCustomerID();
        int user = userComboBox.getValue().getUserID();

        // Date time
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startTimeComboBox.getValue();
        String startDateTime = DateTimeConversion.convertLocalDateLocalTimetoUTCString(startDate, startTime);

        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endTimeComboBox.getValue();
        String endDateTime = DateTimeConversion.convertLocalDateLocalTimetoUTCString(endDate, endTime);

        DBAppointments.addAppointment(title, description, location, type, contactID, custID, startDateTime, endDateTime, user);
        refreshApptTable();
        clearButton(event);
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
    }
}
