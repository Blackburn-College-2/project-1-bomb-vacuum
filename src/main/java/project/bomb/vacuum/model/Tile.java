package project.bomb.vacuum.model;

import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileState;

import static project.bomb.vacuum.TileState.NOT_CLICKED;

public class Tile {

    private TileValue value;
    private TileState state;
    public final Position position;

    /**
     * Passes the tile's value and the rows and columns to another constructor
     * as a Position
     *
     * @param value  the tile's value including empty, a number, or a bomb
     * @param row    the row on the board where the tile is located
     * @param column the column on the board where the tile is located
     */
    public Tile(TileValue value, int row, int column) {
        this(value, new Position(row, column));
    }

    /**
     * Passes the tile's value and Position object to another constructor
     *
     * @param value    the tile's value including empty, a number, or a bomb
     * @param position the location on the game board as a Position object
     */
    public Tile(TileValue value, Position position) {
        this(value, NOT_CLICKED, position);
    }

    /**
     * Creates a new Tile and sets the attributes of the tile
     *
     * @param value    the value to store.
     * @param state    the state to store.
     * @param position the location of this.
     */
    public Tile(TileValue value, TileState state, Position position) {
        this.value = value;
        this.state = state;
        this.position = position;
    }

    /**
     * @return the stored state.
     */
    public TileState getState() {
        return this.state;
    }

    /**
     * @param state the new state of this.
     */
    public void setState(TileState state) {
        this.state = state;
    }

    /**
     * @return the stored value.
     */
    public TileValue getValue() {
        return this.value;
    }

    /**
     * @param value the new value of this.
     */
    public void setValue(TileValue value) {
        this.value = value;
    }

}
