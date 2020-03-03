package project.bomb.vacuum.view;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class BombCounter extends Label {
    /**
     * sets the size of font being used for the bomb counter
     */
    public BombCounter() {
        this.setStyle("-fx-font-size: 15px");
    }
    /**
     * updates the number of bombs on the board
     * @param bombs the number of bombs 
     */
    public void setCounter(int bombs) {
        Platform.runLater(() -> {
            this.setText("Bombs Remaining: " + bombs);
        });
    }
}
