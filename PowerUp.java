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
        BIGGER,MORE,IMMORTAL, SPEED;
    }
    public void consume(Character player){
        //depends on enum

        // Makes explosions larger
        if(powerUp ==  PowerUps.BIGGER) {
            player.improveBombs(1);
        }
        // Adds one more bomb to a players "inventory"
        else if (powerUp ==  PowerUps.MORE) {
            player.addBombLimit(1);
        }
        // Increases the speed of a player
        else if (powerUp ==  PowerUps.SPEED) {
            player.changeSpeed(1.333);
        }
        
        //TODO Add more

        
            
    }
    
}
