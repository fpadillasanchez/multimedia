/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import io.FileIO;
import java.io.IOException;
import java.util.ArrayList;

/***
 * Items pushed out of a circular buffer are queued again so that the buffer never empties.
 * @author gondu
 */
public class CircularBuffer extends Buffer {
    /***
     * 
     * @param files
     * @param maxSize 
     */
    public CircularBuffer(ArrayList<String> files, int maxSize) {
        super(files, maxSize);
    }
    
    @Override
    protected void loadImage() throws IOException {
        this.push(FileIO.readImage((String) files.popCircular()));
    }
    
}
