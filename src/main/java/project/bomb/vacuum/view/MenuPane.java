/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.bomb.vacuum.view;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import project.bomb.vacuum.Controller;
import project.bomb.vacuum.DefaultBoard;

/**
 *
 * @author delaney.satchwell
 */
public class MenuPane extends VBox {
    
        private Controller controller;
        Button easyButton = new Button("Easy");
        Button hardButton = new Button("Hard");
        Button mediumButton = new Button("Medium");
        Button customButton = new Button("Custom");
        
        
        public MenuPane(Controller controller){
            this.controller = controller;
         getChildren().addAll(easyButton, mediumButton,
                hardButton, customButton);
         easyHandle();
         mediumHandle();
         hardHandle();
         
        }
        
        public void easyHandle(){
                  easyButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.PRIMARY) {
                    controller.startNewGame(DefaultBoard.EASY);
                }
            }

        });
        }
        
        public void mediumHandle(){
            mediumButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.PRIMARY) {
                    controller.startNewGame(DefaultBoard.INTERMEDIATE);
                }
            }
        });

        }
        public void hardHandle(){
            hardButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.PRIMARY) {
                    controller.startNewGame(DefaultBoard.EXPERT);
                }
            }
        });
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
