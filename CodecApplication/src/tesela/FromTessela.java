/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesela;

import java.io.Serializable;

/**
 *
 * @author Fernando Padilla Sanchez
 */
public class FromTessela implements Serializable {
    private int tileX,tileY,frame,fila,columna;
    /***
     * Constructor que contiene toda la información necesaria para reconstruir una tesela
     * @param tileX
     * @param tileY
     * @param frame
     * @param fila
     * @param columna 
     */
    public FromTessela(int tileX, int tileY, int frame, int fila, int columna) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.frame = frame;
        this.fila = fila;
        this.columna = columna;
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
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
