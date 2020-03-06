package project.bomb.vacuum.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.DefaultBoard;
import project.bomb.vacuum.HighScore;

public class HighScoresAlert {

    private final Controller controller;

    private static Font font = new Font("", 12);

    public HighScoresAlert(Controller controller) {
        this.controller = controller;
    }

    public void displayHighScores() {
        String OS = System.getProperty("os.name");
        if (OS.contains("win") && !font.getFamily().equals("Courier New")) {
            font = new Font("Courier New", 12);
        } else if (OS.contains("Linux") && !font.getFamily().equals("DejaVu Sans Mono")) {
            font = new Font("DejaVu Sans Mono", 12);
        }

        HBox highScorePane = new HBox();
        for (DefaultBoard board : DefaultBoard.values()) {
            List<HighScore> scores = controller.getScores(board);
            Label title = new Label(" " + board.toString());
            title.setFont(font);
            VBox vBox = new VBox(title);
            vBox.getChildren().addAll(this.createScoreLabels(scores));
            vBox.setMinWidth(125);
            highScorePane.getChildren().add(vBox);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("High Scores");
        alert.setTitle("High Scores");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getChildren().add(highScorePane);
        dialogPane.setMinHeight(220);
        dialogPane.setMinWidth(400);
        highScorePane.translateYProperty().bind(dialogPane.heightProperty().multiply(0.35));
        alert.show();
    }

    private List<Label> createScoreLabels(List<HighScore> scores) {
        List<Label> list = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            if (i >= 5) {
                break;
            }
            HighScore score = scores.get(i);
            String message = String.format("%2d. %-4s%s", (i + 1), score.getName(), Util.formatTime(score.getTime()));
            Label label = new Label(message);
            label.setFont(font);
            list.add(label);
        }
        return list;
    }
}
