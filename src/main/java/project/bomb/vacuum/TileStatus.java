package project.bomb.vacuum;

/**
 * Used to update the state of tiles the user is aware of.
 */
public class TileStatus {

    public final Position position;
    public final TileState state;

    /**
     * 
     * @param state
     * @param position 
     */
    public TileStatus(TileState state, Position position) {
        this.position = position;
        this.state = state;
        
    }
}
