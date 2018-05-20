package BombermanInda;

import java.util.Timer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

private static Pane pane;
private static Stage primaryStage;
private static World world;
private static Render render;
private static AnimationTimer gameLoop;


//NOTE graphicswindow must be evenly divisible by their respective numGrid, and numgrid should be odd to allow correct wall placement
private int graphicsWindowX = 1271; // Window size 16:9
private int graphicsWindowY = 714; // Window size
private int numGridX = 31; 
private int numGridY = 17;
private int numCrates = 250;

  //starting speed for characters
private double speed = Math.min(graphicsWindowX/numGridX, graphicsWindowY/numGridY)/20; 



    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.pane = new Pane();
        primaryStage.setTitle("Bomberman");
        primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));
        render = new Render(pane, primaryStage, this);
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        gameMenu(primaryStage, pane);
    }

    public void gameMenu(Stage primaryStage, Pane pane){

        //init graphic window

        //pane = new Pane();
        //primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));


        ImageView background = new ImageView("BombermanInda/Images/Blue.jpg");
        pane.getChildren().add(background);

        ImageView bombermanLogo = new ImageView("BombermanInda/Images/BomberManTitle.png");
        pane.getChildren().add(bombermanLogo);
        bombermanLogo.setPreserveRatio(true);
        bombermanLogo.setFitWidth(graphicsWindowX/6);
        bombermanLogo.relocate((graphicsWindowX/2)-bombermanLogo.getFitWidth()/2, graphicsWindowY/10);


        // Makes a button that starts the game
        ImageView startButtonImage = new ImageView("BombermanInda/Images/MainCharFront.png");
        Button startButton = createButton(pane,"Start", startButtonImage, graphicsWindowX, graphicsWindowY, 5);



        // Makes a button that exits the program
        ImageView quitButtonImage = new ImageView("BombermanInda/Images/MainCharBack.png");
        Button quitButton = createButton(pane, "Quit", quitButtonImage, graphicsWindowX, graphicsWindowY, 3);
        quitButton.setOnMousePressed(e -> System.exit(1));

        // Makes a button that takes takes you to the options

        ImageView helpButtonImage = new ImageView("BombermanInda/Images/MainCharRight.png");
        Button helpButton = createButton(pane, "Help", helpButtonImage, graphicsWindowX, graphicsWindowY, 2);
        helpButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().removeAll();
                helpMenu(primaryStage, pane);
            }
        });




        startButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().remove(0, pane.getChildren().size());
                startGame(primaryStage,pane);
            }
        });


        primaryStage.show();


    }

    public void helpMenu(Stage primaryStage, Pane pane) {
        //init graphic window
        //primaryStage.setTitle("Bomberman");
        //pane = new Pane();
        //pane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));
        //Render render = new Render(pane, primaryStage, this);


        // Makes a button that goes back to the menu
        ImageView backButtonImage = new ImageView("BombermanInda/Images/MainCharBack.png");
        Button backButton = createButton(pane, "Back to menu", backButtonImage, graphicsWindowX, graphicsWindowY, 2);
        backButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().removeAll();
                gameMenu(primaryStage, pane);
            }
        });
    }

    public void startGame(Stage primaryStage, Pane pane) {

        //init background
        Image background = new Image( "BombermanInda/Images/Grey.png" );
        //init world
        // TODO fix so it will be scaleable.
        world = new World(numGridX, numGridY, render, numCrates); //create playfield


        //create main char
        Node mainCharNode = render.createGraphicsEntity(Render.GraphicsObjects.MAINCHARACTER); //create node for char
        Character mainChar = new Character(mainCharNode,0,0, speed, Math.min(graphicsWindowX/numGridX, graphicsWindowY/numGridY),
                true, true, render, world, pane, this);   //create char on (0,0)
        world.addMovingObject(mainChar);



        //create second char
        Node secondCharNode = render.createGraphicsEntity(Render.GraphicsObjects.SECONDCHARACTER);
        Character secondChar = new Character(secondCharNode,graphicsWindowX-(graphicsWindowX/numGridX),graphicsWindowY-(graphicsWindowY/numGridY),
                speed, Math.min(graphicsWindowX/numGridX, graphicsWindowY/numGridY),
                true,true, render, world, pane, this);



        world.addMovingObject(secondChar);


        //activate movement
        new CharacterMovement(this, primaryStage, render,mainChar, secondChar);


        //draw background
        render.drawBackground(background);

        //draw all map objects based on world matrix
        render.drawAllMapObjects(world);

        primaryStage.show();

        //add all from world to map

        // Gameloop
        gameLoop = new AnimationTimer(){
            private long lastUpdateTime = System.nanoTime();
           
            @Override


            public void handle(long timestamp) {     
                //calculate time since last frame
                long elapsedTimeMs = (timestamp-lastUpdateTime)/10000l;
                

                //move all moveable considering potential collision
                world.moveAllMoveable(elapsedTimeMs);
                
                
                //get new old time
                lastUpdateTime = System.nanoTime();
                
                
            }

        };
        gameLoop.start();


    }

    public void endScreen (Stage primaryStage, Pane pane) {


        // Makes a button that goes back to the menu
        ImageView backButtonImage = new ImageView("BombermanInda/Images/MainCharBack.png");
        Button backButton = createButton(pane,"Back to menu", backButtonImage, graphicsWindowX, graphicsWindowY, 2);
        backButton.setOnMousePressed(e -> gameMenu(primaryStage, pane));

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Pane getPane() { return pane; }

    public World getWorld() {
        return world;
    }

    public void stopGameLoop() {
        gameLoop.stop();
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


    //ends game when one player dies
    public void endGame(){
        pane.getChildren().remove(0, pane.getChildren().size());
        getWorld().clearWorld();
        stopGameLoop();
    }

    public Button createButton(Pane pane, String text, ImageView image, int graphicsWindowX, int graphicsWindowY, int yPos){

        Button button = new Button(text);
        pane.getChildren().add(button);
        image.setPreserveRatio(true);
        image.setFitWidth(graphicsWindowX/10);
        image.setFitHeight(graphicsWindowY/10);
        button.setGraphic(image);
        button.setPrefSize(graphicsWindowX/9, graphicsWindowY/9);
        button.relocate((graphicsWindowX/2)-button.getPrefWidth()/2, graphicsWindowY/yPos);
        return button;
    }

}
    

