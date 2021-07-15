package DAO;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static DAO.DBConnection.getConnection;

/*
 * Queries database for all customers
 * @return ObservableList of customers in the database
 */
public class DBCustomers {
    public static ObservableList<Customer> getCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String getAllCustomers = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, "
                + "customers.Postal_Code, customers.Phone, first_level_divisions.Division, countries.Country "
                + "FROM customers "
                + "INNER JOIN first_level_divisions ON first_level_divisions.Division_ID = customers.Division_ID "
                + "INNER JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllCustomers);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(rs.getInt("customer.Customer_ID"));
                customer.setName(rs.getString("customers.Customer_Name"));
                customer.setAddress(rs.getString("customers.Address"));
                customer.setPostalCode(rs.getString("customers.Postal_Code"));
                customer.setPhoneNumber(rs.getString("customers.Phone"));
                customer.setFirstLevelDivision(rs.getString("first_level_divisions.Division"));
                customer.setCountry(rs.getString("countries.Country"));
                customers.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
}
