package View_Controller;

import Database.DBContacts;
import Database.DBCustomers;
import Database.DBUsers;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    @FXML private TableView<Appointment> appointmentTableView;

    @FXML private TableColumn<Appointment, Integer> apptIDTableColumn;
    @FXML private TableColumn<Appointment, Integer> titleTableColumn;
    @FXML private TableColumn<Appointment, Integer> descriptionTableColumn;
    @FXML private TableColumn<Appointment, Integer> locationTableColumn;
    @FXML private TableColumn<Appointment, Integer> custNameTableColumn;
    @FXML private TableColumn<Appointment, Integer> contactTableColumn;
    @FXML private TableColumn<Appointment, Integer> typeTableColumn;
    @FXML private TableColumn<Appointment, Integer> startDateTableColumn;
    @FXML private TableColumn<Appointment, Integer> startTimeTableColumn;
    @FXML private TableColumn<Appointment, Integer> endDateTableColumn;
    @FXML private TableColumn<Appointment, Integer> endTimeTableColumn;
    @FXML private TableColumn<Appointment, Integer> userTableColumn;

    @FXML private TextField apptIDTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField typeTextField;

    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<User> userComboBox;
    @FXML private ComboBox startTimeComboBox;
    @FXML private ComboBox endTimeComboBox;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Populate combo boxes
        contactComboBox.setItems(DBContacts.getAllContacts());
        customerComboBox.setItems(DBCustomers.getAllCustIDandName());
        userComboBox.setItems(DBUsers.getAllUsers());

    }
}
