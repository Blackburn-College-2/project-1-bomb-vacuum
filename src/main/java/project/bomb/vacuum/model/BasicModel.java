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

    private Tile[][] gameModel;
    private final Controller controller;
    private int bombs;
    private int nonBombsRemaining;
    private List<Tile> bombTiles = new ArrayList<>();
    private List<Tile> highlighted = new ArrayList<>();

    public BasicModel(Controller controller) {
        this.controller = controller;
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

    @Override
    public void newGame(BoardConfiguration boardConfiguration) {
        this.newGame(boardConfiguration.rows, boardConfiguration.columns, boardConfiguration.bombs);
    }

    /**
     * @param rows    // The number of rows to assign to the grid
     * @param columns // The number of columns to assign to the grid
     * @param bombs   // The number of bombs to be placed on the grid
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

        if (this.nonBombsRemaining <= 0) {
            this.endGameStateTransition(GameOverState.WIN);
        }
    }

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

    private void attemptReveal(int row, int column) {
        try {
            Tile otherTile = this.gameModel[row][column];
            recursiveReveal(otherTile);
        } catch (IndexOutOfBoundsException ex) {
            // do nothing.
        }
    }

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

    public void updateHighScore(DefaultBoard board, String name, long time) {
        String path = "./src/main/java/project/bomb/vacuum/";
        HighScore newHighScore = new SimpleHighScore(name, time);
        if (board == DefaultBoard.EASY) {
            saveScore(path + "easyhighscores.txt", newHighScore);
        } else if (board == DefaultBoard.INTERMEDIATE) {
            saveScore(path + "intermediatehighscores.txt", newHighScore);
        } else if (board == DefaultBoard.EXPERT) {
            saveScore(path + "experthighscores.txt", newHighScore);
        }
    }

    private void saveScore(String path, HighScore score) {
        String[] rawScores = loadScores(path);
        HighScore[] currentScores = parseScores(rawScores);
        HighScore[] newScores = new HighScore[currentScores.length + 1];
        System.arraycopy(currentScores, 0, newScores, 0, currentScores.length);
        newScores[newScores.length - 1] = score;

        sortScores(newScores);

        System.arraycopy(newScores, 0, currentScores, 0, currentScores.length);
        saveScores(path, currentScores);
    }

    private void sortScores(HighScore[] scores) {
        HighScore temp;
        for (int i = 0; i < scores.length - 1; i++) {
            int smallest = i;
            for (int k = i + 1; k < scores.length; k++) {
                if ((scores[k].getTime() < scores[smallest].getTime() && scores[k].getTime() > 0) || scores[smallest].getTime() == 0) {
                    smallest = k;
                }
            }
            temp = scores[i];
            scores[i] = scores[smallest];
            scores[smallest] = temp;
        }
    }

    private HighScore[] parseScores(String[] scores) {
        HighScore[] parsed = new HighScore[scores.length];
        for (int i = 0; i < scores.length; i++) {
            parsed[i] = parseScore(scores[i]);
        }
        return parsed;
    }

    private HighScore parseScore(String score) {
        if (score != null) {
            String[] parts = score.split("\\|");
            return new SimpleHighScore(parts[0], Long.parseLong(parts[1]));
        } else {
            return new SimpleHighScore("NUL", 0);
        }
    }

    private String[] loadScores(String path) {
        String[] scores = new String[5];
        BufferedReader reader = null;
        try {
            File file = new File(path);
            reader = new BufferedReader(new FileReader(file));
            for (int i = 0; i < scores.length; i++) {
                scores[i] = reader.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found: " + path);
        } catch (IOException ex) {
            System.out.println("Failed to read line in file: " + path);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                System.out.println("Failed to close reader");
            }
        }
        return scores;
    }

    private void saveScores(String path, HighScore[] scores) {
        String[] textScores = new String[scores.length];
        for (int i = 0; i < scores.length; i++) {
            textScores[i] = String.format("%s|%d", scores[i].getName(), scores[i].getTime());
        }
        saveScores(path, textScores);
    }

    private void saveScores(String path, String[] scores) {
        BufferedWriter writer = null;
        try {
            File file = new File(path);
            writer = new BufferedWriter(new FileWriter(file));
            for (String score : scores) {
                writer.write(score + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Failed to create writer: " + path);
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException ex) {
                System.out.println("Failed to close reader");
            }
        }
    }

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

    @Override
    public HighScores getScores(DefaultBoard board) {
        String path = "./src/main/java/project/bomb/vacuum/";

        if (board == DefaultBoard.EASY) {
            path += "easyhighscores.txt";
        } else if (board == DefaultBoard.INTERMEDIATE) {
            path += "intermediatehighscores.txt";
        } else if (board == DefaultBoard.EXPERT) {
            path += "experthighscores.txt";
        }

        String[] rawScores = loadScores(path);
        HighScore[] parsed = this.parseScores(rawScores);
        this.sortScores(parsed);
        return this.makeHighScores(parsed);
    }

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
