package project.bomb.vacuum.view;

import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import project.bomb.vacuum.BoardConfiguration;
import project.bomb.vacuum.BoardValidator;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.DefaultBoard;

/**
 * Holds the main options for the game.
 */
class MenuPane extends VBox {

    static final double BUTTON_WIDTH = 110;
    private Controller controller;
    private final HighScoresAlert highScoresAlert;

    /**
     * @param controller the {@link Controller} to link to.
     */
    MenuPane(Controller controller) {
        this.controller = controller;
        highScoresAlert = new HighScoresAlert(controller);
        Button easyButton = new Button("Easy");
        Button hardButton = new Button("Hard");
        Button mediumButton = new Button("Medium");
        Button customButton = new Button("Custom");
        Button highScoresButton = new Button("High Scores");
        getChildren().addAll(
                easyButton,
                mediumButton,
                hardButton,
                customButton,
                highScoresButton
        );
        this.setCustomActionAndSize(customButton);
        this.setButtonActionAndSize(easyButton, DefaultBoard.EASY);
        this.setButtonActionAndSize(mediumButton, DefaultBoard.INTERMEDIATE);
        this.setButtonActionAndSize(hardButton, DefaultBoard.EXPERT);
        this.setHighScoresActionAndSize(highScoresButton);
    }

    private void testBoardConfig(TextField rows, TextField columns, TextField bombs, Label error) {
        BoardValidator validator = controller.getBoardValidator();
        boolean isValid = false;
        try {
            isValid = validator.validate(new BoardConfiguration(
                    Integer.parseInt(rows.getText()),
                    Integer.parseInt(columns.getText()),
                    Integer.parseInt(bombs.getText()))
            );
        } catch (Exception ex) {
            // ignore
        }
        validConfig = isValid;
        Platform.runLater(() -> {
            error.setVisible(!validConfig);
        });
    }

    private void setButtonActionAndSize(Button button, DefaultBoard board) {
        button.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                controller.startNewGame(board);
            }
        });
        this.setButtonWidth(button);
    }

    private void setHighScoresActionAndSize(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            this.highScoresAlert.displayHighScores();
        });
        this.setButtonWidth(button);
    }

    private volatile boolean validConfig = false;

    private void setCustomActionAndSize(Button button) {
        this.setButtonWidth(button);
        button.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Create Custom Game");
            alert.setTitle("Create Custom Game");
            Pane pane = alert.getDialogPane();
            pane.setMinHeight(200);

            BoardConfiguration prevConfig = controller.getSavedBoardConfig();

            Label error = new Label("Invalid Board Config");
            error.setTextFill(Color.RED);
            error.setVisible(true);
            error.setMinWidth(240);
            TextField bombs = new TextField();
            TextField rows = new TextField();
            TextField columns = new TextField();

            if (prevConfig != null) {
                rows.setText("" + prevConfig.rows);
                columns.setText("" + prevConfig.columns);
                bombs.setText("" + prevConfig.bombs);
            }

            testBoardConfig(rows, columns, bombs, error);

            rows.textProperty().addListener((observable, oldValue, newValue) -> testBoardConfig(rows, columns, bombs, error));
            columns.textProperty().addListener((observable, oldValue, newValue) -> testBoardConfig(rows, columns, bombs, error));
            bombs.textProperty().addListener((observable, oldValue, newValue) -> testBoardConfig(rows, columns, bombs, error));

            Label bombsLabel = new Label("Bombs");
            bombsLabel.setMinWidth(100);
            Label rowsLabel = new Label("Rows");
            rowsLabel.setMinWidth(100);
            Label columnsLabel = new Label("Columns");
            columnsLabel.setMinWidth(100);

            VBox bombBox = new VBox(bombsLabel, bombs);
            bombBox.setSpacing(5);
            bombBox.setMinWidth(50);
            VBox rowBox = new VBox(rowsLabel, rows);
            rowBox.setSpacing(5);
            rowBox.setMinWidth(50);
            VBox columnBox = new VBox(columnsLabel, columns);
            columnBox.setSpacing(5);
            columnBox.setMinWidth(50);

            bombs.setMinWidth(30);
            rows.setMinWidth(30);
            columns.setMinWidth(30);

            HBox options = new HBox(rowBox, columnBox, bombBox);
            VBox all = new VBox(error, options);
            all.setSpacing(5);
            all.setAlignment(Pos.CENTER);
            options.setSpacing(40);
            options.setMaxHeight(100);

            StackPane mainPane = new StackPane();
            mainPane.setAlignment(Pos.CENTER);
            mainPane.getChildren().add(all);
            pane.getChildren().add(mainPane);

            mainPane.translateYProperty().bind(pane.heightProperty().multiply(0.5));
            mainPane.translateXProperty().bind(pane.widthProperty().multiply(0.5));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty()) {
                return;
            }
            if (result.get() == ButtonType.OK) {
                if (validConfig) {
                    BoardConfiguration configuration = new BoardConfiguration(
                            Integer.parseInt(rows.getText()),
                            Integer.parseInt(columns.getText()),
                            Integer.parseInt(bombs.getText())

                    );
                    controller.saveBoardConfig(configuration);
                    controller.startNewGame(configuration);
                }
            }
        });
    }

    private void setButtonWidth(Button button) {
        button.setMinWidth(BUTTON_WIDTH);
        button.setMaxWidth(BUTTON_WIDTH);
        button.setPrefWidth(BUTTON_WIDTH);
    }

}
