package Controllers;

import bluetooth.LunaClient;
import bluetooth.network.SPPClient;
import bluetooth.network.SPPServer;
import bluetooth.revised.Discover;
import bluetooth.revised.Server;
import data.Authentication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;


public class ChatControllerNew implements Initializable {

    private SPPServer myServer;
    private SPPClient client;
    private String myName;
    private String partnerName;
    Discover explorer;

    private BufferedReader in;
    private PrintWriter out;

    @FXML
    private VBox chatRoomList;
    @FXML
    private Label chatRoom = new Label();
    private LinkedList<RemoteDevice> devices = new LinkedList<>();
    private boolean isHost = false;

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
    private LunaClient chatty;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {

        initClient(); // Always start as a client
        updateInfo();

    }

    @FXML
    // populate the devices vbox
    private void populateDevices() {
        if (devices.isEmpty()) {
            return;
        } else {
            // create a card for each device

            Platform.runLater(() -> {
                for (RemoteDevice device : devices) {
                    try {
                        createCard(device);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    chatRoom.setText("Chat Rooms");
                }
            });


        }
    }

    private void updateInfo() {
        this.profile.setText(Authentication.getLoggedUser().getUsername());

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
        connectToDevice();
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
            sendMsg(message.getText());
            createChatBubble(message.getText(), Authentication.getLoggedUser().getUsername());
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
            startServer();
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

    //    This method starts the server
    public void startServer() {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            System.out.print("Address: " + localDevice.getBluetoothAddress());
            System.out.print("Name: " + localDevice.getFriendlyName());

            // create an object for the server
            myServer = new SPPServer();
            myServer.setOnConnectionSuccessful((java.awt.event.ActionEvent e) -> {
                in = myServer.in;
                out = myServer.out;
                partnerName = myServer.getPartnerName();
                (new streamPoller()).start();
            });
            myServer.start();
        } catch (BluetoothStateException e) {
            System.err.print(e);
        }
    }

    //    start off as a client
    public void initClient() {
        client = new SPPClient();
        client.setOnDeviceDiscovery((java.awt.event.ActionEvent e) -> {
            ObservableList<RemoteDevice> foundDevices = FXCollections.observableList(SPPClient.vecDevices);
            this.devices.addAll(foundDevices);
            populateDevices();
        });

        client.startDiscovery();

        client.setOnConnectionFailed((java.awt.event.ActionEvent e) -> {
            Platform.runLater(() -> {
                serverName.setText("Could Not Connect to Server.");
                serverAddress.setText("Something wrong happened, choose another server or try again later.");
            });
        });
        client.setOnConnectionSuccessful((java.awt.event.ActionEvent e) -> {
            in = client.in;
            out = client.out;
            partnerName = client.getPartnerName();
            Platform.runLater(() -> {
                try {
                    serverName.setText(selectedDevice.getFriendlyName(true));
                    serverAddress.setText(selectedDevice.getBluetoothAddress());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                serverAddress.setText("Something wrong happened, choose another server or try again later.");
            });

            (new streamPoller()).start();
        });
    }

    // This is the streamPoller class
    class streamPoller extends Thread {
        public void run() {
            boolean isRun = true;
            while (isRun) {
                try {
                    if (in != null) {
                        System.out.println("in not null");
                        String s = in.readLine();
                        if (s != null) Platform.runLater(() -> receivedMassage(s));
                        else isRun = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void receivedMassage(String msg) {
        createChatBubble(message.getText(), partnerName);
    }

    @FXML
    public void refresh() {
        chatRoom.setText("Searching...");
        this.devices.clear();
        chatRoomList.getChildren().clear();
        client.startDiscovery();
    }


    public void connectToDevice() {
        if (selectedDevice != null) {
            client.connect(selectedDevice);
        } else {
            System.out.print("Please select a device first");
        }
    }

    public void sendMsg(String s) {
        System.out.println("must send " + s);
        out.write(s + "\r\n");
        out.flush();
    }
}
