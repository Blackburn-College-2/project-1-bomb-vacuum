package project.bomb.vacuum;

public class BoardConfiguration {

    public final int rows;
    public final int columns;
    public final int bombs;

    public BoardConfiguration(int rows, int columns, int bombs) {
        this.rows = rows;
        this.columns = columns;
        this.bombs = bombs;
    }
}
