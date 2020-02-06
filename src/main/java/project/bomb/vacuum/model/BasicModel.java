package project.bomb.vacuum.model;

import java.util.ArrayList;
import java.util.List;
import project.bomb.vacuum.*;
import project.bomb.vacuum.exceptions.InvalidBoardConfiguration;

public class BasicModel implements Model {

    private Tile[][] gameModel;
    private final Controller controller;
    private int bombs;
    private List<Tile> bombTiles = new ArrayList<>();

    public BasicModel(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {  // middle?
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
        if (tile.getValue() == TileValue.BOMB) {
            endGameStateTransition(GameOverState.LOSE);
            return;
        }
        recursiveReveal(tile);
    }

    private void recursiveReveal(Tile tile) {
        if (tile.getState() != TileState.NOT_CLICKED) {
            return;
        } else if (tile.getValue() != TileValue.EMPTY) {
            tile.setState(TileState.values()[tile.getValue().ordinal()]);
            controller.setTileStatuses(new TileStatus[]{new TileStatus(tile.getState(), tile.position)});
            return;
        }
        tile.setState(TileState.values()[tile.getValue().ordinal()]);

        Position position = tile.position;
        this.attemptReveal(position.row - 1, position.column - 1);
        this.attemptReveal(position.row - 1, position.column);
        this.attemptReveal(position.row - 1, position.column + 1);
        this.attemptReveal(position.row, position.column - 1);
        this.attemptReveal(position.row, position.column + 1);
        this.attemptReveal(position.row + 1, position.column - 1);
        this.attemptReveal(position.row + 1, position.column);
        this.attemptReveal(position.row + 1, position.column + 1);

        controller.setTileStatuses(new TileStatus[]{new TileStatus(tile.getState(), tile.position)});
    }

    private void attemptReveal(int row, int column) {
        try {
            Tile otherTile = this.gameModel[row][column];
            recursiveReveal(otherTile);
        } catch (IndexOutOfBoundsException ex) {
            // do nothing.
        }
    }

    private void endGameStateTransition(GameOverState gameOverState) {
        int bombCount = 0;
        TileStatus[] returnedStatus = new TileStatus[bombs];

        for (Tile bombTile : bombTiles) {
            bombTile.setState(TileState.BOMB);
            returnedStatus[bombCount++] = new TileStatus(bombTile.getState(), bombTile.position);
        }

        this.controller.setTileStatuses(returnedStatus);
        this.controller.gameOver(gameOverState);
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
     * @param rows    // The number of rows to assign to the grid
     * @param columns // The number of columns to assign to the grid
     * @param bombs   // The number of bombs to be placed on the grid
     */
    private void newGame(int rows, int columns, int bombs) {
        this.bombTiles.clear();
        this.bombs = bombs;

        Tile[][] state = new Tile[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = new Tile(TileValue.EMPTY, row, col);
                state[row][col] = tile;
            }
        }

        int bombsToPlace = bombs;
        while (bombsToPlace > 0) {
            int x = (int) (Math.random() * ((rows - 1) + 1));
            int y = (int) (Math.random() * ((columns - 1) + 1));

            if (state[x][y].getValue() != TileValue.BOMB) {
                this.bombTiles.add(state[x][y]);
                state[x][y].setValue(TileValue.BOMB);
                bombsToPlace--;
            }
        }
        this.gameModel = state;

        updateNumberedTiles();
        controller.initializeBoard(rows, columns);
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
    private void updateNumberedTiles() { // t[0].length == col.len // t.length == row.len
        for (Tile bombTile : bombTiles) {
            Position position = bombTile.position;
            incrementTile(position.row - 1, position.column - 1);
            incrementTile(position.row - 1, position.column);
            incrementTile(position.row - 1, position.column + 1);
            incrementTile(position.row, position.column - 1);
            incrementTile(position.row, position.column + 1);
            incrementTile(position.row + 1, position.column - 1);
            incrementTile(position.row + 1, position.column);
            incrementTile(position.row + 1, position.column + 1);
        }
    }

    /**
     * This method will allow you to increment the TileValue when the board is
     * being updated for the controller
     *
     * @param row    // The target row to be changed
     * @param column // The target column to be changed
     */
    private void incrementTile(int row, int column) {
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
        TileStatus[] returnedStatus = new TileStatus[bombs];

        if (toggle) {
            int counter = 0;
            for (Tile bombTile : bombTiles) {
                returnedStatus[counter] = new TileStatus(TileState.BOMB, bombTile.position);
                counter++;
            }
        } else {
            int counter = 0;
            for (Tile bombTile : bombTiles) {
                returnedStatus[counter++] = new TileStatus(bombTile.getState(), bombTile.position);
            }
        }

        this.controller.setTileStatuses(returnedStatus);
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
