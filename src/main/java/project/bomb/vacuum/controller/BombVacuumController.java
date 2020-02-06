package project.bomb.vacuum.controller;

import project.bomb.vacuum.*;
import project.bomb.vacuum.exceptions.InvalidBoardConfiguration;
import project.bomb.vacuum.exceptions.InvalidStateException;
import project.bomb.vacuum.model.BasicModel;
import project.bomb.vacuum.view.GUI;

public class BombVacuumController implements Controller {

    private final Model model;
    private View view;
    private Timer timer;

    private DefaultBoard defaultBoard;

    public static void main(String[] args) {
        new BombVacuumController();
    }

    public BombVacuumController() {
        this.model = new BasicModel(this);
        GUI.setController(this);
        GUI.setStartup(() -> {
            model.newGame(DefaultBoard.INTERMEDIATE);
        });
        GUI.launchGUI();
    }

    public void setView(View view) {
        this.view = view;
    }

    private void readyNewGame() {
        this.timer = model.createTimer();
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
        model.newGame(board);
    }

    @Override
    public void startNewGame(BoardConfiguration boardConfiguration) throws InvalidBoardConfiguration {
        this.defaultBoard = null;
        model.newGame(boardConfiguration);
    }

    @Override
    public BoardConfiguration getDefaultBoardConfiguration(DefaultBoard board) {
        return model.getDefaultBoardConfiguration(board);
    }

    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {
        model.tileUpdatedByUser(tileAction, position);
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
