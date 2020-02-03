package project.bomb.vacuum;

/**
 * Implementation should store {@link HighScore}s to be passed to a
 * {@link View} to show the user.
 */
public interface HighScores {

    /**
     * @return first place.
     */
    HighScore getFirst();

    /**
     * @return seconds place.
     */
    HighScore getSecond();

    /**
     * @return third place.
     */
    HighScore getThird();

    /**
     * @return fourth place.
     */
    HighScore getFourth();

    /**
     * @return fifth place.
     */
    HighScore getFifth();
}
