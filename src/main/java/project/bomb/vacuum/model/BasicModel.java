package project.bomb.vacuum.model;

import java.util.List;
import project.bomb.vacuum.*;

public class BasicModel implements Model {

    private final GameBoard gameBoard;
    private final HighScoreHandler highScoreHandler = new HighScoreHandler();
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
        this.gameBoard.cheatToggle(toggle);
    }

    /**
     * Converts a HighScore[] into HighScores
     *
     * @param scores the scores to be converted
     * @return the converted scores
     */
    private HighScores makeHighScores(HighScore[] scores) {
        if (scores.length > 4) {
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
        } else {
            return new HighScores() {
                @Override
                public HighScore getFirst() {
                    return new SimpleHighScore("NUL", 0);
                }

                @Override
                public HighScore getSecond() {
                    return new SimpleHighScore("NUL", 0);
                }

                @Override
                public HighScore getThird() {
                    return new SimpleHighScore("NUL", 0);
                }

                @Override
                public HighScore getFourth() {
                    return new SimpleHighScore("NUL", 0);
                }

                @Override
                public HighScore getFifth() {
                    return new SimpleHighScore("NUL", 0);
                }
            };
        }
    }

}
