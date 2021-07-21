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
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

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

    @FXML private Label messageLabel;

    private ObservableList<Appointment> apptsTable = FXCollections.observableArrayList();
    private static Appointment selectedAppt;

    // Handle Buttons
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
    private void modifyButton(ActionEvent event) throws IOException {
        selectedAppt = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to modify appointment.");
            alert.setContentText("No appointment selected! Please select an appointment from the table.");
            alert.showAndWait();
        } else {
            enableFields();

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
    }

    @FXML
    private void saveButton(ActionEvent event) throws IOException {

    }

    @FXML
    private void deleteButton(ActionEvent event) throws IOException {
        selectedAppt = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (selectedAppt == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to delete appointment.");
            alert.setContentText("No appointment selected! Please select a appointment from the table.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Confirm Delete Appointment");
            alert.setContentText("Are you sure you want to delete " + selectedAppt.getAppointmentID() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                int apptID = selectedAppt.getAppointmentID();
                DBAppointments.deleteApptByApptID(apptID);
                refreshApptTable();
                messageLabel.setText("Appointment ID " + selectedAppt.getAppointmentID() +
                        " of type " + selectedAppt.getType() + " deleted.");
                messageLabel.setVisible(true);
                clearButton(event);
            } else {
                System.out.println("Delete canceled of AppointmentID " + selectedAppt.getAppointmentID());
            }
        }
    }


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

    private void refreshApptTable() {
        apptsTable = DBAppointments.getAllAppointments();
        appointmentsTableView.setItems(apptsTable);
        appointmentsTableView.refresh();
    }

    private Contact lookUpContact(String contactName) {
        for (Contact c : contactComboBox.getItems()) {
            if (c.getContactName().equals(contactName)) {
                return c;
            }
        }
        return null;
    }

    private Customer lookUpCustomer(String custName) {
        for (Customer cu : customerComboBox.getItems()) {
            if (cu.getName().equals(custName)) {
                return cu;
            }
        }
        return null;
    }

    private User lookUpUser(String userName) {
        for (User u : userComboBox.getItems()) {
            if (u.getUserName().equals(userName)) {
                return u;
            }
        }
        return null;
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

        messageLabel.setVisible(false);
    }

}
