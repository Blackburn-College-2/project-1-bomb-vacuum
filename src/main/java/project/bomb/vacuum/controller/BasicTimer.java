package project.bomb.vacuum.controller;

import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Timer;

/**
 * Tracks time using a separate {@link Thread}.
 * <p>
 * Updates every second.
 * <p>
 * Time is tracked in milliseconds.
 */
class BasicTimer implements Timer {

    private final Controller controller;
    private long time = 0;
    private volatile boolean running = false;

    /**
     * Creates a timer object.
     * <p>
     * The timer will not start until {@link BasicTimer#startTimer()}
     * is called.
     *
     * @param controller the controller that will be notified every second.
     */
    BasicTimer(Controller controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startTimer() {
        if (!running) {
            running = true;
            Thread timerThread = new Thread(() -> {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopTimer() {
        running = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTime() {
        return this.time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetTimer() {
        this.stopTimer();
        controller.setTime(0);
        time = 0;
    }
}
