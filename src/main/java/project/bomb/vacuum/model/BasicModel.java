package project.bomb.vacuum.model;

import project.bomb.vacuum.BoardConfiguration;
import project.bomb.vacuum.DefaultBoard;
import project.bomb.vacuum.HighScores;
import project.bomb.vacuum.Model;
import project.bomb.vacuum.TileAction;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.Timer;
import project.bomb.vacuum.exceptions.InvalidBoardConfiguration;

public class BasicModel implements Model {
    @Override
    public void tileUpdatedByUser(TileAction tileAction, Position position) {
        throw new UnsupportedOperationException("Method not yet implemented.");
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

    public void newGame(int rows, int columns, int bombs) {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    @Override
    public void updateHighScore(DefaultBoard board, String name, long time) {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    public void getHighScores() {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    @Override
    public void cheatToggled(boolean toggle) {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    @Override
    public void newGame(BoardConfiguration boardConfiguration) throws InvalidBoardConfiguration {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HighScores getScores(DefaultBoard board) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
