package Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ListController implements Initializable {
    @FXML
    private ListView<String> serverList;

    @FXML
    private Label status;

    private String[] list = {"Hello", "World"};
    private String selectedItem;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       serverList.getItems().addAll(list);

       serverList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

        @Override
        public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
           selectedItem = serverList.getSelectionModel().getSelectedItem();

           status.setText(selectedItem);
        }
        
       });
       
        
       
    }
    
}
