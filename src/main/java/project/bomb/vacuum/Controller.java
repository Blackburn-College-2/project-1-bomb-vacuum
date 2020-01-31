package project.bomb.vacuum;

/**
 * Handles the communications between a {@link View} and {@link Model}
 */
public interface Controller {

    // ##### Calls from the View.

    /**
     * Called when a tile has been altered by the user.
     *
     * @param tileAction which mouse button was pressed.
     * @param position   which tile position was pressed.
     */
    void tileUpdatedByUser(TileAction tileAction, Position position);

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
     * Notifies the Controller of tiles that have (maybe) changed.
     *
     * @param statuses the status of tiles that have been updated.
     */
    void setTileState(TileStatus[] statuses);

    /**
     * Signals that the game has ended.
     *
     * @param gameOverState if the game is a win or loss.
     */
    void gameOver(GameOverState gameOverState);

}
