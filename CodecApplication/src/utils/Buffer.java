/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import io.FileIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author SDP
 * 
 * BufferedImage buffer implemented as a FILO linked list
 */
public class Buffer extends Queue {
    
    protected int maxSize = 10; // maximum number of images the buffer can hold
    protected Queue files;      // image paths to be loaded into the buffer
    
    // Create buffer
    public Buffer(ArrayList<String> files, int maxSize) {
        this.maxSize = maxSize;
        this.files = new Queue();
        
        
        for (String path : files) {
            this.files.push(path);
        }
    }
    
    // Load images directed by paths at files queue into the buffer
    public void load() throws IOException {
        int counter = 0;
        int numFiles = files.size();
        
        while (counter < maxSize && counter < numFiles) {
            loadImage();        // load image from files
            counter++;
        }
    }
    
    // Get image from the buffer
    public BufferedImage getImage() throws IOException {
        if (this.isEmpty()) {
            if (files.isEmpty())    // no more images to give 
                return null;          
            loadImage();   // load from files if empty
        }
 
        return (BufferedImage) pop();
    }
    
    // Removes all images from the buffer
    public void flush() {
        removeAll();
    }
    
    // Extracts image path form files, loads image and pushes it into the buffer queue
    protected void loadImage() throws IOException {
        this.push(FileIO.readImage((String) files.pop()));
    }
    
}
