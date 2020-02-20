package project.bomb.vacuum.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Displays a time on the display.
 */
class TimerPane extends StackPane {

    private Label display = new Label("00:00:00");

    TimerPane() {
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(display);

        this.setMinHeight(30);
    }

    /**
     * Sets the time in milliseconds.
     *
     * @param time the time in milliseconds.
     */
    void setTime(long time) {
        String displayedTime = Util.formatTime(time);

        Platform.runLater(() -> display.setText(displayedTime));
    }

}
