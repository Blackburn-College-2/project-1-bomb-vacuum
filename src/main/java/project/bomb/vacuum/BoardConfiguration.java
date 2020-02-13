package project.bomb.vacuum;

/**
 * 
 * @author rylee.wilson
 */
public class BoardConfiguration {

    public final int rows;
    public final int columns;
    public final int bombs;

    /**
     * 
     * @param rows
     * @param columns
     * @param bombs 
     */
    public BoardConfiguration(int rows, int columns, int bombs) {
        this.rows = rows;
        this.columns = columns;
        this.bombs = bombs;
    }
}
