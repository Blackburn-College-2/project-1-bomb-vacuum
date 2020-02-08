package project.bomb.vacuum.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

class TimerPane extends StackPane {

    private Label display = new Label("00:00:00");

    TimerPane() {
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(display);

        this.setMinHeight(30);
    }

    void setTime(long time) {
        String displayedTime = formatTime(time);

        Platform.runLater(() -> display.setText(displayedTime));
    }

    static String formatTime(long time) {
        int millisInHour = 3600000;
        int millisInMinute = 60000;
        int millisInSecond = 1000;

        long hours = time / millisInHour;
        time = time % millisInHour;
        long minutes = time / millisInMinute;
        time = time % millisInMinute;
        long seconds = time / millisInSecond;

        String hoursText = String.format("%2d", hours).replace(' ', '0');
        String minutesText = String.format("%2d", minutes).replace(' ', '0');
        String secondsText = String.format("%2d", seconds).replace(' ', '0');

        return String.format("%s:%s:%s", hoursText, minutesText, secondsText);
    }

}
