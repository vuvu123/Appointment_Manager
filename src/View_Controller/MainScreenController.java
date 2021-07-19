package View_Controller;

import Model.Appointment;
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

public class MainScreenController implements Initializable {
    @FXML private TableView<Appointment> appointmentsTableView;

    @FXML private TableColumn<Appointment, Integer> apptIDTableColumn;
    @FXML private TableColumn<Appointment, String> contactTableColumn;
    @FXML private TableColumn<Appointment, String> titleTableColumn;
    @FXML private TableColumn<Appointment, String> locationTableColumn;
    @FXML private TableColumn<Appointment, String> typeTableColumn;
    @FXML private TableColumn<Appointment, String> dateTableColumn;
    @FXML private TableColumn<Appointment, String> startTableColumn;
    @FXML private TableColumn<Appointment, String> endTableColumn;

    @FXML private RadioButton weekViewRadioButton;
    @FXML private RadioButton monthViewRadioButton;
    @FXML private RadioButton allViewRadioButton;

    @FXML private ToggleGroup dateViewToggleGroup;

    @FXML private DatePicker datePicker;

    @FXML private Button addAppointmentButton;
    @FXML private Button modifyAppointmentButton;
    @FXML private Button addCustomerButton;
    @FXML private Button modifyCustomerButton;
    @FXML private Button reportsButton;

/*
 *  Handle navigation buttons
 */
    @FXML
    private void addAppointmentButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void modifyAppointmentButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("ModifyAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void addCustomerButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void modifyCustomerButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("ModifyCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void reportsButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Handle radio buttons
    @FXML
    private void weekViewRadioButtonSelected() {

        weekViewRadioButton.setSelected(true);
        monthViewRadioButton.setSelected(false);
        allViewRadioButton.setSelected(false);
    }

    @FXML
    private void monthViewRadioButtonSelected() {

        weekViewRadioButton.setSelected(false);
        monthViewRadioButton.setSelected(true);
        allViewRadioButton.setSelected(false);
    }

    @FXML
    private void allViewRadioButtonSelected() {

        weekViewRadioButton.setSelected(false);
        monthViewRadioButton.setSelected(false);
        allViewRadioButton.setSelected(true);
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        // Add radio buttons to toggleGroup
        weekViewRadioButton.setToggleGroup(dateViewToggleGroup);
        monthViewRadioButton.setToggleGroup(dateViewToggleGroup);
        allViewRadioButton.setToggleGroup(dateViewToggleGroup);
    }

}
