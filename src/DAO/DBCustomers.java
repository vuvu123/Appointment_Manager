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
        String getAllCustomers = "SELECT c.Customer_ID, c.Customer_Name, c.Address, "
                + "c.Postal_Code, c.Phone, fld.Division, co.Country "
                + "FROM customers c"
                + "INNER JOIN first_level_divisions fld ON fld.Division_ID = c.Division_ID "
                + "INNER JOIN countries co ON co.Country_ID = fld.Country_ID";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllCustomers);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(rs.getInt("c.Customer_ID"));
                customer.setName(rs.getString("c.Customer_Name"));
                customer.setAddress(rs.getString("c.Address"));
                customer.setPostalCode(rs.getString("c.Postal_Code"));
                customer.setPhoneNumber(rs.getString("c.Phone"));
                customer.setFirstLevelDivision(rs.getString("fld.Division"));
                customer.setCountry(rs.getString("co.Country"));
                customers.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
}
