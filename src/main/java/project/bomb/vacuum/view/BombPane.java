/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.bomb.vacuum.view;

import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileStatus;

/**
 *
 * @author delaney.satchwell
 */
class BombPane extends GridPane{

    private final Controller controller;
    private HashMap<String, ViewTile> tiles = new HashMap<>();

    private static final int TILE_SIZE = 25;

    BombPane(Controller controller, int columns, int rows) {
        this.controller = controller;
        populateGrid(controller, columns, rows);

        double height = (BombPane.TILE_SIZE * rows);
        double width = (BombPane.TILE_SIZE * columns);

        this.setMaxSize(width, height);
        this.setMinSize(width, height);
        this.setPrefSize(width, height);
    }

    private void populateGrid(Controller controller, int columns, int rows) {
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                ViewTile viewTile = new ViewTile(TILE_SIZE, TILE_SIZE);
                this.tiles.put(convertPositionToKey(row, column), viewTile);
                this.add(viewTile, column, row, 1, 1);
                placeButton(controller, row, column);
            }
        }
    }

    private void placeButton(Controller controller, int row, int column) {
        TileButton tile = new TileButton(controller, row, column, TILE_SIZE);
        this.setTile(tile, row, column);
    }

    private void placeLabel(String message, int row, int column) {
        Label tile = new Label(message);
        tile.setAlignment(Pos.CENTER);
        this.setTile(tile, row, column);
    }

    private void setTile(Region tile, int row, int column) {
        this.tiles.get(convertPositionToKey(row, column)).setTile(tile);
    }

    private String convertPositionToKey(Position position) {
        return convertPositionToKey(position.row, position.column);
    }

    private String convertPositionToKey(int row, int column) {
        return String.format("%d-%d", row, column);
    }

    void updateTiles(TileStatus[] tileStatuses) {
        for (TileStatus tileStatus : tileStatuses) {
            Position position = tileStatus.position;
            int state = tileStatus.state.ordinal();
            if (state < 9) {
                String message = "";
                if (state > 0) {
                    message += state;
                }
                placeLabel(message, position.row, position.column);
            } else {
                switch (tileStatus.state) {
                    case BOMB:
                        placeLabel("B", position.row, position.column);
                        break;
                    case FLAGGED:
                        if (this.getViewTile(position).getTile() instanceof Label) {
                            placeButton(controller, position.row, position.column);
                        }
                        flagTile(position);
                        break;
                    case NOT_CLICKED:
                        if (this.getViewTile(position).getTile() instanceof Label) {
                            placeButton(controller, position.row, position.column);
                        }
                        unFlagTile(position);
                        break;
                    default:
                        System.out.println("State not yet supported: " + tileStatus.state);
                }
            }
        }
    }

    private void flagTile(Position position) {
        TileButton tile = (TileButton) this.getViewTile(position).getTile();
//        tile.highlight(true);
        tile.setFlag(true);
    }

    private void unFlagTile(Position position) {
        TileButton tile = (TileButton) this.getViewTile(position).getTile();
        tile.highlight(false);
        tile.setFlag(false);
    }

    private ViewTile getViewTile(Position position) {
        return this.getViewTile(position.row, position.column);
    }

    private ViewTile getViewTile(int row, int column) {
        return this.tiles.get(convertPositionToKey(row, column));
    }
}
