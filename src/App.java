import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // create the root
        Parent root = FXMLLoader.load(getClass().getResource("/Views/login.fxml"));

        // create a scene
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Views/style/style.css").toExternalForm());
        stage.setMaximized(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600); 
        stage.setScene(scene);
        stage.setTitle("Luna Chat"); //
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}