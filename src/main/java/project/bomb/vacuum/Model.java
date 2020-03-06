package project.bomb.vacuum;

import java.util.List;

/**
 * The base for a model controller, in the MVC paradigm, to play
 * a game of Bomb Vacuum.
 */
public interface Model {

    /**
     * Called when a tile has been altered by the user.
     *
     * @param tileAction which mouse button was pressed.
     * @param position   which tile position was pressed.
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
     * <p>
     * A game must have a minimum of two rows and two columns, and
     * between 1 bomb and (rows * columns) - 1 bombs.
     *
     * @param boardConfiguration the configuration to use.
     */
    void newGame(BoardConfiguration boardConfiguration);

    /**
     * @param name the player's name.
     * @param time how long in milliseconds the game took.
     */
    void updateHighScore(String name, long time);

    /**
     * @return a board validator
     */
    BoardValidator getBoardValidator();

    /**
     * @return the name validator to use.
     */
    NameValidator getNameValidator();

    /**
     * @return the smallest valid board configuration.
     */
    BoardConfiguration getMinBoardConfig();

    /**
     * @return the largest valid board configuration.
     */
    BoardConfiguration getMaxBoardConfig();

    /**
     * @param rows    the number of rows.
     * @param columns the number of columns.
     * @return the maximum number of bombs for the given rows and columns.
     */
    int getMaxBombs(int rows, int columns);

    /**
     * @param rows    the number of rows.
     * @param columns the number of columns.
     * @return the minimum number of bombs for the given rows and columns.
     */
    int getMinBombs(int rows, int columns);

    /**
     * Saves the board configuration.
     *
     * @param configuration the configuration to save.
     */
    void saveBoardConfig(BoardConfiguration configuration);

    /**
     * @return the saved board configuration.
     */
    BoardConfiguration getSavedBoardConfig();

    /**
     * Gets the high scores for the specified type of default board.
     *
     * @param board the board to get scores for.
     * @return the high scores for the specified board.
     */
    List<HighScore> getScores(DefaultBoard board);

    /**
     * Gets the high scores for the current default board.
     *
     * @return the scores for the current default board.
     */
    List<HighScore> getScores();

    /**
     * @param cheat true to show all bombs, false to hide bombs.
     */
    void cheatToggled(boolean cheat);

    /**
     * Get the time for the current game.
     *
     * @return the time for the current game.
     */
    long getTime();

    /**
     * @param listener the listener to be notified when bombs remaining changes.
     */
    void addBombsRemainingListener(ChangeListener<Integer> listener);

    /**
     * @param listener the listener to be notified when tiles are changed.
     */
    void addBoardListener(BoardListener listener);

    /**
     * @return if a default board configuration is currently being used,
     */
    boolean usingDefaultBoard();
}
