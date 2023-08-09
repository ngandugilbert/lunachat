package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;

import Models.User;
import bluetooth.DiscoverDevices;
import bluetooth.Server;
import data.Authentication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class ChatController implements Initializable {
    private User user;

    @FXML
    private ListView<RemoteDevice> serverList;

    @FXML
    private Circle avatar;

    @FXML
    private MenuButton profile;

    @FXML
    private Label hostName;

    private LinkedList<RemoteDevice> devices = new LinkedList<>();

    @FXML
    private void fetchDevices() {
        try {
            DiscoverDevices.discover(); // Discover
            this.devices = DiscoverDevices.getDiscoveredDevices();
        } catch (BluetoothStateException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle) {

        setAvatar();
        updateUser();
        fetchDevices();
        populateDevices();

        // How to change the style of the cell

    }

    private void setAvatar() {
        // setting up the avatar
        Image avatarImage = new Image("/Views/assets/login.png");

        avatar.setFill(new ImagePattern(avatarImage));
    }

    private void updateUser() {
        this.user = Authentication.getLoggedUser();
        profile.setText(user.getName());
    }

    private void populateDevices() {
        serverList.setCellFactory(list -> new ListCell<RemoteDevice>() {
            @Override
            protected void updateItem(RemoteDevice item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    getStyleClass().remove(".list-cell");
                } else {
                    try {
                        setText(item.getFriendlyName(false));
                        getStyleClass().add(".list-cell");
                    } catch (IOException e) {
                        // Handle this bro
                    }
                }
            }
        });
        serverList.getItems().addAll(devices);
    }

    @FXML
    private void refreshDevices() {
        fetchDevices();
        populateDevices();
        System.out.println("refereshed!");
    }

    @FXML
    private void logout() {
        // logs the user out of the chatapp

        devices.clear();
        profile.setText("Guest");
        serverList.getItems().clear();
        System.out.println("loggedout");

    }

    // 
    @FXML
    private void host() {
        System.out.println("Starting server...");
        var server = new Server();
        server.start();
        hostName.setText(server.getServerName());
    }

}