package project.bomb.vacuum.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

        connectModel();

        Thread game = new Thread(GUI.startup);
        game.setDaemon(true);
        game.start();
    }

    private void connectModel() {
        GUI.controller.addBombsRemainingListener(this.bombCounter.getChangeListener());
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
            GUI.controller.addBoardListener(this.bombPane.getBoardListener());
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

    private volatile boolean nameOK = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameOver(GameOverState gameOverState, long time, boolean newHighScore) {
        nameOK = true;
        NameValidator nameValidator = controller.getNameValidator();

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
        nameField.setPrefColumnCount(4);
        Label errorLabel = new Label("TAG MUST BE 3 CHARACTERS LONG");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setMinWidth(200);
        errorLabel.setVisible(false);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            nameOK = nameValidator.validate(newValue);
            errorLabel.setVisible(!nameOK);
        });
        if (newHighScore) {
            nameOK = false;
            Pane pane = alert.getDialogPane();
            Label nameLabel = new Label("Enter a 3 character tag");
            nameLabel.setMinWidth(200);
            nameField.setMinWidth(40);
            VBox box = new VBox(errorLabel, nameLabel, nameField);
            box.setMinWidth(600);
            box.translateXProperty().bind(pane.widthProperty().multiply(0.6));
            box.translateYProperty().bind(pane.heightProperty().multiply(0.5));
            pane.getChildren().add(box);
        }

        alert.getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, event -> {
            if (!nameOK) {
                event.consume();
            }
        });

        alert.setContentText(message.toString());
        alert.setOnCloseRequest(dialogEvent -> {
            if (newHighScore) {
                if (nameOK) {
                    controller.updateHighScore(nameField.getText());
                }
            }
        });
        alert.setWidth(1900);

        alert.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(long time) {
        timerPane.setTime(time);
    }

}
