/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
/**
 *
 * @author simon
 */
public class Explosion extends TimerTask{
    private Bomb bomb;
    public Explosion(Bomb bomb) {
        super();
        this.bomb = bomb;
    }
    @Override
    public void run() {
        Platform.runLater(new Runnable(){
            @Override public void run() {
                bomb.detonate();
                
            }
        });
               
    }  

}