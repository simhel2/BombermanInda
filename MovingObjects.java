/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import javafx.scene.Node;

/**
 * Abstract class to give standard movement to objects
 * @author humamb, simon
 */


public abstract class MovingObjects extends MapObject{
    
    private double speedX = 0;     
    private double speedY = 0;
    
    //defaults
    private double nudgeSpeedMod = 0.25;
    private double maxSpeed;    
    private double moveDistLimit;
   


   
    /**
     * Create a default moving object
     * @param graphic
     * @param posX
     * @param posY
     * @param maxSpeed
     * @param moveDistLimit
     * @param isVisible
     * @param collisionEnable 
     */
    public MovingObjects(Node graphic, double posX, double posY, double maxSpeed, double moveDistLimit, boolean isVisible, boolean collisionEnable){
        super(graphic, posX, posY, isVisible, collisionEnable);
        this.maxSpeed = maxSpeed;
        this.moveDistLimit= moveDistLimit-1;
    }
    /**
     * Set which direction in x you are currently moving or stop
     * @param direction the direction you want to move in -1 for left, +1 for right for example.
     */
    public void setSpeedXDirection(int direction){
        if (speedX==(-direction)*maxSpeed) {     //stop
           speedX = 0;
        } else {
            speedX = direction*maxSpeed;
            speedY = 0;
        }
        
    }
    /**
     * Set which direction in y you are currently moving or stop
     * @param direction the direction you want to move in -1 for up, +1 for down for example.
     */
    public void setSpeedYDirection(int direction){
        if (speedY==(-direction)*maxSpeed) {     //stop
           speedY = 0;
        } else {
            speedY = direction*maxSpeed;
            speedX = 0;
        }        
    }
    
    /**
     * Move graphical and logical position to new coordinates without checking for collision
     * @param newPosX the new x position
     * @param newPosY the new y position
     */
    public void Move(double newPosX, double newPosY){  
       //Move X
       getNode().relocate((int)newPosX, getY()); 
       setX(newPosX);
       //Move Y
       getNode().relocate(getX(), (int)newPosY); 
       setY(newPosY);
    }   
    /**
     * Function that calculates where you will be after traveling at your current speed since last frame in x
     * @param elapsedTimeMs time since last frame
     * @return the new X position
     */
    public double getNewAfterMoveX(long elapsedTimeMs){
        double deltaMoveX =  (elapsedTimeMs* speedX)/1000;
        //check so we do not move to far and can cause collision bug
        if (deltaMoveX>0) {
            deltaMoveX = Math.min(deltaMoveX, moveDistLimit);
        } else {
            deltaMoveX = Math.max(deltaMoveX, -moveDistLimit);
        }        
        return getX()+deltaMoveX;
    }
    
    /**
     * Function that calculates where you will be after traveling at your current speed since last frame in y
     * @param elapsedTimeMs time since last frame
     * @return the new Y position
     */
    public double getNewAfterMoveY(long elapsedTimeMs){
        double deltaMoveY =  (elapsedTimeMs* speedY)/1000; 
        //check so we do not move to far and can cause collision bug
        if (deltaMoveY>0) {
            deltaMoveY = Math.min(deltaMoveY, moveDistLimit);
        } else {
            deltaMoveY = Math.max(deltaMoveY, -moveDistLimit);
        }
        return getY()+deltaMoveY;
    }
    
    /**
     * Function that calculates where you will be after being nudged at your current speed since last frame in x 
     * @param elapsedTimeMs time since last frame
     * @return the new X position
     */    
    public double getNewAfterNudgeX(long elapsedTimeMs){
        double deltaMoveX =  (elapsedTimeMs* maxSpeed* nudgeSpeedMod)/1000; 
        //check so we do not move to far and can cause collision bug
        if (deltaMoveX>0) {
            deltaMoveX = Math.min(deltaMoveX, moveDistLimit);
        } else {
            deltaMoveX = Math.max(deltaMoveX, -moveDistLimit);
        }        
        return getX()+deltaMoveX;
    }
    /**
     * Function that calculates where you will be after being nudged at your current speed since last frame in y
     * @param elapsedTimeMs time since last frame
     * @return the new Y position
     */
    public double getNewAfterNudgeY(long elapsedTimeMs){
        double deltaMoveY =  (elapsedTimeMs* maxSpeed* nudgeSpeedMod)/1000; 
        //check so we do not move to far and can cause collision bug
        if (deltaMoveY>0) {
            deltaMoveY = Math.min(deltaMoveY, moveDistLimit);
        } else {
            deltaMoveY = Math.max(deltaMoveY, -moveDistLimit);
        } 
        return getY()+deltaMoveY;
    }
    /**
     * Sets the y position of the logic (not graphic) of this object
     * @param toY The new y
     */ 
    public void setY(double toY){
        posY = toY;
    }
    
    /**
     * Sets the x position of the logic (not graphic) of this object
     * @param toX The new x
     */ 
    public void setX(double toX){
        posX = toX;
    }
    /**
     * Function for setting the maxSpeed for this object
     * @param maxSpeed the new max speed
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    /**
     * Get the max speed of this object
     * @return the current max speed
     */
    public double getMaxSpeed(){
        return maxSpeed;
    }
    /**
     * Gets the nudge speed modifier of this object
     * @return the current nudge speed modifier
     */
    
    public double getNudgeSpeedMod(){
        return nudgeSpeedMod;
    }
    
    /**
     * Sets the nudge speed modifier of this object
     * @return the new nudge speed modifier
     */
    
    public void setNudgeSpeedMod(double newSpeedMod){
        nudgeSpeedMod = newSpeedMod;
    }
    
    /**
     * Gets the speed modifier of this object in x
     * @return the current speed in x
     */
    
    public double getSpeedX(){
        return speedX;
    }
    
    
    /**
     * Gets the speed modifier of this object in y
     * @return the current speed in y
     */
    public double getSpeedY(){
        return speedY;
    }

    /**
     * Abstract function that all moving objects has to implement that handles what happens when damage is dealt to them
     */
    abstract void damage();
}
