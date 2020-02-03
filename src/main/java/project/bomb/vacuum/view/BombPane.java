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

/**
 *
 * @author delaney.satchwell
 */
public class BombPane extends GridPane{

    private static final int buttonSize = 50;

    public BombPane(int columns, int rows) {

        populateGrid(columns, rows);

        double screenHeight = (BombPane.buttonSize * rows);
        double screenWidth = (BombPane.buttonSize * columns);

    }

    private Button makeButton() {
        Button button = new Button();
        button.setPrefSize(BombPane.buttonSize, BombPane.buttonSize);
        return button;
    }

    private void populateGrid(int gridColumns, int gridRows) {
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                Button button = makeButton();
                placeButton(button, i, j);
            }

        }
    }

    private void placeButton(Button button, int gridColumn, int gridRow) {
        this.add(button, gridColumn, gridRow, 1, 1);
    }
}
