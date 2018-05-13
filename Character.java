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
                else if(ke.getCode()== KeyCode.PERIOD) {
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

    public void secondPlayerControl(Stage primaryStage, Render render){

        this.primaryStage = primaryStage;

        //random keylistener for no reason
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.W) {
                    setSpeedYDirection(-1);
                    render.drawSecondCharacterBack(getNode());
                }
                else if (ke.getCode() == KeyCode.S) {
                    setSpeedYDirection(1);
                    render.drawSecondCharacterFront(getNode());
                }
                else if (ke.getCode() == KeyCode.A) {
                    setSpeedXDirection(-1);
                    render.drawSecondCharacterLeft(getNode());
                }
                else if (ke.getCode() == KeyCode.D) {
                    setSpeedXDirection(1);
                    render.drawSecondCharacterRight(getNode());
                }
                //layBomb
                else if(ke.getCode()== KeyCode.T) {
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
                if (ke.getCode() == KeyCode.W || ke.getCode() == KeyCode.S) {
                    setSpeedYDirection(0);
                }
                if (ke.getCode() == KeyCode.A || ke.getCode() == KeyCode.D) {
                    setSpeedXDirection(0);
                }

            }
        });

    }


    //TODO add function for setting controls
    public void layBomb(int graphicsWindowX, int graphicsWindowY, int numGrid) throws InterruptedException{
        //TODO: add new class for bomb
        //TODO: Fix collision with bomb placement
        Node newBomb;
        newBomb = render.createGraphicsEntity(Render.GraphicsObjects.BOMB);
        Bomb bomb = new Bomb(newBomb,true, true, world,render);  
        int xCord  = ((int)((getX())/(graphicsWindowX/numGrid)));
        int yCord = ((int)((getY())/(graphicsWindowY/numGrid)));
        world.setObject(xCord, yCord, bomb); //should maybe make one ?
        render.drawMapObject(xCord, yCord, world);   
        bomb.setFuse(detTime, bombSize, world, render, getNode(), xCord, yCord, pane);       
    }
}
