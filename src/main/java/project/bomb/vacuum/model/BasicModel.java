package project.bomb.vacuum.model;

import java.util.List;
import project.bomb.vacuum.*;

public class BasicModel implements Model {

    private final Timer timer;
    private final GameBoard gameBoard;
    private final HighScoreHandler highScoreHandler = new HighScoreHandler();
    private final CustomConfigSaver configSaver = new CustomConfigSaver();
    private final Controller controller;
    private final BoardValidator validator = (boardConfig) -> {
        boolean validRows = false;
        boolean validColumns = false;
        boolean validBombs = false;
        if (boardConfig.rows >= 2 && boardConfig.rows <= 50) {
            validRows = true;
        }
        if (boardConfig.columns >= 2 && boardConfig.columns <= 50) {
            validColumns = true;
        }
        if (boardConfig.bombs >= 1 && boardConfig.bombs < (boardConfig.rows * boardConfig.columns)) {
            validBombs = true;
        }

        return validRows && validColumns && validBombs;
    };
    private final NameValidator nameValidator = (name) -> name.length() == 3;

    private DefaultBoard currentBoard;
    private boolean timerRunning;

    /**
     * Gets the minimum amount of bombs for a given board configuration
     *
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     * @return the min amount of bombs
     */
    public int getMinBombs(int rows, int columns) {
        return 1;
    }

    /**
     * Gets the maximum amount of bombs for a given board configuration
     *
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     * @return the max amount of bombs
     */
    public int getMaxBombs(int rows, int columns) {
        return (rows * columns - 1);
    }

    /**
     * Creates the basic model and allows the controller to communicate with it
     *
     * @param controller the controller to connect the model and view of Bomb
     *                   Vacuum
     */
    public BasicModel(Controller controller) {
        this.controller = controller;
        this.gameBoard = new GameBoard(this);
        this.timer = new BasicTimer(controller);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void newGame(DefaultBoard board) {
        this.currentBoard = board;
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
        this.currentBoard = this.isSameAsDefaultBoard(boardConfiguration);
        this.newGame(boardConfiguration.rows, boardConfiguration.columns, boardConfiguration.bombs);
    }

    private DefaultBoard isSameAsDefaultBoard(BoardConfiguration configuration) {
        DefaultBoard board = null;
        int rows = configuration.rows;
        int columns = configuration.columns;
        int bombs = configuration.bombs;

        if (rows == columns) {
            if (rows == 8 && bombs == 10) {
                board = DefaultBoard.EASY;
            } else if (rows == 16 && bombs == 40) {
                board = DefaultBoard.INTERMEDIATE;
            } else if (rows == 24 && bombs == 99) {
                board = DefaultBoard.EXPERT;
            }
        }

        return board;
    }

    /**
     * @param rows    the number of rows to assign to the grid
     * @param columns the number of columns to assign to the grid
     * @param bombs   the number of bombs to be placed on the grid
     */
    private void newGame(int rows, int columns, int bombs) {
        this.timer.stop();
        this.timer.reset();
        this.gameBoard.newGame(rows, columns, bombs);
        this.controller.initializeBoard(rows, columns);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {
        if (!timerRunning) {
            timerRunning = true;
            this.timer.start();
        }
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
    public void updateHighScore(String name, long time) {
        this.highScoreHandler.saveHighScore(this.currentBoard, name, time);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<HighScore> getScores(DefaultBoard board) {
        return this.highScoreHandler.loadSortedScores(board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HighScore> getScores() {
        return this.getScores(this.currentBoard);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void cheatToggled(boolean toggle) {
        this.gameBoard.cheatToggle(toggle);
    }

    @Override
    public BoardValidator getBoardValidator() {
        return this.validator;
    }

    public NameValidator getNameValidator() {
        return this.nameValidator;
    }

    @Override
    public BoardConfiguration getMinBoardConfig() {
        return new BoardConfiguration(2, 2, 1);
    }

    @Override
    public BoardConfiguration getMaxBoardConfig() {
        return new BoardConfiguration(25, 35, (25 * 35) - 1);
    }

    @Override
    public void saveBoardConfig(BoardConfiguration configuration) {
        this.configSaver.saveConfig(configuration);
    }

    @Override
    public BoardConfiguration getSavedBoardConfig() {
        return this.configSaver.getSavedConfig();
    }

    @Override
    public long getTime() {
        return this.timer.getTime();
    }

    void gameOver(GameOverState gameOverState) {
        timerRunning = false;
        this.timer.stop();
        if (this.currentBoard != null && gameOverState == GameOverState.WIN) {
            boolean betterTime = this.highScoreHandler.isNewHighScore(this.currentBoard, this.timer.getTime());
            this.controller.gameOver(gameOverState, betterTime);
        } else {
            this.controller.gameOver(gameOverState, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBombsRemainingListener(ChangeListener<Integer> listener) {
        this.gameBoard.addBombsRemainingListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBoardListener(BoardListener listener) {
        this.gameBoard.addBoardListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean usingDefaultBoard() {
        return this.currentBoard != null;
    }

}
