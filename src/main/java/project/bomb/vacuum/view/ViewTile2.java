package project.bomb.vacuum.view;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileAction;

public class ViewTile2 extends StackPane {

    private final static String DEFAULT_STYLE = "-fx-border-color: rgba(100, 100, 100, 0.2); -fx-stroke-width: 0.3px";
    private final static String HIGHLIGHT_STYLE = "-fx-border-color: rgba(255, 0, 0, .9)";
    private final Controller controller;
    private final Position position;
    private final double size;
    private Region tile;

    public ViewTile2(Controller controller, int row, int column, double size) {
        this.controller = controller;
        this.position = new Position(row, column);
        this.size = size;

        this.setStyle(DEFAULT_STYLE);
        this.setSize(this, size);
    }

    public void setTile(Region tile) {
        tile.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    controller.tileUpdatedByUser(TileAction.REVEAL_TILE, position);
                    break;
                case MIDDLE:
                    controller.tileUpdatedByUser(TileAction.HIGHLIGHT, position);
                    break;
                case SECONDARY:
                    controller.tileUpdatedByUser(TileAction.FLAG_TILE, position);
                    break;
            }
        });
        this.setSize(tile, this.size);
        this.getChildren().remove(this.tile);
        this.getChildren().add(tile);
    }

    private void setSize(Region region, double size) {
        region.setMinSize(size, size);
        region.setPrefSize(size, size);
        region.setMaxSize(size, size);
    }

    void highlight(boolean highlight) {
        Platform.runLater(() -> {
            if (highlight) {
                this.setStyle(HIGHLIGHT_STYLE);
            } else {
                this.setStyle(DEFAULT_STYLE);
            }
        });
    }
}
