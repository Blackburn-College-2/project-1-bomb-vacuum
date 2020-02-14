package project.bomb.vacuum.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileAction;

/**
 * A {@link Button} that notifies a {@link Controller} when acted
 * upon.
 */
class TileButton extends StackPane {

    private Button button = new Button();
    private boolean isFlagged = false;
    private Pane highlighter;

    /**
     * @param controller the {@link Controller} to link to
     * @param row        the row for this.
     * @param column     the column for this.
     * @param size       the size for this's width and height.
     */
    TileButton(Controller controller, int row, int column, double size) {
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(this.button);
        this.highlighter = new Pane();
        this.highlighter.setMaxSize(size * 0.8, size * 0.8);
        this.highlighter.setMinSize(size * 0.8, size * 0.8);
        this.highlighter.setPrefSize(size * 0.8, size * 0.8);
        this.highlighter.setStyle("-fx-background-color: rgba(255, 255, 0, 0.3)");
        this.button.setMinSize(size, size);
        this.button.setMaxSize(size, size);
        this.button.setPrefSize(size, size);
        TileButton that = this;
        this.button.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    if (!that.isFlagged) {
                        controller.tileUpdatedByUser(TileAction.REVEAL_TILE, new Position(row, column));
                    }
                    break;
                case SECONDARY:
                    controller.tileUpdatedByUser(TileAction.FLAG_TILE, new Position(row, column));
                    break;
                case MIDDLE:
                    controller.tileUpdatedByUser(TileAction.HIGHLIGHT, new Position(row, column));
            }
        });
        this.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                controller.tileUpdatedByUser(TileAction.DEHIGHLIGHT, new Position(row, column));
            }
        });
    }

    /**
     * Sets if this tile has been flagged or not.
     * <p>
     * If this is flagged, then left-clicking functionality is disabled.
     *
     * @param isFlagged true to flag this tile, otherwise false.
     */
    void setFlag(boolean isFlagged) {
        Platform.runLater(() -> {
            this.isFlagged = isFlagged;
            if (isFlagged) {
                this.button.setText("F");
            } else {
                this.button.setText("");
            }
        });
    }

    /**
     * Sets if this tile has been highlighted or not.
     *
     * @param highlight true to highlight this tile, false otherwise.
     */
    void highlight(boolean highlight) {
        Platform.runLater(() -> {
            if (highlight) {
                this.button.setStyle("-fx-border-color: rgba(255, 0, 0, .9)");
                this.getChildren().add(0, highlighter);
            } else {
                this.button.setStyle("");
                this.getChildren().remove(highlighter);
            }
        });
    }

}
