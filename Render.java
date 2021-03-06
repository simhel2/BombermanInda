package BombermanInda;

import java.util.Timer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class that draws all graphics
 * @author simon
 */

public class Render {
    private Pane pane;
    private GraphicsContext gc;
    private Stage primaryStage;  
    private int graphicsWindowX;    
    private int graphicsWindowY;
    private int numGridX;
    private int numGridY;
    private Game game;

    /**
     *
     * @param pane the graphicspane where everything will be drawn
     * @param stage which stage this is going to be in
     * @param game what game this is
     */
    public Render (Pane pane, Stage stage, Game game) {
        this.game = game;
        this.pane = pane;
        this.primaryStage = stage;
        this.graphicsWindowX = game.getGraphicsWindowX();
        this.graphicsWindowY= game.getGraphicsWindowY();
        this.numGridX = game.getNumGridX();
        this.numGridY = game.getNumGridY();

        //initial graphic
        
        Canvas canvas = new Canvas(graphicsWindowX, graphicsWindowY );
        pane.getChildren().add( canvas );
        gc = canvas.getGraphicsContext2D();

    }


    /**
     * Draws an explosion with a middle, and goes in four directions: up, down, right, left. Each direction has a "size".
     * @param xCord which x position this explosion starts in
     * @param yCord which y position this explosion starts in
     * @param upSize how far up the explosion will go
     * @param downSize how far down the explosion will go
     * @param leftSize how far left the explosion will go
     * @param rightSize how far right the explosion will go
     * @param world what world this is in
     * @return returns the image (Node) of the polygon for the explosion
     */
    public Node drawExplosion(int xCord, int yCord, int upSize, int downSize, int leftSize, 
            int rightSize, World world) {

        Polygon explosion  = new Polygon();
        double multX = graphicsWindowX/numGridX;
        double multY = graphicsWindowY/numGridY;
        explosion.getPoints().addAll(new Double[]{
                   
        //cross
        xCord*multX-multX*leftSize, yCord*multY,             //leftside
        xCord*multX-multX*leftSize, yCord*multY+multY, 
        xCord*multX+multX*(rightSize+1), yCord*multY+multY,   //rightside
        xCord*multX+multX*(rightSize+1), yCord*multY,
        xCord*multX, yCord*multY,                         //mid
        xCord*multX, yCord*multY+multY*(downSize+1), //down
        xCord*multX+multX, yCord*multY+multY*(downSize+1),
        xCord*multX+multX, yCord*multY-multY*(upSize), //up     
        xCord*multX, yCord*multY-multY*(upSize),
        xCord*multX, yCord*multY             //mid

                
        });

        // Fills the polygon-shaped explosion with an image of an explosion
        explosion.setFill(new ImagePattern(new Image("BombermanInda/Images/explosion.png"),0,0,1,1,true));   //change
        pane.getChildren().add(explosion);
        return explosion;
    }


    /**
     * Different kinds of objects
     */
    public enum GraphicsObjects{

        MAINCHARACTER, CRATE, BOMB, SECONDCHARACTER, POWER_BIGGER, POWER_MORE, POWER_SPEED, WALL; //TODO add more

    }

    // Creates graphics for every single object in the game, depending on its enum.

    /**
     * Creates graphics for every single object in the game, depending on its enum.
     * @param grp which kind of object this is
     * @return returns the image (Node) of the object
     */
    public Node createGraphicsEntity(GraphicsObjects grp){
        if(grp==GraphicsObjects.MAINCHARACTER) {

            ImageView mainCharacter = new ImageView("BombermanInda/Images/MainCharFront.png");
            mainCharacter.setFitWidth(graphicsWindowX/numGridX);
            mainCharacter.setFitHeight(graphicsWindowY/numGridY);
            return (Node) mainCharacter;

        } else if (grp == GraphicsObjects.CRATE) {

            ImageView crate = new ImageView("BombermanInda/Images/Crate.png");
            crate.setFitWidth(graphicsWindowX/numGridX);
            crate.setFitHeight(graphicsWindowY/numGridY);
            return crate;
        
        } else if (grp == GraphicsObjects.BOMB) {

            ImageView bomb = new ImageView("BombermanInda/Images/Bomb.png");
            bomb.setFitWidth(graphicsWindowX/numGridX);
            bomb.setFitHeight(graphicsWindowY/numGridY);
            return bomb;
        

        } else if (grp == GraphicsObjects.SECONDCHARACTER) {

            ImageView secondCharacter = new ImageView("BombermanInda/Images/CharTwoFront.png");
            secondCharacter.setFitWidth(graphicsWindowX/numGridX);
            secondCharacter.setFitHeight(graphicsWindowY/numGridY);
            return secondCharacter;


        } else if (grp == GraphicsObjects.POWER_BIGGER) {
            ImageView biggerFlames = new ImageView("BombermanInda/Images/BiggerFlames.png");
            biggerFlames.setFitWidth(graphicsWindowX/numGridX);
            biggerFlames.setFitHeight(graphicsWindowY/numGridY);
            return biggerFlames;
        } else if (grp == GraphicsObjects.POWER_MORE) {
            ImageView moreBombs = new ImageView("BombermanInda/Images/MoreBombs.png");
            moreBombs.setFitWidth(graphicsWindowX/numGridX);
            moreBombs.setFitHeight(graphicsWindowY/numGridY);

            return moreBombs;
        } else if (grp == GraphicsObjects.POWER_SPEED) {
            ImageView speedUp = new ImageView("BombermanInda/Images/SpeedUp.png");
            speedUp.setFitWidth(graphicsWindowX/numGridX);
            speedUp.setFitHeight(graphicsWindowY/numGridY);
            return speedUp;

        }   else if (grp == GraphicsObjects.WALL) {
            ImageView wall = new ImageView("BombermanInda/Images/Wall.png");
            wall.setFitWidth(graphicsWindowX / numGridX);
            wall.setFitHeight(graphicsWindowY / numGridY);
            return wall;
        }
        else {

            throw new Error("could not construct object"+grp.toString());
          
        }
        
    
    }



    /**
     * Draws every object inside worldMatrix
     * @param world which world to draw the objects
     */
    public void drawAllMapObjects(World world) {
        //draw all in matrix
        for (int i = 0; i < world.getWorldMatrix().length; ++i) {
            for (int j = 0; j < world.getWorldMatrix()[i].length; ++j) {
                if (world.getWorldMatrix()[i][j] != null) {   //if worldMatrix has something draw it
                    if (!pane.getChildren().contains((world.getWorldMatrix()[i][j]).getNode())) {    //if it is not in world matrix add it
                        pane.getChildren().add(world.getWorldMatrix()[i][j].getNode());                   //(optimize?)
                    }
                    //calculate position:
                    int posX = i * graphicsWindowX / world.getWorldMatrix().length;
                    int posY = j * graphicsWindowY / world.getWorldMatrix()[0].length;
                    world.getWorldMatrix()[i][j].getNode().relocate(posX, posY);
                }
            }
        }
        //draw all moveable objects 
        for (MovingObjects movObj : world.getMovingObjects()) {
            if (!pane.getChildren().contains(movObj.getNode())) {    //if it is not in world matrix add it
                pane.getChildren().add(movObj.getNode());                   //(optimize?)    
            }
        }
    }

    /**
     * Draws one object on a position in the worldMatrix

     * @param x which x position to draw the object
     * @param y which y position to draw the object
     * @param world what world to draw the object in
     */
    public void drawMapObject(int x, int y,  World world) {
        if (world.getWorldMatrix()[x][y]!=null) {   //if worldMatrix has something draw it
                   if (!pane.getChildren().contains((world.getWorldMatrix()[x][y]).getNode())) {    //if it is not in world matrix add it
                       pane.getChildren().add(world.getWorldMatrix()[x][y].getNode());                   //(optimize?)    
                   } 
                   //calculate position:
                   int posX = x*graphicsWindowX/world.getWorldMatrix().length; 
                   int posY = y*graphicsWindowY/world.getWorldMatrix()[0].length;
                   world.getWorldMatrix()[x][y].getNode().relocate(posX,posY);                   
        }           
    }


    /**
     * Removes an object that was in the matrix
     * @param world in what world to remove the object
     * @param xCord what x coordinate this object is in
     * @param yCord what y coordinate this object is in
     */
    public void removeMapObject(World world, int xCord,int yCord) {
        pane.getChildren().remove(world.getWorldMatrix()[xCord][yCord].getNode());
    }

    public int getGraphicsWindowX(){
        return graphicsWindowX;
    }

    public int getGraphicsWindowY(){
        return graphicsWindowY;
    }

    public int getNumGridX(){
        return numGridX;
    }
    
    public int getNumGridY(){
        return numGridY;
    }



    /**
     * Draws the background of the gameworld.
     * @param image what image to have as a background
     */
    public void drawBackground(Image image){
        ImageView background = new ImageView(image);
        pane.getChildren().add(background);
    }

    // Removes an object from the pane
    public void removeObject(Node node) {
        pane.getChildren().remove(node);
    }




    /**
     * Draws every orientation of both characters, and the invulnerability state. (All methods)
     * @param graphic what node to set an Image to.
     */
    public void drawMainCharacterFront(Node graphic) {
        Image image = new Image("BombermanInda/Images/MainCharFront.png");
        ((ImageView) graphic).setImage(image);
    }
    public void drawMainCharacterBack(Node graphic) {
        Image image = new Image("BombermanInda/Images/MainCharBack.png");
        ((ImageView) graphic).setImage(image);

    }
    public void drawMainCharacterRight(Node graphic) {
        Image image = new Image("BombermanInda/Images/MainCharRight.png");
        ((ImageView) graphic).setImage(image);
    }
    public void drawMainCharacterLeft(Node graphic) {
        Image image = new Image("BombermanInda/Images/MainCharLeft.png");
        ((ImageView) graphic).setImage(image);
    }

    public void drawSecondCharacterFront(Node graphic) {
        Image image = new Image("BombermanInda/Images/CharTwoFront.png");
        ((ImageView) graphic).setImage(image);
    }
    public void drawSecondCharacterBack(Node graphic) {
        Image image = new Image("BombermanInda/Images/CharTwoBack.png");
        ((ImageView) graphic).setImage(image);

    }
    public void drawSecondCharacterRight(Node graphic) {
        Image image = new Image("BombermanInda/Images/CharTwoRight.png");
        ((ImageView) graphic).setImage(image);
    }
    public void drawSecondCharacterLeft(Node graphic) {
        Image image = new Image("BombermanInda/Images/CharTwoLeft.png");
        ((ImageView) graphic).setImage(image);
    }

    public void drawInvulnerableCharacterFront(Node graphic) {
        Image image = new Image("BombermanInda/Images/InvulnerableFront.png");
        ((ImageView) graphic).setImage(image);
    }
    public void drawInvulnerableCharacterRight(Node graphic) {
        Image image = new Image("BombermanInda/Images/InvulnerableRight.png");
        ((ImageView) graphic).setImage(image);
    }
    public void drawInvulnerableCharacterLeft(Node graphic) {
        Image image = new Image("BombermanInda/Images/InvulnerableLeft.png");
        ((ImageView) graphic).setImage(image);
    }
    public void drawInvulnerableCharacterBack(Node graphic) {
        Image image = new Image("BombermanInda/Images/InvulnerableBack.png");
        ((ImageView) graphic).setImage(image);
    }


}

