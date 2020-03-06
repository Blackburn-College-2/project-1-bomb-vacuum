package project.bomb.vacuum;

import java.util.List;

/**
 * Handles the communications between a {@link View} and {@link Model}
 */
public interface Controller {

    // ##### Calls from the View.

    /**
     * Starts a new game using a default configuration.
     *
     * @param board the default board type to use.
     */
    void startNewGame(DefaultBoard board);

    /**
     * @param view the view to use.
     */
    void setView(View view);

    /**
     * Starts a new game with the specified rows, columns, and bombs.
     * <p>
     * A game must have a minimum of two rows and two columns, and
     * between 1 bomb and (rows * columns) - 1 bombs.
     *
     * @param boardConfiguration the configuration to use.
     */
    void startNewGame(BoardConfiguration boardConfiguration);

    /**
     * Called when a tile has been altered by the user.
     *
     * @param tileAction which mouse button was pressed.
     * @param position   which tile position was pressed.
     */
    void tileUpdatedByUser(TileAction tileAction, Position position);

    /**
     * Gets the high scores for the current board.
     *
     * @return the high scores for the board.
     */
    List<HighScore> getScores();

    /**
     * Gets the high scores for the specified type of default board.
     *
     * @return the high scores for the specified board.
     */
    List<HighScore> getScores(DefaultBoard board);

    /**
     * @param cheat true to show all bombs, false to hide bombs.
     */
    void cheatToggled(boolean cheat);

    /**
     * Updates the high scores for a board.
     * <p>
     * SHOULD only be called if the user was playing on a default
     * board configuration.
     * <p>
     * SHOULD throw an {@link project.bomb.vacuum.exceptions.InvalidStateException}
     * if this is called when using a non-default board configuration.
     *
     * @param name the name of the player.
     */
    void updateHighScore(String name);

    /**
     * @return a board validator
     */
    BoardValidator getBoardValidator();

    /**
     * @return the name validator to use.
     */
    NameValidator getNameValidator();

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
     * @param listener the listener to be notified when bombs remaining changes.
     */
    void addBombsRemainingListener(ChangeListener<Integer> listener);

    /**
     * @param listener the listener to be notified when tiles are changed.
     */
    void addBoardListener(BoardListener listener);

    // ##### Calls from the Model.

    /**
     * Notifies the Controller what the board size is.
     * <p>
     * MUST be called before the start of a game.
     *
     * @param rows    how many rows in the tile grid.
     * @param columns how many columns in the tile grid.
     */
    void initializeBoard(int rows, int columns);

    /**
     * Signals that the game has ended.
     *
     * @param gameOverState if the game is a win or loss.
     */
    void gameOver(GameOverState gameOverState, boolean newHighScore);

    /**
     * Sets the time displayed.
     *
     * @param time the time to display.
     */
    void setTime(long time);

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
     * @return the smallest valid board configuration.
     */
    BoardConfiguration getMinBoardConfig();

    /**
     * @return the largest valid board configuration.
     */
    BoardConfiguration getMaxBoardConfig();

}
