/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.ArrayList;
import java.util.Timer;
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
    public Bomb(Node newBomb, boolean isVisible, boolean collisionEnable, Character owner, Pane pane, Render render,World world,
            int xCord, int yCord, int bombSize, int milliseconds) {
        super(newBomb, 0, 0 , isVisible , collisionEnable);
        this.world = world;
        this.render = render;
        this.xCord = xCord;
        this.yCord  =yCord;
        this.pane = pane;
        this.owner = owner;
        this.bombSize = bombSize;
        
        detTimer = new Timer();
        Explosion explosion = new Explosion(this); 
        detTimer.schedule(explosion, milliseconds); 
        
    }
    
    public void detonate () {
        if (!detonated) {
            //make sure bomb only explodes once
            detonated = true;
            //remove the timer
            detTimer.cancel();
            detTimer.purge();

            //calc size & do damage
            Border border = doDamageAndCalcSize(xCord, yCord, bombSize);

            render.drawExplosion(xCord, yCord, border.up, border.down, border.left, border.right, world);
            //remove logical
            world.remove(xCord, yCord);
            //remove graphical
            pane.getChildren().remove(getNode());  
            //remove from person
            owner.removeBomb(getNode());
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
        
        int leftCord = xCord -size;
        int rightCord= xCord +size; 
        int upCord = yCord -size;
        int downCord= yCord +size;       
        
        //left
        for (int i = 1; i <= size; i++) {
            if (xCord-i <0 ) {
                break;
            }
            if (world.getWorldMatrix()[xCord-i][yCord]!= null && world.getWorldMatrix()[xCord-i][yCord].isCollisionEnable()){
                //crate
                if (world.getWorldMatrix()[xCord-i][yCord].getClass()==Crate.class){              
                    left = size-(size-i);
                    leftCord = xCord -left;   
                    //remove crate
                    ((Crate)world.getWorldMatrix()[xCord-i][yCord]).damage(pane, world, xCord-i, yCord);
                    
                    break;
                } 
                else if(world.getWorldMatrix()[xCord-i][yCord].getClass()==Bomb.class){
                    left = size-(size-i);
                    leftCord = xCord -left;  
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
                    rightCord = xCord + right; 
                    //remove crate
                    pane.getChildren().remove(world.getWorldMatrix()[xCord+i][yCord].getNode());
                    world.destroyCrate(xCord+i,yCord); 
                    
                    break;
                } 
                else if(world.getWorldMatrix()[xCord+i][yCord].getClass()==Bomb.class){
                    right = size-(size-i);
                    rightCord = xCord + right; 
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
                    upCord = yCord -up; 

                    //remove crate
                    pane.getChildren().remove(world.getWorldMatrix()[xCord][yCord-i].getNode());
                    world.destroyCrate(xCord,yCord-i); 
                    
                    break;
                } 
                //bomb
                else if(world.getWorldMatrix()[xCord][yCord-i].getClass()==Bomb.class){
                    up = size-(size-i);
                    upCord = yCord -up; 
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
                    downCord = downCord + down; 

                    //remove crate
                    pane.getChildren().remove(world.getWorldMatrix()[xCord][yCord+i].getNode());
                    world.destroyCrate(xCord,yCord+i); 
                    break;
                } 
                else if(world.getWorldMatrix()[xCord][yCord+i].getClass()==Bomb.class){
                    down = size-(size-i);
                    downCord = downCord + down; 
                    ((Bomb)world.getWorldMatrix()[xCord][yCord+i]).detonate();
                    break;
                }
            }
            
            
            
        }
        //here we have left right up down calculated
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
        
        
        
        
        
        return new Border (left,right,up,down);
    }
}
