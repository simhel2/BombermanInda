package BombermanInda;

import java.util.ArrayList;
import java.util.Timer;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * A class that makes different characters, player-controlled for now, but could be AI
 */

public class Character extends MovingObjects{
    private Render render;
    private World world;
    private Pane pane;
    private Game game;
    private Bomb onTopOfBomb;
    private boolean isInvulnerable = false;
    private Player player;
    private lastMoved lastMoved;

    //defaults
    private ArrayList<Bomb> currentBombs= new ArrayList<Bomb>(); //hashmap would be more optimal
    private int maxBombs = 1;
    private int lives = 3;
    private int bombSize = 2;   
    private int detTime = 3000; //ms
    private int invulnerabilityTime = 1000; //ms

    /**
     *
     * @param graphic the characters corresponding Node
     * @param posX which X-position this character starts at
     * @param posY which X-position this character starts at
     * @param maxSpeed the characters max speed
     * @param moveDistLimit the limit of the distance of which the character can move
     * @param isVisible if the character is visible
     * @param collisionEnable if the character can collide with other things
     * @param render the render used for graphics
     * @param world the world the character is in
     * @param pane the graphicspane
     * @param game which game the character is in
     * @param player which player this character is
     */

    public Character(Node graphic, double posX, double posY, double maxSpeed, double moveDistLimit, boolean isVisible, 
            boolean collisionEnable, Render render, World world, Pane pane, Game game, Player player){
        super(graphic, posX, posY, maxSpeed, moveDistLimit, isVisible, collisionEnable);
        this.render = render;
        this.world = world;
        this.pane = pane;
        this.game = game;
        this.player = player;
    }

    /**
     * Which direction this player last moved
     */
    public enum lastMoved {
        UP, DOWN, LEFT, RIGHT
    }


    /**
     * Which different player this can be
     */
    public enum Player {
        PLAYERONE, PLAYERTWO
    }


    /**
     * This character takes damage
     */
    public void damage () {

        if(!isInvulnerable){
            // Takes one damage
            lives--;
            // Checks which direction this character last moved in so it can update the picture instantly
            if(lastMoved == lastMoved.UP){
                render.drawInvulnerableCharacterBack(getNode());
            }else if(lastMoved == lastMoved.DOWN){
                render.drawInvulnerableCharacterFront(getNode());
            }else if(lastMoved == lastMoved.RIGHT){
                render.drawInvulnerableCharacterRight(getNode());
            }else if(lastMoved == lastMoved.LEFT){
                render.drawInvulnerableCharacterLeft(getNode());
            }
        }

        if (lives==0){
            //remove from graphics and world
            pane.getChildren().remove(graphic);
            world.removeMovingObject(this);
            world.addBombsToDefuse(currentBombs);
            
            //check if game over
            int charsLeft = 0;
            for (MovingObjects movObj : world.getMovingObjects() ) {
                if(movObj.getClass() == Character.class) {
                    charsLeft++;
                }
                
            }
            if (!(charsLeft>1))
            {
                game.endGame();
                game.endScreen(game.getPrimaryStage(), game.getPane(), player);
            }
        } 

        // Makes this character invulnerable
        isInvulnerable = true;

        // Starts a timer that makes the character vulnerable again after some time
        Timer makeInvulnerable = new Timer();
        Invulnerability invulnerabilityTimer = new Invulnerability(this, makeInvulnerable);
        makeInvulnerable.schedule(invulnerabilityTimer, invulnerabilityTime);
        
    }

    /**
     * Removes a bomb from list of placed bombs
     * @param bomb what bomb to remove
     */
    public void removeBomb(Bomb bomb) {
        currentBombs.remove(bomb);
        if (bomb == onTopOfBomb) {
            onTopOfBomb = null;
        }
    }
    public ArrayList<Bomb> getBombs(){
        return currentBombs;
    }
    
    //powerups

    /**
     * makes the explosion bigger
     * @param i how much bigger to make the explosion
     */
    public void improveBombs(int i){
        bombSize = bombSize+i; 
    }

    /**
     * adds more bombs to the capacity
     * @param i how many more bombs to add
     */
    public void addBombLimit(int i){
        maxBombs=maxBombs+i;
    }

    /**
     * changes the speed of a character by some multiplier
     * @param mult multiplier to change the speed
     */
    public void changeSpeed(double mult){
        setMaxSpeed(getMaxSpeed()*mult);
    }
    
    public void changeNudgeSpeed(double mult){
        setNudgeSpeedMod(getNudgeSpeedMod()*mult);
    }
    
    public Bomb getOnTopOfBomb(){
        return onTopOfBomb;
    }
    public void resetOnTopOfBomb(){
        onTopOfBomb = null;
    }


    /**
     * Shows if this character is invulnerable or not
     * @return returns if this character is invulnerable or not
     */
    public boolean getIsInvulnerable(){
        return isInvulnerable;
    }


    /**
     * Changes isInvulnerable to desired value
     * @param value what value to change isInvulnerable into
     */
    public void setInvulnerable(boolean value){
        isInvulnerable = value;
    }



    /**
     * Lays bomb at this characters position
     * @throws InterruptedException
     */
    public void layBomb() throws InterruptedException{
        
        if (currentBombs.size()<maxBombs && lives > 0 
                //disallow putting bombs on top of stuff
                && world.getWorldMatrix()[getXIndex(world, render)][getYIndex(world, render)]==null) {
            Node newBomb;
            newBomb = render.createGraphicsEntity(Render.GraphicsObjects.BOMB);
            int xCord  =  getXIndex(world,render);
            int yCord = getYIndex(world,render);
            Bomb bomb = new Bomb(newBomb,true, true,this, pane, render, world,
                xCord, yCord, getX(), getY(), bombSize, detTime);  
            world.setObject(xCord, yCord, bomb); 
            render.drawMapObject(xCord, yCord, world);   
            currentBombs.add(bomb);
            onTopOfBomb = bomb;
            
        }
    }

    /**
     * changes the direction in which a player last moved
     * @param lastMove in which direction a player last moved
     */
    public void setLastMoved(lastMoved lastMove){
        lastMoved = lastMove;
    }
}
