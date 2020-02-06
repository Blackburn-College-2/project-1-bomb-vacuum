package project.bomb.vacuum.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private boolean cheating = false;
    private boolean firstInit = true;

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
        setKeyboardHandler(scene);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        GUI.startup.run();
    }

    private void setKeyboardHandler(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SHIFT) {
                    cheating = !cheating;
                    controller.cheatToggled(cheating);
                }
            }
        });
    }

    @Override
    public void initializeBoard(int rows, int columns) {
        double widthPadding = 60;
        double heightPadding = 70;
        this.bombPane = new BombPane(controller, rows, columns);
        mainPane.setCenter(this.bombPane);

        double screenWidth = this.bombPane.getMinWidth() + MenuPane.BUTTON_WIDTH + widthPadding;
        double screenHeight = this.bombPane.getMinHeight() + timerPane.getMinHeight()+ heightPadding;
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);

        if (firstInit) {
            firstInit = false;
            Thread hotfixThread = new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        stage.setHeight(screenHeight);
                        stage.setWidth(screenWidth);
                    });
                }
            });
            hotfixThread.setDaemon(true);
            hotfixThread.start();
        }
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
