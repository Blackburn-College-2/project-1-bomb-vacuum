package project.bomb.vacuum;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.bomb.vacuum.view.BombPane;

public class GUI extends Application {

    public static void launchGUI() {
        launch();
    }
    
    //private static final int buttonSize = 50;
    
    @Override
    public void start(Stage stage) throws Exception {
        double screenWidth = 800;
        double screenHeight = 600;
        Pane anchorPane  = new Pane();
        
        BombPane bombPane = new BombPane(10,10);
        anchorPane.getChildren().add(bombPane);
        Scene scene = new Scene(anchorPane, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
    }


}
