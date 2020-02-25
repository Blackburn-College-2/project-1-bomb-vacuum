package project.bomb.vacuum.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.bomb.vacuum.*;

/**
 * The GUI Controller responsible for user interaction.
 */
public class GUI extends Application implements View {

    private static Controller controller;
    private static Runnable startup;

    public static void setController(Controller controller) {
        GUI.controller = controller;
    }

    public static void launchGUI() {
        launch();
    }

    public static void setStartup(Runnable startup) {
        GUI.startup = startup;
    }

    private boolean cheating = false;
    private boolean firstInit = true;

    private Stage stage;
    private BorderPane mainPane = new BorderPane();
    private TimerPane timerPane = new TimerPane();
    private BombCounter bombCounter = new BombCounter();
    private BombPane bombPane;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        View.setView(this); // Required
        controller.setView(this);

        mainPane.setRight(new MenuPane(controller));
        HBox top = new HBox(this.bombCounter, this.timerPane);
        top.setAlignment(Pos.CENTER);
        top.setSpacing(100);
        mainPane.setTop(top);

        Scene scene = new Scene(mainPane, 50, 50);
        setKeyboardHandler(scene);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Thread game = new Thread(GUI.startup);
        game.setDaemon(true);
        game.start();
    }

    private void setKeyboardHandler(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SHIFT) {
                cheating = !cheating;
                controller.cheatToggled(cheating);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeBoard(int rows, int columns) {
        Platform.runLater(() -> {
            double widthPadding = 60;
            double heightPadding = 70;
            // BombPane constructor is columns, rows....
            this.bombPane = new BombPane(controller, columns, rows);
            mainPane.setCenter(this.bombPane);

            double screenWidth = this.bombPane.getMinWidth() + MenuPane.BUTTON_WIDTH + widthPadding;
            double screenHeight = this.bombPane.getMinHeight() + timerPane.getMinHeight() + heightPadding;
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
                            if (stage.getHeight() < 100) {
                                stage.setHeight(screenHeight);
                                stage.setWidth(screenWidth);
                            }
                        });
                    }
                });
                hotfixThread.setDaemon(true);
                hotfixThread.start();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTileStatuses(TileStatus[] tileStates) {
        this.bombPane.updateTiles(tileStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameOver(GameOverState gameOverState, long time, boolean newHighScore) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        StringBuilder message = new StringBuilder();
        StringBuilder header = new StringBuilder();
        if (gameOverState.equals(GameOverState.WIN)) {
            header.append("You won, dude!");
        } else {
            header.append("You lost, but gg.");
        }
        header.append("Your time was: ").append(Util.formatTime(time)).append('\n');
        alert.setHeaderText(header.toString());

        TextField nameField = new TextField();
        if (newHighScore) {
            Pane pane = alert.getDialogPane();
            Label nameLabel = new Label("Enter a 3 character tag");
            nameLabel.setMinWidth(200);
            nameField.setMinWidth(40);
            VBox box = new VBox(nameLabel, nameField);
//        box.setAlignment(Pos.CENTER);
            box.setMinWidth(600);
            box.translateXProperty().bind(pane.widthProperty().multiply(0.6));
            box.translateYProperty().bind(pane.heightProperty().multiply(0.5));
            pane.getChildren().add(box);
        }

        alert.setContentText(message.toString());
        alert.setOnCloseRequest(dialogEvent -> {
            if (newHighScore) {
                controller.updateHighScore(nameField.getText());
            }
        });
        alert.setWidth(1900);

        alert.show();
    }

    @Override
    public void setBombCounter(int bombs) {
        this.bombCounter.setCounter(bombs);
    }

    @Override
    public String getUserName(int maxChars) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(long time) {
        timerPane.setTime(time);
    }

}
