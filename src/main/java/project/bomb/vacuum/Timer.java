package project.bomb.vacuum;

/**
 * Tracks total time that has passed collectively between start and
 * stops.
 * <p>
 * Time SHOULD be tracked in milliseconds.
 */
public interface Timer {

    /**
     * Starts tracking time from when this method is called.
     * <p>
     * Consecutive calls to this method, without first stopping this
     * timer, SHOULD be ignored.
     */
    void start();

    /**
     * Stops tracking time.
     * <p>
     * If this timer has not been started, the call to this method
     * SHOULD be ignored.
     */
    void stop();

    /**
     * SHOULD include the amount of time that is currently being
     * tracked (this timer has started but not yet stopped).
     *
     * @return the cumulative amount of time that has been tracked
     * between start and stops.
     */
    long getTime();

    /**
     * SHOULD set the total time passed to zero.
     * <p>
     * MAY also stop the timer.
     */
    void reset();
}
