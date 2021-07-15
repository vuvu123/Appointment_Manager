package DAO;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBCustomers {
    public static ObservableList<Customer> getCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String getAllCustomers = "SELECT * FROM customers";

        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getAllCustomers);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
}
