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
    
    private double speedX = 0;     
    private double speedY = 0;
    //private int acceleration = 3;  
    
    //defaults
    private double nudgeSpeedMod = 0.25;
    private double maxSpeed = 1.5;


   
    
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
    

    public void Move(double newPosX, double newPosY){  
       //Move X
       getNode().relocate((int)newPosX, getY()); 
       setX(newPosX);
       //Move Y
       getNode().relocate(getX(), (int)newPosY); 
       setY(newPosY);
    }   
    public double getNewAfterMoveX(long elapsedTimeMs){
        double deltaMoveX =  (elapsedTimeMs* speedX)/1000;
        return getX()+deltaMoveX;
    }
    
    public double getNewAfterMoveY(long elapsedTimeMs){
        double deltaMoveY =  (elapsedTimeMs* speedY)/1000; 
        return getY()+deltaMoveY;
    }
    public double getNewAfterNudgeX(long elapsedTimeMs){
        double deltaMoveX =  (elapsedTimeMs* maxSpeed* nudgeSpeedMod)/1000; 
        return getX()+deltaMoveX;
    }
    public double getNewAfterNudgeY(long elapsedTimeMs){
        double deltaMoveY =  (elapsedTimeMs* maxSpeed* nudgeSpeedMod)/1000; 
        return getY()+deltaMoveY;
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
    public double getMaxSpeed(){
        return maxSpeed;
    }
    public double getNudgeSpeedMod(){
        return nudgeSpeedMod;
    }
    public void setNudgeSpeedMod(double newSpeedMod){
        nudgeSpeedMod = newSpeedMod;
    }
    public double getSpeedX(){
        return speedX;
    }
    
    public double getSpeedY(){
        return speedY;
    }

    abstract void damage();
}
