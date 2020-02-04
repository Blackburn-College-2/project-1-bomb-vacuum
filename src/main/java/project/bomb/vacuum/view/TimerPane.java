/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.bomb.vacuum.view;


import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Isaac.Garrett
 */
public class TimerPane extends Timer{
    int hours;
    int minutes;
    int seconds;
    public TimerPane(){
        hours = 0;
        minutes = 0;
        seconds = 0;
        
    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask(){
        public void run() {
            seconds++;
            System.out.println(seconds);
            if(seconds == 59){
                minutes++;
                seconds = 0;
            }
            if(minutes == 59){
                hours++;
                minutes = 0;
            }
            if(hours == 99){
                seconds = 0;
                minutes = 0;
                hours = 0;
            }
        }
    };
    
    public long getTime(){
        long time = 0;
        return time;
    }
    public void startTimer() {
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }
    public void stopTimer(){
        
    }
}
