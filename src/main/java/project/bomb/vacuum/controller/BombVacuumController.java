package project.bomb.vacuum.controller;

import java.util.List;
import project.bomb.vacuum.*;
import project.bomb.vacuum.exceptions.InvalidStateException;
import project.bomb.vacuum.model.BasicModel;
import project.bomb.vacuum.view.GUI;

/**
 * The controller for running a game of Bomb Vacuum.
 */
public class BombVacuumController implements Controller {

    public static void main(String[] args) {
        new BombVacuumController();
    }

    private final Model model;
    private View view;
    private boolean timerRunning = false;
    private boolean gameOver = false;

    /**
     * Starts the game
     */
    public BombVacuumController() {
        this.model = new BasicModel(this);
        GUI.setController(this);
        GUI.setStartup(() -> model.newGame(DefaultBoard.INTERMEDIATE));
        GUI.launchGUI();
    }


    // ##### Called By Model #####

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeBoard(int rows, int columns) {
        view.initializeBoard(rows, columns);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gameOver(GameOverState gameOverState, boolean newHighScore) {
        timerRunning = false;
        gameOver = true;
        view.gameOver(gameOverState, this.model.getTime(), newHighScore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTime(long time) {
        view.setTime(time);
    }

    @Override
    public int getMaxBombs(int rows, int columns) {
        return this.model.getMaxBombs(rows, columns);
    }

    @Override
    public int getMinBombs(int rows, int columns) {
        return this.model.getMinBombs(rows, columns);
    }

    // ##### Called By View #####

    /**
     * {@inheritDoc}
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startNewGame(DefaultBoard board) {
        this.prepareNewGame();
        model.newGame(board);
    }
  
    /**
     * {@inheritDoc}
     */
    @Override
    public void startNewGame(BoardConfiguration boardConfiguration) {
        this.prepareNewGame();
        model.newGame(boardConfiguration);
    }

    private void prepareNewGame() {
        timerRunning = false;
        gameOver = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {
        if (!timerRunning && !gameOver) {
            timerRunning = true;
            this.startTimer();
        }
        if (!gameOver) {
            model.tileUpdatedByUser(tileAction, position);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HighScore> getScores() {
        return this.model.usingDefaultBoard() ? model.getScores() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HighScore> getScores(DefaultBoard board) {
        return model.getScores(board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startTimer() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateHighScore(String name) {
        if (!this.model.usingDefaultBoard()) {
            throw new InvalidStateException("Not playing a default board configuration");
        }
        this.model.updateHighScore(name, this.model.getTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cheatToggled(boolean cheat) {
        model.cheatToggled(cheat);
    }

    @Override
    public BoardValidator getBoardValidator() {
        return this.model.getBoardValidator();
    }

    @Override
    public NameValidator getNameValidator() {
        return this.model.getNameValidator();
    }

    @Override
    public void saveBoardConfig(BoardConfiguration configuration) {
        this.model.saveBoardConfig(configuration);
    }

    @Override
    public BoardConfiguration getSavedBoardConfig() {
        return this.model.getSavedBoardConfig();
    }

    @Override
    public void addBombsRemainingListener(ChangeListener<Integer> listener) {
        this.model.addBombsRemainingListener(listener);
    }

    @Override
    public void addBoardListener(BoardListener listener) {
        this.model.addBoardListener(listener);
    }

    public BoardConfiguration getMinBoardConfig() {
        return this.model.getMinBoardConfig();
    }

    public BoardConfiguration getMaxBoardConfig() {
        return this.model.getMaxBoardConfig();
    }
}
