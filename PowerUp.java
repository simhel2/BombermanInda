/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import javafx.scene.Node;

/**
 *
 * @author simon
 */
public class PowerUp extends MapObject{ 
    private PowerUps powerUp;
    
    public PowerUp(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable, PowerUps powerUp) {
        super(graphic, posX, posY, isVisible, collisionEnable);
        this.powerUp = powerUp;
        
    }
    
    public enum PowerUps{
        BIGGER,MORE,IMMORTAL;
    }
    
}
