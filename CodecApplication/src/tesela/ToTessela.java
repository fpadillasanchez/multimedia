/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesela;

import java.awt.image.BufferedImage;

/**
 *
 * @author Fernando Padilla Sanchez
 */
public class ToTessela {
    
    BufferedImage image;
    private int fila, columna;

    public ToTessela(BufferedImage image, int fila, int columna) {
        this.image = image;
        this.fila = fila;
        this.columna = columna;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
       
}
