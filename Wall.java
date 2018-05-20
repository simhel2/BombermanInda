/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.Node;

/**
 * A class for the walls
 * @author simon
 */

public class Wall extends MapObject {
    /**
     * 
     * @param graphic the visual representation of the wall
     * @param posX the x position of the wall
     * @param posY the y position of the wall
     * @param isVisible if the wall should be visible
     * @param collisionEnable  if the wall should have collision enabled
     */
    public Wall(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable){
        super(graphic, posX, posY, isVisible, collisionEnable);

    }



}
