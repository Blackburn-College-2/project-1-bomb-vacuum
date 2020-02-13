package project.bomb.vacuum.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
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
    static DefaultBoard board;

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
        mainPane.setTop(timerPane);

        Scene scene = new Scene(mainPane, 50, 50);
        setKeyboardHandler(scene);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        GUI.startup.run();
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
                        stage.setHeight(screenHeight);
                        stage.setWidth(screenWidth);
                    });
                }
            });
            hotfixThread.setDaemon(true);
            hotfixThread.start();
        }
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
    public void gameOver(GameOverState gameOverState, long time) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        StringBuilder message = new StringBuilder();
        StringBuilder header = new StringBuilder();
        if (gameOverState.equals(GameOverState.WIN)) {
            header.append("You won, dude!");
        } else {
            header.append("You lost, but gg.");
        }
        header.append("Your time was: ").append(TimerPane.formatTime(time)).append('\n');
        alert.setHeaderText(header.toString());

        TextField nameField = new TextField();
        if (GUI.board != null) {
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

        HighScores scores = controller.getScores();
        if (scores != null) {
            appendScore(message, "1. ", scores.getFirst());
            appendScore(message, "2. ", scores.getSecond());
            appendScore(message, "3. ", scores.getThird());
            appendScore(message, "4. ", scores.getFourth());
            appendScore(message, "5. ", scores.getFifth());
        }
        alert.setContentText(message.toString());
        alert.setOnCloseRequest(dialogEvent -> {
            if (GUI.board != null) {
                controller.updateHighScore(nameField.getText());
            }
        });
        alert.show();
    }

    private void appendScore(StringBuilder stringBuilder, String title, HighScore score) {
        String timeString = TimerPane.formatTime(score.getTime());
        stringBuilder.append(title).append(score.getName()).append(": ").append(timeString).append('\n');
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(long time) {
        timerPane.setTime(time);
    }

}
