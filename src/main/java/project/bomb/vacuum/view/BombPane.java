/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.bomb.vacuum.view;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileAction;

/**
 *
 * @author delaney.satchwell
 */
public class BombPane extends GridPane{

    private static final int buttonSize = 50;

    public BombPane(Controller controller, int columns, int rows) {
        populateGrid(controller, columns, rows);

        double screenHeight = (BombPane.buttonSize * rows);
        double screenWidth = (BombPane.buttonSize * columns);

    }

    private TileButton makeButton(Controller controller, int row, int column) {
        TileButton button = new TileButton(controller, row, column);
        button.setPrefSize(BombPane.buttonSize, BombPane.buttonSize);

        return button;
    }

    private void populateGrid(Controller controller, int gridColumns, int gridRows) {
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                TileButton button = makeButton(controller, i, j);
                placeButton(button, i, j);
            }

        }
    }

    private void placeButton(TileButton button, int gridColumn, int gridRow) {
        this.add(button, gridColumn, gridRow, 1, 1);
    }
}
