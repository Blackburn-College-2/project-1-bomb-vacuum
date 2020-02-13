package project.bomb.vacuum.controller;

import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Timer;

public class BasicTimer implements Timer {

    private final Controller controller;
    private long time = 0;
    private volatile boolean running = false;
    private Thread timerThread;

    BasicTimer(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void startTimer() {
        if (!running) {
            running = true;
            timerThread = new Thread(() -> {
                long waitTime = 1000;
                long lastRunTime = System.currentTimeMillis();
                long now = lastRunTime;
                while (running) {
                    while (now - lastRunTime < waitTime) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            running = false;
                        }
                        now = System.currentTimeMillis();
                    }
                    if (running) {
                        lastRunTime = System.currentTimeMillis();
                        time += 1000;
                        controller.setTime(time);
                    }
                }
            });
            timerThread.setDaemon(true);
            timerThread.start();
        }
    }

    @Override
    public void stopTimer() {
        running = false;
        try {
//            timerThread.interrupt();
        } catch (Exception ex) {
            // Don't care
        }
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public void resetTimer() {
        this.stopTimer();
        controller.setTime(0);
        time = 0;
    }
}
