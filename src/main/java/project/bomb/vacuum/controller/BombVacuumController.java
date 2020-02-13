package project.bomb.vacuum.controller;

import project.bomb.vacuum.*;
import project.bomb.vacuum.exceptions.InvalidBoardConfiguration;
import project.bomb.vacuum.exceptions.InvalidStateException;
import project.bomb.vacuum.model.BasicModel;
import project.bomb.vacuum.view.GUI;

/**
 * 
 * @author cordell.stocker
 */
public class BombVacuumController implements Controller {

    private final Model model;
    private View view;
    private Timer timer = new BasicTimer(this);
    private boolean timerRunning = false;
    private boolean gameOver = false;

    private DefaultBoard defaultBoard;

    public static void main(String[] args) {
        new BombVacuumController();
    }

    public BombVacuumController() {
        this.model = new BasicModel(this);
        GUI.setController(this);
        GUI.setStartup(() -> model.newGame(DefaultBoard.INTERMEDIATE));
        GUI.launchGUI();
    }

    public void setView(View view) {
        this.view = view;
    }

    // ##### Called By Model #####


    @Override
    public void initializeBoard(int rows, int columns) {
        view.initializeBoard(rows, columns);
    }


    @Override
    public void setTileStatuses(TileStatus[] statuses) {
        view.setTileStatuses(statuses);
    }

    @Override
    public void gameOver(GameOverState gameOverState) {
        timer.stopTimer();
        timerRunning = false;
        gameOver = true;
        view.gameOver(gameOverState, this.timer.getTime());
    }

    @Override
    public void setTime(long time) {
        view.setTime(time);
    }

    // ##### Called By View #####

    @Override
    public void startNewGame(DefaultBoard board) {
        this.defaultBoard = board;
        this.prepareNewGame();
        model.newGame(board);
    }
    
    @Override
    public void startNewGame(BoardConfiguration boardConfiguration) throws InvalidBoardConfiguration {
        this.defaultBoard = null;
        this.prepareNewGame();
        model.newGame(boardConfiguration);
    }

    private void prepareNewGame() {
        timerRunning = false;
        gameOver = false;
        timer.resetTimer();
    }

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

    @Override
    public HighScores getScores() {
        if (defaultBoard != null) {
            return model.getScores(defaultBoard);
        } else {
            return null;
        }
    }


    @Override
    public HighScores getScores(DefaultBoard board) {
        return model.getScores(board);
    }

    @Override
    public void startTimer() {
        this.timer.startTimer();
    }

    @Override
    public void updateHighScore(String name) {
        if (this.defaultBoard == null) {
            throw new InvalidStateException("Not playing a default board configuration");
        }
        this.model.updateHighScore(this.defaultBoard, name, timer.getTime());
    }

    @Override
    public void cheatToggled(boolean cheat) {
        model.cheatToggled(cheat);
    }

}
