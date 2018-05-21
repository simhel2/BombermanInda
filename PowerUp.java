/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import javafx.scene.Node;

/**
 * Class for different powerups that have different effects on a character
 * @author simon
 */
public class PowerUp extends MapObject{

    private PowerUps powerUp;

    /**
     *
     * @param graphic the corresponding node of this powerup
     * @param posX which x-position this is in
     * @param posY which y-position this is in
     * @param isVisible if this is visible
     * @param collisionEnable if this can be collided with
     * @param powerUp which kind of powerup this is
     */
    public PowerUp(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable, PowerUps powerUp) {
        super(graphic, posX, posY, isVisible, collisionEnable);
        this.powerUp = powerUp;
    }



    /**
     * Different kind of powerups
     */
    public enum PowerUps{
        BIGGER,MORE,IMMORTAL, SPEED;
    }

    /**
     * Makes a player use a powerup
     * @param player which player took the powerup
     */
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

    }
    
}
