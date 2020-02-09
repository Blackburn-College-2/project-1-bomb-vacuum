package project.bomb.vacuum.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import project.bomb.vacuum.*;

/**
 * @author delaney.satchwell
 */
class MenuPane extends VBox {

    static final double BUTTON_WIDTH = 80;
    private int spacing = -20;
    private Controller controller;

    MenuPane(Controller controller) {
        this.controller = controller;
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(renderHighScores());
            alert.setHeaderText("High Scores");
            alert.setTitle("High Scores");
            alert.show();
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

            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent dialogEvent) {
                    controller.startNewGame(new BoardConfiguration(
                            Integer.parseInt(rows.getText()),
                            Integer.parseInt(columns.getText()),
                            Integer.parseInt(bombs.getText())
                    ));
                }
            });
            alert.show();
        });
    }

    private void setButtonWidth(Button button) {
        button.setMinWidth(BUTTON_WIDTH);
        button.setMaxWidth(BUTTON_WIDTH);
        button.setPrefWidth(BUTTON_WIDTH);
    }

    private String renderHighScores() {
        StringBuilder sb = new StringBuilder();
        HighScores easyScores = controller.getScores(DefaultBoard.EASY);
        HighScores mediumScores = controller.getScores(DefaultBoard.INTERMEDIATE);
        HighScores hardScores = controller.getScores(DefaultBoard.EXPERT);
        sb.append(String.format("%-27s%-22s%-20s", "Easy", "Medium", "Hard")).append('\n');
        sb.append(formatRow(easyScores.getFirst(), mediumScores.getFirst(), hardScores.getFirst(), "1. ")).append('\n');
        sb.append(formatRow(easyScores.getSecond(), mediumScores.getSecond(), hardScores.getSecond(), "2. ")).append('\n');
        sb.append(formatRow(easyScores.getThird(), mediumScores.getThird(), hardScores.getThird(), "3. ")).append('\n');
        sb.append(formatRow(easyScores.getFourth(), mediumScores.getFourth(), hardScores.getFourth(), "4. ")).append('\n');
        sb.append(formatRow(easyScores.getFifth(), mediumScores.getFifth(), hardScores.getFifth(), "5. ")).append('\n');

        return sb.toString();
    }

    private String formatRow(HighScore easy, HighScore medium, HighScore hard, String title) {
        return String.format("%"+spacing+"s%"+spacing+"s%"+spacing+"s", formatScore(easy, title), formatScore(medium, title), formatScore(hard, title));
    }

    private String formatScore(HighScore score, String title) {
        return String.format(title + "%-4s%s", score.getName(), TimerPane.formatTime(score.getTime()));
    }

}
