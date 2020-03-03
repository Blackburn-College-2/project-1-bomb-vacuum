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
    private HashMap<String, ViewTile> tiles = new HashMap<>();

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
    /**
     * places buttons on grid seen by the user
     * @param controller controller
     * @param columns number of columns 
     * @param rows number of rows
     */
    private void populateGrid(Controller controller, int columns, int rows) {
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                ViewTile viewTile = new ViewTile(controller, row, column, TILE_SIZE);
                this.tiles.put(convertPositionToKey(row, column), viewTile);
                this.add(viewTile, column, row, 1, 1);
                placeButton(row, column);
            }
        }
    }
    /**
     * places the button on the correct row and column
     * @param row specific row
     * @param column specific column
     */
    private void placeButton(int row, int column) {
        Button tile = new Button();
        this.setTile(tile, row, column);
    }
    /**
     * places a label in the correct place on the board
     * @param message message being displayed
     * @param row specific row
     * @param column specific column
     */
    private void placeLabel(String message, int row, int column) {
        Label tile = new Label(message);
        tile.setAlignment(Pos.CENTER);
        this.setTile(tile, row, column);
    }
    /**
     * updates a specific tile
     * @param tile the tile being changed
     * @param row specific row
     * @param column specific column
     */
    private void setTile(Labeled tile, int row, int column) {
        ViewTile viewTile = this.tiles.get(convertPositionToKey(row, column));
        viewTile.highlight(false);
        viewTile.setTile(tile);
    }

    private String convertPositionToKey(Position position) {
        return convertPositionToKey(position.row, position.column);
    }
    /**
     * 
     * @param row
     * @param column
     * @return 
     */
    private String convertPositionToKey(int row, int column) {
        return String.format("%d-%d", row, column);
    }
    /**
     * decides what the tile should do based on the given state of that tile
     * @param tileStatuses status of a tile
     */
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
    /**
     * flags a tile
     * @param position the given position of the tile 
     */
    private void flagTile(Position position) {
        ViewTile tile = this.getViewTile(position);
        tile.flag(true);
    }
    /**
     * unflags a tile
     * @param position the given position of the tile
     */
    private void unFlagTile(Position position) {
        ViewTile tile = this.getViewTile(position);
        tile.flag(false);
    }
    /**
     * highlights a tile
     * @param position the given position of the tile
     */
    private void highlightTile(Position position) {
        ViewTile tile = this.getViewTile(position);
        tile.highlight(true);
    }
    /**
     * dehighlights a tile
     * @param position the given position of the tile
     */
    private void deHighlightTile(Position position) {
        ViewTile tile = this.getViewTile(position);
        tile.highlight(false);
    }
    /**
     * 
     * @param position
     * @return 
     */
    private ViewTile getViewTile(Position position) {
        return this.getViewTile(position.row, position.column);
    }
    /**
     * 
     * @param row
     * @param column
     * @return 
     */
    private ViewTile getViewTile(int row, int column) {
        return this.tiles.get(convertPositionToKey(row, column));
    }
}
