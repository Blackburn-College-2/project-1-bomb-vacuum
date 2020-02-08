package project.bomb.vacuum.view;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.DefaultBoard;
import project.bomb.vacuum.HighScore;
import project.bomb.vacuum.HighScores;

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

            HBox options = new HBox();
            pane.getChildren().add(options);

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
