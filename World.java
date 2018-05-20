package BombermanInda;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 * Class for handling the main logic and collision behind the world based on a grid matrix system.
 * 
 * @author simon
 */
public class World { 
    private MapObject[][] worldMatrix; //contains all non moving (blocking?) objects over the background (crate)
    private ArrayList<MovingObjects> movingObjects;
    
    
    private final int numCrates;
    private Render render;
    
    //defaults
    private double nudgeRatio = 0.4; //ratio from top or bottom where nudge is allowed, max <1/2
    
            
    /**Create a world with a selected size 
     * 
     * @param x How wide the grid is 
     * @param y How high the grid is
     * @param render The render used for the graphical elements
     * @param numCrates The number of crates in the world
     */
    public World (int x, int y, Render render, int numCrates) {   // can add WorldType w for multiple maps
        worldMatrix = new MapObject[x][y];
        movingObjects = new ArrayList<MovingObjects>();
        this.numCrates = numCrates;
        this.render = render;
        generateWorld();
    } 

    /**
     * Returns the world matrix.
     * @return The worldmatrix.
     */
    public MapObject[][] getWorldMatrix() {
        return worldMatrix;
    }
    /**
     * Returns a list of moving objects in the world.
     * @return A list of all the moving objects in the world
     */
    public ArrayList<MovingObjects> getMovingObjects(){
        return movingObjects;
    }
    
    /**
     * Inserts a mapobject into the world.
     * @param x x coordinate for the insertion
     * @param y y cordinate for the insertion
     * @param insert what mapobject to insert
     */
    public void setObject(int x, int y, MapObject insert) {
        worldMatrix[x][y] = insert;
    }
    /**Inserts a moving object into the world
     * 
     * @param insert what moving object to insert
     */
    public void addMovingObject(MovingObjects insert){
        movingObjects.add(insert);
    }

    /**
     * Generate the worlds play area.
     */
    
    public void generateWorld(){
        //generate wall
        //note that an even x or y causes one row to be without walls
        for (int x=1; x< worldMatrix.length-1; x+=2){
            for (int y=1; y< worldMatrix[0].length-1; y+=2){
                worldMatrix[x][y] = new Wall(render.createGraphicsEntity(Render.GraphicsObjects.WALL)
                                ,0,0,true,true);    
            }
            
        }
        // Generate Crates:
        for(int i = 0; i<numCrates; i++) {
            int x = ThreadLocalRandom.current().nextInt(0,worldMatrix.length);
            int y = ThreadLocalRandom.current().nextInt(0,worldMatrix[0].length);
            
            if(worldMatrix[x][y]==null && x+y >= 2 && x+y <= worldMatrix.length+worldMatrix[0].length-4) {
                worldMatrix[x][y] = new Crate(render.createGraphicsEntity(Render.GraphicsObjects.CRATE)
                         ,0,0,true,true);                       

            } else {       
               i--;    //WARNING this is may become infinite loop with bad params
            }
            
        }
        // fill world with mapObj.
    }
/**
 * Function that is responsible for movement of all characters in the world
 * 
 * @param elapsedTimeMs 
 */
  
    public void moveAllMoveable(long elapsedTimeMs){
        for (MovingObjects movObj: movingObjects) {
            double newX = movObj.getNewAfterMoveX(elapsedTimeMs);
            double newY = movObj.getNewAfterMoveY(elapsedTimeMs);
            double oldX = movObj.getX(); 
            double oldY = movObj.getY(); 
            if(!movObj.isCollisionEnable())        //if collision disabled
                        {
                movObj.Move(newX, newY);
            } else {
                Position pos = moveWithCollision(oldX,oldY,newX,newY,render.getGraphicsWindowX()/worldMatrix.length,

                        render.getGraphicsWindowY()/worldMatrix[0].length, movObj, elapsedTimeMs); 

                movObj.Move(pos.xPos,pos.yPos);
            }
        }
    }
    /**
     * helper class position, used for returning a x,y coordinate
     */
    private class Position{
        public double xPos;
        public double yPos;    
        public Position(double xPos, double yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }
    };
    
    //Notes: should ideally check the entire line becasue otherwise slow logic or
           //high speed may give you noclip, this was scrapped for performance instead we limited maxspeed of character
    //notes: startX and startY for everything is top left
    
    /** Function for moving with collision that also handles nudging people into the lanes. 
     *  Designed specifically to take constant time in respect to amount of things in the world matrix.
     * (except for powerups and other moving objects)
     * 
     * @param startX start X position (not coordinate)
     * @param startY start Y position (not coordinate)
     * @param endX end X position (not coordinate)
     * @param endY end Y position (not coordinate)
     * @param radiusX the diameter of the object in x direction
     * @param radiusY the diameter of the object in y direction
     * @param thisObj the object that is moving
     * @param elapsedTimeMs The time since the last frame
     * @return The new position we reached
     */
    public Position moveWithCollision(double startX, double startY, double endX, double endY,double radiusX, 
                double radiusY, MovingObjects thisObj, long elapsedTimeMs){
        try {
            int startXIndex = (int) (((startX*worldMatrix.length)/render.getGraphicsWindowX()));        
            int startYIndex = (int) ((startY*worldMatrix[0].length)/render.getGraphicsWindowY());        
            int endXIndex = (int) (((endX*worldMatrix.length)/render.getGraphicsWindowX()));        
            int endYIndex = (int) ((endY*worldMatrix[0].length)/render.getGraphicsWindowY());

            //disable collision for bomb you are on top of 
            if(thisObj.getClass()==Character.class && ((Character)thisObj).getOnTopOfBomb()!=null)  {
               //disable collision for bomb
               ((Character)thisObj).getOnTopOfBomb().setCollision(Boolean.FALSE);
            }

            //if not move no collision
            if(startX==endX && startY==endY){
                return new Position(endX, endY); 
            }


            //check for out of bounds X
            if (endX<0 ) {
                return new Position(0, startY);          
            }
            else if( render.getGraphicsWindowX()<endX+radiusX){
                return new Position(render.getGraphicsWindowX()-radiusX, startY);           
            }
            //check for out of bounds Y     
            if ( endY<0 ){
                return new Position(startX, 0); 
            }  
            else if (render.getGraphicsWindowY()< endY+radiusY) {
                return new Position(startX, render.getGraphicsWindowY()-radiusY); 
            }   

            //check for collision with mapObjects

            //moving in +x direction
            if(startX<endX&&endX>endXIndex*render.getGraphicsWindowX()/worldMatrix.length){ //if entering new tile
                //and new tile not empty 
                if(worldMatrix[endXIndex+1][endYIndex]!=null&&worldMatrix[endXIndex+1][endYIndex].isCollisionEnable()){ 

                    //nudge downwards (recursive) 
                    if (endYIndex+1<worldMatrix[0].length && endXIndex+1< worldMatrix.length
                            &&( worldMatrix[endXIndex+1][endYIndex+1]==null
                            ||!worldMatrix[endXIndex+1][endYIndex+1].isCollisionEnable())
                            &&startX == (startXIndex+1)*render.getGraphicsWindowX()/worldMatrix.length-radiusX 
                            &&endY>(radiusY*(1.0-nudgeRatio)+(double)endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length)) { 
                        //calculate new movement 
                        double newY = thisObj.getNewAfterNudgeY(elapsedTimeMs);
                        //only move to edge 
                        newY = Math.min(newY, 
                                radiusY+(double)endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length);
                        //recursively do new movement
                        return moveWithCollision(startX, startY, startX, newY, radiusX, 
                                   radiusY, thisObj, elapsedTimeMs);
                    }            

                    //dont nudge but put next to
                    else {
                        return new Position((endXIndex+1)*render.getGraphicsWindowX()/worldMatrix.length-radiusX, startY);
                    }
                } 

                //check tile under as well
                else if(endY>(endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length)

                        &&worldMatrix[endXIndex+1][endYIndex+1]!=null&&worldMatrix[endXIndex+1][endYIndex+1].isCollisionEnable()){
                    //chek for nudge
                    if(endXIndex+1<worldMatrix.length
                            &&(worldMatrix[endXIndex+1][endYIndex]==null
                            ||!worldMatrix[endXIndex+1][endYIndex].isCollisionEnable())

                            &&startX == (endXIndex+1)*render.getGraphicsWindowX()/worldMatrix.length-radiusX
                            &&endY<(-radiusY*nudgeRatio+((double)endYIndex+1.0)*render.getGraphicsWindowY()/worldMatrix[0].length))  
                         {

                        //calculate new movement 
                        double newY = thisObj.getNewAfterNudgeY(-elapsedTimeMs); //- for move upwards
                        //only move to edge 
                        newY = Math.max(newY, 
                                endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length);
                        //recursively do new movement
                        return moveWithCollision(startX, startY, startX, newY, radiusX, 
                                   radiusY, thisObj, elapsedTimeMs);                    

                    }
                    //dont nudge bug put next to
                    else{
                        return new Position((endXIndex+1)*render.getGraphicsWindowX()/worldMatrix.length-radiusX, startY);
                    }
                }

                }
                //moving in -x direction
                else if(startXIndex>endXIndex){
                    if(worldMatrix[endXIndex][endYIndex]!=null&&worldMatrix[endXIndex][endYIndex].isCollisionEnable()){ 

                        //check for nudging
                        if (endYIndex+1<worldMatrix[0].length
                                &&(worldMatrix[endXIndex][endYIndex+1]==null
                                ||!worldMatrix[endXIndex][endYIndex+1].isCollisionEnable())
                                &&startX == endXIndex*render.getGraphicsWindowX()/worldMatrix.length+radiusX
                                &&endY>(radiusY*(1-nudgeRatio)+(double)endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length)) {

                            //calculate new movement 
                            double newY = thisObj.getNewAfterNudgeY(elapsedTimeMs);
                            //only move to edge 
                            newY = Math.min(newY, 
                                    radiusY+(double)endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length);
                            //recursively do new movement
                            return moveWithCollision(startX, startY, startX, newY, radiusX, 
                                       radiusY, thisObj, elapsedTimeMs);

                        } else { 

                            return new Position(endXIndex*render.getGraphicsWindowX()/worldMatrix.length+radiusX, startY);
                        }
                    }             

                    //check tile under as well 
                    else if(endY>(endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length)

                            &&worldMatrix[endXIndex][endYIndex+1]!=null&&worldMatrix[endXIndex][endYIndex+1].isCollisionEnable()){
                        //check for nudging

                        if ((worldMatrix[endXIndex][endYIndex]==null||
                                !worldMatrix[endXIndex][endYIndex].isCollisionEnable())
                                &&startX==endXIndex*render.getGraphicsWindowX()/worldMatrix.length+radiusX
                                &&endY<(radiusY*(-nudgeRatio)+((double)endYIndex+1.0)*render.getGraphicsWindowY()/worldMatrix[0].length)) {
                            //calculate new movement 
                            double newY = thisObj.getNewAfterNudgeY(-elapsedTimeMs); //- for move upwards
                            //only move to edge 
                            newY = Math.max(newY, 
                                    endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length);
                            //recursively do new movement
                            return moveWithCollision(startX, startY, startX, newY, radiusX, 
                                       radiusY, thisObj, elapsedTimeMs);                    

                        }
                        //put as close as possible
                        else {
                            return new Position(endXIndex*render.getGraphicsWindowX()/worldMatrix.length+radiusX, startY);
                        } 
                    }

                }

                //moving in -y direction
                if(startYIndex>endYIndex){
                    if(worldMatrix[endXIndex][endYIndex]!=null&&worldMatrix[endXIndex][endYIndex].isCollisionEnable()){  
                        if (endXIndex+1<worldMatrix.length
                                &&(worldMatrix[endXIndex+1][endYIndex]==null
                                ||!worldMatrix[endXIndex+1][endYIndex].isCollisionEnable())
                               &&startY == endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length+radiusY
                                &&endX>(radiusX*(1-nudgeRatio)+(double)endXIndex*render.getGraphicsWindowX()/worldMatrix.length)) {

                            //calculate new movement 
                            double newX = thisObj.getNewAfterNudgeX(elapsedTimeMs);
                            //only move to edge 
                            newX = Math.min(newX, 
                                    radiusX+(double)endXIndex*render.getGraphicsWindowX()/worldMatrix.length);
                            //recursively do new movement
                            return moveWithCollision(startX, startY, newX, startY, radiusX, 
                                       radiusY, thisObj, elapsedTimeMs); 
                        }
                        else {
                            return new Position(startX, endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length+radiusY);
                        }
                    } 

                    //check tile right as well 
                    else if(endX>(endXIndex*render.getGraphicsWindowX()/worldMatrix.length)
                        &&worldMatrix[endXIndex+1][endYIndex]!=null&&worldMatrix[endXIndex+1][endYIndex].isCollisionEnable()){
                        //check for nudging 
                        if ((worldMatrix[endXIndex][endYIndex]==null
                                ||!worldMatrix[endXIndex][endYIndex].isCollisionEnable())
                                &&startY ==  endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length+radiusY
                                &&endX<(+radiusX*(1-nudgeRatio)+((double)endXIndex+1.0)*render.getGraphicsWindowX()/worldMatrix.length)) {


                            //calculate new movement 
                            double newX = thisObj.getNewAfterNudgeX(-elapsedTimeMs);
                            //only move to edge 
                            newX = Math.min(newX, 
                                    radiusX+(double)endXIndex*render.getGraphicsWindowX()/worldMatrix.length);
                            //recursively do new movement
                            return moveWithCollision(startX, startY, newX, startY, radiusX, 
                                       radiusY, thisObj, elapsedTimeMs); 

                        }
                        else {
                            return new Position(startX, endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length+radiusY);
                        }
                    }
                }
                //moving in +y  (down)
                else if(startY<endY&&endY>endYIndex*render.getGraphicsWindowY()/worldMatrix[0].length){

                    if(worldMatrix[endXIndex][endYIndex+1]!=null&&worldMatrix[endXIndex][endYIndex+1].isCollisionEnable()){  
                        if (endXIndex+1<worldMatrix.length && endYIndex+1< worldMatrix[0].length
                                &&( worldMatrix[endXIndex+1][endYIndex+1]==null
                                ||!worldMatrix[endXIndex+1][endYIndex+1].isCollisionEnable())
                                &&startY == (startYIndex+1)*render.getGraphicsWindowY()/worldMatrix[0].length-radiusY 
                                &&endX>(radiusX*(1.0-nudgeRatio)+(double)endXIndex*render.getGraphicsWindowX()/worldMatrix.length) ) {
                            //calculate new movement 
                            double newX = thisObj.getNewAfterNudgeX(elapsedTimeMs);
                            //only move to edge 
                            newX = Math.min(newX, 
                                    radiusX+(double)endXIndex*render.getGraphicsWindowX()/worldMatrix.length);
                            //recursively do new movement
                            return moveWithCollision(startX, startY, newX, startY, radiusX, 
                                       radiusY, thisObj, elapsedTimeMs);
                        } 
                        else {
                            return new Position(startX, (endYIndex+1)*render.getGraphicsWindowY()/worldMatrix[0].length-radiusY);
                        }
                    } 



                    else if(endX>(endXIndex*render.getGraphicsWindowX()/worldMatrix.length)
                            &&worldMatrix[endXIndex+1][endYIndex+1]!=null&&worldMatrix[endXIndex+1][endYIndex+1].isCollisionEnable()){
                        if ( endYIndex+1< worldMatrix[0].length
                                &&( worldMatrix[endXIndex][endYIndex+1]==null
                                ||!worldMatrix[endXIndex][endYIndex+1].isCollisionEnable())
                                &&startY == (startYIndex+1)*render.getGraphicsWindowY()/worldMatrix[0].length-radiusY
                                &&endX<(radiusX*(1-nudgeRatio)+(double)endXIndex*render.getGraphicsWindowX()/worldMatrix.length) ) {
                            //calculate new movement 
                            double newX = thisObj.getNewAfterNudgeX(-elapsedTimeMs);
                            //only move to edge 
                            newX = Math.min(newX, 
                                    radiusX+(double)endXIndex*render.getGraphicsWindowX()/worldMatrix.length);
                            //recursively do new movement
                            return moveWithCollision(startX, startY, newX, startY, radiusX, 
                                       radiusY, thisObj, elapsedTimeMs);                    
                        } 
                        else {
                            return new Position(startX, (endYIndex+1)*render.getGraphicsWindowY()/worldMatrix[0].length-radiusY);
                        }
                    }
                }


                
          //check for collision with another moving obj. but not itself
                for (MovingObjects movObj: movingObjects) {
                    //check vs itself 
                    if(!(movObj==thisObj)){
                        //y aligned
                        if((endY<=movObj.getY()&&(endY+radiusY>movObj.getY()))||(endY<=movObj.getY()+radiusY&&(endY>movObj.getY()))){
                            //x aligned
                            if((endX<=movObj.getX()&&(endX+radiusX>movObj.getX()))||(endX<=movObj.getX()+radiusX&&(endX>movObj.getX()))){
                              return new Position(startX, startY);
                            }

                        }



                    }
                    //if character check for collison with powerup. TODO add enemies??
                    if(movObj.getClass() == BombermanInda.Character.class) {
                        if (worldMatrix[movObj.getXIndex(this,render)][movObj.getYIndex(this,render)]!=null){                    
                            if (worldMatrix[movObj.getXIndex(this,render)][movObj.getYIndex(this,render)].getClass()
                                    == PowerUp.class ) {
                                //consume the powerup
                                ((PowerUp)worldMatrix[movObj.getXIndex(this,render)][movObj.getYIndex(this,render)]
                                        ).consume((Character)movObj);
                                  //remove the powerup
                                  render.removeObject(worldMatrix[movObj.getXIndex(this,render)][movObj.getYIndex(this,render)].getNode());
                                  worldMatrix[movObj.getXIndex(this,render)][movObj.getYIndex(this,render)]= null;                 
                            }
                        }

                    }
                }

                return new Position(endX, endY);       
            }
            //finally check if you are not standing on top of bomb anymore
            finally {
                //If standing on bomb       
                //if moved from current tile stop standing on top of bomb
                if(thisObj.getClass()==Character.class
                        &&((Character)thisObj).getOnTopOfBomb()!=null){
                    if (!(thisObj.getX() < ((Character)thisObj).getOnTopOfBomb().getX()+getPixelsPerSquareX() 
                            && thisObj.getX()+getPixelsPerSquareX() > ((Character)thisObj).getOnTopOfBomb().getX() 
                            && thisObj.getY() < ((Character)thisObj).getOnTopOfBomb().getY()+getPixelsPerSquareY()
                            &&thisObj.getY()+getPixelsPerSquareY()> ((Character)thisObj).getOnTopOfBomb().getY())) {
                        ((Character)thisObj).getOnTopOfBomb().setCollision(true);
                        ((Character)thisObj).resetOnTopOfBomb();
                    }    
                    
                }
              
            }
            

        
    }  

    /**
     * Function for removing the logical (but not graphical) crate and randomly creating powerups
     * @param xCord x coordinate of the crate to destroy
     * @param yCord y coordinate of the crate to destroy 
     */
    
    public void destroyCrate(int xCord, int yCord){
        remove(xCord, yCord);

        //replace with random powerup
        int randInt = ThreadLocalRandom.current().nextInt(0, 99);
        
        int probPerPowerup = 10;
         if (randInt>100-probPerPowerup) {    //speed
            //create powerup 
            Node powerGraphic = render.createGraphicsEntity(Render.GraphicsObjects.POWER_SPEED);
            PowerUp powerUp = new PowerUp(powerGraphic, 0, 0, true, false, PowerUp.PowerUps.SPEED); 
            //add it to world
            setObject(xCord, yCord, powerUp);
            //draw it 
            render.drawMapObject(xCord, yCord, this);
        } else if (randInt>100-2*probPerPowerup){     //More bombs
            //create powerup 
            Node powerGraphic = render.createGraphicsEntity(Render.GraphicsObjects.POWER_MORE);
            PowerUp powerUp = new PowerUp(powerGraphic, 0, 0, true, false, PowerUp.PowerUps.MORE); 
            //add it to world
            setObject(xCord, yCord, powerUp);
            //draw it 
            render.drawMapObject(xCord, yCord, this);

        } else if (randInt>100-3*probPerPowerup) {            //bigger bombs
            //create powerup 
            Node powerGraphic = render.createGraphicsEntity(Render.GraphicsObjects.POWER_BIGGER);
            PowerUp powerUp = new PowerUp(powerGraphic, 0, 0, true, false, PowerUp.PowerUps.BIGGER); 
            //add it to world
            setObject(xCord, yCord, powerUp);
            //draw it 
            render.drawMapObject(xCord, yCord, this);
        }
    }    
    /**
     * Function for removing mapobject on a certain place in the world matrix.
     * @param x the x coordinate of the object to be removed
     * @param y the y coordinate of the object to be removed
     */
    public void remove(int x, int y){
        worldMatrix[x][y]= null;
    }
 
    /**
     * Function for clearing the world. 
     */
    public void clearWorld(){
        worldMatrix = null;
        movingObjects = null;

        render = null;
    }
    /**
     * Function that returns how many pixels there are per square in X direction
     * @return how many pixels per square in X direction
     */
    public double getPixelsPerSquareX(){
        return render.getGraphicsWindowX()/worldMatrix.length;
    }
    /**
     * Function that returns how many pixels there are per square in Y direction
     * @return how many pixels per square in Y direction
     */
    public double getPixelsPerSquareY(){
        return render.getGraphicsWindowY()/worldMatrix[0].length;
    }
    /**
     * Function for removing a specific moving object
     * @param movObj the moving object to be removed
     */
    
    public void removeMovingObject(MovingObjects movObj){
        movingObjects.remove(movObj);
    }


}