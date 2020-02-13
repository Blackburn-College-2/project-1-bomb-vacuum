package project.bomb.vacuum;

/**
 * SHOULD be implemented as an immutable data object that stores
 * a name and a time in milliseconds.
 */
public interface HighScore {

    /**
     * @return the name of the player.
     */
    String getName();

    /**
     * @return the time in milliseconds for this score.
     */
    long getTime();
}
