package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;

import Models.User;
import bluetooth.DiscoverDevices;
import data.Authentication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class ChatController implements Initializable {
    private User user;

    @FXML
    private ListView<RemoteDevice> serverList;

    @FXML
    private Label currentUser;

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
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.user = Authentication.getLoggedUser();
        currentUser.setText(user.getName());
        fetchDevices();

        // How to change the style of the cell
        serverList.setCellFactory(list->new ListCell<RemoteDevice>(){
            @Override
            protected void updateItem(RemoteDevice item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item==null){
                    setText(null);
                    setGraphic(null);
                    getStyleClass().remove(".list-cell");
                }
                else{
                    try {
                        setText(item.getFriendlyName(false));
                        getStyleClass().add(".list-cell");
                    } catch (IOException e) {
                    //    Handle this bro
                    }
                }
            }
        });
        serverList.getItems().addAll(devices);
    }

    

}