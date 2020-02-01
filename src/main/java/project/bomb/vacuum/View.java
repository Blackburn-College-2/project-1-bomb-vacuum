package project.bomb.vacuum;

/**
 * Handles displaying the state of the game to the user.
 */
public interface View {

    /**
     * Creates a new board of tiles using the specified size.
     *
     * @param rows number of rows in the tile grid.
     * @param columns number of columns in the tile grid.
     */
    void initializeBoard(int rows, int columns);

    /**
     * Sets the state of the specified tiles.
     *
     * @param tileStates the tiles that need updated in the view.
     */
    void setTileStatuses(TileStatus[] tileStates);

    /**
     * The user has won or lost, let them know!
     */
    void gameOver(GameOverState gameOverState, long time);

    /**
     * Set the displayed time.
     *
     * @param time the time to display.
     */
    void setTime(long time);
}
