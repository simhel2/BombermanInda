package BombermanInda;

import javafx.application.Application;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author simon
 */

public class Render {
    private Pane pane;
    private GraphicsContext gc;
    private Stage primaryStage;  
    private int graphicsWindowX;    
    private int graphicsWindowY;
    private int numGrid;
    private Game game;

    public Render (Pane pane, Stage stage, Game game) {
        this.game = game;
        this.pane = pane;
        this.primaryStage = stage;
        this.graphicsWindowX = game.getGraphicsWindowX();//(int)stage.getWidth(); // TODO FIX, this does not work
        this.graphicsWindowY= game.getGraphicsWindowY();//(int)stage.getHeight();
        this.numGrid = game.getNumGrid();
        //initial graphic
        
        Canvas canvas = new Canvas(graphicsWindowX, graphicsWindowY );
        pane.getChildren().add( canvas );
        gc = canvas.getGraphicsContext2D();

        
        
    }
   
    public enum GraphicsObjects{
        MAINCHARACTER, CRATE, BOMB;
    }
            
    public Node createGraphicsEntity(GraphicsObjects grp){
        if(grp==GraphicsObjects.MAINCHARACTER) {
            ImageView mainCharacter = new ImageView("BombermanInda/Images/MainCharFront.png");
            mainCharacter.setFitHeight(graphicsWindowX/numGrid);
            mainCharacter.setFitWidth(graphicsWindowY/numGrid);

            return (Node) mainCharacter;

        } else if (grp == GraphicsObjects.CRATE) {
            //CHANGE
            Rectangle rect = new Rectangle(graphicsWindowX/numGrid,graphicsWindowY/numGrid);
            rect.setFill(Color.ORANGE);
            return rect;
        
        } else if (grp == GraphicsObjects.BOMB) {
            //CHANGE
            Rectangle rect = new Rectangle(graphicsWindowX/numGrid,graphicsWindowY/numGrid);
            rect.setFill(Color.RED);
            return rect;
        
        } else {
            //make error
            throw new Error("could not construct object"+grp.toString());
          
        }
        
    
    }
    
    public void drawAllMapObjects(World world) {
        //draw all in matrix
        for(int i = 0; i < world.getWorldMatrix().length; ++i) {
            for(int j = 0; j < world.getWorldMatrix()[i].length; ++j) {
                if (world.getWorldMatrix()[i][j]!=null) {   //if worldMatrix has something draw it
                   if (!pane.getChildren().contains((world.getWorldMatrix()[i][j]).getNode())) {    //if it is not in world matrix add it
                       pane.getChildren().add(world.getWorldMatrix()[i][j].getNode());                   //(optimize?)    
                   } 
                   //calculate position:
                   int posX = i*graphicsWindowX/world.getWorldMatrix().length;  //change here to change size (20)
                   int posY = j*graphicsWindowY/world.getWorldMatrix()[0].length;
                   world.getWorldMatrix()[i][j].getNode().relocate(posX,posY);                   
               }                     
            }
        }
        //draw all moveable objects 
        for (MovingObjects movObj: world.getMovingObjects()) {
            if (!pane.getChildren().contains(movObj.getNode())) {    //if it is not in world matrix add it
                pane.getChildren().add(movObj.getNode());                   //(optimize?)    
            }             
        }
    }

    public int getGraphicsWindowX(){
        return graphicsWindowX;
    }

    public int getGraphicsWindowY(){
        return graphicsWindowX;
    }

    public int getNumGrid(){
        return numGrid;
    }

    public void drawBackground(World world){
        gc.drawImage(world.getBackground(), 0, 0, graphicsWindowX, graphicsWindowY );
    }

    public void drawMainCharacterFront(Node graphic) {
        Image image = new Image("BombermanInda/Images/MainCharFront.png");
        ((ImageView) graphic).setImage(image);
    }
    public void drawMainCharacterBack(Node graphic) {
        Image image = new Image("BombermanInda/Images/MainCharBack.png");
        ((ImageView) graphic).setImage(image);

    }
    public static void drawMainCharacterRight(Node graphic) {
        Image image = new Image("BombermanInda/Images/MainCharRight.png");
        ((ImageView) graphic).setImage(image);
    }
    public static void drawMainCharacterLeft(Node graphic) {
        Image image = new Image("BombermanInda/Images/MainCharLeft.png");
        ((ImageView) graphic).setImage(image);
    }

}

