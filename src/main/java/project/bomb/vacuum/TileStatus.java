package project.bomb.vacuum;

public class TileStatus {

    public final Position position;
    public final TileState state;

    /**
     * @param state    the state of the tile to display.
     * @param position the location of the tile.
     */
    public TileStatus(TileState state, Position position) {
        this.position = position;
        this.state = state;

    }
}
