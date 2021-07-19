package Database;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Database.DBConnection.getConnection;

public class DBCustomers {

    /**
     * Queries database for all customers from customers table
     * @return ObservableList of customer objects
     */
    public static ObservableList<Customer> getCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String getAllCustomers = "SELECT c.Customer_ID, c.Customer_Name, c.Address, "
                + "c.Postal_Code, c.Phone, fld.Division, co.Country, fld.Division_ID "
                + "FROM customers c "
                + "INNER JOIN first_level_divisions fld ON fld.Division_ID = c.Division_ID "
                + "INNER JOIN countries co ON co.Country_ID = fld.Country_ID "
                + "ORDER BY c.Customer_ID";

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
                customer.setDivisionID(rs.getInt("fld.Division_ID"));
                customers.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

    /**
     * Deletes appointments associated with customerID, then deletes customer
     * @param customerID
     */
    public static void deleteCustomer(int customerID) {
        // Delete all appointments from customer first
        DBAppointments.deleteApptByCustID(customerID);

        String deleteCust = "DELETE FROM customers WHERE Customer_ID = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(deleteCust);
            ps.setInt(1, customerID);
            int numCustDeleted = ps.executeUpdate();

            System.out.println(numCustDeleted + " customer deleted. Customer ID: " + customerID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Query to add customer to customers table
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param divID
     */
    public static void addCustomer(String name, String address, String postalCode, String phone, int divID) {
        String addCustQuery = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, " +
                "Division_ID, Created_By, Last_Updated_By) VALUES(?, ?, ?, ?, ?, 'user', 'user')";

        try {
            PreparedStatement ps = getConnection().prepareStatement(addCustQuery);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divID);
            int numRowsAffected = ps.executeUpdate();
            System.out.println(numRowsAffected + " customer(s) added.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Query to update customers table with textfield inputs on Modify Customers screen
     * @param custID
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param divID
     */
    public static void updateCustomer(int custID, String name, String address, String postalCode, String phone, int divID) {
        String updateCustQuery = "UPDATE customers SET Customer_Name = '?', Address = '?', Postal_Code = '?', "
                + "Phone = '?', Division_ID = ? WHERE Customer_ID = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(updateCustQuery);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divID);
            ps.setInt(6, custID);
            int numRowsUpdated = ps.executeUpdate();
            System.out.println(numRowsUpdated + " customer(s) updated.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
