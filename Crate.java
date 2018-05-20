/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Class for destructible crates
 * @author simon
 */


public class Crate extends MapObject {
/**Default constructor
 * 
 * @param graphic The graphical representation of the crate
 * @param posX the actual y position of the crate
 * @param posY The actual x position of the crate 
 * @param isVisible Determines if the crate is visible
 * @param collisionEnable Determines if the crate has collision
 */
    public Crate(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable){
                super(graphic, posX, posY, isVisible, collisionEnable);
                
    }
    
    public void damage (Pane pane, World world, int xCord, int yCord){
        //remove graphic
        pane.getChildren().remove(this.getNode());
        //remove logic
        world.destroyCrate(xCord,yCord);
    }

}
    

