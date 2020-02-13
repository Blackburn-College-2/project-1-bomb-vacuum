package project.bomb.vacuum;

/**
 * 
 * @author isaac.garret
 */
public interface Timer {

    void startTimer();

    void stopTimer();

    long getTime();

    void resetTimer();
}
