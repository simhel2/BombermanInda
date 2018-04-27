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
    
    double speedX = 0;     //make modifiable
    double speedY = 0;
    //int acceleration = 3;   // make modifiable
    double maxSpeed = 10;  //x+y speed? 
   
    
    public MovingObjects(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable){
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
    public void Move(double timeMs){
       double deltaMoveX =  (timeMs* speedX)/1000; 
       double deltaMoveY =  (timeMs* speedY)/1000;
       System.out.println(timeMs);
       //check for collision
       //Move X
       getNode().relocate((int)(getX()+deltaMoveX), getY()); //may become conversionproblem? 
       setX(getX()+deltaMoveX);
       //Move Y
       getNode().relocate(getX(), (int)(getY()+deltaMoveY));  
       setY(getY()+deltaMoveY);
       
       
       
       
    }
    
    
    private boolean CollisionDetection(int posX, int posY, int afterX, int afterY){
        return true;
    }
    
    
    
    public void setY(double toY){
        posY = toY;
    }
    
    public void setX(double toX){
        posX = toX;
    }
}
