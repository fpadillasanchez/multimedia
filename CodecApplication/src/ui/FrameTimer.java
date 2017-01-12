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
 * Class which task is to start the video simulation from the images array
 * @author Fernando Padilla Sergi Diaz
 */
public class FrameTimer extends TimerTask {

    VideoPlayer vp;

    public FrameTimer(VideoPlayer vp) {
        this.vp = vp;
    }

    @Override
    public void run() {
        // if fps lower than 0, positive image sequecen
        if (vp.fps > 0) {
            try {
                vp.next();
            } catch (IOException ex) {
                Logger.getLogger(FrameTimer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
