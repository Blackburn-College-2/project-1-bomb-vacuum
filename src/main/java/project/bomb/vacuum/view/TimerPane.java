/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.bomb.vacuum.view;

import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author Isaac.Garrett
 */
public class TimerPane extends Pane implements project.bomb.vacuum.Timer {

    int hours;
    int minutes;
    int seconds;
    Timer timer;
    TimerTask task;

    public TimerPane() {
        hours = 0;
        minutes = 0;
        seconds = 0;
        HBox hbox = new HBox(8);
        Label label = new Label(hours + " : " + minutes + " : " + seconds);
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                seconds++;
                System.out.println(seconds);
                if (seconds == 59) {
                    minutes++;
                    seconds = 0;
                }
                if (minutes == 59) {
                    hours++;
                    minutes = 0;
                }
                if (hours == 99) {
                    seconds = 0;
                    minutes = 0;
                    hours = 0;
                }
            }
        };
        label.setText(hours + " : " + minutes + " : " + seconds);
        hbox.getChildren().addAll(label);
    }

    HBox hbox = new HBox(8);
    Label label = new Label(hours + " : " + minutes + " : " + seconds);

    public long getTime() {
        long time = 0;
        return time;
    }

    public void startTimer() {
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void stopTimer() {
        timer.cancel();
    }
}
