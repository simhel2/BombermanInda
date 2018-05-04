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
    
    
    private Render render;
    private World world;
    
    public Character(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable, Render render, World world){
        super(graphic, posX, posY, isVisible, collisionEnable);
        this.render = render;
        this.world = world;
        
    }
    
    
public void testControls(Stage primaryStage, Pane pane){
        this.primaryStage = primaryStage;
    
        //random keylistener for no reason
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.UP) {
                    setSpeedYDirection(-1);
                }
                else if (ke.getCode() == KeyCode.DOWN) {
                    setSpeedYDirection(1);
                }
                else if (ke.getCode() == KeyCode.LEFT) {
                    setSpeedXDirection(-1);
                }
                else if (ke.getCode() == KeyCode.RIGHT) {
                    setSpeedXDirection(1);
                }
                //layBomb
                else if(ke.getCode()== KeyCode.SPACE) {
                    layBomb();

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

    //TODO add function for setting controls



    public void layBomb(){
        
        //TODO: add new class for bomb
        //TODO: get 25 properly
        Node newBomb;
        newBomb = render.createGraphicsEntity(Render.GraphicsObjects.BOMB);
        Character bomb = new Character(newBomb,0,0, true, true, render, world);   //create char on (0,0)
        world.setObject(((int)(getX()/25)),((int)(getY()/25)),bomb);
        render.drawAllMapObjects(world);    //bad
        
    }
    
}