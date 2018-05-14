/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.Timer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 *
 * @author simon
 */
public class Bomb extends MapObject{
 
    Character owner;
    
    public Bomb(Node newBomb, boolean isVisible, boolean collisionEnable, World world, Render render, Character owner) {
        super(newBomb, 0, 0 , isVisible , collisionEnable);
        this.owner = owner;
    }
    
    public void setFuse(int milliseconds,int bombSize, World world, Render render,
            Node bomb, int xCord, int yCord, Pane pane) throws InterruptedException {
        Timer detTimer = new Timer();
        Explosion explosion = new Explosion(bombSize,world, render, 
                getNode(), xCord, yCord, pane, owner); //bomb
        detTimer.schedule(explosion, milliseconds);      
        
    }
}
