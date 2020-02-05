package project.bomb.vacuum.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.event.EventHandler;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.DefaultBoard;
import project.bomb.vacuum.GameOverState;
import project.bomb.vacuum.TileStatus;
import project.bomb.vacuum.View;

public class GUI extends Application implements View {

    private static Controller controller;

    public static void setController(Controller controller) {
        GUI.controller = controller;
    }

    private Pane anchorPane = new Pane();
    private MenuPane menuPane = new MenuPane(controller);
    private BorderPane borderPane = new BorderPane();

    public static void launchGUI() {
        launch();
    }

    private static final int buttonSize = 50;

    @Override
    public void start(Stage stage) {
        View.setView(this); // Required

        double screenWidth = 800;
        double screenHeight = 600;

        borderPane.setRight(menuPane);
        anchorPane.getChildren().add(borderPane);

        Scene scene = new Scene(anchorPane, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //}
    @Override
    public void initializeBoard(int rows, int columns) {
        BombPane initBombPane = new BombPane(controller, rows, columns);
        borderPane.setCenter(initBombPane);
    }

    @Override
    public void setTileStatuses(TileStatus[] tileStates) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameOver(GameOverState gameOverState, long time) {
        if (gameOverState.equals(gameOverState.WIN)){
            Label winLabel = new Label("You won, dude!");
            Popup winPop = new Popup();
            winPop.getContent().add(winLabel);
            
        } else {
           Label loseLabel = new Label("You lost, but gg");
           Popup losePop = new Popup();
           losePop.getContent().add(loseLabel);
        }
        }

    @Override
    public void setTime(long time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
