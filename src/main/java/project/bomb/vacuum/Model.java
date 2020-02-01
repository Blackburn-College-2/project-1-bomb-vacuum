package project.bomb.vacuum;

import com.google.common.annotations.Beta;
import project.bomb.vacuum.exceptions.InvalidBoardConfiguration;

public interface Model {

    /**
     * Called when a tile has been altered by the user.
     *
     * @param tileAction which mouse button was pressed.
     * @param position    which tile position was pressed.
     */
    void tileUpdatedByUser(TileAction tileAction, Position position);

    /**
     * Starts a new game using the specified default board type.
     *
     * @param board the default board type to use.
     */
    void newGame(DefaultBoard board);

    /**
     * States a new game using the specified board size and bomb count.
     *
     * A game must have a minimum of two rows and two columns, and
     * between 1 bomb and (rows * columns) - 1 bombs.
     *
     * @param boardConfiguration the configuration to use.
     * @throws InvalidBoardConfiguration if the requirements are not met for a valid board.
     */
    void newGame(BoardConfiguration boardConfiguration) throws InvalidBoardConfiguration;

    /**
     * @param board the board played.
     * @param name  the player's name.
     * @param time  how long in milliseconds the game took.
     */
    void updateHighScore(DefaultBoard board, String name, long time);

    /**
     * Gets the high scores for the specified type of default board.
     *
     * @param board the board to get scores for.
     * @return the high scores for the specified board.
     */
    HighScores getScores(DefaultBoard board);

    /**
     * @param cheat true to show all bombs, false to hide bombs.
     */
    void cheatToggled(boolean cheat);

    /**
     * Gets the configuration for a default board, as this may change
     * between {@link Model}s.
     *
     * @param board the default board type.
     * @return the configuration for the default board type.
     */
    BoardConfiguration getDefaultBoardConfiguration(DefaultBoard board);

    /**
     * Creates a new {@link Timer} to be used by the controller.
     *
     * @return a new Timer.
     */
    Timer createTimer();
}
