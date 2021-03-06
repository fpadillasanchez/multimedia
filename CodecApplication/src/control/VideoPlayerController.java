/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.VideoPlayer;

/***
 * 
 * @author Fernando Padilla Sergi Diaz
 */
public class VideoPlayerController extends Thread {

    public static int fps;
    public static ArrayList<String> imageFiles;
    VideoPlayer vp;

    @Override
    public void run() {
        vp.setVisible(true);                    // show GUI
    }

    /***
     * Builder
     */ 
    public VideoPlayerController() {
        vp = new VideoPlayer(CodecConfig.input, CodecConfig.output);    // init videoplayer

        vp.setFPS(fps);                // set frames per second

        try {   // Try to load buffer
            vp.loadBuffer();
        } catch (IOException ex) {
            Logger.getLogger(VideoPlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /***
     * Method main that starts the thread
     * @param args 
     */
    public static void main(String args[]) {
        (new VideoPlayerController()).start();
    }

}
