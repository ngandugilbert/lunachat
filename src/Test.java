

import Controllers.Bubble;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        
        // Example usage of the ChatBubble class
        Bubble userBubble = new Bubble("Hello, how are you?", true);
        Bubble otherBubble = new Bubble("I'm doing well, thank you!", false);

        root.getChildren().addAll(userBubble, otherBubble);
        
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
