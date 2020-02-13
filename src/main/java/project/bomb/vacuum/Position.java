package project.bomb.vacuum;

/**
 * Tile location.
 */
public class Position {

    public final int row;
    public final int column;

    /**
     * 
     * @param row
     * @param column 
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * 
     * @return 
     */
    public String toString() {
        return String.format("(%s, %s)", row, column);
    }
}
