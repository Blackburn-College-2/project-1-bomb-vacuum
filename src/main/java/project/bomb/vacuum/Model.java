package project.bomb.vacuum;

import com.google.common.annotations.Beta;

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
     * @param rows    how many rows in the tile grid.
     * @param columns how many columns in the tile grid.
     * @param bombs   how many bombs in the tile grid.
     */
    void newGame(int rows, int columns, int bombs);

    /**
     * @param board the board played.
     * @param name  the player's name.
     * @param time  how long in milliseconds the game took.
     */
    void updateHighScore(DefaultBoard board, String name, long time);

    @Beta
    void getHighScores();

    /**
     * @param toggle true to show all bombs, false to hide bombs.
     */
    void cheatToggled(boolean toggle);
}
