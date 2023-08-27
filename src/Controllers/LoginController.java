package Controllers;

import java.io.IOException;

import data.Authentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private PasswordField password;

    @FXML
    private Label LoginStatus;

    @FXML
    private Label signUpStatus;

    @FXML
    private TextField username;

    @FXML
    private TextField signUpusername;

    @FXML
    private TextField signUpfullname;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    void submitLogin(ActionEvent event) {
        if (Authentication.login(username.getText(), password.getText())) {
            // Login was a success
            switchScene(event, "chat");
            System.out.println("Logged in");
        } else {
            // failed to log in
            LoginStatus.setText("Username or password was incorrect! Please Try again.");
            System.out.println("Login failed");
        }
    }

    @FXML
    void submitSignup(ActionEvent event) {
        if (Authentication.createAccount(signUpusername.getText(), signUpPassword.getText(),
                signUpfullname.getText())) {
            // Login was a success
            switchScene(event, "chat");
            System.out.println("Logged in");
        } else {
            // failed to login
            signUpStatus = new Label();
            signUpStatus.setText("Username or password was incorrect! Please Try again.");
        }
    }

    // switch to another scene
    private void switchScene(ActionEvent event, String sceneName) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/" + sceneName + ".fxml"));
            Parent root = loader.load();

            Node node = (Node) event.getSource();
            Scene currentScene = node.getScene();

            // show the current scene
            currentScene.setRoot(root);

        } catch (IOException e) {
            // Remember to handle this bro!
        }
    }

    public void loginSwitch(ActionEvent event) {
        switchScene(event, "login");
    }

    public void signUpSwitch(ActionEvent event) {
        switchScene(event, "signup");
    }

}
