package project.bomb.vacuum.view;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class BombCounter extends Label {

    public BombCounter() {
        this.setStyle("-fx-font-size: 15px");
    }

    public void setCounter(int bombs) {
        Platform.runLater(() -> {
            this.setText("Bombs Remaining: " + bombs);
        });
    }
}