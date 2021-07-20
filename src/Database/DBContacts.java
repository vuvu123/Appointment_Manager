package Database;

import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Database.DBConnection.getConnection;

public class DBContacts {

    /**
     * Returns all contacts from contacts table
     * @return ObservableList of contact objects
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        String getAllContacts = "SELECT * FROM contacts";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllContacts);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Contact contact = new Contact();
                contact.setContactID(rs.getInt("Contact_ID"));
                contact.setContactName(rs.getString("Contact_Name"));
                contact.setEmail(rs.getString("Email"));

                allContacts.add(contact);
            }
            return allContacts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
