package project.bomb.vacuum.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileState;
import project.bomb.vacuum.TileStatus;

public class GameBoard {

    private final Controller controller;
    private Tile[][] board;
    private final List<Tile> bombs = new ArrayList<>();
    private final List<Tile> highlighted = new ArrayList<>();
    private int tilesLeftToReveal;

    private TileModifier recursiveReveal = tile -> {
        if (tile.getState() != TileState.NOT_CLICKED) {
            return;
        }

        if (tile.getValue() != TileValue.BOMB) {
            this.tilesLeftToReveal--;
        }
        TileState state = TileState.values()[tile.getValue().ordinal()];
        this.updateTileState(tile, state);
        if (tile.getValue() != TileValue.EMPTY) {
            return;
        }

        modifySurroundingTiles(tile, this.recursiveReveal);
    };
    private TileModifier attemptTileHighlight = tile -> {
        if (tile.getState() == TileState.NOT_CLICKED) {
            updateTileState(tile, TileState.HIGHLIGHTED);
        }
    };
    private TileModifier attemptTileIncrement = tile -> {
        if (tile.getValue() == TileValue.BOMB) {
            return;
        }
        int nextTileValue = tile.getValue().ordinal() + 1;
        tile.setValue(TileValue.values()[nextTileValue]);
    };

    public GameBoard(Controller controller) {
        this.controller = controller;
    }

    public void newGame(int rows, int columns, int bombs) {
        this.tilesLeftToReveal = (rows * columns) - bombs;
        this.initBoard(rows, columns);
        this.placeBombs(rows, columns, bombs);
        this.updateBombsSurroundingTiles();
    }

    private void initBoard(int rows, int columns) {
        this.board = new Tile[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                this.board[row][column] = new Tile(TileValue.EMPTY, row, column);
            }
        }
    }

    private void placeBombs(int rows, int columns, int bombs) {
        Random random = new Random();
        int row;
        int column;
        while (bombs > 0) {
            row = random.nextInt(rows);
            column = random.nextInt(columns);
            Tile tile = this.board[row][column];
            if (tile.getValue() != TileValue.BOMB) {
                tile.setValue(TileValue.BOMB);
                this.bombs.add(tile);
                bombs--;
            }
        }
    }

    private void updateBombsSurroundingTiles() {
        for (Tile bomb : this.bombs) {
            modifySurroundingTiles(bomb, attemptTileIncrement);
        }
    }

    private void modifySurroundingTiles(Tile tile, TileModifier modifier) {
        Position position = tile.position;
        for (int row = position.row - 1; row <= position.row + 1; row++) {
            for (int column = position.column - 1; column <= position.column + 1; column++) {
                if (row != position.row && column != position.column) {
                    this.attemptToModifyTilePosition(row, column, modifier);
                }
            }
        }
    }

    private void attemptToModifyTilePosition(int row, int column, TileModifier modifier) {
        if (row >= 0 && row < this.board.length) {
            if (column >= 0 && column < this.board[row].length) {
                modifier.alterTile(this.board[row][column]);
            }
        }
    }

    public void revealTile(Position position) {
        Tile tile = this.board[position.row][position.column];
        this.recursiveReveal.alterTile(tile);
    }

    public void highlightSurroundingTiles(Position position) {
        Tile tile = this.board[position.row][position.column];
        this.modifySurroundingTiles(tile, this.attemptTileHighlight);
    }

    private void deHighlightTile() {
        for (Tile highlighted : this.highlighted) {
            // If the tile is still highlighted, in case it was revealed.
            if (highlighted.getState() == TileState.HIGHLIGHTED) {
                updateTileState(highlighted, TileState.NOT_CLICKED);
            }
        }
    }

    private void updateTileState(Tile tile, TileState state) {
        tile.setState(state);
        this.controller.setTileStatuses(new TileStatus[]{new TileStatus(state, tile.position)});
    }

}
