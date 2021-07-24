package View_Controller;

import Database.DBAppointments;
import Model.Appointment;
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
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML private TableView<Appointment> appointmentsTableView;

    @FXML private TableColumn<Appointment, Integer> apptIDTableColumn;
    @FXML private TableColumn<Appointment, String> titleTableColumn;
    @FXML private TableColumn<Appointment, String> descriptionTableColumn;
    @FXML private TableColumn<Appointment, String> locationTableColumn;
    @FXML private TableColumn<Appointment, String> contactTableColumn;
    @FXML private TableColumn<Appointment, String> typeTableColumn;
    @FXML private TableColumn<Appointment, String> startDateTableColumn;
    @FXML private TableColumn<Appointment, String> startTimeTableColumn;
    @FXML private TableColumn<Appointment, String> endDateTableColumn;
    @FXML private TableColumn<Appointment, String> endTimeTableColumn;
    @FXML private TableColumn<Appointment, Integer> custIDTableColumn;

    @FXML private RadioButton weekViewRadioButton;
    @FXML private RadioButton monthViewRadioButton;
    @FXML private RadioButton allViewRadioButton;

    @FXML private ToggleGroup dateViewToggleGroup = new ToggleGroup();

    @FXML private DatePicker datePicker;

/*
 *  Handle navigation buttons
 */
    @FXML
    private void signoutButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
}

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

    // Filter appointments table by week
    @FXML
    private void weekViewRadioButtonSelected() {
        LocalDate filterDate = datePicker.getValue();
        String year = String.valueOf(filterDate.getYear());
        int weekOfYear = filterDate.get(WeekFields.of(Locale.US).weekOfYear());

        appointmentsTableView.setItems(DBAppointments.getApptByWeek(weekOfYear, year));
        appointmentsTableView.refresh();
    }

    // Filter appointments table by month
    @FXML
    private void monthViewRadioButtonSelected() {
        String filterDate = datePicker.getValue().toString();
        String month = filterDate.substring(5, 7);
        String year = filterDate.substring(0, 4);

        appointmentsTableView.setItems(DBAppointments.getApptByMonth(month, year));
        appointmentsTableView.refresh();
    }

    // Show all appointments in table
    @FXML
    private void allViewRadioButtonSelected() {
        appointmentsTableView.setItems(DBAppointments.getAllAppointments());
    }

    // Refreshes table if date is changed
    @FXML private void datePickerChanged(ActionEvent event) throws IOException {
        if (weekViewRadioButton.isSelected()) {
            weekViewRadioButtonSelected();
        } else if (monthViewRadioButton.isSelected()) {
            monthViewRadioButtonSelected();
        } else {
            allViewRadioButtonSelected();
        }
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        // Populate appointments table
        apptIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactTableColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        custIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        appointmentsTableView.setItems(DBAppointments.getAllAppointments());

        // Add radio buttons added to toggleGroup
        weekViewRadioButton.setToggleGroup(dateViewToggleGroup);
        monthViewRadioButton.setToggleGroup(dateViewToggleGroup);
        allViewRadioButton.setToggleGroup(dateViewToggleGroup);
        allViewRadioButton.setSelected(true);

        // Set datePicker to today's date
        datePicker.setValue(LocalDate.now());

    }

}
