package Database;

import Model.Appointment;
import Model.DateTimeConversion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static Database.DBConnection.getConnection;

public class DBAppointments {

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String getAllAppointments = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, cu.Customer_Name, " +
                "co.Contact_Name, a.Type, a.Start, a.End, u.User_Name\n" +
                "FROM appointments a\n" +
                "INNER JOIN customers cu ON cu.Customer_ID = a.Customer_ID\n" +
                "INNER JOIN contacts co ON co.Contact_ID = a.Contact_ID\n" +
                "INNER JOIN users u ON u.User_ID = a.User_ID";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllAppointments);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment();

                appt.setAppointmentID(rs.getInt("a.Appointment_ID"));
                appt.setTitle(rs.getString("a.Title"));
                appt.setDescription(rs.getString("a.Description"));
                appt.setLocation(rs.getString("a.Location"));
                appt.setCustName(rs.getString("cu.Customer_Name"));
                appt.setContactName(rs.getString("co.Contact_Name"));
                appt.setType(rs.getString("a.Type"));
                appt.setUserName(rs.getString("u.User_Name"));

                LocalDateTime startLDT = rs.getTimestamp("a.Start").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime startZDT = startLDT.atZone(ZoneId.systemDefault());
                appt.setStart(startZDT);

                appt.setStartDate(DateTimeConversion.convertZDTtoStringLocalDate(startZDT));
                appt.setStartTime(DateTimeConversion.convertZDTtoStringLocalTime(startZDT));

                LocalDateTime endLDT = rs.getTimestamp("a.End").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime endZDT = endLDT.atZone(ZoneId.systemDefault());
                appt.setStart(endZDT);

                appt.setEndDate(DateTimeConversion.convertZDTtoStringLocalDate(endZDT));
                appt.setEndTime(DateTimeConversion.convertZDTtoStringLocalTime(endZDT));

                allAppointments.add(appt);
            }
            return allAppointments;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes all appointments from the parameter customerID
     * @param customerID
     */
    public static void deleteApptByCustID(int customerID) {
        String deleteApptByID = "DELETE FROM appointments WHERE Customer_ID = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(deleteApptByID);
            ps.setInt(1, customerID);
            int numRowsDeleted = ps.executeUpdate();

            System.out.println(numRowsDeleted + " appointments deleted for customerID " + customerID + ".");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
