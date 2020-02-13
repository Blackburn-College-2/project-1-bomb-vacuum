package project.bomb.vacuum.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import project.bomb.vacuum.*;

public class BasicModel implements Model {

    private final HighScoreHandler highScoreHandler = new HighScoreHandler();
    private Tile[][] gameModel;
    private final Controller controller;
    private int bombs;
    private int nonBombsRemaining;
    private List<Tile> bombTiles = new ArrayList<>();
    private List<Tile> highlighted = new ArrayList<>();

    /**
     * Creates the basic model and allows the controller to communicate with it
     *
     * @param controller the controller to connect the model and view of Bomb
     * Vacuum
     */
    public BasicModel(Controller controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc }
     */
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
     * {@inheritDoc }
     */
    @Override
    public void newGame(BoardConfiguration boardConfiguration) {
        this.newGame(boardConfiguration.rows, boardConfiguration.columns, boardConfiguration.bombs);
    }

    /**
     * @param rows // The number of rows to assign to the grid
     * @param columns // The number of columns to assign to the grid
     * @param bombs // The number of bombs to be placed on the grid
     */
    private void newGame(int rows, int columns, int bombs) {
        this.bombTiles.clear();
        this.bombs = bombs;
        this.nonBombsRemaining = rows * columns - bombs;

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
     * @param row // The target row to be changed
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

    /**
     * {@inheritDoc }
     */
    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {  // middle?
        Tile tile = gameModel[position.row][position.column];

        if (tileAction == TileAction.FLAG_TILE) {
            if (tile.getState() == TileState.FLAGGED || tile.getState() == TileState.NOT_CLICKED) {
                flagTile(tile);
            }
        } else if (tileAction == TileAction.REVEAL_TILE) {
            revealTile(tile);
        } else if (tileAction == TileAction.HIGHLIGHT) {
            highlightTiles(tile);
        }
    }

    /**
     * Handles the action of flagging a tile and updates the controller's tile
     * statuses
     *
     * @param tile the chosen tile to be flagged
     */
    private void flagTile(Tile tile) {
        TileState tileState = tile.getState() == TileState.FLAGGED ? TileState.NOT_CLICKED : TileState.FLAGGED;
        tile.setState(tileState);
        TileStatus status = new TileStatus(tileState, tile.position);
        this.controller.setTileStatuses(new TileStatus[]{status});
    }

    /**
     * Handles the action of revealing a single tile. If the tile is empty, it
     * will call the recursiveReveal method. If the tile is a bomb, the game
     * ends
     *
     * @param tile the tile to be revealed
     */
    private void revealTile(Tile tile) {
        if (tile.getValue() == TileValue.BOMB) {
            endGameStateTransition(GameOverState.LOSE);
            return;
        }
        recursiveReveal(tile);

        if (this.nonBombsRemaining <= 0) {
            this.endGameStateTransition(GameOverState.WIN);
        }
    }

    /**
     * Recursively reveals the tiles around the chosen tile as long as they are
     * empty or, if they are a number, one tile depth
     *
     * @param tile the tile to begin revealing around
     */
    private void recursiveReveal(Tile tile) {
        if (tile.getState() != TileState.NOT_CLICKED && tile.getState() != TileState.HIGHLIGHTED) {
            return;
        }

        tile.setState(TileState.values()[tile.getValue().ordinal()]);
        controller.setTileStatuses(new TileStatus[]{new TileStatus(tile.getState(), tile.position)});
        this.nonBombsRemaining--;

        if (tile.getValue() != TileValue.EMPTY) {
            return;
        }

        Position position = tile.position;
        this.attemptReveal(position.row - 1, position.column - 1);
        this.attemptReveal(position.row - 1, position.column);
        this.attemptReveal(position.row - 1, position.column + 1);
        this.attemptReveal(position.row, position.column - 1);
        this.attemptReveal(position.row, position.column + 1);
        this.attemptReveal(position.row + 1, position.column - 1);
        this.attemptReveal(position.row + 1, position.column);
        this.attemptReveal(position.row + 1, position.column + 1);
    }

    /**
     * Attempts to reveal the tile unless the tile does not exist such as
     * checking around corner tiles
     *
     * @param row the row of the tile
     * @param column the column of the tile
     */
    private void attemptReveal(int row, int column) {
        try {
            Tile otherTile = this.gameModel[row][column];
            recursiveReveal(otherTile);
        } catch (IndexOutOfBoundsException ex) {
            // do nothing.
        }
    }

    /**
     * Highlights the 8 tiles around the chosen tile when the middle mouse
     * button is pressed unless the tile is revealed already.
     *
     * @param tile the starting tile
     */
    private void highlightTiles(Tile tile) {
        for (Tile tileH : highlighted) {
            if (tileH.getState() == TileState.HIGHLIGHTED) {
                tileH.setState(TileState.NOT_CLICKED);
                controller.setTileStatuses(new TileStatus[]{new TileStatus(tileH.getState(), tileH.position)});
            }
        }
        if (highlighted.size() > 0) {
            if (highlighted.get(0) == tile) {
                highlighted.clear();
                return;
            }
        }
        highlighted.clear();
        Position position = tile.position;
        this.attemptHighlight(position.row, position.column);
        this.attemptHighlight(position.row - 1, position.column - 1);
        this.attemptHighlight(position.row - 1, position.column);
        this.attemptHighlight(position.row - 1, position.column + 1);
        this.attemptHighlight(position.row, position.column - 1);
        this.attemptHighlight(position.row, position.column + 1);
        this.attemptHighlight(position.row + 1, position.column - 1);
        this.attemptHighlight(position.row + 1, position.column);
        this.attemptHighlight(position.row + 1, position.column + 1);
    }

    /**
     * Attempts to highlight the surrounding tiles unless the tile does not
     * exist or is already revealed
     *
     * @param row
     * @param column
     */
    private void attemptHighlight(int row, int column) {
        try {
            Tile tile = this.gameModel[row][column];
            if (tile.getState() == TileState.NOT_CLICKED) {
                tile.setState(TileState.HIGHLIGHTED);
                controller.setTileStatuses(new TileStatus[]{new TileStatus(tile.getState(), tile.position)});
                this.highlighted.add(tile);
            }
        } catch (IndexOutOfBoundsException ex) {
            // do nothing
        }
    }

    /**
     * Tells the controller to begin the end game process for either the win or
     * lose state
     *
     * @param gameOverState either win or lose
     */
    private void endGameStateTransition(GameOverState gameOverState) {
        if (gameOverState == GameOverState.LOSE) {
            int bombCount = 0;
            TileStatus[] returnedStatus = new TileStatus[bombs];

            for (Tile bombTile : bombTiles) {
                bombTile.setState(TileState.BOMB);
                returnedStatus[bombCount++] = new TileStatus(bombTile.getState(), bombTile.position);
            }

            this.controller.setTileStatuses(returnedStatus);
        }

        this.controller.gameOver(gameOverState);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void updateHighScore(DefaultBoard board, String name, long time) {
        this.highScoreHandler.saveHighScore(board, name, time);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public HighScores getScores(DefaultBoard board) {
        List<HighScore> scores = this.highScoreHandler.loadSortedScores(board);
        HighScore[] highScores = scores.toArray(new HighScore[0]);
        return this.makeHighScores(highScores);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void cheatToggled(boolean toggle) {
        TileStatus[] returnedStatus = new TileStatus[bombs];

        int counter = 0;
        if (toggle) {
            for (Tile bombTile : bombTiles) {
                returnedStatus[counter] = new TileStatus(TileState.BOMB, bombTile.position);
                counter++;
            }
        } else {
            for (Tile bombTile : bombTiles) {
                returnedStatus[counter++] = new TileStatus(bombTile.getState(), bombTile.position);
            }
        }

        this.controller.setTileStatuses(returnedStatus);
    }

    /**
     * Converts a HighScore[] into HighScores
     * @param scores the scores to be converted
     * @return the converted scores
     */
    private HighScores makeHighScores(HighScore[] scores) {
        return new HighScores() {
            @Override
            public HighScore getFirst() {
                return scores[0];
            }

            @Override
            public HighScore getSecond() {
                return scores[1];
            }

            @Override
            public HighScore getThird() {
                return scores[2];
            }

            @Override
            public HighScore getFourth() {
                return scores[3];
            }

            @Override
            public HighScore getFifth() {
                return scores[4];
            }
        };
    }

}
