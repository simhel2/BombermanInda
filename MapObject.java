package BombermanInda;

import javafx.scene.Node;

public abstract class MapObject {
    protected double posX;
    protected double posY;
    private Node graphic; 
    private boolean isVisible;
    private boolean collisionEnable;

    public MapObject(Node graphic, double posX, double posY, boolean isVisible, boolean collisionEnable){
        this.graphic = graphic;
        this.isVisible = isVisible;
        this.collisionEnable = collisionEnable; 
        this.posX = posX;
        this.posY= posY;
        graphic.relocate(posX,posY);
    }

    public void setCollision(boolean b)  {
        collisionEnable = b;
    }
    void setIsVisible(boolean b) {
        isVisible = b;
    }

    public boolean isVisible(){
        return isVisible;
    }

    public boolean isCollisionEnable() {
        return collisionEnable;
    }
    public double getX(){
        return posX;
    }
    public double getY(){
        return posY;
    }
    public Node getNode(){
        return graphic;
    }
    //I don't think all of these has to know position so I changed our idea, only moving chars needs this
}