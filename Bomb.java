/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Bomb is a MapObject used for representing bombs and detonating them.
 *
 * @author simon
 * 
 */
public class Bomb extends MapObject{
 
    private Character owner;
    private Pane pane;
    private World world;
    private int xCord;
    private int yCord;
    private Render render;
    private int size;
    private boolean detonated = false;
    private Timer detTimer;
    
    //default
    private int explosionAnimTime = 400;
    private int damageInterval = 50;
    private int numTicks = explosionAnimTime/damageInterval;
   
    /** Constructor for the bomb class
     * 
     * @param newBomb The graphical node for this bomb
     * @param isVisible Is this visible
     * @param collisionEnable is this obj collisionenabled
     * @param owner Which character planted this bomb
     * @param pane The graphicspane
     * @param render The render that is used for drawing
     * @param world The world the bomb is placed in
     * @param xCord x coordinate in world
     * @param yCord y coordinate
     * @param x actual x where the bomb was placed
     * @param y actual y where the bomb was placed 
     * @param size size of the bomb (coordinates)
     * @param milliseconds time until bomb detonates
     */
    public Bomb(Node newBomb, boolean isVisible, boolean collisionEnable, Character owner, Pane pane, Render render,World world,
            int xCord, int yCord, double x, double y, int size, int milliseconds) {
        super(newBomb, x,  
                y, isVisible , collisionEnable);
        this.world = world;
        this.render = render;
        this.xCord = xCord;
        this.yCord  =yCord;
        this.pane = pane;
        this.owner = owner;
        this.size = size;

        //start ticking
        detTimer = new Timer();
        Explosion explosion = new Explosion(this, detTimer); 
        detTimer.schedule(explosion, milliseconds); 
    }
    /**
     * Detonate will detonate this bomb, deal damage and then erase it both graphically and logically
     */
    public void detonate () {
        if (!detonated) {
            //make sure bomb only explodes once
            detonated = true;
            //remove the timer
            detTimer.cancel();
            detTimer.purge();

            //remove bomb logical
            world.remove(xCord, yCord);
         
            //remove bomb graphical
            pane.getChildren().remove(getNode());  
            //remove bomb from person
            owner.removeBomb(this);
            
            //calc size & do damage
            Border border = doDamageAndCalcSize();

            //animate explosion
            Node explosion = render.drawExplosion(xCord, yCord, border.up, border.down, border.left, border.right, world);

            //start timer that removes explosion
            Timer remExpl = new Timer();
            RemoveExplosion remTimer = new RemoveExplosion(pane,(Node)explosion, remExpl);
            remExpl.schedule(remTimer, explosionAnimTime);        
      
            //do damage within border for the rest of the animation
            Timer doDmg = new Timer();
            ExplosionDamage explDmg = new ExplosionDamage(this, doDmg, border, numTicks); 
            doDmg.schedule(explDmg, 0, damageInterval); 
                        
            
        }
    }
    /**
     * helper class Border that is used to return the borders of the explosion
     */
    public class Border{
        public int left;
        public int right;
        public int up;
        public int down;
        public Border(int left, int right, int up, int down) {
            this.left = left;
            this.right = right;
            this.up = up;
            this.down = down;
        }
    };
    /**Function for calculating the size of this bombs blast and dealing damage to all affected mapobjects.
     * 
     * @return Border The up, down, left and right border as a index distance from the center of this bomb.
     */
    public Border doDamageAndCalcSize() {
        int left = size;
        int right = size;
        int up = size;
        int down = size;
        

        //left
        for (int i = 1; i <= size; i++) {
            if (xCord-i <0 ) {
                break;
            }
            if (world.getWorldMatrix()[xCord-i][yCord]!= null && world.getWorldMatrix()[xCord-i][yCord].isCollisionEnable()){
                //crate
                if (world.getWorldMatrix()[xCord-i][yCord].getClass()==Crate.class){              
                    left = size-(size-i);
                    //remove crate
                    ((Crate)world.getWorldMatrix()[xCord-i][yCord]).damage(pane, world, xCord-i, yCord);
                    
                    break;
                }
                // Wall
                else if (world.getWorldMatrix()[xCord-i][yCord].getClass()==Wall.class){
                    left = size - (size-i);
                    break;
                }
                // Bomb
                else if(world.getWorldMatrix()[xCord-i][yCord].getClass()==Bomb.class){
                    left = size-(size-i);
                    ((Bomb)world.getWorldMatrix()[xCord-i][yCord]).detonate();
                    break;
                }
                
            } 
        }
        //Right
        for (int i = 1; i <= size; i++) {
            if (xCord+i >= world.getWorldMatrix().length) {
                break;
            }
            if (world.getWorldMatrix()[xCord+i][yCord]!= null && world.getWorldMatrix()[xCord+i][yCord].isCollisionEnable()){
                //crate
                if (world.getWorldMatrix()[xCord+i][yCord].getClass()==Crate.class){              
                    right = size-(size-i);
                    //remove crate
                    pane.getChildren().remove(world.getWorldMatrix()[xCord+i][yCord].getNode());
                    world.destroyCrate(xCord+i,yCord); 
                    
                    break;
                }
                // Wall
                else if(world.getWorldMatrix()[xCord+i][yCord].getClass()==Wall.class){
                    right = size - (size-i);
                    break;
                }
                // Bomb
                else if(world.getWorldMatrix()[xCord+i][yCord].getClass()==Bomb.class){
                    right = size-(size-i);
                    ((Bomb)world.getWorldMatrix()[xCord+i][yCord]).detonate();
                    break;
                }
                    
            }
        }
        //UP
        for (int i = 1; i <= size; i++) {
            if (yCord-i < 0 ) {
                break;
            }
            if (world.getWorldMatrix()[xCord][yCord-i]!= null && world.getWorldMatrix()[xCord][yCord-i].isCollisionEnable()){
                //crate
                if (world.getWorldMatrix()[xCord][yCord-i].getClass()==Crate.class){              
                    up = size-(size-i);

                    //remove crate
                    pane.getChildren().remove(world.getWorldMatrix()[xCord][yCord-i].getNode());
                    world.destroyCrate(xCord,yCord-i); 
                    
                    break;
                }
                // Wall
                else if(world.getWorldMatrix()[xCord][yCord-i].getClass()==Wall.class) {
                    up = size - (size-i);
                    break;
                }
                //bomb
                else if(world.getWorldMatrix()[xCord][yCord-i].getClass()==Bomb.class){
                    up = size-(size-i);
                    ((Bomb)world.getWorldMatrix()[xCord][yCord-i]).detonate();
                    break;
                }
            } 
        }
        //DOWN
        for (int i = 1; i <= size; i++) {
            if (yCord+i >= world.getWorldMatrix()[0].length) {
                break;
            }
            if (world.getWorldMatrix()[xCord][yCord+i]!= null && world.getWorldMatrix()[xCord][yCord+i].isCollisionEnable()){
                //crate
                if (world.getWorldMatrix()[xCord][yCord+i].getClass()==Crate.class){              
                    down = size-(size-i);

                    //remove crate
                    pane.getChildren().remove(world.getWorldMatrix()[xCord][yCord+i].getNode());
                    world.destroyCrate(xCord,yCord+i); 
                    break;
                }
                //Wall
                else if(world.getWorldMatrix()[xCord][yCord+i].getClass()==Wall.class){
                    down = size - (size-i);
                    break;
                }
                //Bomb
                else if(world.getWorldMatrix()[xCord][yCord+i].getClass()==Bomb.class){
                    down = size-(size-i);
                    ((Bomb)world.getWorldMatrix()[xCord][yCord+i]).detonate();
                    break;
                }
            }
            
            
            
        }
        Border border = new Border (left,right,up,down);
        doDamageToMovObj(border);
        
        return border;
    }
    /**
     * Function that does damage inside the borders of the bomb, this is used to do continual damage to moving characters
     * that move into the graphical representation of the bomb.
     * @param Border border 
     */
    
    public void doDamageToMovObj(Border border){
        int leftCord = xCord -border.left;  
        int rightCord = xCord + border.right; 
        int upCord = yCord -border.up; 
        int downCord = yCord +border.down;
        
        //if empty end
        if (world.getMovingObjects()== null) {
            return;
        }
        ArrayList<MovingObjects> copyOfMovObjList =  new ArrayList<MovingObjects>(world.getMovingObjects());
        for (MovingObjects movObj:copyOfMovObjList){
            int xIndex = movObj.getXIndex(world, render);
            int yIndex = movObj.getYIndex(world, render);
            //boolean that will determine wheter or not we got hit
            boolean hit = false;
            //horisontal hitbox
            if(yIndex == yCord && leftCord <= xIndex && xIndex <=rightCord ){
                hit = true;
            //vertical hitbox
            } else if (xIndex == xCord && upCord <= yIndex && yIndex <=downCord){ 
                hit = true;
            }
            if (hit) {
                movObj.damage();
            }
            
        }
        
    }
    /**
     * Defuse this bomb ensuring it will not detonate
     */
    public void defuse () {
        if (!detonated) {
            detonated = true;
            detTimer.cancel();
            detTimer.purge();
        }
    
    }
    
}
