/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.Node;

/**
 *
 * @author simon
 */

//TODO: add different types of crates


public class Crate extends MapObject {
    public Crate(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable){
                super(graphic, posX, posY, isVisible, collisionEnable);
                
    }
    
    
// TODO: add destroy functionality / animation ? WARNING NOT CALLED ATM!
public void playDestroyAnim(){
   
}
    
    
}