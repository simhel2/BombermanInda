package BombermanInda;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Character extends MovingObjects{
    private Render render;
    private World world;
    private Pane pane;
    private Game game;
    private Node onTopOfBomb;
    
    //defaults
    private ArrayList<Node> currentBombs= new ArrayList<Node>(); //hashmap would be more optimal
    private int maxBombs = 1;
    private int lives = 3;
    private int bombSize = 3;   
    private int detTime = 3000; //ms
    
    public Character(Node graphic, double posX, double posY, boolean isVisible, 
            boolean collisionEnable, Render render, World world, Pane pane, Game game){
        super(graphic, posX, posY, isVisible, collisionEnable);
        this.render = render;
        this.world = world;
        this.pane = pane;
        this.game = game;
    }

    public void damage () {
        
        lives--;
        if (lives==0){
            //remove from graphics and world
            pane.getChildren().remove(graphic);
            world.removeMovingObject(this);
            
            //check if game over
            int charsLeft = 0;
            for (MovingObjects movObj : world.getMovingObjects() ) {
                if(movObj.getClass() == Character.class) {
                    charsLeft++;
                }
                
            }
            if (charsLeft<=1)
            {
                game.endScreen(game.getPrimaryStage());
            }
        } 
        
        //TODO add animation/imortality on taking damage
        
    }
    
    public void removeBomb(Node bomb) {
        currentBombs.remove(bomb);
        if (bomb == onTopOfBomb) {
            onTopOfBomb = null;
        }
    }
    
    //powerups
    public void improveBombs(int i){
        bombSize = bombSize+i; 
    }
    public void addBombLimit(int i){
        maxBombs=maxBombs+i;
    }
    public void changeSpeed(double mult){
        setMaxSpeed(getMaxSpeed()*mult);
    }
    
    public void changeNudgeSpeed(double mult){
        setNudgeSpeedMod(getNudgeSpeedMod()*mult);
    }
    
    public Node getOnTopOfBomb(){
        return onTopOfBomb;
    }
    public void resetOnTopOfBomb(){
        onTopOfBomb = null;
    }
    
    
    public void layBomb() throws InterruptedException{
        
        if (currentBombs.size()<maxBombs 
                //disallow putting bombs on top of stuff
                && world.getWorldMatrix()[getXIndex(world, render)][getYIndex(world, render)]==null) {
            Node newBomb;
            newBomb = render.createGraphicsEntity(Render.GraphicsObjects.BOMB);
            currentBombs.add(newBomb);
            Bomb bomb = new Bomb(newBomb,true, true, world,render, this);  
            int xCord  =  getXIndex(world,render);
            int yCord = getYIndex(world,render);
            world.setObject(xCord, yCord, bomb); 
            render.drawMapObject(xCord, yCord, world);   
            bomb.setFuse(detTime, bombSize, world, render, getNode(), xCord, yCord, pane);    
            onTopOfBomb = bomb.getNode();
            
        }
    }
}
