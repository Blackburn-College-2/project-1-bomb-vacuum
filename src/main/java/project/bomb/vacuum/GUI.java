package project.bomb.vacuum;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void launchGUI() {
        launch();
    }
    
    private static final int buttonSize = 50;
    
    @Override
    public void start(Stage stage) throws Exception {
        Button[] gridbuttons = new Button[50];
        int gridColumns = 10;
        int gridRows = 10;
        GridPane gridPane = new GridPane();
        populateGrid(gridPane, gridColumns, gridRows);
        
        double screenHeight = (GUI.buttonSize * gridRows);
        double screenWidth = (GUI.buttonSize * gridColumns);
        
        
        Scene scene = new Scene(gridPane, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
    }

    private Button makeButton() {
        Button button = new Button();
        button.setPrefSize(GUI.buttonSize,GUI.buttonSize);
        return button;
    }
    
    private void populateGrid(GridPane gridpane, int gridColumns, int gridRows){
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                Button button = makeButton();
                placeButton(button, gridpane, i, j);
            }
            
        }
    }
    
    private void placeButton(Button button, GridPane gridpane, int gridColumn, int gridRow){
        gridpane.add(button, gridColumn, gridRow, 1, 1);
    }
}
