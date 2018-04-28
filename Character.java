package BombermanInda;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Character extends MovingObjects{
    //TODO add hotkeys as fields
    
    
    private Stage primaryStage;
    
    
    
    public Character(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable){
        super(graphic, posX, posY, isVisible, collisionEnable);
 
    }
    
    
public void testMovement3(Stage primaryStage, Pane pane){
        this.primaryStage = primaryStage;
    
        //random keylistener for no reason
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.UP) {
                    setSpeedYDirection(-1);
                }
                if (ke.getCode() == KeyCode.DOWN) {
                    setSpeedYDirection(1);
                }
                 if (ke.getCode() == KeyCode.LEFT) {
                    setSpeedXDirection(-1);
                }
                if (ke.getCode() == KeyCode.RIGHT) {
                    setSpeedXDirection(1);
                }
            }
        });
        
        primaryStage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN) {
                    setSpeedYDirection(0);
                }
                if (ke.getCode() == KeyCode.LEFT || ke.getCode() == KeyCode.RIGHT) {
                    setSpeedXDirection(0);
                }
                
            }
        });
    }
    //WARNING ROUNDS DOWN AGGRESSIVELY
    public int getCurrentPosAsIndexX(int worldWidth){
        int pxWidth =  500;   //primaryStage.getWidth(); TODO FIX 
         
        return (((int)getX())*worldWidth)/pxWidth;
       
        //ex: (300* 20)/500  = 12. 
    }
    //WARNING ROUNDS DOWN AGGRESSIVELY
    public int getCurrentPosAsIndexY(int worldHeight){
        int pxHeight = 500; //primaryStage.getHeight(); TODO FIX 

        return (((int)getY())*worldHeight)/pxHeight;
    }
    
   
    //TODO add function for setting controls

}