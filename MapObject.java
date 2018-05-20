package BombermanInda;

import javafx.scene.Node;
/**Abstract class to standardise the objects that can be put on the map with basic functionality.
 * 
 * @author simon
 */

public abstract class MapObject {
    protected double posX;
    protected double posY;
    Node graphic; 
    private boolean isVisible;
    private boolean collisionEnable;
/**Default constructor
 * 
 * @param graphic The graphical representation of the mapobject
 * @param posX the actual y position of the mapobject
 * @param posY The actual x position of the mapobject  
 * @param isVisible Determines if the mapobject is visible
 * @param collisionEnable Determines if the mapobject has collision
 */
    public MapObject(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable){
        this.graphic = graphic;
        this.isVisible = isVisible;
        this.collisionEnable = collisionEnable; 
        this.posX = posX;
        this.posY= posY;
        graphic.relocate(posX,posY);
    }

    /**
     * Sets collision
     * @param b what to set collision to
     */
    public void setCollision(boolean b)  {
        collisionEnable = b;
    }
    /**
     * Sets visibility
     * @param b  what to set visibility to
     */

    void setIsVisible(boolean b) {
        isVisible = b;
    }
    /**
     * Checks if the object is visible 
     * @return The current visiblity status 
     */
    public boolean isVisible(){
        return isVisible;
    }
    /**
     * Checks if the object is collision enabled
     * @return The current collision enabled/disabled status
     */
    public boolean isCollisionEnable() {
        return collisionEnable;
    }
    /**
     * Sets collision for the map object
     * @param bool the new collision status
     */
    public void setCollision(Boolean bool) {
        collisionEnable  = bool;
    }
    /**
     * Get the X position of the object
     * @return The x position of the objects
     */
    public double getX(){
        return posX;
    }
    
    /**
     * Get the Y position of the object
     * @return The Y position of the objects
     */
    public double getY(){
        return posY;
    }
    /**
     * Get the index of the center of the object.
     * @param world the world which the object inhabits
     * @param render the render used for the object
     * @return the y index of the center of the object
     */

    public int getYIndex(World world, Render render){
        return (int) ((((getY()+world.getPixelsPerSquareY()/2)*world.getWorldMatrix()[0].length))
                /render.getGraphicsWindowY());        
    }

    

    /**
     * Get the index of the center of the object.
     * @param world the world which the object inhabits
     * @param render the render used for the object
     * @return the Y index of the center of the object
     */
    public int getXIndex(World world, Render render){
        return (int) ((((getX()+world.getPixelsPerSquareX()/2)*world.getWorldMatrix().length))
                /render.getGraphicsWindowX());        
    }
    
    /**
     * Get the index of the top left of the object.
     * @param world the world which the object inhabits
     * @param render the render used for the object
     * @return the X index of the top left of the object
     */
    public int getTopLeftXIndex(World world, Render render){
        return (int) (((getX()*world.getWorldMatrix().length))
                /render.getGraphicsWindowX());        
    }
    /**
     * Get the index of the top left of the object.
     * @param world the world which the object inhabits
     * @param render the render used for the object
     * @return the Y index of the top left of the object
     */
    public int getTopLeftYIndex(World world, Render render){
        return (int) (((getY()*world.getWorldMatrix()[0].length))
                /render.getGraphicsWindowY());        
    }
    /**
     * Get the graphical node of the object
     * @return The graphical node
     */
    public Node getNode(){
        return graphic;
    }
}