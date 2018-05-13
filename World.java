package BombermanInda;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.Node;
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
                         ,0,0,true,true);                       
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
            if(!movObj.isCollisionEnable())        //if collision disabled
                        {
                movObj.Move(newX, newY);
            } else {
                Position pos = lineIsClear(oldX,oldY,newX,newY,render.getGraphicsWindowX()/render.getNumGrid(),
                        render.getGraphicsWindowY()/render.getNumGrid(), movObj);                 
                movObj.Move(pos.xPos,pos.yPos);
            }
        }
    }
    /**
     * helper class position
     */
    class Position{
        public double xPos;
        public double yPos;    
        public Position(double xPos, double yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }
    };
    
    //TODO: should put as close as possible
    //TODO: should ideally check the entire line becasue otherwise slow logic or high speed gives you noclip
    //TODO: Maybe there is a better way to check moveable?
    //notes: startX and startY for everything is top left
    //notes: maybe move to move?
        public Position lineIsClear(double startX, double startY, double endX, double endY,double radiusX, double radiusY, MovingObjects thisObj){
        int startXIndex = (int) (((startX*worldMatrix.length)/render.getGraphicsWindowX()));        
        int startYIndex = (int) ((startY*worldMatrix[0].length)/render.getGraphicsWindowY());        
        int endXIndex = (int) (((endX*worldMatrix.length)/render.getGraphicsWindowX()));        
        int endYIndex = (int) ((endY*worldMatrix[0].length)/render.getGraphicsWindowY()); 
        
        //if not move no collision
        if(startX==endX && startY==endY){
            return new Position(endX, endY); 
        }
        
        //2 px margin
        int margin = 2;
        //check for out of bounds
        if (endX+margin<0 || endY+margin<0 || render.getGraphicsWindowX()<endX+radiusX-margin|| render.getGraphicsWindowY()< endY+radiusY-margin) {
            return new Position(startX, startY); //TODO math to get close
        }
        
        //check for other collision

        //moving in +x direction        
        if(startX<endX&&endX-margin>endXIndex*render.getGraphicsWindowX()/worldMatrix.length){
            if(worldMatrix[endXIndex+1][endYIndex]!=null&&worldMatrix[endXIndex+1][endYIndex].isCollisionEnable()){ 
                return new Position(startX, startY);
            } 
            //add else if for nudge TODO
            //check tile under as well supports margin overlap
            else if(endY>margin+(endYIndex*render.getGraphicsWindowY()/worldMatrix.length)
                    &&worldMatrix[endXIndex+1][endYIndex+1]!=null&&worldMatrix[endXIndex+1][endYIndex+1].isCollisionEnable()){
                return new Position(startX, startY);
            }
        }
        //moving in -x direction
        else if(startXIndex>endXIndex){
            if(worldMatrix[endXIndex][endYIndex]!=null&&worldMatrix[endXIndex][endYIndex].isCollisionEnable()){ 
                return new Position(startX, startY);
            } 
            //check tile under as well supports margin overlap
            else if(endY>margin+(endYIndex*render.getGraphicsWindowY()/worldMatrix.length)
                    &&worldMatrix[endXIndex][endYIndex+1]!=null&&worldMatrix[endXIndex][endYIndex+1].isCollisionEnable()){
                return new Position(startX, startY);
            }
        }
        //moving in +y direction
        if(startYIndex>endYIndex){
            if(worldMatrix[endXIndex][endYIndex]!=null&&worldMatrix[endXIndex][endYIndex].isCollisionEnable()){  
                return new Position(startX, startY);
            } 
            //check tile right as well supports margin overlap
            else if(endX>margin+(endXIndex*render.getGraphicsWindowY()/worldMatrix.length)
                    &&worldMatrix[endXIndex+1][endYIndex]!=null&&worldMatrix[endXIndex+1][endYIndex].isCollisionEnable()){
                return new Position(startX, startY);
            }
        }
        //moving in -y direction (supports margin overlap)
        else if(startY<endY&&endY-margin>endYIndex*render.getGraphicsWindowY()/worldMatrix.length){
            if(worldMatrix[endXIndex][endYIndex+1]!=null&&worldMatrix[endXIndex][endYIndex+1].isCollisionEnable()){  
                return new Position(startX, startY);
            } 
            else if(endX>margin+(endXIndex*render.getGraphicsWindowX()/worldMatrix.length)
                    &&worldMatrix[endXIndex+1][endYIndex+1]!=null&&worldMatrix[endXIndex+1][endYIndex+1].isCollisionEnable()){
                return new Position(startX, startY);
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
    
    //remove the logical (but not the graphical) crate
    public void destroyCrate(int xCord, int yCord){
        remove(xCord, yCord);

        //replace with random powerup
        int randInt = ThreadLocalRandom.current().nextInt(0, 99);

        if (randInt<50) {   //50% chance to find nothing
             //do nothing
        } else if(randInt>95) {    //??

        } else if(randInt>90) {     //??
             //TODO powerup2
        } else if (randInt>85) {    //speed
            //create powerup 
            Node powerGraphic = render.createGraphicsEntity(Render.GraphicsObjects.POWER_SPEED);
            PowerUp powerUp = new PowerUp(powerGraphic, 0, 0, true, false, PowerUp.PowerUps.SPEED); //xCord*render.getGraphicsWindowX()/render.getNumGrid(), yCord*render.getGraphicsWindowY()/render.getNumGrid()
            //add it to world
            setObject(xCord, yCord, powerUp);
            //draw it 
            render.drawMapObject(xCord, yCord, this);
        } else if (randInt>80){     //More bombs
            //create powerup 
            Node powerGraphic = render.createGraphicsEntity(Render.GraphicsObjects.POWER_MORE);
            PowerUp powerUp = new PowerUp(powerGraphic, 0, 0, true, false, PowerUp.PowerUps.MORE); //xCord*render.getGraphicsWindowX()/render.getNumGrid(), yCord*render.getGraphicsWindowY()/render.getNumGrid()
            //add it to world
            setObject(xCord, yCord, powerUp);
            //draw it 
            render.drawMapObject(xCord, yCord, this);

        } else if (randInt>=75) {            //bigger bombs
            //create powerup 
            Node powerGraphic = render.createGraphicsEntity(Render.GraphicsObjects.POWER_BIGGER);
            PowerUp powerUp = new PowerUp(powerGraphic, 0, 0, true, false, PowerUp.PowerUps.BIGGER); //xCord*render.getGraphicsWindowX()/render.getNumGrid(), yCord*render.getGraphicsWindowY()/render.getNumGrid()
            //add it to world
            setObject(xCord, yCord, powerUp);
            //draw it 
            render.drawMapObject(xCord, yCord, this);
        }
    }    
    
    public void remove(int x, int y){
        worldMatrix[x][y]= null;
    }
    public int getWidth(){
        return worldMatrix.length;
    }
    public int getHeight(){
        return worldMatrix[0].length;
    }    
    
    
    
    public double getPixelsPerSquareX(){
        return render.getGraphicsWindowX()/worldMatrix.length;
    }
     
    public double getPixelsPerSquareY(){
        return render.getGraphicsWindowY()/worldMatrix[0].length;
    }
    
    public void removeMovingObject(MovingObjects movObj){
        movingObjects.remove(movObj);
    }
    
    public Image getBackground(){
        return background;
    }
    public void setBackground(Image background){
        this.background= background;
    }
}