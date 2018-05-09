package BombermanInda;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class GameMenu {
/**
    public void gameMenu(Stage primaryStage, Pane pane){
        //size of window
        int graphicsWindowX = 500;
        int graphicsWindowY = 500;

        //init graphic window
        primaryStage.setTitle("Bomberman");
        pane = new Pane();
        pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));
        Render render = new Render(pane, primaryStage);

        //Makes a button that starts the game
        Button startButton = new Button("Start");
        pane.getChildren().add(startButton);
        startButton.relocate(250, 300);
        ImageView startButtonImage = new ImageView("BombermanInda/Images/MainCharFront.png");
        startButton.setGraphic((Node) startButtonImage);
        startButton.setOnMousePressed(e -> startGame(primaryStage));

        //Makes a button that exits the program
        Button quitButton = new Button("Quit");
        pane.getChildren().add(quitButton);
        quitButton.relocate(250, 100);
        ImageView quitButtonImage = new ImageView("BombermanInda/Images/MainCharBack.png");
        quitButton.setGraphic((Node) quitButtonImage);
        quitButton.setOnMousePressed(e -> System.exit(1));

        primaryStage.show();

*/


}

