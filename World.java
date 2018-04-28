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
               i--;    //this is may become infinite loop with bad params
            }
            
        }
        // fill world with mapObj.
    }
    public void moveAllMoveable(long elapsedTimeMs){
         for(int i = 0; i < worldMatrix.length; ++i) {
            for(int j = 0; j < worldMatrix[i].length; ++j) {
                if(worldMatrix[i][j]!=null) {
                    if(worldMatrix[i][j].getClass()== Character.class ){   //== MovingObjects.class){ //TODO fix
                        ((MovingObjects)worldMatrix[i][j]).Move(elapsedTimeMs);
                    }
                }
            }
        }

    }
    
    public Image getBackground(){
        return background;
    }
    public void setBackground(Image background){
        this.background= background;
    }
}