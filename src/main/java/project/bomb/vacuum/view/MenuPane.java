package project.bomb.vacuum.view;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.DefaultBoard;

/**
 * @author delaney.satchwell
 */
class MenuPane extends VBox {

    static final double BUTTON_WIDTH = 80;

    private Controller controller;

    MenuPane(Controller controller) {
        this.controller = controller;
        Button easyButton = new Button("Easy");
        Button hardButton = new Button("Hard");
        Button mediumButton = new Button("Medium");
        Button customButton = new Button("Custom");
        getChildren().addAll(
                easyButton,
                mediumButton,
                hardButton
        );
        this.setButtonActionAndSize(easyButton, DefaultBoard.EASY);
        this.setButtonActionAndSize(mediumButton, DefaultBoard.INTERMEDIATE);
        this.setButtonActionAndSize(hardButton, DefaultBoard.EXPERT);
    }

    private void setButtonActionAndSize(Button button, DefaultBoard board) {
        button.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                controller.startNewGame(board);
            }
        });
        button.setMinWidth(BUTTON_WIDTH);
        button.setMaxWidth(BUTTON_WIDTH);
        button.setPrefWidth(BUTTON_WIDTH);
    }

    //custom handle, eventually maybe?
//        customButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
//            @Override
//            public void handle(MouseEvent t){
//                if (t.getButton() == MouseButton.PRIMARY){
//                    controller.setsomethingtosomething);
//                }
//            }
//        });
}
