package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.bluetooth.RemoteDevice;

import bluetooth.DiscoverDevices;
import bluetooth.Server;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class ChatControllerNew implements Initializable {
    @FXML
    private VBox chatRoomList;
    private LinkedList<RemoteDevice> devices;
    @FXML
    private Label serverName;

    @FXML
    private VBox bubbleContainer;

    @FXML
    private Circle avatar;

    @FXML
    private MenuButton profile;

    @FXML
    private Label hostName;

    @FXML
    private Label serverAddress;
    private RemoteDevice selectedDevice;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        // fetch the devices
        fetchDevices();

    }

    // populate the devices vbox
    private void populateDevices() {
        // create a card for each device
        for (RemoteDevice device : devices) {
            try {
                createCard(device);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void fetchDevices() {
        // create a thread to discover devices
        DiscoverDevices discoverDevices = new DiscoverDevices();

        Thread thread = new Thread(discoverDevices);
        thread.start();
        // when the thread is done get the discovered devices
        try {
            thread.join();
            devices = discoverDevices.getDiscoveredDevices();

            populateDevices();
            // print done
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createCard(RemoteDevice device) throws IOException {
        HBox chatRoomCard = new HBox();
        chatRoomCard.getStyleClass().add("chat-room-card");
        chatRoomCard.setPrefHeight(60);
        chatRoomCard.setPrefWidth(266);
        chatRoomCard.setSpacing(10);
        chatRoomCard.setAlignment(javafx.geometry.Pos.CENTER);
        chatRoomCard.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

        VBox chatRoomDetails = new VBox();
        Label chatRoomName = new Label(device.getFriendlyName(false));
        chatRoomName.getStyleClass().add("chat-room-name");
        Label chatRoomAddress = new Label(device.getBluetoothAddress());

        chatRoomDetails.getChildren().add(chatRoomName);
        chatRoomDetails.getChildren().add(chatRoomAddress);
        chatRoomDetails.setPrefHeight(60);
        chatRoomDetails.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        chatRoomDetails.setPrefWidth(200);

        Circle circle = new Circle();
        circle.setRadius(23);
        circle.getStyleClass().add("chat-room-card-image");

        // Button connect = new Button();
        // connect.getStyleClass().add("chat-room-connect-button");
        // connect.setPrefHeight(40);
        // connect.setPrefWidth(40);
        // add an image inside the button
        // set the image size to 25x25
        // Image image = new
        // Image(getClass().getResourceAsStream("/Views/assets/connection.png"));
        // ImageView imageView = new ImageView(image);
        // imageView.setFitHeight(25);
        // imageView.setFitWidth(25);
        // connect.setGraphic(imageView);

        chatRoomCard.getChildren().add(circle);
        chatRoomCard.getChildren().add(chatRoomDetails);
        // chatRoomCard.getChildren().add(connect);

        chatRoomList.getChildren().add(chatRoomCard);
        // on card click show details
        chatRoomCard.setOnMouseClicked(e -> {
            // set server name and address
            try {
                this.selectedDevice = device;
                serverName.setText(device.getFriendlyName(false));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            serverAddress.setText(device.getBluetoothAddress());
        });
    }

    @FXML
    private boolean connectDevice() {
        // connect to the selected device using bluetooth
        try {
            if (selectedDevice.authenticate()) {
                // print the device name
                System.out.println("Connected to " + selectedDevice.getFriendlyName(false));
                return true;
            }

        } catch (Exception e) {
        }
        // print the device name
        System.out.println("failed to connect");
        return false;
    }

    @FXML
    private void host() {
        System.out.println("Starting server...");
        var server = new Server();
        server.start();

    }

    @FXML
    private void logout() {
        // logs the user out of the chatapp

        devices.clear();
        profile.setText("Guest");
        chatRoomList.getChildren().clear();
        System.out.println("loggedout");

    }

    @FXML
    private void sendMessage() {
        // send a message to the selected device
        System.out.println("sending message");
    }

    @FXML
    private void becomeHost() {
        // become a host
        System.out.println("becoming host");
    }
}
