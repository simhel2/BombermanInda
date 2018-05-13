/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

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
    public Explosion(int size, World world, Render render, 
            Node bomb,int xCord, int yCord, Pane pane) {
        super();
        this.size = size;
        this.world = world;
        this.render = render;
        this.bomb = bomb;
        this.xCord = xCord;
        this.yCord = yCord;
        this.pane = pane;
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
                    pane.getChildren().remove(world.getWorldMatrix()[xCord-i][yCord].getNode());
                    world.remove(xCord-i,yCord); //TODO destroyCrate
                    
                    break;
                } 
                //bomb TODO
                //character TODO         
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
                    world.remove(xCord+i,yCord); //TODO destroyCrate
                    
                    break;
                } 
                //bomb TODO
                //character TODO         
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
                    world.remove(xCord,yCord-i); //TODO destroyCrate
                    
                    break;
                } 
                //bomb TODO
                //character TODO         
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
                    world.remove(xCord,yCord+i); //TODO destroyCrate
                    
                    break;
                } 
                //bomb TODO
                //character TODO         
            } 
        }
        
        
        
        return new Border (left,right,up,down);
    }

}
