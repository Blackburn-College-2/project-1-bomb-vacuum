package project.bomb.vacuum.view;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.Position;
import project.bomb.vacuum.TileAction;

class TileButton extends Button {

    final int row;
    final int column;

    TileButton(Controller controller, int row, int column) {
        this.row = row;
        this.column = column;

        this.setOnMousePressed(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    controller.tileUpdatedByUser(TileAction.REVEAL_TILE, new Position(row, column));
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

}
