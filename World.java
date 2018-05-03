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
            if(worldMatrix[x][y]==null) {   
                try {
                    worldMatrix[x][y] = new Crate(render.createGraphicsEntity(Render.GraphicsObjects.CRATE)
                         ,0,0,true,true);            //could be split for more code clarity
                    //maybe add powerup TODO
                    
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
    /** old and unused
    public void moveAllMoveable(long elapsedTimeMs){
         for(int i = 0; i < worldMatrix.length; ++i) {
            for(int j = 0; j < worldMatrix[i].length; ++j) {
                if(worldMatrix[i][j]!=null) {
                    //if of type movingObjects
                    if((MovingObjects.class).isAssignableFrom(worldMatrix[i][j].getClass())){ 
                        double newX = ((MovingObjects)worldMatrix[i][j]).getNewAfterMoveX(elapsedTimeMs);
                        double newY = ((MovingObjects)worldMatrix[i][j]).getNewAfterMoveY(elapsedTimeMs);
                        double oldX = worldMatrix[i][j].getX(); 
                        double oldY = worldMatrix[i][j].getY(); 
                        if(!worldMatrix[i][j].isCollisionEnable() ||        //if collision disabled
                                lineIsClear(oldX,oldY,newX,newY,20)){  //20 is diameter TODO FIX
                            ((MovingObjects)worldMatrix[i][j]).Move(newX, newY);
                        }
                        //TODO else move as close as possible?
                    }
                }
            }
        }
    }
    **/ 
    //TODO: should ideally put as close as possible
    //TODO: should check the entire line becasue otherwise slow logic or high speed gives you noclip
    //TODO: should fix hitbox for moveable so it does not have to check all of them
    //TODO: TOP and LEFT lineups work, right and down does not
    //notes: startX and startY for everything is top left
    
    public boolean lineIsClear(double startX, double startY, double endX, double endY, double radius, MovingObjects thisObj){
        int startXIndex = (int) (((startX*worldMatrix.length)/render.getGraphicsWindowX()));        
        int startYIndex = (int) ((startY*worldMatrix[0].length)/render.getGraphicsWindowY());        
        int endXIndex = (int) (((endX*worldMatrix.length)/render.getGraphicsWindowX()));        
        int endYIndex = (int) ((endY*worldMatrix[0].length)/render.getGraphicsWindowY()); 
        
        //if not move no collision
        if(startX==endX && startY==endY){
            return true;
        }
        
            
        //check for out of bounds
        if (endX<0 || endY<0 || render.getGraphicsWindowX()<endX+radius|| render.getGraphicsWindowY()< endY+radius+1) {
            return false;
        }
        
        //check for other collision

        //moving in +x direction        
        if(startX<endX&&endX>endXIndex*render.getGraphicsWindowX()/worldMatrix.length){
            if(worldMatrix[endXIndex+1][endYIndex]!=null){ //check for collision enabled!! TODO
                return false; 
            } 
            //check tile under as well supports 1px overlap
            else if(endY>1+(endYIndex*render.getGraphicsWindowY()/worldMatrix.length)&&worldMatrix[endXIndex+1][endYIndex+1]!=null){//check for collision enabled!! TODO
                return false;
            }
        }
        //moving in -x direction
        else if(startXIndex>endXIndex){
            if(worldMatrix[endXIndex][endYIndex]!=null){ //check for collision enabled!! TODO
                return false; 
            } 
            //check tile under as well supports 1px overlap
            else if(endY>1+(endYIndex*render.getGraphicsWindowY()/worldMatrix.length)&&worldMatrix[endXIndex][endYIndex+1]!=null){//check for collision enabled!! TODO
                return false;
            }
        }
        //moving in +y direction
        if(startYIndex>endYIndex){
            if(worldMatrix[endXIndex][endYIndex]!=null){ //check for collision enabled!! TODO
                return false; 
            } 
            //check tile right as well supports 1px overlap
            else if(endX>1+(endXIndex*render.getGraphicsWindowY()/worldMatrix.length)&&worldMatrix[endXIndex+1][endYIndex]!=null){//check for collision enabled!! TODO
                return false;
            }
        }
        //moving in -y direction (supports 1px overlap)
        else if(startY<endY&&endY>1+endYIndex*render.getGraphicsWindowY()/worldMatrix.length){
            if(worldMatrix[endXIndex][endYIndex+1]!=null){ //check for collision enabled!! TODO
                return false; 
            } 
            else if(endX>1+(endXIndex*render.getGraphicsWindowX()/worldMatrix.length)&&worldMatrix[endXIndex+1][endYIndex+1]!=null){//check for collision enabled!! TODO
                return false;
            }
        }
        //check for collision with another moving obj. but not itself!!! 
        for (MovingObjects movObj: movingObjects) {
            //check vs itself 
            if(!(movObj==thisObj)){
                //y aligned
                if(endY>=movObj.getY()&&endY<movObj.getY()+radius){
                    //x aligned
                    if(endX>=movObj.getX()&&endX<movObj.getX()+radius){
                        return false;
                    }

                }
            }
        }
        
        
        return true;        
        
    }  
            
    
    
    public Image getBackground(){
        return background;
    }
    public void setBackground(Image background){
        this.background= background;
    }
}