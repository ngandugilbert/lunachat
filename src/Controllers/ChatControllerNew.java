package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javax.bluetooth.RemoteDevice;
import bluetooth.Chat;
import bluetooth.Discover;
import bluetooth.Server;
import data.Authentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class ChatControllerNew implements Initializable {
    @FXML
    private VBox chatRoomList;
    private LinkedList<RemoteDevice> devices;
    private boolean isHost = false;
    private Chat chat;

    @FXML
    private ScrollPane messageScroll;

    @FXML
    private MenuItem host;
    @FXML
    private Button connectBtn;

    @FXML
    private TextField message;

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

    @FXML
    private VBox bubble;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        // fetch the devices
        fetchDevices();
        updateInfo();

    }

    @FXML
    // populate the devices vbox
    private void populateDevices() {
        if (devices.isEmpty()) {
            return;
        } else {
            // create a card for each device
            for (RemoteDevice device : devices) {
                try {
                    createCard(device);
                } catch (IOException e) {
                    // handle this
                }
            }
            // clear the list once all are added
            devices.clear();
        }
    }

    private void updateInfo() {
        this.profile.setText(Authentication.getLoggedUser().getUsername());

    }

    @FXML
    private void fetchDevices() {
        // create a thread to discover devices
        Discover explorer = new Discover();
        Thread thread = new Thread(explorer);
        thread.start();

        // when the thread is done get the discovered devices
        try {
            thread.join();
            devices = explorer.getFoundDevices();
            // print done
            System.out.println("done fetching");
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

                e1.printStackTrace();
            }
            serverAddress.setText(device.getBluetoothAddress());
        });
    }
    

    // create a chat bubble
    public void createChatBubble(String message, String sender) {
        HBox messageBubble = new HBox();
        messageBubble.getStyleClass().add("chat-bubble");
        messageBubble.setPrefHeight(100);
        messageBubble.setMinWidth(580);
        messageBubble.setMinHeight(100);

        messageBubble.setSpacing(10);

        messageBubble.setPadding(new javafx.geometry.Insets(0, 0, 0, 20));

        Label username = new Label(sender);
        // username.getStyleClass().add("chat-bubble-header");
        username.setMinWidth(652);
        username.setMinHeight(40);
        username.setStyle(username.getStyle() + "-fx-font-size: 16px; -fx-fill: black; -fx-font-weight: bold;");

        HBox.setHgrow(username, javafx.scene.layout.Priority.ALWAYS);

        Text messageTest = new Text(message);
        messageTest.setWrappingWidth(700);
        messageTest.setStyle(messageTest.getStyle() + "-fx-font-size: 14px; -fx-fill: black;-fx-background: red;");

        bubble = new VBox();
        bubble.getChildren().add(username);
        bubble.getChildren().add(messageTest);
        bubble.setSpacing(10);
        messageBubble.getChildren().add(bubble);

        messageScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        messageScroll.setContent(bubbleContainer);
        bubbleContainer.getChildren().add(messageBubble);

    }

    @FXML
    private void connectDevice() {
        // connect to the selected device using bluetooth
        // Find the device
        chat = new Chat();
        chat.setSearchArgs(selectedDevice.getBluetoothAddress());
        var thread = new Thread(chat);
        try {
            thread.join();

            if (selectedDevice == null) {
                System.out.println("Failed");
            } else {
                if (chat.isSessionStarted()) {
                    // Start session
                    connectBtn.getStyleClass().add("chat-room-connect-button-active");
                    System.out.println("Connected");
                }

            }
        } catch (Exception e) {
            // Something went wrong
            System.out.println("Something happened");
        }
    }

    @FXML
    private void host() {
        System.out.println("Starting server...");
        var server = new Server();
        server.start();

    }

    /**
     * @param event
     */
    @FXML
    private void logout(ActionEvent event) {
        // logs the user out of the chatapp
        devices.clear();
        profile.setText("Guest");
        chatRoomList.getChildren().clear();
        switchScene(event, "login");
        System.out.println("loggedout");

    }

    @FXML
    private void sendMessage() {
        // send a message to the selected device
        if (message.getText().isEmpty()) {
            return;
        } else {
            if (chat.isSessionStarted()) {
                createChatBubble(message.getText(), "Me");
                chat.sendMessage(message.getText());
            }
        }
        message.setText("");

    }

    @FXML
    private void becomeHost() {
        // become a host
        // toggle become host
        isHost = !isHost;
        if (isHost) {
            hostName.setText(Authentication.getLoggedUser().getUsername() + "(Hosting)");
            avatar.setStroke(Color.GREEN);
            host.setText("Stop Hosting");
        } else {
            hostName.setText("Not Hosting");
            avatar.setStroke(Color.RED);
            host.setText("Become Host");
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
}
