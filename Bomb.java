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
 *
 * @author simon
 */
public class Bomb extends MapObject{
 
    private Character owner;
    private Pane pane;
    private World world;
    private int xCord;
    private int yCord;
    private Render render;
    private int bombSize;
    private boolean detonated = false;
    private Timer detTimer;
    
    //default
    private int explosionAnimTime = 400;
    private int damageInterval = 50;
    private int numTicks = explosionAnimTime/damageInterval;
            
    public Bomb(Node newBomb, boolean isVisible, boolean collisionEnable, Character owner, Pane pane, Render render,World world,
            int xCord, int yCord, double x, double y, int bombSize, int milliseconds) {
        super(newBomb, x,  
                y, isVisible , collisionEnable);
        this.world = world;
        this.render = render;
        this.xCord = xCord;
        this.yCord  =yCord;
        this.pane = pane;
        this.owner = owner;
        this.bombSize = bombSize;

        //start ticking
        detTimer = new Timer();
        Explosion explosion = new Explosion(this, detTimer); 
        detTimer.schedule(explosion, milliseconds); 
    }
    
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
            Border border = doDamageAndCalcSize(xCord, yCord, bombSize);

            //animate explosion
            Node explosion = render.drawExplosion(xCord, yCord, border.up, border.down, border.left, border.right, world);

            //start timer that removes explosion
            Timer remExpl = new Timer();
            RemoveExplosion remTimer = new RemoveExplosion(pane,(Node)explosion, remExpl);
            remExpl.schedule(remTimer, explosionAnimTime);        
      
            //do damage within border for the rest of the animation
            Timer doDmg = new Timer();
            ExplosionDamage explDmg = new ExplosionDamage(this, doDmg, border, xCord, yCord, numTicks); 
            doDmg.schedule(explDmg, 0, damageInterval); 
            
            //doDamageToMovObj(border, xCord, yCord);--
            
            
        }
    }
    /**
     * helper class Border
     */
    class Border{
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
    public Border doDamageAndCalcSize(int xCord, int yCord, int size) {
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
                else if(world.getWorldMatrix()[xCord-i][yCord].getClass()==Bomb.class){
                    left = size-(size-i);
                    ((Bomb)world.getWorldMatrix()[xCord-i][yCord]).detonate();
                    break;
                }
                
            } 
        }
        //Right
        for (int i = 1; i <= size; i++) {
            if (xCord+i >= world.getWidth() ) {
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
            if (yCord+i >= world.getHeight() ) {
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
                else if(world.getWorldMatrix()[xCord][yCord+i].getClass()==Bomb.class){
                    down = size-(size-i);
                    ((Bomb)world.getWorldMatrix()[xCord][yCord+i]).detonate();
                    break;
                }
            }
            
            
            
        }
        Border border = new Border (left,right,up,down);
        doDamageToMovObj(border, xCord,yCord);
        
        return border;
    }
    
    public void doDamageToMovObj(Border border, int xCord,int yCord){
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
            //check for collision and deal damage TODO
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
    
}
