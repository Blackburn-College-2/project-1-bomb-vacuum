package project.bomb.vacuum.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import project.bomb.vacuum.*;
import project.bomb.vacuum.exceptions.InvalidBoardConfiguration;

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
    public void newGame(BoardConfiguration boardConfiguration) throws InvalidBoardConfiguration {
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
        } else if (gameOverState == GameOverState.WIN) {
            // do nothing
        }
        this.controller.gameOver(gameOverState);
    }

//    @Override
//    //Still need to replace lines after 5 and order them 
//    public void updateHighScore(DefaultBoard board, String name, long time) {
//        PrintWriter pw;
//        File path = new File("/project-1-bomb-vacuum/src/main/java/project/bomb/vacuum/");
//        BufferedReader br;
//        String line;
//        int count;
//
//        try {
//            if (board == DefaultBoard.EASY) {
//                
//                br = new BufferedReader(new FileReader(path + "easyhighscores.txt"));
//                line = br.readLine();
//                if (line == null) {
//                    count = 0;
//                } else {
//                    count = 1;
//                }
//                while (line != null) {
//                    line = br.readLine();
//                    count++;
//                }
//
//                if (count < 5) {
//                    pw = new PrintWriter(new FileWriter(path + "easyhighscores.txt"));
//                    pw.write(name + "|" + time + "\n");
//                } else {
//                    //replace lines here
//                }
//            } else if (board == DefaultBoard.INTERMEDIATE) {
//                pw = new PrintWriter(new FileWriter(path + "intermediatehighscores.txt"));
//                pw.println(name + "|" + time + "\n");
//            } else if (board == DefaultBoard.EXPERT) {
//                pw = new PrintWriter(new FileWriter(path + "experthighscores.txt"));
//                pw.println(name + "|" + time + "\n");
//            }
//        } catch (IOException e) {
//            System.out.println("File does not exist!");
//        }
//    }
    public void updateHighScore(DefaultBoard board, String name, long time) {
//        String path = "/project-1-bomb-vacuum/src/main/java/project/bomb/vacuum/";
//        if (board == DefaultBoard.EASY) {
//            addToAndSortHighScores(board, path + "easyhighscores.txt", name, time);
//        } else if (board == DefaultBoard.INTERMEDIATE) {
//            addToAndSortHighScores(board, path + "intermediatehighscores.txt", name, time);
//        } else if (board == DefaultBoard.EXPERT) {
//            addToAndSortHighScores(board, path + "experthighscores.txt", name, time);
//        }

    }

//    private void addToAndSortHighScores(DefaultBoard board, String file, String name, long time) {
//        PrintWriter pw;
//        String[] highScores;
//        int count = 0;
//        try {
//            pw = new PrintWriter(new FileWriter(file));
//            highScores = getScores(board);
//            for (int i = 0; i < highScores.length; i++) {
//                if (highScores[i] != null) {
//                    count++;
//                }
//            }
//            if (count < highScores.length) {
//                pw.write(name + "|" + time + "\n");
//                pw.close();
//            } else {
//                highScores[4] = highScores[4].replace(highScores[4], name + "|" + time + "\n");
//                pw.write(highScores[4]);
//                pw.close();
//            }
//            pw.write(Collections.sort(Arrays.asList(highScores)));
//            pw.close();
//        } catch (IOException e) {
//            System.out.println("File does not exist!");
//        }
//    }

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
    public HighScores getScores(DefaultBoard board) {
        String[] rawScores = getScoresHelper(board);
        
        return new HighScores() {
            @Override
            public HighScore getFirst() {
                return convertRawScore(rawScores[0]);
            }

            @Override
            public HighScore getSecond() {
                return convertRawScore(rawScores[1]);
            }

            @Override
            public HighScore getThird() {
                return convertRawScore(rawScores[2]);
            }

            @Override
            public HighScore getFourth() {
                return convertRawScore(rawScores[3]);
            }

            @Override
            public HighScore getFifth() {
                return convertRawScore(rawScores[4]);
            }
        };
    }

    private HighScore convertRawScore(String score) {
        if (score == null) {
            return new HighScore() {
            public String getName() {
                return "NUL";
            }

            public long getTime() {
                return 0;
            }
        };
        }
        String[] parts = score.split("\\|");
        return new HighScore() {
            public String getName() {
                return parts[0];
            }
            public long getTime() {
                return Long.parseLong(parts[1]);
            }
        };
    }

    private String[] getScoresHelper(DefaultBoard board) {
        BufferedReader br;
        String path = "./src/main/java/project/bomb/vacuum/";
        String line;
        String[] highScores = new String[5];

        try {
            if (board == DefaultBoard.EASY) {
                br = new BufferedReader(new FileReader(path + "easyhighscores.txt"));
                for (int i = 0; i < highScores.length; i++) {
                    line = br.readLine();
                    highScores[i] = line;
                }
            } else if (board == DefaultBoard.INTERMEDIATE) {
                br = new BufferedReader(new FileReader(path + "intermediatehighscores.txt"));
                for (int i = 0; i < highScores.length; i++) {
                    line = br.readLine();
                    highScores[i] = line;
                }
            } else if (board == DefaultBoard.EXPERT) {
                br = new BufferedReader(new FileReader(path + "experthighscores.txt"));
                for (int i = 0; i < highScores.length; i++) {
                    line = br.readLine();
                    highScores[i] = line;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File does not exist!");
        }
        return highScores;
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
