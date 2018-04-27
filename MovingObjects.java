/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import javafx.scene.Node;

/**
 *
 * @author humamb
 */


public class MovingObjects extends MapObject{
    
    int speedX = 0;     //make modifiable
    int speedY = 0;
    //int acceleration = 3;   // make modifiable
    int maxSpeed = 30;  //x+y speed? 
   
    
    public MovingObjects(Node graphic, int posX, int posY, boolean isVisible, boolean collisionEnable){
        super(graphic, posX, posY, isVisible, collisionEnable);
    }
    
    public void setSpeedX(int direction){
        if (speedX==(-direction)*maxSpeed) {     //stop
           speedX = 0;
        } else {
            speedX = direction*maxSpeed;
            speedY = 0;
        }
        
    }
    public void setSpeedY(int direction){
        if (speedY==(-direction)*maxSpeed) {     //stop
           speedY = 0;
        } else {
            speedY = direction*maxSpeed;
            speedX = 0;
        }        
    }
    
 
    //time = time since last frame
    public void Move(double time){
       double deltaMoveX =  speedX/30;//time* speedX; TODO FIX
       double deltaMoveY =  speedY/30;//time* speedY;
       //check for collision
       //Move X
       getNode().relocate(getX()+(int)deltaMoveX, getY()); //may become conversionproblem? 
       setX(getX()+(int)deltaMoveX);
       //Move Y
       getNode().relocate(getX(), getY()+(int)deltaMoveY);  
       setY(getY()+(int)deltaMoveY);
       
       
       
       
    }
    
    
    private boolean CollisionDetection(int posX, int posY, int afterX, int afterY){
        return true;
    }
    
    
    
    public void setY(int toY){
        posY = toY;
    }
    
    public void setX(int toX){
        posX = toX;
    }
}
