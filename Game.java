package BombermanInda;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
//https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835

public class Game extends Application {

private Pane pane;
private GameMenu menu;

//NOTE graphicswindow must be evenly divisible by their respective numGrid
private int graphicsWindowX = 1280; // Window size 16:9
private int graphicsWindowY = 720; // Window size
private int numGridX = 32; //16:9
private int numGridY = 18;
private int numCrates = 180;

    @Override
    public void start(Stage primaryStage) {
        gameMenu(primaryStage);
    }

    public void gameMenu(Stage primaryStage){

        //init graphic window
        primaryStage.setTitle("Bomberman");

        pane = new Pane();
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));
        Render render = new Render(pane, primaryStage, this);

        ImageView background = new ImageView("BombermanInda/Images/Blue.jpg");
        pane.getChildren().add(background);

        ImageView bombermanLogo = new ImageView("BombermanInda/Images/BomberManTitle.png");
        pane.getChildren().add(bombermanLogo);
        bombermanLogo.setFitHeight(71);
        bombermanLogo.setFitWidth(256);
        bombermanLogo.relocate(graphicsWindowX/3, graphicsWindowY/10);


        // Makes a button that starts the game
        Button startButton = new Button("Start");
        pane.getChildren().add(startButton);
        startButton.relocate(graphicsWindowX/3, graphicsWindowY/5);
        ImageView startButtonImage = new ImageView("BombermanInda/Images/MainCharFront.png");
        startButton.setGraphic((Node) startButtonImage);
        startButton.setOnMousePressed(e -> startGame(primaryStage));

        // Makes a button that exits the program
        Button quitButton = new Button("Quit");
        pane.getChildren().add(quitButton);
        quitButton.relocate(graphicsWindowX/3, graphicsWindowY/3);
        ImageView quitButtonImage = new ImageView("BombermanInda/Images/MainCharBack.png");
        quitButton.setGraphic((Node) quitButtonImage);
        quitButton.setOnMousePressed(e -> System.exit(1));

        // Makes a button that takes takes you to the options
        Button optionsButton = new Button("Options");
        pane.getChildren().add(optionsButton);
        optionsButton.relocate(graphicsWindowX/3,  graphicsWindowY/2);
        ImageView optionsButtonImage = new ImageView("BombermanInda/Images/MainCharRight.png");
        optionsButton.setGraphic((Node) optionsButtonImage);
        optionsButton.setOnMousePressed(e -> optionsMenu(primaryStage));

        primaryStage.show();


    }

    public void optionsMenu(Stage primaryStage) {
        //init graphic window
        primaryStage.setTitle("Bomberman");
        pane = new Pane();
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));
        Render render = new Render(pane, primaryStage, this);
    }

    public void startGame(Stage primaryStage) {
        //init graphic window
        primaryStage.setTitle("Bomberman");
        pane = new Pane();
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));
        Render render = new Render(pane, primaryStage, this);

        //init background
        Image background = new Image( "BombermanInda/Images/Grey.png" );

        //init world
        // TODO fix so it will be scaleable.
        World world = new World(numGridX, numGridY, render, numCrates , background); //create playfield

        //create main char
        Node mainCharNode = render.createGraphicsEntity(Render.GraphicsObjects.MAINCHARACTER); //create node for char
        Character mainChar = new Character(mainCharNode,0,0, true, true, render, world, pane);   //create char on (0,0)
        world.addMovingObject(mainChar);
        //activate movement
        mainChar.testControls(primaryStage, render);

        //create moving obj dummy
        Node dummyNode1 = render.createGraphicsEntity(Render.GraphicsObjects.SECONDCHARACTER); //TODO change
        Character dummyChar1 = new Character(dummyNode1,graphicsWindowX-(graphicsWindowX/numGridX),graphicsWindowY-(graphicsWindowY/numGridY), true,true, render, world, pane);
        world.addMovingObject(dummyChar1);

        dummyChar1.secondPlayerControl(primaryStage, render);


        
        
        //draw background
        render.drawBackground(world);

        //draw all map objects based on world matrix
        render.drawAllMapObjects(world);

        primaryStage.show();

        //add all from world to map

        // Gameloop
        final LongProperty lastUpdateTime = new SimpleLongProperty(0);

        new AnimationTimer(){

            @Override
            public void handle(long timestamp) {
                long elapsedTimeMs = (timestamp - lastUpdateTime.get())/100000000000l;
                //move all moveable considering potential collision
                world.moveAllMoveable(elapsedTimeMs);
            }

        }.start();


        /**
        //Exits from the game to the menu when ESC key is pressed
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode .ESCAPE) {
                gameMenu(primaryStage);
                }
            }
        });
        */

    }

    public void endScreen (Stage primaryStage) {

    }

    public  int getGraphicsWindowX() {
        return graphicsWindowX;
    }

    public int getGraphicsWindowY() {
        return graphicsWindowY;
    }

    public void setGraphicsWindowX(int newX) {
        graphicsWindowX = newX;
    }

    public void setGraphicsWindowY(int newY) {
        graphicsWindowY = newY;
    }

    public int getNumGridX(){
        return numGridX;
    }
    
    public int getNumGridY(){
        return numGridY;
    }

}
    

