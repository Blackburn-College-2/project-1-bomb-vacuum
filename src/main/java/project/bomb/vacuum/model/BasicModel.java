package project.bomb.vacuum.model;

import java.util.ArrayList;
import java.util.List;
import project.bomb.vacuum.*;
import project.bomb.vacuum.exceptions.InvalidBoardConfiguration;

public class BasicModel implements Model {

    private Tile[][] gameModel;
    private final Controller controller;
    private int bombs;

    public BasicModel(Controller controller) {
        this.controller = controller;

//        How to convert list to array:
//        List<Tile> list = new ArrayList<>();
//        Tile[] tiles = list.toArray(new Tile[0]);
    }

    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {
        Tile tile = gameModel[position.row][position.column];

        if (tileAction == TileAction.FLAG_TILE) {
            if (tile.getState() == TileState.FLAGGED || tile.getState() == TileState.NOT_CLICKED) {
                flagTile(tile);
            }
        } else if (tileAction == TileAction.REVEAL_TILE) {
            revealTile(tile);
        }
    }

    private void flagTile(Tile tile) {
        TileState tileState = tile.getState() == TileState.FLAGGED ? TileState.NOT_CLICKED : TileState.FLAGGED;
        tile.setState(tileState);
        TileStatus status = new TileStatus(tileState, tile.position);
        this.controller.setTileStatuses(new TileStatus[]{status});
    }

    private void revealTile(Tile tile) { // this needs fixed
        TileState tileState = tile.getState();
        TileValue tileValue = tile.getValue();
        if (tileState == TileState.NOT_CLICKED) {
            if (tileValue == TileValue.BOMB) {
                tile.setState(TileState.values()[tile.getValue().ordinal()]);
                //  handle endgame
            } else if (tileValue == TileValue.EMPTY) {
                tile.setState(TileState.values()[tile.getValue().ordinal()]);
            } else {
                tile.setState(TileState.values()[tile.getValue().ordinal()]);
            }
        }
        TileStatus status = new TileStatus(tile.getState(), tile.position);
        this.controller.setTileStatuses(new TileStatus[]{status});
    }

    public void endGameStateTransition() { // this needs fixed 
        int rows = (this.gameModel.length - 1);
        int col = (this.gameModel[0].length);
        int bombCount = 0;
        TileStatus[] returnedStatus = new TileStatus[bombs];
        for (int i = 0; i < this.gameModel.length; i++) {
            for (int j = 0; j < this.gameModel[0].length; j++) {
                if (this.gameModel[i][j].getValue() == TileValue.BOMB){
                    Tile temp = this.gameModel[i][j];
                    temp.setState(TileState.BOMB);
                    returnedStatus[bombCount] = new TileStatus(temp.getState(),temp.position);
                    
                }
            }
        }
        this.controller.setTileStatuses(returnedStatus);
    } 

    public static void main(String[] args) {  // FOR TESTS

        BasicModel n = new BasicModel(null);
        n.newGame(DefaultBoard.INTERMEDIATE);
    }

    @Override
    public void newGame(DefaultBoard board) {
        switch (board) {
            case EASY:
                newGame(8, 8, 10);
                break;
            case INTERMEDIATE:
                newGame(16, 16, 40);
                break;
            case EXPERT:
                newGame(24, 24, 99);
                break;
        }
    }

    /**
     *
     *
     * @param rows // The number of rows to assign to the grid
     * @param columns // The number of columns to assign to the grid
     * @param bombs // The number of bombs to be placed on the grid
     */
    public void newGame(int rows, int columns, int bombs) {
        this.bombs = bombs;
        Tile[][] state = new Tile[rows][columns];
        for (int row = 0; row < rows; row++) { // assigns EMPTY in order 
            // to avoid NULL POINTER
            for (int col = 0; col < columns; col++) {
                state[row][col] = new Tile(TileValue.EMPTY, row, col);
            }
        }
        int bombsToPlace = bombs;
        while (bombsToPlace > 0) {
            int x = (int) (Math.random() * ((rows - 1) + 1));
            int y = (int) (Math.random() * ((columns - 1) + 1)); //Generates an 
            //int from 0 to 
            //max - 1

            if (state[x][y].getValue() == TileValue.BOMB) { // making sure there 
                // isn't overlap of bomb

            } else {
                state[x][y].setValue(TileValue.BOMB); // assigning bombs to tiles 
                bombsToPlace--;
            }

        }
        this.gameModel = state;
        updateNumberedTiles();
        printGameState();
    }

    public void printGameState() {
        for (int i = 0; i < this.gameModel.length; i++) {
            System.out.println("");
            for (int j = 0; j < this.gameModel[0].length; j++) {
                System.out.print(String.format("%-6s", gameModel[i][j].getValue()));
            }
        }
    }

    /**
     * This method will iterate through the gameState Array and increment all
     * tiles that touch a bomb
     */
    public void updateNumberedTiles() { // t[0].length == col.len // t.length == row.len

        int rowLen = this.gameModel.length - 1; // # of rows
        int colLen = this.gameModel[0].length - 1; // # of columns

        for (int i = 0; i < rowLen + 1; i++) {
            for (int j = 0; j < colLen + 1; j++) {

                if (gameModel[i][j].getValue() == TileValue.BOMB) {

                    incrementTile(i - 1, j - 1);
                    incrementTile(i - 1, j);
                    incrementTile(i - 1, j + 1);
                    incrementTile(i, j - 1);
                    incrementTile(i, j + 1);
                    incrementTile(i + 1, j - 1);
                    incrementTile(i + 1, j);
                    incrementTile(i + 1, j + 1);
                }
            }
        }
    }

    /**
     * This method will allow you to increment the TileValue when the board is
     * being updated for the controller
     *
     * @param row // The target row to be changed
     * @param column // The target column to be changed
     */
    public void incrementTile(int row, int column) {
        try {
            if (this.gameModel[row][column].getValue() == TileValue.BOMB) {
                return;
            }
            int temp = this.gameModel[row][column].getValue().ordinal();
            this.gameModel[row][column].setValue(TileValue.values()[temp + 1]);
        } catch (IndexOutOfBoundsException ex) {
            // ignore
        }
    }

    @Override
    public void updateHighScore(DefaultBoard board, String name, long time) {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    public void getHighScores() {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    @Override
    public void cheatToggled(boolean toggle) {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    @Override
    public void newGame(BoardConfiguration boardConfiguration) throws InvalidBoardConfiguration {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HighScores getScores(DefaultBoard board) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BoardConfiguration getDefaultBoardConfiguration(DefaultBoard board) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Timer createTimer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
