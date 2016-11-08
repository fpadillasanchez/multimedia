/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.TimerTask;

/**
 * FrameTimer es la clase que dependiendo de si fps es mayor o menor que 0
 * mostrar una secuencia de imágenes en orden o no.
 * @author Fernando Padilla Sergi Díaz
 */
public class FrameTimer extends TimerTask {

    VideoPlayer vp;

    public FrameTimer() {
        this.vp = vp;
    }

    @Override
    public void run() {
        // si fps mayor que 0, secuencia de imágenes positivo
        if (vp.fps > 0) {
            vp.next();
            // si fps menor que 0, secuencia de imágenes negativo
        } else if (vp.fps < 0) {
            vp.previous();

        }
    }

}
