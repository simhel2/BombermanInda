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
    private Pane pane;
    
    //defaults
    private int lives = 3;
    private int bombSize = 3;   
    private int detTime = 3000; //ms
    
    public Character(Node graphic, double posX, double posY, boolean isVisible, 
            boolean collisionEnable, Render render, World world, Pane pane){
        super(graphic, posX, posY, isVisible, collisionEnable);
        this.render = render;
        this.world = world;
        this.pane = pane;
    }
    
    //TODO make less janky

public void testControls(Stage primaryStage, Render render){

        this.primaryStage = primaryStage;

        //random keylistener for no reason
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.UP) {
                    setSpeedYDirection(-1);
                    render.drawMainCharacterBack(getNode());
                }
                else if (ke.getCode() == KeyCode.DOWN) {
                    setSpeedYDirection(1);
                    render.drawMainCharacterFront(getNode());
                }
                else if (ke.getCode() == KeyCode.LEFT) {
                    setSpeedXDirection(-1);
                    render.drawMainCharacterLeft(getNode());
                }
                else if (ke.getCode() == KeyCode.RIGHT) {
                    setSpeedXDirection(1);
                    render.drawMainCharacterRight(getNode());
                }
                //layBomb
                else if(ke.getCode()== KeyCode.SPACE) {
                    try {
                    layBomb(render.getGraphicsWindowY(), render.getGraphicsWindowY(), render.getNumGrid());
                    } catch (InterruptedException e){
                        //TODO
                    }
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
    public void damage () {
        
        lives--;
        if (lives==0){
            //remove from graphics and world
            pane.getChildren().remove(graphic);
            world.removeMovingObject(this);
            
            //TODO add check to see if gameover
        } 
        
        //TODO add animation/imortality on taking damage
        
    }

    //TODO add function for setting controls
    public void layBomb(int graphicsWindowX, int graphicsWindowY, int numGrid) throws InterruptedException{
        //TODO: add new class for bomb
        //TODO: Fix collision with bomb placement
        Node newBomb;
        newBomb = render.createGraphicsEntity(Render.GraphicsObjects.BOMB);
        Bomb bomb = new Bomb(newBomb,true, true, world,render);  
        int xCord  =  getXIndex(world,render);
        int yCord = getYIndex(world,render);
        world.setObject(xCord, yCord, bomb); //should maybe make one ?
        render.drawMapObject(xCord, yCord, world);   
        bomb.setFuse(detTime, bombSize, world, render, getNode(), xCord, yCord, pane);       
    }
}
