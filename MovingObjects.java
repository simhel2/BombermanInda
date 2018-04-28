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


public abstract class MovingObjects extends MapObject{
    
    double speedX = 0;     
    double speedY = 0;
    //int acceleration = 3;  
    double maxSpeed = 5;  //x+y speed? 
   
    
    public MovingObjects(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable){
        super(graphic, posX, posY, isVisible, collisionEnable);
    }
    
    public void setSpeedXDirection(int direction){
        if (speedX==(-direction)*maxSpeed) {     //stop
           speedX = 0;
        } else {
            speedX = direction*maxSpeed;
            speedY = 0;
        }
        
    }
    public void setSpeedYDirection(int direction){
        if (speedY==(-direction)*maxSpeed) {     //stop
           speedY = 0;
        } else {
            speedY = direction*maxSpeed;
            speedX = 0;
        }        
    }
    
 
    //time = time since last frame
    /**Function for moving MovingObjects of any type, based on time since last frame and their speed
     * 
     * @param timeMs 
     */
    public void Move(double timeMs){
       double deltaMoveX =  (timeMs* speedX)/1000; 
       double deltaMoveY =  (timeMs* speedY)/1000;
       //calc newpos here
       double newPosX = getX()+deltaMoveX;
       double newPosY = getY()+deltaMoveY;
       //check for collision
       if (checkMoveForCollision(newPosX, newPosY)) {//final
          //Move X
          getNode().relocate((int)newPosX, getY()); //may become conversionproblem? 
          setX(newPosX);
          //Move Y
          getNode().relocate(getX(), (int)newPosY); 
          setY(newPosY);

       }
       
       
    }
    //should not be here TODO 
    
    private boolean checkMoveForCollision(double afterX, double afterY){
        if (false ) {
            //TODO
            //need world here, before xy after xy radius
            return false;
        }
        else {
            return true;
        }
    }
    
    
        
    public void setY(double toY){
        posY = toY;
    }
    
    public void setX(double toX){
        posX = toX;
    }
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
