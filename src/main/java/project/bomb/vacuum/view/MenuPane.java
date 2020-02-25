package project.bomb.vacuum.view;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import project.bomb.vacuum.*;

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

    private void setCustomActionAndSize(Button button) {
        this.setButtonWidth(button);
        button.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Create Custom Game");
            alert.setTitle("Create Custom Game");
            Pane pane = alert.getDialogPane();

            TextField bombs = new TextField();
            TextField rows = new TextField();
            TextField columns = new TextField();

            Label bombsLabel = new Label("Bombs");
            Label rowsLabel = new Label("Rows");
            Label columnsLabel = new Label("Columns");

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
            options.setSpacing(40);
//            options.setStyle("-fx-border-color: BLUE; -fx-stroke-width: 4px");
            options.setMaxHeight(100);

            StackPane mainPane = new StackPane();
            mainPane.setAlignment(Pos.CENTER);
            pane.getChildren().add(mainPane);
            mainPane.getChildren().add(options);

            options.translateYProperty().bind(pane.heightProperty().multiply(0.5));
            options.translateXProperty().bind(pane.widthProperty().multiply(0.5));

            alert.setOnCloseRequest(dialogEvent -> {
                controller.startNewGame(new BoardConfiguration(
                        Integer.parseInt(rows.getText()),
                        Integer.parseInt(columns.getText()),
                        Integer.parseInt(bombs.getText())

                ));
            });

            alert.show();
        });
    }

    private void setButtonWidth(Button button) {
        button.setMinWidth(BUTTON_WIDTH);
        button.setMaxWidth(BUTTON_WIDTH);
        button.setPrefWidth(BUTTON_WIDTH);
    }

}
