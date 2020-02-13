package project.bomb.vacuum.model;

import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileState;
import static project.bomb.vacuum.TileState.NOT_CLICKED;


/**
 * 
 * @author rylee.wilson
 */
public class Tile {
    
    private TileValue value;
    private TileState state;
    public final Position position;
    
    /**
     * 
     * @param value
     * @param row
     * @param column 
     */
    public Tile(TileValue value, int row, int column){
        this(value, new Position(row, column));
    }
    
    /**
     * 
     * @param value
     * @param position 
     */
    public Tile(TileValue value, Position position){
        this(value, NOT_CLICKED, position);
    }
    
    /**
     * 
     * @param value
     * @param state
     * @param position 
     */
    public Tile(TileValue value, TileState state, Position position){
        this.value = value;
        this.state = state;
        this.position = position;
    }
    
    /**
     * 
     * @return 
     */
    public TileState getState(){
        return this.state;
    }
    
    /**
     * 
     * @param state 
     */
    public void setState(TileState state){
        this.state = state;
    }
    
    /**
     * 
     * @return 
     */
    public TileValue getValue(){
        return this.value;
    }
    
    /**
     * 
     * @param value 
     */
    public void setValue(TileValue value){
        this.value = value;
    }
    
    
}
