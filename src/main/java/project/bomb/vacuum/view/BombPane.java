package project.bomb.vacuum.view;

import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileStatus;

class BombPane extends GridPane {

    private final Controller controller;
    private HashMap<String, ViewTile2> tiles = new HashMap<>();

    private static final int TILE_SIZE = 30;

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
                ViewTile2 viewTile = new ViewTile2(controller, row, column, TILE_SIZE);
                this.tiles.put(convertPositionToKey(row, column), viewTile);
                this.add(viewTile, column, row, 1, 1);
                placeButton(row, column);
            }
        }
    }

    private void placeButton(int row, int column) {
        Button tile = new Button();
        this.setTile(tile, row, column);
    }

    private void placeLabel(String message, int row, int column) {
        Label tile = new Label(message);
        tile.setAlignment(Pos.CENTER);
        this.setTile(tile, row, column);
    }

    private void setTile(Labeled tile, int row, int column) {
        ViewTile2 viewTile = this.tiles.get(convertPositionToKey(row, column));
        viewTile.highlight(false);
        viewTile.setTile(tile);
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
            // State is a number or EMPTY
            if (state < 9) {
                String message = state == 0 ? "" : String.valueOf(state);
                placeLabel(message, position.row, position.column);
            } else {
                switch (tileStatus.state) {
                    case BOMB:
                        placeLabel("B", position.row, position.column);
                        break;
                    case FLAGGED:
                        placeButton(position.row, position.column);
                        flagTile(position);
                        break;
                    case NOT_CLICKED:
                        placeButton(position.row, position.column);
                        deHighlightTile(position);
                        break;
                    case HIGHLIGHTED:
                        highlightTile(position);
                        break;
                    default:
                        System.out.println("State not yet supported: " + tileStatus.state);
                }
            }
        }
    }

    private void flagTile(Position position) {
        ViewTile2 tile = this.getViewTile(position);
        tile.flag(true);
    }

    private void unFlagTile(Position position) {
        ViewTile2 tile = this.getViewTile(position);
        tile.flag(false);
    }

    private void highlightTile(Position position) {
        ViewTile2 tile = this.getViewTile(position);
        tile.highlight(true);
    }

    private void deHighlightTile(Position position) {
        ViewTile2 tile = this.getViewTile(position);
        tile.highlight(false);
    }

    private ViewTile2 getViewTile(Position position) {
        return this.getViewTile(position.row, position.column);
    }

    private ViewTile2 getViewTile(int row, int column) {
        return this.tiles.get(convertPositionToKey(row, column));
    }
}
