package project.bomb.vacuum.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import project.bomb.vacuum.*;

public class GameBoard {

    private final BasicModel model;
    private final List<ChangeListener<Integer>> bombsRemainingListeners = new ArrayList<>();
    private final List<BoardListener> boardListeners = new ArrayList<>();
    private Tile[][] board;
    private final List<Tile> bombs = new ArrayList<>();
    private final List<Tile> highlighted = new ArrayList<>();
    private final List<Tile> flagged = new ArrayList<>();
    private int tilesLeftToReveal;
    private boolean gameOver = false;
    private boolean cheating = false;
    private Position previousHighlightLocation;

    private TileModifier recursiveReveal = tile -> {
        if (!(tile.getState() == TileState.NOT_CLICKED || tile.getState() == TileState.HIGHLIGHTED)) {
            return;
        }

        TileState state = TileState.values()[tile.getValue().ordinal()];

        if (tile.getValue() == TileValue.BOMB){
            if (this.cheating) {
                return;
            }
            this.updateAndSetTileState(tile, state);
            this.gameOver = true;
            return;
        }

        this.updateAndSetTileState(tile, state);
        this.tilesLeftToReveal--;

        if (tile.getValue() != TileValue.EMPTY) {
            return;
        }
        modifySurroundingTiles(tile, this.recursiveReveal);
    };
    private TileModifier attemptTileHighlight = tile -> {
        if (tile.getState() == TileState.NOT_CLICKED) {
            this.highlighted.add(tile);
            updateAndSetTileState(tile, TileState.HIGHLIGHTED);
        }
    };
    private TileModifier attemptTileIncrement = tile -> {
        if (tile.getValue() == TileValue.BOMB) {
            return;
        }
        int nextTileValue = tile.getValue().ordinal() + 1;
        tile.setValue(TileValue.values()[nextTileValue]);
    };

    public GameBoard(BasicModel model) {
        this.model = model;
    }

    public void newGame(int rows, int columns, int bombs) {
        this.gameOver = false;
        this.cheating = false;
        this.bombs.clear();
        this.highlighted.clear();
        this.flagged.clear();
        this.previousHighlightLocation = null;
        this.tilesLeftToReveal = (rows * columns) - bombs;
        this.initBoard(rows, columns);
        this.placeBombs(rows, columns, bombs);
        this.updateBombsSurroundingTiles();
        this.updateBombCounter();
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
                if (!(row == position.row && column == position.column)) {
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
        if (this.tilesLeftToReveal <= 0) {
            this.endGameStateTransition(GameOverState.WIN);
        }
        if (this.gameOver) {
            this.endGameStateTransition(GameOverState.LOSE);
        }
    }

    public void flagTile(Position position) {
        Tile tile = this.board[position.row][position.column];
        TileState state;
        switch (tile.getState()) {
            case FLAGGED:
                state = this.highlighted.contains(tile) ? TileState.HIGHLIGHTED : TileState.NOT_CLICKED;
                this.flagged.remove(tile);
                break;
            case HIGHLIGHTED:
                // Fall through
            case NOT_CLICKED:
                state = TileState.FLAGGED;
                this.flagged.add(tile);
                break;
            default:
                state = tile.getState();
                break;
        }
        this.updateBombCounter();
        this.updateAndSetTileState(tile, state);
    }

    public void highlightTiles(Position position) {
        Tile tile = this.board[position.row][position.column];
        if (!(tile.getState().ordinal() <= 8)) {
            return;
        }
        if (position.equals(this.previousHighlightLocation)) {
            if (this.highlighted.size() > 0) {
                this.deHighlightTiles();
                this.highlighted.clear();
                return;
            }
        }
        this.deHighlightTiles();
        this.highlighted.clear();

        this.previousHighlightLocation = position;

        this.highlighted.add(tile);
        this.modifySurroundingTiles(tile, this.attemptTileHighlight);
    }

    private void deHighlightTiles() {
        for (Tile highlighted : this.highlighted) {
            // If the tile is still highlighted, in case it was revealed.
            if (highlighted.getState() == TileState.HIGHLIGHTED) {
                updateAndSetTileState(highlighted, TileState.NOT_CLICKED);
            }
        }
    }

    private void endGameStateTransition(GameOverState gameOverState) {
        if (gameOverState == GameOverState.LOSE) {
            for (Tile bombTile : bombs) {
                this.updateAndSetTileState(bombTile, TileState.BOMB);
            }
        }

        this.model.gameOver(gameOverState);
    }

    private void updateAndSetTileState(Tile tile, TileState state) {
        tile.setState(state);
        this.updateTileState(tile, state);
    }

    private void updateTileState(Tile tile, TileState state) {
        for (BoardListener listener : this.boardListeners) {
            listener.updateTileStatuses(new TileStatus[]{new TileStatus(state, tile.position)});
        }
    }

    public void cheatToggle(boolean toggle) {
        this.cheating = toggle;
        // We lie
        if (toggle) {
            for (Tile bombTile : bombs) {
                this.updateTileState(bombTile, TileState.BOMB);
            }
        } else {
            for (Tile bombTile : bombs) {
                this.updateTileState(bombTile, bombTile.getState());
            }
        }
    }

    public void addBombsRemainingListener(ChangeListener<Integer> listener) {
        this.bombsRemainingListeners.add(listener);
    }

    public void addBoardListener(BoardListener listener) {
        this.boardListeners.add(listener);
    }

    private void updateBombCounter() {
        int bombs = this.bombs.size() - this.flagged.size();
        bombs = Math.max(bombs, 0);
        for (ChangeListener<Integer> listener : this.bombsRemainingListeners) {
            listener.onChange(0, bombs);
        }
    }

}
