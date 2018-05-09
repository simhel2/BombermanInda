package BombermanInda;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.image.Image;


public class World { 
    private MapObject[][] worldMatrix; //contains all non moving (blocking?) objects over the background (crate)
    private ArrayList<MovingObjects> movingObjects;
    
    //WorldType w;
    private Image background;
    private final int numCrates;
    private final Render render;

    public World (int x, int y, Render render, int numCrates, Image background) {   // can add WorldType w for multiple maps
        worldMatrix = new MapObject[x][y];
        movingObjects = new ArrayList<MovingObjects>();
        this.numCrates = numCrates;
        this.background = background;
        this.render = render;
        generateWorld();
    } 

    public MapObject[][] getWorldMatrix() {
        return worldMatrix;
    }
    public ArrayList<MovingObjects> getMovingObjects(){
        return movingObjects;
    }
    

    public void setObject(int x, int y, MapObject insert) {
        worldMatrix[x][y] = insert;
    }
    public void addMovingObject(MovingObjects insert){
        movingObjects.add(insert);
    }

    public void generateWorld(){
        // TODO improve generation
        
        // Generate Crates:
        for(int i = 0; i<numCrates; i++) {
            int x = ThreadLocalRandom.current().nextInt(0,worldMatrix.length);
            int y = ThreadLocalRandom.current().nextInt(0,worldMatrix[0].length);
            
            if(worldMatrix[x][y]==null ) {   
                try {
                    worldMatrix[x][y] = new Crate(render.createGraphicsEntity(Render.GraphicsObjects.CRATE)
                         ,0,0,true,true);            //could be split for more code clarity                    
                } catch (Error e) {
                    System.out.println(e.getMessage());
                }
            } else {       
               i--;    //WARNING this is may become infinite loop with bad params
            }
            
        }
        // fill world with mapObj.
    }

    public void moveAllMoveable(long elapsedTimeMs){
        for (MovingObjects movObj: movingObjects) {
            double newX = movObj.getNewAfterMoveX(elapsedTimeMs);
            double newY = movObj.getNewAfterMoveY(elapsedTimeMs);
            double oldX = movObj.getX(); 
            double oldY = movObj.getY(); 
            if(!movObj.isCollisionEnable() ||        //if collision disabled
                        lineIsClear(oldX,oldY,newX,newY,25, movObj)){  //25 is diameter TODO FIX
                movObj.Move(newX, newY);
            }
        }
    }
    //TODO: should ideally put as close as possible
    //TODO: should check the entire line becasue otherwise slow logic or high speed gives you noclip
    //TODO: maybe should fix hitbox for moveable so it does not have to check all of them??
    //notes: startX and startY for everything is top left
    //notes: maybe move to move?
    
    public boolean lineIsClear(double startX, double startY, double endX, double endY, double radius, MovingObjects thisObj){
        int startXIndex = (int) (((startX*worldMatrix.length)/render.getGraphicsWindowX()));        
        int startYIndex = (int) ((startY*worldMatrix[0].length)/render.getGraphicsWindowY());        
        int endXIndex = (int) (((endX*worldMatrix.length)/render.getGraphicsWindowX()));        
        int endYIndex = (int) ((endY*worldMatrix[0].length)/render.getGraphicsWindowY()); 
        
        //if not move no collision
        if(startX==endX && startY==endY){
            return true;
        }
        
        //2 px margin
        int margin = 2;
        //check for out of bounds
        if (endX+margin<0 || endY+margin<0 || render.getGraphicsWindowX()<endX+radius-margin|| render.getGraphicsWindowY()< endY+radius-margin) {
            return false;
        }
        
        //check for other collision

        //moving in +x direction        
        if(startX<endX&&endX-margin>endXIndex*render.getGraphicsWindowX()/worldMatrix.length){
            if(worldMatrix[endXIndex+1][endYIndex]!=null&&worldMatrix[endXIndex+1][endYIndex].isCollisionEnable()){ 
                return false; 
            } 
            //check tile under as well supports margin overlap
            else if(endY>margin+(endYIndex*render.getGraphicsWindowY()/worldMatrix.length)
                    &&worldMatrix[endXIndex+1][endYIndex+1]!=null&&worldMatrix[endXIndex+1][endYIndex+1].isCollisionEnable()){
                return false;
            }
        }
        //moving in -x direction
        else if(startXIndex>endXIndex){
            if(worldMatrix[endXIndex][endYIndex]!=null&&worldMatrix[endXIndex][endYIndex].isCollisionEnable()){ 
                return false; 
            } 
            //check tile under as well supports margin overlap
            else if(endY>margin+(endYIndex*render.getGraphicsWindowY()/worldMatrix.length)
                    &&worldMatrix[endXIndex][endYIndex+1]!=null&&worldMatrix[endXIndex][endYIndex+1].isCollisionEnable()){
                return false;
            }
        }
        //moving in +y direction
        if(startYIndex>endYIndex){
            if(worldMatrix[endXIndex][endYIndex]!=null&&worldMatrix[endXIndex][endYIndex].isCollisionEnable()){  
                return false; 
            } 
            //check tile right as well supports margin overlap
            else if(endX>margin+(endXIndex*render.getGraphicsWindowY()/worldMatrix.length)
                    &&worldMatrix[endXIndex+1][endYIndex]!=null&&worldMatrix[endXIndex+1][endYIndex].isCollisionEnable()){
                return false;
            }
        }
        //moving in -y direction (supports margin overlap)
        else if(startY<endY&&endY-margin>endYIndex*render.getGraphicsWindowY()/worldMatrix.length){
            if(worldMatrix[endXIndex][endYIndex+1]!=null&&worldMatrix[endXIndex][endYIndex+1].isCollisionEnable()){  
                return false; 
            } 
            else if(endX>margin+(endXIndex*render.getGraphicsWindowX()/worldMatrix.length)
                    &&worldMatrix[endXIndex+1][endYIndex+1]!=null&&worldMatrix[endXIndex+1][endYIndex+1].isCollisionEnable()){
                return false;
            }
        }
        //check for collision with another moving obj. but not itself!!! 
        for (MovingObjects movObj: movingObjects) {
            //check vs itself 
            if(!(movObj==thisObj)){
                //y aligned
                if((endY<=movObj.getY()&&(endY+radius>movObj.getY()))||(endY<=movObj.getY()+radius&&(endY>movObj.getY()))){
                    //x aligned
                    if((endX<=movObj.getX()&&(endX+radius>movObj.getX()))||(endX<=movObj.getX()+radius&&(endX>movObj.getX()))){
                        return false;
                    }

                }
            }
        }
        
        
        return true;        
        
    }  
    
   public void breakCrate(){
    //TODO actually break the crate
    
    //replace with random powerup
    int x = ThreadLocalRandom.current().nextInt(0, 99);
    
    if (x<75) {   //75% chance to find nothing
        //do nothing
    } else if(x>95) {
        //TODO insert powerup1
    } else if(x>90) {
        //TODO powerup2
    } else if (x>85) {
        //TODO powerup3
    } else if (x>80){
        //TODO powerup4
    } else if (x>=75) {
        //TODO powerup5
    }
}    
    
    
    
    public Image getBackground(){
        return background;
    }
    public void setBackground(Image background){
        this.background= background;
    }
}