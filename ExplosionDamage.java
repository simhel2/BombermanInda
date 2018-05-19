/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/**
 *
 * @author simon
 */
public class ExplosionDamage extends TimerTask {
    private Bomb bomb;
    private Bomb.Border border;
    private int xCord;
    private int yCord;
    private int tick = 1;
    private int numTicks;
    private Timer timer;
    public ExplosionDamage(Bomb bomb, Timer timer, Bomb.Border border, int xCord, int yCord, int numTicks){
        super();
        this.bomb = bomb;
        this.border = border;
        this.xCord = xCord;
        this.yCord = yCord;
        this.timer = timer;
        this.numTicks =numTicks;
        
        
    }

    @Override
    public void run() {
        if(tick < numTicks) {
            Platform.runLater(new Runnable(){
                @Override public void run() {
                    if (bomb!= null ) {
                        bomb.doDamageToMovObj(border, xCord, yCord);
                        tick++;
                    }
                }
            });        
        } else {
            timer.cancel();
            timer.purge();
            this.cancel();
            
        }
            
    }
    
    
}
