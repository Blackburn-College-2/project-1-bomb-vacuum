package project.bomb.vacuum.view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import project.bomb.vacuum.ChangeListener;

/**
 * Displays the number of bombs remaining.
 * <p>
 * This SHOULD display the total number of
 */
public class BombCounter extends Label {

    private ChangeListener<Integer> changeListener;

    /**
     * sets the size of font being used for the bomb counter
     */
    public BombCounter() {
        this.setStyle("-fx-font-size: 15px");
        this.initChangeListener();
    }

    private void initChangeListener() {
        this.changeListener = (oldValue, newValue) -> update(newValue);
    }

    private void update(int remainingBombs) {
        Platform.runLater(() -> setText(String.format("Bombs Remaining: %d", remainingBombs)));
    }

    /**
     * @return the listener to update this.
     */
    public ChangeListener<Integer> getChangeListener() {
        return this.changeListener;
    }

}
