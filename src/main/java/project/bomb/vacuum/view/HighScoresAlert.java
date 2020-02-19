package project.bomb.vacuum.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.DefaultBoard;
import project.bomb.vacuum.HighScore;

public class HighScoresAlert {

    private final Controller controller;

    public HighScoresAlert(Controller controller) {
        this.controller = controller;
    }


    public void displayHighScores() {
        HBox highScorePane = new HBox();
        for (DefaultBoard board : DefaultBoard.values()) {
            List<HighScore> scores = controller.getScores(board);
            String title = board.toString();
            VBox vBox = new VBox(new Label(title));
            vBox.getChildren().addAll(this.createScoreLabels(scores));
            highScorePane.getChildren().add(vBox);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("High Scores");
        alert.setTitle("High Scores");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getChildren().add(highScorePane);
        alert.show();
    }

    private List<Label> createScoreLabels(List<HighScore> scores) {
        List<Label> list = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            HighScore score = scores.get(i);
            String message = String.format("%-2d. %-4s%s", i, score.getName(), Util.formatTime(score.getTime()));
            list.add(new Label(message));
        }
        return list;
    }
}
