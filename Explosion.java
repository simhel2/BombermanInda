/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
/**
 *
 * @author simon
 */
public class Explosion extends TimerTask{
    private int size;
    private World world;
    private Render render;
    private Node bomb;
    private int xCord;
    private int yCord;
    private Pane pane;
    private Character owner;
    public Explosion(int size, World world, Render render, 
            Node bomb,int xCord, int yCord, Pane pane, Character owner) {
        super();
        this.size = size;
        this.world = world;
        this.render = render;
        this.bomb = bomb;
        this.xCord = xCord;
        this.yCord = yCord;
        this.pane = pane;
        this.owner = owner;
    }
    @Override
    public void run() {
        Platform.runLater(new Runnable(){
            @Override public void run() {
                
                //calc size & do damage
                Border border = doDamageAndCalcSize(xCord, yCord, size);
                 
                
                
                try {
                    render.drawExplosion(xCord, yCord, border.up, border.down, border.left, border.right, world);
                    world.remove(xCord, yCord);
                    pane.getChildren().remove(bomb);  
                    owner.removeBomb(bomb);
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(Explosion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
                    leftCord = xCord -left;   //TODO wrong
                    //remove crate
                    pane.getChildren().remove(world.getWorldMatrix()[xCord-i][yCord].getNode());
                    world.destroyCrate(xCord-i,yCord);
                    
                    break;
                } 
                //bomb TODO
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
                //bomb TODO
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
                //bomb TODO
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
                //bomb TODO
            } 
            
            
        }
        //here we have left right up down calculated
        ArrayList<MovingObjects> copyOfMovObjList =  new ArrayList<MovingObjects>(world.getMovingObjects());
        for (MovingObjects movObj:copyOfMovObjList){
            //check for collision and deal damage TODO
            int xIndex = movObj.getXIndex(world, render);
            int yIndex = movObj.getYIndex(world, render);
                
            /**    
            //square hitbox test
            if (leftCord<=xIndex && xIndex <= rightCord && upCord<=yIndex && yIndex <= downCord){
                movObj.damage();
            }
            **/
            
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
