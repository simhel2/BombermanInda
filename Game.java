package BombermanInda;

import java.util.Timer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
//https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835

/**
 *  Game is the application class where everything runs
 */

public class Game extends Application {

private static Pane pane;
private static Stage primaryStage;
private static World world;
private static Render render;
private static AnimationTimer gameLoop;
private static CharacterMovement characterMovement;


//NOTE graphicswindow must be evenly divisible by their respective numGrid, and numgrid should be odd to allow correct wall placement
private int graphicsWindowX = 1271; // Window size 16:9
private int graphicsWindowY = 714; // Window size
private int numGridX = 31; 
private int numGridY = 17;
private int numCrates = 250;

//starting speed for characters
private double speed = Math.min(graphicsWindowX/numGridX, graphicsWindowY/numGridY)/20; 




    /**
     *  Is the first thing that happens when Bomber is run, creates the basics for handling the window
     * @param primaryStage which stage to use
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.pane = new Pane();
        primaryStage.setTitle("Bomberman");
        primaryStage.setScene(new Scene(pane, graphicsWindowX ,graphicsWindowY));
        render = new Render(pane, primaryStage, this);
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // Goes directly into the game menu
        gameMenu(primaryStage, pane);
    }


    /**
     * The game menu screen
     * @param primaryStage which stage to use
     * @param pane which graphicspane to display in
     */
    public void gameMenu(Stage primaryStage, Pane pane){


        ImageView background = new ImageView("BombermanInda/Images/Blue.jpg");
        pane.getChildren().add(background);

        ImageView bombermanLogo = new ImageView("BombermanInda/Images/BomberManTitle.png");
        pane.getChildren().add(bombermanLogo);
        bombermanLogo.setPreserveRatio(true);
        bombermanLogo.setFitWidth(graphicsWindowX/4);
        bombermanLogo.relocate((graphicsWindowX/2)-bombermanLogo.getFitWidth()/2, graphicsWindowY/10);


        // Makes a button that exits the program
        ImageView quitButtonImage = new ImageView("BombermanInda/Images/MainCharBack.png");
        Button quitButton = createButton(pane, "Quit", quitButtonImage, graphicsWindowX, graphicsWindowY, 2);
        quitButton.setOnMousePressed(e -> System.exit(1));

        // Makes a button that takes takes you to the help screen
        ImageView helpButtonImage = new ImageView("BombermanInda/Images/MainCharRight.png");
        Button helpButton = createButton(pane, "Help", helpButtonImage, graphicsWindowX, graphicsWindowY, 3);
        helpButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().remove(0, pane.getChildren().size());
                helpScreen(primaryStage, pane);
            }
        });

        // Makes a button that starts the game
        ImageView startButtonImage = new ImageView("BombermanInda/Images/MainCharFront.png");
        Button startButton = createButton(pane,"Start", startButtonImage, graphicsWindowX, graphicsWindowY, 6);
        startButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().remove(0, pane.getChildren().size());
                startGame(primaryStage,pane);
            }
        });

        // Opens the window with all the info
        primaryStage.show();


    }



    /**
     *     // Starts the game
     * @param primaryStage which stage to use
     * @param pane which graphicspane to display this in
     */
    public void startGame(Stage primaryStage, Pane pane) {

        //init background
        Image background = new Image( "BombermanInda/Images/Grey.png" );
        //init world.
        world = new World(numGridX, numGridY, render, numCrates); //create playfield


        //create main character
        Node mainCharNode = render.createGraphicsEntity(Render.GraphicsObjects.MAINCHARACTER); //create node for char
        Character mainChar = new Character(mainCharNode,0,0, speed, Math.min(graphicsWindowX/numGridX, graphicsWindowY/numGridY),
                true, true, render, world, pane, this, Character.Player.PLAYERONE);   //create char on (0,0)
        world.addMovingObject(mainChar);



        //create second character
        Node secondCharNode = render.createGraphicsEntity(Render.GraphicsObjects.SECONDCHARACTER);
        Character secondChar = new Character(secondCharNode,graphicsWindowX-(graphicsWindowX/numGridX),graphicsWindowY-(graphicsWindowY/numGridY),
                speed, Math.min(graphicsWindowX/numGridX, graphicsWindowY/numGridY),
                true,true, render, world, pane, this, Character.Player.PLAYERTWO);
        world.addMovingObject(secondChar);


        //activate movement
        characterMovement = new CharacterMovement(this, primaryStage, render,mainChar, secondChar);


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



    /**
     * Help screen with information on how to play
     * @param primaryStage which stage to use
     * @param pane which graphicspane to show this in
     */
    public void helpScreen(Stage primaryStage, Pane pane) {

        ImageView background = new ImageView("BombermanInda/Images/Black.png");
        pane.getChildren().add(background);

        Label helpText = new Label();
        helpText.setTextAlignment(TextAlignment.CENTER);
        helpText.setFont(Font.font("Verdana", 15));
        helpText.setTextFill(Color.WHITE);
        helpText.setPrefWidth(graphicsWindowX/2);
        helpText.setText("This is a two-player Bomberman game. \n" +
                "One player uses the arrow keys to move and the dot-key to drop a bomb. \n" +
                "And the other player uses the WASD-keys to move and the T-key to drop a bomb. \n"
                + "If you want to quit the game, press the ESC-key.");
        helpText.relocate((graphicsWindowX/2)-helpText.getPrefWidth()/2, graphicsWindowY/4);
        pane.getChildren().add(helpText);


        // Makes a button that goes back to the menu
        ImageView backButtonImage = new ImageView("BombermanInda/Images/MainCharBack.png");
        Button backButton = createButton(pane, "Menu", backButtonImage, graphicsWindowX, graphicsWindowY, 2);
        backButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.getChildren().removeAll();
                gameMenu(primaryStage, pane);
            }
        });
    }



    /**
     * End screen menu that shows which player lost
     * @param primaryStage what stage to use
     * @param pane which graphicspane to show this in
     * @param player which player lost
     */
    public void endScreen (Stage primaryStage, Pane pane, Character.Player player) {


        ImageView background = new ImageView("BombermanInda/Images/Black.png");
        pane.getChildren().add(background);
        StackPane stack = new StackPane();

        // Checks which player died in order to display the right text
        if(player == Character.Player.PLAYERONE){
            Label playerOneDied = new Label("Player One died. Please return to main menu");
            playerOneDied.setFont(Font.font("Verdana", 20));
            playerOneDied.setPrefWidth(graphicsWindowX/2);
            playerOneDied.setTextFill(Color.WHITE);
            pane.getChildren().add(playerOneDied);
            playerOneDied.relocate((graphicsWindowX/3)-playerOneDied.getWidth()/2,graphicsWindowY/3);
            playerOneDied.setTextAlignment(TextAlignment.CENTER);
        }

        else if(player == Character.Player.PLAYERTWO){
            Label playerTwoDied = new Label("Player Two died. Please return to main menu");
            playerTwoDied.setFont(Font.font("Verdana", 20));
            playerTwoDied.setPrefWidth(graphicsWindowX/2);
            playerTwoDied.setTextFill(Color.WHITE);
            pane.getChildren().add(playerTwoDied);
            playerTwoDied.relocate((graphicsWindowX/3)-playerTwoDied.getWidth()/2,graphicsWindowY/3);
            playerTwoDied.setTextAlignment(TextAlignment.CENTER);
        }


        // Makes a button that goes back to the menu
        ImageView backButtonImage = new ImageView("BombermanInda/Images/MainCharBack.png");
        Button backButton = createButton(pane,"Menu", backButtonImage, graphicsWindowX, graphicsWindowY, 2);
        backButton.setOnMousePressed(e -> gameMenu(primaryStage, pane));

    }



    /**
     * Stops the animationtimer in startGame()
     */
    public void stopGameLoop() {
        gameLoop.stop();
    }



    /**
     * ends game when one player dies
     */
    public void endGame(){
        pane.getChildren().remove(0, pane.getChildren().size());
        getWorld().clearWorld();
        characterMovement.removeControls();
        stopGameLoop();
    }


    /**
     * Creates a button with an image and a text in the middle of the X-axis and somewhere on the Y-axis according to yPos
     * @param pane which graphicspane to show this in
     * @param text what text to display
     * @param image what image to display
     * @param graphicsWindowX how big the window is
     * @param graphicsWindowY how big the window is
     * @param yPos what y position the button will be located in
     * @return
     */
    public Button createButton(Pane pane, String text, ImageView image, int graphicsWindowX, int graphicsWindowY, int yPos){

        Button button = new Button(text);
        pane.getChildren().add(button);
        image.setPreserveRatio(true);
        image.setFitWidth(graphicsWindowX/10);
        image.setFitHeight(graphicsWindowY/10);
        button.setGraphic(image);
        button.setPrefSize(graphicsWindowX/7, graphicsWindowY/7);
        button.relocate((graphicsWindowX/2)-button.getPrefWidth()/2, (graphicsWindowY/yPos)+ button.getPrefHeight()/2);
        return button;
    }

    // Multiple gets
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Pane getPane() { return pane; }

    public World getWorld() {
        return world;
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
    

