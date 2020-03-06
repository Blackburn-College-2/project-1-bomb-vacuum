package project.bomb.vacuum;

/**
 * Holds logic to be called whenever the game board state changes.
 */
public interface BoardListener {

    void updateTileStatuses(TileStatus[] statuses);
}
