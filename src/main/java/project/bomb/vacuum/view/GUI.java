package project.bomb.vacuum.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.event.EventHandler;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.bomb.vacuum.GameOverState;
import project.bomb.vacuum.TileStatus;
import project.bomb.vacuum.View;

public class GUI extends Application implements View {
    Pane anchorPane  = new Pane();
    BorderPane borderPane = new BorderPane();


    public static void launchGUI() {
        launch();
    }
    
    private static final int buttonSize = 50;
    
    @Override
    public void start(Stage stage) throws Exception {
        double screenWidth = 800;
        double screenHeight = 600;
        Button easyButton = new Button("Easy");
        Button hardButton = new Button("Hard");
        Button customButton = new Button("Custom");
        

        VBox gameSelect = new VBox();
        gameSelect.getChildren().addAll(easyButton, 
                hardButton, customButton);
        
        
        

        borderPane.setRight(gameSelect);
        initializeBoard(10,10);
        anchorPane.getChildren().add(borderPane);

        Scene scene = new Scene(anchorPane, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //public void handleEasy(){
        
    //}
    @Override
    public void initializeBoard(int rows, int columns) {
        BombPane initBombPane = new BombPane(rows,columns);
        borderPane.setCenter(initBombPane);
    }

    @Override
    public void setTileStatuses(TileStatus[] tileStates) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
