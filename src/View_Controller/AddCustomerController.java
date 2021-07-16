package View_Controller;

import Model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController {
    @FXML private TableView<Customer> customersTableView;
    @FXML private TableColumn<Customer, String> customerIDTableColumn;



//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//
//    }
}
