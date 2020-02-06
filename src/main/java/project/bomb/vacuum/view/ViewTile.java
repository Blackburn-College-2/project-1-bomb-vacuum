package project.bomb.vacuum.view;

import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class ViewTile extends StackPane {

    private Region tile;
    private final double width;
    private final double height;

    ViewTile(double width, double height) {
        this.width = width;
        this.height = height;
        this.setSize(this);
        this.setAlignment(Pos.CENTER);
    }

    void setTile(Region tile) {
        this.setSize(tile);
        this.getChildren().remove(this.tile);
        this.getChildren().add(tile);
        this.tile = tile;
    }

    private void setSize(Region region) {
        region.setMaxSize(width, height);
        region.setMinSize(width, height);
        region.setPrefSize(width, height);
    }

    Region getTile() {
        return this.tile;
    }
}
