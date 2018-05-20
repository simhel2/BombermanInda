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
 *
 * @author simon
 */
public class RemoveExplosion extends TimerTask{
    private Pane pane;    
    private Node explosion;
    private Timer timer;
    public RemoveExplosion(Pane pane, Node explosion, Timer timer){
        super();
        this.pane = pane;
        this.explosion = explosion;
        this.timer = timer;
    }
            
    
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