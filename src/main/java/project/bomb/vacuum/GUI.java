package project.bomb.vacuum;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void launchGUI() {
        launch();
    }


    @Override
    public void start(Stage stage) throws Exception {

        Pane pane = new BorderPane();
        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
