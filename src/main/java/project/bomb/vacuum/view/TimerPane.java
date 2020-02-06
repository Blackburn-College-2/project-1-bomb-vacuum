/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.bomb.vacuum.view;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * https://www.youtube.com/watch?v=36jbBSQd3eU 
 * reference used for making timer
 * @author Isaac.Garrett
 */
public class TimerPane extends Pane implements project.bomb.vacuum.Timer {

    int hours;
    int minutes;
    int seconds;
    long currentTime;
    Timer timer;
    TimerTask task;

    public TimerPane() {
        hours = 0;
        minutes = 0;
        seconds = 0;
        HBox hbox = new HBox(8);
        this.getChildren().add(hbox);
        Label label = new Label(hours + " : " + minutes + " : " + seconds);
        hbox.getChildren().add(label);
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                seconds++;
                currentTime = currentTime + 1000;
                Platform.runLater(() -> {
                    String time = "";
                    if(hours < 10){
                        time += "0" + hours;
                    }else{
                        time += hours;
                    }
                    time += ":";
                    if(minutes < 10){
                        time += "0" + minutes;
                    }else{
                        time += minutes;
                    }
                    time += ":";
                    if(seconds < 10){
                        time += "0" + seconds;
                    }else{
                        time += seconds;
                    }
                    label.setText(time);                    
                });
                if (seconds == 59) {
                    minutes++;
                    seconds = 0 - 1;
                }
                if (minutes == 59) {
                    hours++;
                    minutes = 0 - 1;
                }
                if (hours == 99) {
                    seconds = 0;
                    minutes = 0;
                    hours = 0;
                }
            }
        };
    }

    public long getTime() {
        return currentTime;
    }

    public void startTimer() {
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public void stopTimer() {
        timer.cancel();
    }
}
