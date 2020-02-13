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

    /**
     * Saves the score in the correct file
     * @param path the path to the correct high score file
     * @param score the score to add
     */
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

    /**
     * Sorts the high scores in the file from best to worst with best at the top
     * Must always have 5 scores so handles null values by placing them at the bottom
     * Sorted using selection sort
     *
     * @param scores the scores to be sorted
     */
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

    /**
     * Compares two long values
     * @param one the first value
     * @param two the second value
     * @return an int representing the comparison. 0 if one and two are equal
     * 1 if one is greater than two
     * -1 if two is greater than one
     */
    private int compareLong(long one, long two) {
        if (one == two) {
            return 0;
        } else if (one > two) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Takes the String[] scores to be turned into a HighScore[]
     * @param scores the scores to be converted
     * @return the proper format for high scores using a HighScore object array
     */
    private HighScore[] parseScores(String[] scores) {
        HighScore[] parsed = new HighScore[scores.length];
        for (int i = 0; i < scores.length; i++) {
            parsed[i] = parseScore(scores[i]);
        }
        return parsed;
    }

    /**
     * Parses the score into the HighScore object
     * @param score the score to be added to the HighScore object
     * @return the HighScore object
     */
    private HighScore parseScore(String score) {
        if (score != null) {
            String[] parts = score.split("\\|");
            return new SimpleHighScore(parts[0], Long.parseLong(parts[1]));
        } else {
            return new SimpleHighScore("NUL", 0);
        }
    }

    /**
     * The model logic for displaying the high scores in each high score file
     * Simply reads each file and parses them into a String[] to be read
     * @param path the path to the correct high score file
     * @return the String array containing the high scores
     */
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

    /**
     * Saves the sorted scores in the correct location. Converts the scores into
     * a String array to be passed to a wrapper method
     * @param path the file to be added to
     * @param scores the scores to be added
     */
    private void saveScores(String path, HighScore[] scores) {
        String[] textScores = new String[scores.length];
        for (int i = 0; i < scores.length; i++) {
            textScores[i] = String.format("%s|%d", scores[i].getName(), scores[i].getTime());
        }
        saveScores(path, textScores);
    }

    /**
     * A wrapper method to handle a String[] of scores
     * @param path the file to be added to
     * @param scores the scores to be added
     */
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

    /**
     * Prints the array
     * @param <T> Java generics representing Type
     * @param arr the array to be printed
     */
    private <T> void printArray(T[] arr) {
        for (T e : arr) {
            if (e != null) {
                System.out.println(e.toString());
            }
        }
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
     * {@inheritDoc }
     */
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
