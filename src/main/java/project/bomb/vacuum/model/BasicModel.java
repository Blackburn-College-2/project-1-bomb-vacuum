package project.bomb.vacuum.model;

import java.io.*;
import project.bomb.vacuum.*;

public class BasicModel implements Model {

    private final GameBoard gameBoard;
    private final Controller controller;

    /**
     * Creates the basic model and allows the controller to communicate with it
     *
     * @param controller the controller to connect the model and view of Bomb
     *                   Vacuum
     */
    public BasicModel(Controller controller) {
        this.controller = controller;
        this.gameBoard = new GameBoard(controller);
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
     * @param rows    the number of rows to assign to the grid
     * @param columns the number of columns to assign to the grid
     * @param bombs   the number of bombs to be placed on the grid
     */
    private void newGame(int rows, int columns, int bombs) {
        this.gameBoard.newGame(rows, columns, bombs);
        this.controller.initializeBoard(rows, columns);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {  // middle?
        if (tileAction == TileAction.FLAG_TILE) {
            this.gameBoard.flagTile(position);
        } else if (tileAction == TileAction.REVEAL_TILE) {
            this.gameBoard.revealTile(position);
        } else if (tileAction == TileAction.HIGHLIGHT) {
            this.gameBoard.highlightTiles(position);
        }
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
     *
     * @param path  the path to the correct high score file
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
     * Takes the String[] scores to be turned into a HighScore[]
     *
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
     *
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
     *
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
     *
     * @param path   the file to be added to
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
     *
     * @param path   the file to be added to
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
     * {@inheritDoc }
     */
    @Override
    public void cheatToggled(boolean toggle) {
        this.gameBoard.cheatToggle(toggle);
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
     *
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
