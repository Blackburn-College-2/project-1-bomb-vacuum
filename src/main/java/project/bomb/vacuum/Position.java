package project.bomb.vacuum;

/**
 * A Data Object that stores a row and column of a tile.
 */
public class Position {

    public final int row;
    public final int column;

    /**
     *
     * @param row the row to store.
     * @param column the column to store.
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.format("(%s, %s)", row, column);
    }
}
