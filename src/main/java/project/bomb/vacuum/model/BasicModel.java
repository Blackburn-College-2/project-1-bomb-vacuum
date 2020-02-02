package project.bomb.vacuum.model;

import project.bomb.vacuum.BoardConfiguration;
import project.bomb.vacuum.DefaultBoard;
import project.bomb.vacuum.HighScores;
import project.bomb.vacuum.Model;
import project.bomb.vacuum.TileAction;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.Timer;
import project.bomb.vacuum.exceptions.InvalidBoardConfiguration;

public class BasicModel implements Model {

    private TileValue[][] gameState;

    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    public static void main(String[] args) {
        BasicModel n = new BasicModel();
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
     * So far, newGame will build the grid and place bombs. Next action is to
     * make it count the number of tiles touched by bombs.
     *
     * @param rows // The number of rows to assign to the grid
     * @param columns // The number of columns to assign to the grid
     * @param bombs // The number of bombs to be placed on the grid
     */

    
    public void newGame(int rows, int columns, int bombs) {
        TileValue[][] state = new TileValue[rows][columns];
        for (int a = 0; a < rows; a++) { // assigns EMPTY in order 
            // to avoid NULL POINTER
            for (int b = 0; b < columns; b++) {
                state[a][b] = TileValue.EMPTY;
            }
        }
        for (int i = 0; i < (bombs); i++) {
            int x = (int) (Math.random() * ((rows - 1) + 1));
            int y = (int) (Math.random() * ((columns - 1) + 1)); //Generates an 
            //int from 0 to 
            //max - 1

            if (state[x][y].equals(TileValue.BOMB)) { // making sure there 
                // isn't overlap of bomb
                i--;
            } else {
                state[x][y] = TileValue.BOMB; // assigning bombs to tiles 
            }
            this.gameState = state;
        }

        updateNumberedTiles();
        printGameState();

//        for (int j = 0; j < rows; j++) { // testing the values of the tiles                       ----TESTS----
//            for (int k = 0; k < columns; k++) {
//                System.out.println(state[j][k]);
//            }                                                                                                      
//        }                                                                                                         
//        int bombCount = 0;                                                                                        
//        for (int j = 0; j < rows; j++) { // testing the number of bombs placed                    ---- TESTS -----
//            for (int k = 0; k < columns; k++) {
//                if (state[j][k].equals(TileValue.BOMB)) {
//                    bombCount++;
//                }
//            }
//        }
//        throw new UnsupportedOperationException("Method not yet implemented.");                   ----TESTS-----
    }

    public void printGameState() {
        for (int i = 0; i < this.gameState.length; i++) {
            System.out.println("");
            for (int j = 0; j < this.gameState[0].length; j++) {
                System.out.print(String.format("%-6s", gameState[i][j]));
            }
        }
    }

    /**
     * This method will iterate through the gameState Array and increment all
     * tiles that touch a bomb
     */
    public void updateNumberedTiles() { // t[0].length == col.len // t.length == row.len

        int rowLen = this.gameState.length - 1; // # of rows
        int colLen = this.gameState[0].length - 1; // # of columns
        System.out.println(rowLen);
        System.out.println(colLen);
        for (int i = 0; i < rowLen + 1; i++) {
            for (int j = 0; j < colLen + 1; j++) {
                int tempRow = i;
                int tempCol = j;

                if (gameState[i][j].equals(TileValue.BOMB)) {

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
            if (this.gameState[row][column] == TileValue.BOMB) {
                return;
            }
            int temp = this.gameState[row][column].ordinal();
            this.gameState[row][column] = TileValue.values()[temp + 1];
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
