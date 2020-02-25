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
     * @param board the board played.
     * @param name  the player's name.
     * @param time  how long in milliseconds the game took.
     */
    void updateHighScore(DefaultBoard board, String name, long time);

    
    /**
     * @return a board validator 
    */
    BoardValidator getBoardValidator();
    
    /**
     * Gets the high scores for the specified type of default board.
     *
     * @param board the board to get scores for.
     * @return the high scores for the specified board.
     */
    List<HighScore> getScores(DefaultBoard board);

    /**
     * @param cheat true to show all bombs, false to hide bombs.
     */
    void cheatToggled(boolean cheat);
}
