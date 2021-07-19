package View_Controller;

import Database.DBCountries;
import Database.DBCustomers;
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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Database.DBFirstLevelDivision.*;

public class ModifyCustomerController implements Initializable {
    @FXML
    private TableView<Customer> customersTableView;

    @FXML private TableColumn<Customer, Integer> customerIDTableColumn;
    @FXML private TableColumn<Customer, String> customerNameTableColumn;
    @FXML private TableColumn<Customer, String> customerAddressTableColumn;
    @FXML private TableColumn<Customer, String> postalCodeTableColumn;
    @FXML private TableColumn<Customer, String> phoneTableColumn;
    @FXML private TableColumn<Customer, String> divisionTableColumn;
    @FXML private TableColumn<Customer, String> countryTableColumn;

    @FXML private TextField customerIDTextField;
    @FXML private TextField customerNameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField searchTextField;

    @FXML private Label infoLabel;

    @FXML private ComboBox<FirstLevelDivision> firstLevelDivisionComboBox;
    @FXML private ComboBox<Country> countryComboBox;

    private ObservableList<Customer> custTableView = FXCollections.observableArrayList();

    private static Customer selectedCust;

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
        updateCustomersTable();
    }

    @FXML
    private void searchButton(ActionEvent event) throws IOException {
        customersTableView.setItems(lookUpCustomer(searchTextField.getText()));
        customersTableView.refresh();
    }

    @FXML
    private void saveButton(ActionEvent event) throws IOException {
        selectedCust = customersTableView.getSelectionModel().getSelectedItem();

        if (selectedCust != null) {
            int custID = Integer.parseInt(customerIDTextField.getText());
            String custName = customerNameTextField.getText();
            String address = addressTextField.getText();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneNumberTextField.getText();
            int divID = firstLevelDivisionComboBox.getValue().getDivisionID();

            // Need to add validation for blank fields
            DBCustomers.updateCustomer(custID, custName, address, postalCode, phone, divID);
            updateCustomersTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to save product.");
            alert.setContentText("No customer selected! Please select a customer from the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteButton(ActionEvent event) throws IOException {
        selectedCust = customersTableView.getSelectionModel().getSelectedItem();

        if (selectedCust != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Confirm Delete Customer");
            alert.setContentText("Are you sure you want to delete " + selectedCust.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                int custID = selectedCust.getCustomerID();
                DBCustomers.deleteCustomer(custID);
                updateCustomersTable();
                infoLabel.setText(selectedCust.getName() + " was successfully deleted.");
                infoLabel.setVisible(true);
            } else {
                System.out.println("Deletion of customer " + selectedCust.getName() +" delete cancelled.");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to delete product.");
            alert.setContentText("No customer selected! Please select a customer from the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void modifyButton(ActionEvent event) throws IOException {
        selectedCust = customersTableView.getSelectionModel().getSelectedItem();

        if (selectedCust != null) {
            enableFields();

            customerIDTextField.setText(String.valueOf(selectedCust.getCustomerID()));
            customerNameTextField.setText(selectedCust.getName());
            addressTextField.setText(selectedCust.getAddress());
            postalCodeTextField.setText(selectedCust.getPostalCode());
            phoneNumberTextField.setText(selectedCust.getPhoneNumber());
            countryComboBox.setValue(lookUpCountry(selectedCust.getCountry()));
            firstLevelDivisionComboBox.setValue(lookUpFirstDiv(selectedCust.getDivisionID()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Unable to modify product.");
            alert.setContentText("No customer selected! Please select a customer from the table.");
            alert.showAndWait();
        }
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

    private FirstLevelDivision lookUpFirstDiv(int divID) {
        for (FirstLevelDivision div : firstLevelDivisionComboBox.getItems()) {
            if (div.getDivisionID() == divID) {
                return div;
            }
        }
        return null;
    }

    private Country lookUpCountry(String name) {
        for (Country c : countryComboBox.getItems()) {
            if (c.getCountry().equals(name)) {
                return c;
            }
        }
        return null;
    }

    // Refresh customersTableView
    private void updateCustomersTable() {
        customersTableView.setItems(DBCustomers.getCustomers());
        customersTableView.refresh();
    }

    private void enableFields() {
        customerNameTextField.setDisable(false);
        addressTextField.setDisable(false);
        postalCodeTextField.setDisable(false);
        phoneNumberTextField.setDisable(false);
        firstLevelDivisionComboBox.setDisable(false);
        countryComboBox.setDisable(false);
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

        infoLabel.setVisible(false);

        // Listener action when country comboBox selection is changed
        countryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setAssocDivisions(getDivisionsByCountry(newValue.getCountryID()));
                firstLevelDivisionComboBox.setItems(newValue.getAssocDivisions());
            }
        });
    }
}
