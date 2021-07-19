package Main;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml")); // change back after testing
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        primaryStage.setTitle("Appointment Management");
//        primaryStage.setScene(new Scene(root, 600, 400));  // change back after testing
        primaryStage.setScene(new Scene(root, 800, 550));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        DBConnection.startConnection();

//        Locale.setDefault(new Locale("fr"));   // sets language to french

        launch(args);
        DBConnection.closeConnection();
    }
}
