package Model;

import javafx.scene.control.Alert;

/** Alert class used to generate alerts */
public class Alerts {
    /**
     * Appointment alert when unable to add appointment
     * @param s
     */
    public static void apptErrorAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Unable to add appointment.");
        alert.setContentText(s);
        alert.showAndWait();
    }

    /**
     * Customer alert used when unable to add/delete/modify customer
     * @param s1
     * @param s2
     */
    public static void custErrorAlert(String s1, String s2) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Unable to " + s1 + " customer.");
        alert.setContentText(s2);
        alert.showAndWait();
    }

    /**
     * Alert used when no there is no table selection
     * @param s1
     * @param s2
     */
    public static void noSelectionAlert(String s1, String s2) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("No " + s1 + " selected!");
        alert.setContentText("Please select a " + s1 + " from the table and click " + s2 + ".");
        alert.showAndWait();
    }

    /**
     * Alert used when no appointment selected
     * @param s1
     * @param s2
     */
    public static void noApptSelected(String s1, String s2) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Unable to " + s1 + " " + s2 + ".");
        alert.setContentText("No " + s2 + " selected! Please select " + s2 + " from the table.");
        alert.showAndWait();
    }

}
