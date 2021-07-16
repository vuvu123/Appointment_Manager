package Main;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        primaryStage.setTitle("Appointment Creator");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
//        Locale.setDefault(new Locale("fr"));   // sets language to french, use to test translation on login screen

        DBConnection.startConnection();

        ZonedDateTime date = ZonedDateTime.ofInstant(LocalDateTime.now(), ZoneOffset.UTC, ZoneId.systemDefault());
        System.out.println(date);

        Connection conn = DBConnection.getConnection();

//        launch(args);
        DBConnection.closeConnection();
    }
}
