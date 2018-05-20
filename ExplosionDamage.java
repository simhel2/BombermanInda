/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/**Explosiondamage is a periodical timertask that does damage within the area of a bomb to moveable characters
 * 
 * @author simon
 */
public class ExplosionDamage extends TimerTask {
    private Bomb bomb;
    private Bomb.Border border;
    private int tick = 1;
    private int numTicks;
    private Timer timer;

    public ExplosionDamage(Bomb bomb, Timer timer, Bomb.Border border, int numTicks){
        super();
        this.bomb = bomb;
        this.border = border;
        this.timer = timer;
        this.numTicks =numTicks;
        
        
    }

    /**
     * Run class that is ran periodically and does damage within the area of the bomb.
     */
    @Override
    public void run() {
        if(tick < numTicks) {
            Platform.runLater(new Runnable(){
                @Override public void run() {
                    if (bomb!= null ) {
                        bomb.doDamageToMovObj(border);
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
