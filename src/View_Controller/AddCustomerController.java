package View_Controller;

import Database.DBCountries;
import Database.DBCustomers;
import Database.DBFirstLevelDivision;
import Model.Country;
import Model.Customer;
import Model.FirstLevelDivision;
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
import java.util.ResourceBundle;
import static Database.DBFirstLevelDivision.getDivisionsByCountry;

public class AddCustomerController implements Initializable {
    @FXML private TableView<Customer> customersTableView;

    @FXML private TableColumn<Customer, Integer> customerIDTableColumn;
    @FXML private TableColumn<Customer, String> customerNameTableColumn;
    @FXML private TableColumn<Customer, String> customerAddressTableColumn;
    @FXML private TableColumn<Customer, String> postalCodeTableColumn;
    @FXML private TableColumn<Customer, String> phoneTableColumn;
    @FXML private TableColumn<Customer, String> divisionTableColumn;
    @FXML private TableColumn<Customer, String> countryTableColumn;

    @FXML private TextField customerNameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField searchTextField;

    @FXML private ComboBox<FirstLevelDivision> firstLevelDivisionComboBox;
    @FXML private ComboBox<Country> countryComboBox;

    private ObservableList<Customer> custTableView = FXCollections.observableArrayList();

    // Handle buttons
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
        customerNameTextField.setText("");
        addressTextField.setText("");
        postalCodeTextField.setText("");
        phoneNumberTextField.setText("");
        countryComboBox.getSelectionModel().clearSelection();
        firstLevelDivisionComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void clearSearchFieldButton(ActionEvent event) throws IOException {
        searchTextField.setText("");
        customersTableView.setItems(DBCustomers.getCustomers());
        customersTableView.refresh();
    }

    @FXML
    private void searchButton(ActionEvent event) throws IOException {
        customersTableView.setItems(lookUpCustomer(searchTextField.getText()));
        customersTableView.refresh();
    }

    @FXML
    private void addButton(ActionEvent event) throws IOException {
        String custName = customerNameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phone = phoneNumberTextField.getText();
        int divID = firstLevelDivisionComboBox.getValue().getDivisionID();

        DBCustomers.addCustomer(custName, address, postalCode, phone, divID);
        updateCustomersTable();
    }

    private ObservableList<Customer> lookUpCustomer(String custName) {
        String sanitizedCustName = custName.toLowerCase().trim();

        if (!custTableView.isEmpty()) {
            ObservableList<Customer> filteredCustTable = FXCollections.observableArrayList();
            for (Customer cust : custTableView) {
                if (cust.getName().toLowerCase().contains(sanitizedCustName)) {
                    filteredCustTable.add(cust);
                }
            }
            return filteredCustTable;
        }
        return null;
    }

    // Refresh customersTableView
    private void updateCustomersTable() {
        custTableView = DBCustomers.getCustomers();
        customersTableView.setItems(custTableView);
        customersTableView.refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        custTableView = DBCustomers.getCustomers();

        // Populate Customers TableView
        customerIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeTableColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstLevelDivision"));
        countryTableColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        customersTableView.setItems(custTableView);

        //Populate combo boxes
        countryComboBox.setItems(DBCountries.getAllCountries());
        countryComboBox.getSelectionModel().selectFirst();
        firstLevelDivisionComboBox.setItems(getDivisionsByCountry(1));
//        firstLevelDivisionComboBox.getSelectionModel().selectFirst();

        // Listener action when country comboBox selection is changed
        countryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Country ID: " + newValue.getCountryID());
                newValue.setAssocDivisions(getDivisionsByCountry(newValue.getCountryID()));
                firstLevelDivisionComboBox.setItems(newValue.getAssocDivisions());
            }
        });

    }
}
