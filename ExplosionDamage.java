/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BombermanInda;

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
    private int tick = 0;
    private int numTicks;
    public ExplosionDamage(Bomb bomb, Bomb.Border border, int xCord, int yCord, int numTicks){
        super();
        this.bomb = bomb;
        this.border = border;
        this.xCord = xCord;
        this.yCord = yCord;
        this.numTicks =numTicks;
        
        
    }

    @Override
    public void run() {
        if(tick < numTicks) {
            Platform.runLater(new Runnable(){
                @Override public void run() {
                    bomb.doDamageToMovObj(border, xCord, yCord);
                    tick++;
                }
            });        
        } else {
            this.cancel();
        
        }
            
    }
    
    
}
