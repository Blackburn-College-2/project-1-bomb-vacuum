package project.bomb.vacuum;

/**
 * Data object to store the number of rows, columns, and bombs
 * for a game of Bomb Vacuum.
 */
public class BoardConfiguration {

    public final int rows;
    public final int columns;
    public final int bombs;

    /**
     * @param rows    the number of rows to use.
     * @param columns the number of columns to use.
     * @param bombs   the number of bombs to use.
     */
    public BoardConfiguration(int rows, int columns, int bombs) {
        this.rows = rows;
        this.columns = columns;
        this.bombs = bombs;
    }
}
