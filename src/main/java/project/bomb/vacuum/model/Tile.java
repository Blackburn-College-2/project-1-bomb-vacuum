package project.bomb.vacuum.model;

import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileState;
import static project.bomb.vacuum.TileState.NOT_CLICKED;



public class Tile {
    
    private TileValue value;
    private TileState state;
    public final Position position;
    
    public Tile(TileValue value, int row, int column){
        this(value, new Position(row, column));
    }
    
    public Tile(TileValue value, Position position){
        this(value, NOT_CLICKED, position);
    }
    
    public Tile(TileValue value, TileState state, Position position){
        this.value = value;
        this.state = state;
        this.position = position;
    }
    
    public TileState getState(){
        return this.state;
    }
    
    public void setState(TileState state){
        this.state = state;
    }
    public TileValue getValue(){
        return this.value;
    }
    public void setValue(TileValue value){
        this.value = value;
    }
    
    
}
