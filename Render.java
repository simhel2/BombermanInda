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
    public Render (Pane pane, Stage stage) {
        this.pane = pane;
        this.primaryStage = stage;
        this.graphicsWindowX = 500;//(int)stage.getWidth(); // TODO FIX, this does not work
        this.graphicsWindowY= 500;//(int)stage.getHeight();
        
        //initial graphic
        
        Canvas canvas = new Canvas(graphicsWindowX, graphicsWindowY );
        pane.getChildren().add( canvas );
        gc = canvas.getGraphicsContext2D();

        
        
    }
   
    public enum GraphicsObjects{
        MAINCHARACTER, CRATE;
    }
            
    public Node createGraphicsEntity(GraphicsObjects grp){
        if(grp==GraphicsObjects.MAINCHARACTER) {
            //CHANGE
            Circle circle = new Circle(0,0,graphicsWindowX/40); //maybe make the 20 a variable
            circle.setFill(Color.GREEN);
            return circle;
        } else if (grp == GraphicsObjects.CRATE) {
            //CHANGE
            Rectangle rect = new Rectangle(graphicsWindowX/20,graphicsWindowY/20); 
            rect.setFill(Color.ORANGE);
            return rect;
        
        } else {
            //make error
            throw new Error("could not construct object"+grp.toString());
          
        }
        
    
    }
    
    public void drawAllMapObjects(World world) {
        
        for(int i = 0; i < world.getWorldMatrix().length; ++i) {
            for(int j = 0; j < world.getWorldMatrix()[i].length; ++j) {
                if (world.getWorldMatrix()[i][j]!=null) {   //if worldMatrix has something draw it
                   if (!pane.getChildren().contains((world.getWorldMatrix()[i][j]).getNode())) {    //if it is not in world matrix add it
                       pane.getChildren().add(world.getWorldMatrix()[i][j].getNode());                       
                   } 
                   //calculate position:
                   int posX = i*graphicsWindowX/world.getWorldMatrix().length;
                   int posY = j*graphicsWindowY/world.getWorldMatrix()[0].length;
                   world.getWorldMatrix()[i][j].getNode().relocate(posX,posY);                   
               } 
                    
              
            }
        }
    }
        
    public void drawBackground(World world){
        gc.drawImage( world.getBackground(), 0, 0, graphicsWindowX,graphicsWindowY );        
    }
    
    

}

