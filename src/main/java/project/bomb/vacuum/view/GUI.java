package project.bomb.vacuum.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.event.EventHandler;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.DefaultBoard;
import project.bomb.vacuum.GameOverState;
import project.bomb.vacuum.TileStatus;
import project.bomb.vacuum.View;

public class GUI extends Application implements View {

    private static Controller controller;
    private static Runnable startup;

    public static void setController(Controller controller) {
        GUI.controller = controller;
    }
    
    private Pane anchorPane = new Pane();
    private MenuPane menuPane = new MenuPane(controller);
    private BorderPane borderPane = new BorderPane();
    private BombPane bombPane;

    public static void launchGUI() {
        launch();
    }

    public static void setStartup(Runnable startup) {
        GUI.startup = startup;
    }

    @Override
    public void start(Stage stage) {
        View.setView(this); // Required
        controller.setView(this);

        double screenWidth = 800;
        double screenHeight = 600;

        borderPane.setRight(menuPane);
        anchorPane.getChildren().add(borderPane);

        Scene scene = new Scene(anchorPane, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        GUI.startup.run();
    }

    @Override
    public void initializeBoard(int rows, int columns) {
        this.bombPane = new BombPane(controller, rows, columns);
        borderPane.setCenter(this.bombPane);
    }

    @Override
    public void setTileStatuses(TileStatus[] tileStates) {
        this.bombPane.updateTiles(tileStates);
    }

    @Override
    public void gameOver(GameOverState gameOverState, long time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTime(long time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
