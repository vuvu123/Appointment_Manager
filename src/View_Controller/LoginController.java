package View_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static Database.DBUsers.verifyCredentials;

import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;

    @FXML private Button submitButton;

    @FXML private Label errorMessageLabel;
    @FXML private Label userLocationLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label titleLabel;

    private Locale userLocale;
    private ResourceBundle rb;

    @FXML
    private void submitButton(ActionEvent event) throws IOException {
        String user = usernameTextField.getText();
        String pass = passwordTextField.getText();

        if (verifyCredentials(user, pass)) {
            Parent parent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            errorMessageLabel.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLocale = Locale.getDefault();
        userLocationLabel.setText(ZoneId.systemDefault().toString());

        rb = ResourceBundle.getBundle("Language.lang", userLocale);
        titleLabel.setText(rb.getString("title"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        submitButton.setText(rb.getString("submitButton"));
        errorMessageLabel.setText(rb.getString("errorLabel"));

    }

}
