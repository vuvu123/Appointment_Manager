package Main;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

/** Main Class launches application */
public class Main extends Application {

    /**
     * Opens application starting at Login scene
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        primaryStage.setTitle("Appointment Management");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /** Main starts and closes connection */
    public static void main(String[] args) throws SQLException {
        DBConnection.startConnection();
//        Locale.setDefault(new Locale("fr"));   // sets language to french
        launch(args);
        DBConnection.closeConnection();
    }
}
