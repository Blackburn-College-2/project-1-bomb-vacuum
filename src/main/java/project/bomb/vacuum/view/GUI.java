package project.bomb.vacuum.view;

import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.GameOverState;
import project.bomb.vacuum.TileStatus;
import project.bomb.vacuum.View;

public class GUI extends Application implements View {

    private static Controller controller;
    private static Runnable startup;

    public static void setController(Controller controller) {
        GUI.controller = controller;
    }

    private Stage stage;
    private BorderPane mainPane = new BorderPane();
    private BombPane bombPane;
    private TimerPane timerPane = new TimerPane();

    public static void launchGUI() {
        launch();
    }

    public static void setStartup(Runnable startup) {
        GUI.startup = startup;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        View.setView(this); // Required
        controller.setView(this);

        mainPane.setRight(new MenuPane(controller));
        mainPane.setTop(timerPane);

        Scene scene = new Scene(mainPane, 50, 50);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        GUI.startup.run();
    }

    @Override
    public void initializeBoard(int rows, int columns) {
        this.bombPane = new BombPane(controller, rows, columns);
        mainPane.setCenter(this.bombPane);

        double screenWidth = this.bombPane.getMinWidth() + MenuPane.BUTTON_WIDTH + 40;
        double screenHeight = this.bombPane.getMinHeight() + timerPane.getMinHeight()+ 50;
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);
    }

    @Override
    public void setTileStatuses(TileStatus[] tileStates) {
        this.bombPane.updateTiles(tileStates);
    }

    @Override
    public void gameOver(GameOverState gameOverState, long time) {
        System.err.println("Game over not yet implemented in GUI");
    }

    @Override
    public void setTime(long time) {
        timerPane.setTime(time);
    }

}
