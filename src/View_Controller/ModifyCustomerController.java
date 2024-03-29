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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Database.DBFirstLevelDivision.*;
import static Model.Alerts.custErrorAlert;
import static Model.Alerts.noSelectionAlert;

/** Controls Modify Customer screen */
public class ModifyCustomerController implements Initializable {
    @FXML private TableView<Customer> customersTableView;

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

    @FXML private Button clearSearchFieldButton;
    @FXML private Button searchButton;
    @FXML private Button deleteButton;

    @FXML private Label infoLabel;

    @FXML private ComboBox<FirstLevelDivision> firstLevelDivisionComboBox;
    @FXML private ComboBox<Country> countryComboBox;

    private ObservableList<Customer> custTableView = FXCollections.observableArrayList();

    private static Customer selectedCust;

    /**
     * Handles cancel button, returns to MainScreen scene
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
     * Handles clear button, clears user input fields
     * @param event
     * @throws IOException
     */
    @FXML
    private void clearButton(ActionEvent event) throws IOException {
        customerIDTextField.setText("");
        customerNameTextField.setText("");
        addressTextField.setText("");
        postalCodeTextField.setText("");
        phoneNumberTextField.setText("");
        countryComboBox.getSelectionModel().clearSelection();
        firstLevelDivisionComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Handles clearSarchField button, clears field and refreshes customer table view
     * @param event
     * @throws IOException
     */
    @FXML
    private void clearSearchFieldButton(ActionEvent event) throws IOException {
        searchTextField.setText("");
        updateCustomersTable();
    }

    /**
     * Handles search button, searches by customer name, displays customer list on table
     * @param event
     * @throws IOException
     */
    @FXML
    private void searchButton(ActionEvent event) throws IOException {
        customersTableView.setItems(lookUpCustomer(searchTextField.getText()));
        customersTableView.refresh();
    }

    /**
     * Handles save button: If customer is selected and passes input validation, then update customer.
     * Then disables/enables appropriate fields.
     * @param event
     * @throws IOException
     */
    @FXML
    private void saveButton(ActionEvent event) throws IOException {
        selectedCust = customersTableView.getSelectionModel().getSelectedItem();

        if (selectedCust != null) {
            String custID = customerIDTextField.getText();
            String custName = customerNameTextField.getText();
            String address = addressTextField.getText();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneNumberTextField.getText();
            boolean isDivisionEmpty = firstLevelDivisionComboBox.getValue() == null;

            if (custID.isEmpty() || custName.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty() || isDivisionEmpty) {
                custErrorAlert("modify", "Please fill out all input fields.");
            } else {
                int divID = firstLevelDivisionComboBox.getValue().getDivisionID();

                DBCustomers.updateCustomer(custID, custName, address, postalCode, phone, divID);
                updateCustomersTable();
                enableFieldsOnSave();
                clearButton(event);
                disableFieldsOnSave();
            }
        } else {
            noSelectionAlert("customer", "modify");
        }
    }

    /**
     * Handles deleting customers: Adds prompt to confirm deletion, then displays message of customer deletion
     * @param event
     * @throws IOException
     */
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
                clearButton(event);
            } else {
                System.out.println("Deletion of customer " + selectedCust.getName() +" delete canceled.");
            }
        } else {
            noSelectionAlert("customer", "delete");
        }
    }

    /**
     * Handles modify button: enables and populates input fields of selected customer
     * @param event
     * @throws IOException
     */
    @FXML
    private void modifyButton(ActionEvent event) throws IOException {
        selectedCust = customersTableView.getSelectionModel().getSelectedItem();

        if (selectedCust != null) {
            enableFieldsOnModify();
            disableFieldsOnModify();

            customerIDTextField.setText(String.valueOf(selectedCust.getCustomerID()));
            customerNameTextField.setText(selectedCust.getName());
            addressTextField.setText(selectedCust.getAddress());
            postalCodeTextField.setText(selectedCust.getPostalCode());
            phoneNumberTextField.setText(selectedCust.getPhoneNumber());
            countryComboBox.setValue(lookUpCountry(selectedCust.getCountry()));
            firstLevelDivisionComboBox.setValue(lookUpFirstDiv(selectedCust.getDivisionID()));
        } else {
            noSelectionAlert("customer", "modify");
        }
    }

    /**
     * Used Lambda expression to add matching customer names to ObservableList filteredCustTable
     * @param custName String passed in from searchTextField
     * @return ObservableList of Customer objects
     */
    private ObservableList<Customer> lookUpCustomer(String custName) {
        String sanitizedCustName = custName.toLowerCase().trim();

        if (!custTableView.isEmpty()) {
            ObservableList<Customer> filteredCustTable = FXCollections.observableArrayList();
            // LAMBDA USED HERE //
            custTableView.forEach(cust -> {
                if (cust.getName().toLowerCase().contains(sanitizedCustName)) {
                    filteredCustTable.add(cust);
                }
            });
            return filteredCustTable;
        }
        return null;
    }

    /**
     * Searches for divisionID passed in from combobox, returns associated FirstLevelDivision object
     * @param divID passed in from combobox selection
     * @return FirstLevelDivision object
     */
    private FirstLevelDivision lookUpFirstDiv(int divID) {
        for (FirstLevelDivision div : firstLevelDivisionComboBox.getItems()) {
            if (div.getDivisionID() == divID) {
                return div;
            }
        }
        return null;
    }

    /**
     * Looks up country name passed in from combobox, returns associated country object
     * @param name passed in from combobox selection
     * @return Country object
     */
    private Country lookUpCountry(String name) {
        for (Country c : countryComboBox.getItems()) {
            if (c.getCountry().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Updates observableList custTableView and refreshes table
     */
    private void updateCustomersTable() {
        custTableView = DBCustomers.getCustomers();
        customersTableView.setItems(custTableView);
        customersTableView.refresh();
    }

    /**
     * Enables appropriate fields when modify button is clicked
     */
    private void enableFieldsOnModify() {
        customerNameTextField.setDisable(false);
        addressTextField.setDisable(false);
        postalCodeTextField.setDisable(false);
        phoneNumberTextField.setDisable(false);
        firstLevelDivisionComboBox.setDisable(false);
        countryComboBox.setDisable(false);
    }

    /**
     * Disables appropriate fields when modify button is clicked
     */
    private void disableFieldsOnModify() {
        clearSearchFieldButton.setDisable(true);
        deleteButton.setDisable(true);
        searchTextField.setDisable(true);
        searchButton.setDisable(true);
    }

    /**
     * Enables appropriate fields when Save button is clicked
     */
    private void enableFieldsOnSave() {
        clearSearchFieldButton.setDisable(false);
        deleteButton.setDisable(false);
        searchTextField.setDisable(false);
        searchButton.setDisable(false);
    }

    /**
     * Disables appropriate fields when Save button is clicked
     */
    private void disableFieldsOnSave() {
        customerNameTextField.setDisable(true);
        addressTextField.setDisable(true);
        postalCodeTextField.setDisable(true);
        phoneNumberTextField.setDisable(true);
        firstLevelDivisionComboBox.setDisable(true);
        countryComboBox.setDisable(true);
    }

    /**
     * Lambda used to create an action on the countryComboBox which changes the firstLevelDivision
     * comboBox list to the reflect the country selected
     * Populates customers table, populates combo boxes, adds listener for country combobox
     * @param url
     * @param rb
     */
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

        // Lambda used to add listener for changes in country combobox
        countryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setAssocDivisions(getDivisionsByCountry(newValue.getCountryID()));
                firstLevelDivisionComboBox.setItems(newValue.getAssocDivisions());
                firstLevelDivisionComboBox.getSelectionModel().selectFirst();
            }
        });
    }
}
