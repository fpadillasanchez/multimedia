/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * 
 * @author Fernando Padilla Sergi Diaz
 */
public class FrameTimer extends TimerTask {

    VideoPlayer vp;

    public FrameTimer(VideoPlayer vp) {
        this.vp = vp;
    }

    @Override
    public void run() {
        // si fps mayor que 0, secuencia de imágenes positivo
        if (vp.fps > 0) {
            try {
                
                vp.next();
                // si fps menor que 0, secuencia de imágenes negativo
            } catch (IOException ex) {
                Logger.getLogger(FrameTimer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
