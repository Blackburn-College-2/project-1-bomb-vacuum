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

    private DefaultBoard defaultBoard;

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
    public void setTileStatuses(TileStatus[] statuses) {
        view.setTileStatuses(statuses);
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
        this.defaultBoard = board;
        this.prepareNewGame();
        model.newGame(board);
    }
  
    /**
     * {@inheritDoc}
     */
    @Override
    public void startNewGame(BoardConfiguration boardConfiguration) {
        this.defaultBoard = null;
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
        if (defaultBoard != null) {
            return model.getScores(defaultBoard);
        } else {
            return null;
        }
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
        if (this.defaultBoard == null) {
            throw new InvalidStateException("Not playing a default board configuration");
        }
        this.model.updateHighScore(this.defaultBoard, name, this.model.getTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cheatToggled(boolean cheat) {
        model.cheatToggled(cheat);
    }

    @Override
    public void setBombCounter(int bombs) {
        this.view.setBombCounter(bombs);
    }

}
