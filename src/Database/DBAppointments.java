package Database;

import Model.Appointment;
import Model.DateTimeConversion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static Database.DBConnection.getConnection;
import static Model.DateTimeConversion.formatLDT;

/** All SQL queries associated with Appointments */
public class DBAppointments {

    /**
     * Query to populate appointments table on AddAppointment.fxml and ModifyAppointment.fxml
     * @return ObservableList of Appointment objects
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String getAllAppointments = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, cu.Customer_Name, " +
                "co.Contact_Name, a.Type, a.Start, a.End, u.User_Name, a.Customer_ID\n" +
                "FROM appointments a\n" +
                "INNER JOIN customers cu ON cu.Customer_ID = a.Customer_ID\n" +
                "INNER JOIN contacts co ON co.Contact_ID = a.Contact_ID\n" +
                "INNER JOIN users u ON u.User_ID = a.User_ID\n" +
                "ORDER BY a.Start";

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
                appt.setCustomerID(rs.getInt("a.Customer_ID"));

                LocalDateTime startLDT = rs.getTimestamp("a.Start").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime startZDT = startLDT.atZone(ZoneId.systemDefault());
                appt.setStart(startZDT);

                appt.setStartDate(DateTimeConversion.convertZDTtoStringLocalDate(startZDT));
                appt.setStartTime(DateTimeConversion.convertZDTtoStringLocalTime(startZDT));

                LocalDateTime endLDT = rs.getTimestamp("a.End").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime endZDT = endLDT.atZone(ZoneId.systemDefault());
                appt.setEnd(endZDT);

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

            System.out.println(numRowsDeleted + " appointment(s) deleted for customerID " + customerID + ".");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete appointment by appointment ID.
     * @param apptID
     */
    public static void deleteApptByApptID(int apptID) {
        String deleteByApptID = "DELETE FROM appointments WHERE Appointment_ID = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(deleteByApptID);
            ps.setInt(1, apptID);
            int numRowsDeleted = ps.executeUpdate();

            System.out.println(numRowsDeleted + " appointment(s) deleted for appointmentID " + apptID + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add new appointment to appointments table
     * @param title
     * @param description
     * @param location
     * @param type
     * @param contactID
     * @param custID
     * @param startDateTime
     * @param endDateTime
     * @param userID
     */
    public static void addAppointment(String title, String description, String location, String type, int contactID,
                                      int custID, String startDateTime, String endDateTime, int userID) {
        String addApptQuery = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Customer_ID, " +
                "User_ID, Contact_ID, CREATED_BY, Last_Updated_By) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, 'user', 'user')";

        try {
            PreparedStatement ps = getConnection().prepareStatement(addApptQuery);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, startDateTime);
            ps.setString(6, endDateTime);
            ps.setInt(7, custID);
            ps.setInt(8, userID);
            ps.setInt(9, contactID);

            int numRowsAdded = ps.executeUpdate();

            System.out.println(numRowsAdded + " appointment(s) added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates appointment in database
     * @param apptID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param custID
     * @param userID
     * @param contactID
     * @param start
     * @param end
     */
    public static void updateAppointment(int apptID, String title, String description, String location, String type,
                                         int custID, int userID, int contactID, String start, String end) {
        String updateApptQuery = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(updateApptQuery);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, start);
            ps.setString(6, end);
            ps.setInt(7, custID);
            ps.setInt(8, userID);
            ps.setInt(9, contactID);
            ps.setInt(10, apptID);
            int numRowsUpdated = ps.executeUpdate();
            System.out.println(numRowsUpdated + " appointment(s) updated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Query to populate table with appointments in same month as date picked
     * @param month
     * @param year
     * @return Filtered list of appointments in the same month as date picked
     */
    public static ObservableList<Appointment> getApptByMonth(String month, String year) {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String getAllAppointments = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, " +
                "co.Contact_Name, a.Type, a.Start, a.End, a.Customer_ID\n" +
                "FROM appointments a\n" +
                "INNER JOIN customers cu ON cu.Customer_ID = a.Customer_ID\n" +
                "INNER JOIN contacts co ON co.Contact_ID = a.Contact_ID\n" +
                "WHERE MONTH(a.Start) = ? AND YEAR(a.Start) = ?\n" +
                "ORDER BY a.Start";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllAppointments);
            ps.setString(1, month);
            ps.setString(2, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment();

                appt.setAppointmentID(rs.getInt("a.Appointment_ID"));
                appt.setTitle(rs.getString("a.Title"));
                appt.setDescription(rs.getString("a.Description"));
                appt.setLocation(rs.getString("a.Location"));
                appt.setContactName(rs.getString("co.Contact_Name"));
                appt.setType(rs.getString("a.Type"));
                appt.setCustomerID(rs.getInt("a.Customer_ID"));

                LocalDateTime startLDT = rs.getTimestamp("a.Start").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime startZDT = startLDT.atZone(ZoneId.systemDefault());
                appt.setStart(startZDT);

                appt.setStartDate(DateTimeConversion.convertZDTtoStringLocalDate(startZDT));
                appt.setStartTime(DateTimeConversion.convertZDTtoStringLocalTime(startZDT));

                LocalDateTime endLDT = rs.getTimestamp("a.End").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime endZDT = endLDT.atZone(ZoneId.systemDefault());
                appt.setEnd(endZDT);

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
     * Returns all appointments filtered by week and year
     * Used in appointments by week report
     * @param week
     * @param year
     * @return ObservableList of Appointment objects
     */
    public static ObservableList<Appointment> getApptByWeek(int week, String year) {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String getAllAppointments = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, " +
                "co.Contact_Name, a.Type, a.Start, a.End, a.Customer_ID\n" +
                "FROM appointments a\n" +
                "INNER JOIN customers cu ON cu.Customer_ID = a.Customer_ID\n" +
                "INNER JOIN contacts co ON co.Contact_ID = a.Contact_ID\n" +
                "WHERE WEEK(a.Start) + 1 = ? AND YEAR(a.Start) = ?\n" +
                "ORDER BY a.Start";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllAppointments);
            ps.setInt(1, week);
            ps.setString(2, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment();

                appt.setAppointmentID(rs.getInt("a.Appointment_ID"));
                appt.setTitle(rs.getString("a.Title"));
                appt.setDescription(rs.getString("a.Description"));
                appt.setLocation(rs.getString("a.Location"));
                appt.setContactName(rs.getString("co.Contact_Name"));
                appt.setType(rs.getString("a.Type"));
                appt.setCustomerID(rs.getInt("a.Customer_ID"));

                LocalDateTime startLDT = rs.getTimestamp("a.Start").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime startZDT = startLDT.atZone(ZoneId.systemDefault());
                appt.setStart(startZDT);

                appt.setStartDate(DateTimeConversion.convertZDTtoStringLocalDate(startZDT));
                appt.setStartTime(DateTimeConversion.convertZDTtoStringLocalTime(startZDT));

                LocalDateTime endLDT = rs.getTimestamp("a.End").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime endZDT = endLDT.atZone(ZoneId.systemDefault());
                appt.setEnd(endZDT);

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
     * Returns appointment scheduled filtered by contact
     * Used in Schedule report
     * @param contactID
     * @return ObservableList of appointment objects
     */
    public static ObservableList<Appointment> getApptByContact(int contactID) {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String getAllAppointments = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, " +
                "a.Type, a.Start, a.End, a.Customer_ID\n" +
                "FROM appointments a\n" +
                "INNER JOIN customers cu ON cu.Customer_ID = a.Customer_ID\n" +
                "INNER JOIN contacts co ON co.Contact_ID = a.Contact_ID\n" +
                "WHERE co.Contact_ID = ?\n" +
                "ORDER BY a.Start";

        try {
            PreparedStatement ps = getConnection().prepareStatement(getAllAppointments);
            ps.setInt(1, contactID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment();

                appt.setAppointmentID(rs.getInt("a.Appointment_ID"));
                appt.setTitle(rs.getString("a.Title"));
                appt.setDescription(rs.getString("a.Description"));
                appt.setLocation(rs.getString("a.Location"));
                appt.setType(rs.getString("a.Type"));
                appt.setCustomerID(rs.getInt("a.Customer_ID"));

                LocalDateTime startLDT = rs.getTimestamp("a.Start").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime startZDT = startLDT.atZone(ZoneId.systemDefault());
                appt.setStart(startZDT);

                appt.setStartDate(DateTimeConversion.convertZDTtoStringLocalDate(startZDT));
                appt.setStartTime(DateTimeConversion.convertZDTtoStringLocalTime(startZDT));

                LocalDateTime endLDT = rs.getTimestamp("a.End").toLocalDateTime();
                // Convert from UTC to systemDefault (User's Local Time)
                ZonedDateTime endZDT = endLDT.atZone(ZoneId.systemDefault());
                appt.setEnd(endZDT);

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
     * Looks up appointments starting within 15 minutes of current dateTime and logged in user
     * Generates alert saying if there is an upcoming appointment or not
     * @param userID
     */
    public static void lookUpApptsInFifteen(int userID) {
        String ApptsInFifteenUserQuery = "SELECT Appointment_ID, Start FROM appointments WHERE User_ID = ? AND Start " +
                "BETWEEN ? AND ?";

        try {
            PreparedStatement ps = getConnection().prepareStatement(ApptsInFifteenUserQuery);
            ps.setInt(1, userID);
            ps.setString(2, String.valueOf(LocalDateTime.now(ZoneId.of("UTC"))));
            ps.setString(3, String.valueOf(LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(15)));
            ResultSet rs = ps.executeQuery();
            rs.next();
            int apptID = rs.getInt("Appointment_ID");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            System.out.println(start);
//            String startDateTime = DateTimeConversion.LDTUTCtoStringLocalTime(start);

            System.out.println("Appointment ID: " + apptID + ", Start: " + formatLDT(start));

            // Upcoming Appointment Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment!");
            alert.setHeaderText("Upcoming Appointment!");
            alert.setContentText("Starting soon!\nAppointment ID: " + apptID + " , Starting at: " + formatLDT(start));
            alert.showAndWait();

        } catch (Exception e) {
            System.out.println("No appointment in next 15 minutes.");

            // Freedom Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You are free!");
            alert.setHeaderText("No Upcoming Appointment!");
            alert.setContentText("No upcoming appointments within the next 15 minutes.");
            alert.showAndWait();
        }
    }
}
