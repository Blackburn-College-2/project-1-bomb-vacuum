package project.bomb.vacuum.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileAction;

class TileButton extends StackPane {

    private Button button = new Button();
    private boolean isFlagged = false;
    private Pane highlighter;

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

    void setFlag(boolean isFlagged) {
        this.isFlagged = isFlagged;
        if (isFlagged) {
            this.button.setText("F");
        } else {
            this.button.setText("");
        }
    }

    void highlight(boolean highlight) {
        if (highlight) {
            this.button.setStyle("-fx-border-color: rgba(255, 0, 0, .9)");
            this.getChildren().add(0, highlighter);
        } else {
            this.button.setStyle("");
            this.getChildren().remove(highlighter);
        }
    }

}
