package View_Controller;

import Database.DBAppointments;
import Model.Appointment;
import Model.Contact;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static Database.DBConnection.getConnection;

public class ReportsController implements Initializable {
    @FXML private TableView<Appointment> contactSchedTableView;

    @FXML private TableColumn<Appointment, Integer> apptIDTableColumn;
    @FXML private TableColumn<Appointment, String> titleTableColumn;
    @FXML private TableColumn<Appointment, String> descriptionTableColumn;
    @FXML private TableColumn<Appointment, String> locationTableColumn;
    @FXML private TableColumn<Appointment, String> typeTableColumn;
    @FXML private TableColumn<Appointment, String> startDateTableColumn;
    @FXML private TableColumn<Appointment, String> startTimeTableColumn;
    @FXML private TableColumn<Appointment, String> endDateTableColumn;
    @FXML private TableColumn<Appointment, String> endTimeTableColumn;
    @FXML private TableColumn<Appointment, Integer> custIDTableColumn;

    @FXML private ComboBox<Contact> contactComboBox;

    @FXML private RadioButton apptTypePerMonthRadio;
    @FXML private RadioButton numApptByCust;

    @FXML private TextArea resultTextArea;

    @FXML private ToggleGroup reportToggleGroup = new ToggleGroup();

    @FXML
    private void backButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void apptTypePerMonthReport(ActionEvent event) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Description: Count of customer appointments grouped by type and month.\n");
        sb.append("-----------------------------------------------------------------------------------------\n");

        String apptTypePerMonthQuery = "SELECT MONTHNAME(Start) AS Month, Type, COUNT(*) AS ApptCount\n" +
                                        "FROM appointments GROUP BY MONTHNAME(start), Type;";

        try {
            PreparedStatement ps = getConnection().prepareStatement(apptTypePerMonthQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String month = rs.getString("Month");
                String type = rs.getString("Type");
                String numAppt = rs.getString("ApptCount");

                sb.append(numAppt + " appointment(s) of type " + type + " in " + month + ".\n");
            }
            resultTextArea.setText(sb.toString());
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    @FXML
    private void numApptByCust(ActionEvent event) throws IOException {
        StringBuilder sb = new StringBuilder();
        String numApptPerCustQuery = "SELECT count(*) AS ApptCount, c.Customer_Name\n" +
                                    "FROM appointments a\n" +
                                    "INNER JOIN customers c ON c.Customer_ID = a.Customer_ID\n" +
                                    "GROUP BY a.Customer_ID\n" +
                                    "ORDER BY c.Customer_Name;";

        try {
            PreparedStatement ps = getConnection().prepareStatement(numApptPerCustQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String numAppt = rs.getString("ApptCount");
                String custName = rs.getString("c.Customer_Name");

                sb.append(custName + " has " + numAppt + " appointments scheduled.\n");
            }

            resultTextArea.setText(sb.toString());
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate table values
        apptIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        custIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        // Populate combo box
        contactComboBox.setItems(Database.DBContacts.getAllContacts());

        // Add radiobuttons to table group
        apptTypePerMonthRadio.setToggleGroup(reportToggleGroup);
        numApptByCust.setToggleGroup(reportToggleGroup);

        // Lambda used to implement the listener which populates the contact schedule table according to the
        // contact selected in the comboBox
        contactComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldContact, newContact) -> {
            contactSchedTableView.setItems(DBAppointments.getApptByContact(newContact.getContactID()));
            contactSchedTableView.refresh();
        });
    }
}
