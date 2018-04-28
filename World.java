package BombermanInda;

import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.image.Image;


public class World { 
    private MapObject[][] worldMatrix; //contains all blocking objects over the background (crate)
    
    //WorldType w;
    private Image background;
    private final int numCrates;
    private final Render render;

    public World (int x, int y, Render render, int numCrates, Image background) {   // can add WorldType w for multiple maps
        worldMatrix = new MapObject[x][y];
        this.numCrates = numCrates;
        this.background = background;
        this.render = render;
        generateWorld();
    } 

    public MapObject[][] getWorldMatrix() {
        return worldMatrix;
    }

    public void setObject(int x, int y, MapObject insert) {
        worldMatrix[x][y] = insert;
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
         for(int i = 0; i < worldMatrix.length; ++i) {
            for(int j = 0; j < worldMatrix[i].length; ++j) {
                if(worldMatrix[i][j]!=null) {
                    //if of type movingObjects
                    if((MovingObjects.class).isAssignableFrom(worldMatrix[i][j].getClass())){ 
                        double newX = ((MovingObjects)worldMatrix[i][j]).getNewAfterMoveX(elapsedTimeMs);
                        double newY = ((MovingObjects)worldMatrix[i][j]).getNewAfterMoveY(elapsedTimeMs);

                        if(!worldMatrix[i][j].isCollisionEnable() ||        //if collision disabled
                                posIsClear(newX,newY,20)){  //20 is radius TODO FIX
                            ((MovingObjects)worldMatrix[i][j]).Move(newX, newY);
                        }
                        //TODO else move as close as possible?
                    }
                }
            }
        }
    }
    //left collision works, down collision late, right collision late
    public boolean posIsClear(double X, double Y, double radius){
        int xIndex = (int) (X*worldMatrix.length/render.getGraphicsWindowX());        
        int yIndex = (int) (Y*worldMatrix[0].length/render.getGraphicsWindowY());
        System.out.println(xIndex + ", "+ yIndex );
        //check for out of bounds
        if (xIndex<0 || yIndex<0 || worldMatrix.length-1<xIndex|| worldMatrix[0].length -1< yIndex) {
            return false;
        }
        
        //TODO improve
        if(worldMatrix[xIndex][yIndex]==null) {
            return true;
        } else if (worldMatrix[xIndex][yIndex].getClass()==Crate.class){
            return false;
        }
        else {
            return true;
        }
        
    }
    
    public Image getBackground(){
        return background;
    }
    public void setBackground(Image background){
        this.background= background;
    }
}