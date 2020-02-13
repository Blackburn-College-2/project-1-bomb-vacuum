package project.bomb.vacuum;

public class Position {

    public final int row;
    public final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return String.format("(%s, %s)", row, column);
    }
}
