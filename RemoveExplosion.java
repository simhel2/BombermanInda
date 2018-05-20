/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Timer task that removes the visual explosion after a set time.
 * @author simon
 */
public class RemoveExplosion extends TimerTask{
    private Pane pane;    
    private Node explosion;
    private Timer timer;
    /**
     * Default constructor
     * @param pane the pane to remove the explosion from
     * @param explosion the node to be removed
     * @param timer the timer who called this timer task
     */
    public RemoveExplosion(Pane pane, Node explosion, Timer timer){
        super();
        this.pane = pane;
        this.explosion = explosion;
        this.timer = timer;
    }
            
    /**
     * Function that will run after a set time and remove the explosion
     */
    @Override
    public void run() {
         Platform.runLater(new Runnable(){
            @Override public void run() {        

                if (explosion!= null) {
                    pane.getChildren().remove(explosion);
                    timer.cancel();
                    timer.purge();
                }
            }
         });    
             
    }

}