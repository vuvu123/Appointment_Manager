package Main;

import Utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        primaryStage.setTitle("Appointment Creator");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
//        Locale.setDefault(new Locale("fr"));   // sets language to french, use to test translation on login screen

        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}
