/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import io.FileIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Sergi Diaz, Fernando Padilla
 */
public abstract class SimpleFilter {
    
    protected BufferedImage image; // image to be filtered
    
    // Build filter by using an already buffered image as argument
    public SimpleFilter(BufferedImage image) {
        this.image = image;
    }
    
    // Load image from file
    public SimpleFilter(String file) throws IOException {
        image = FileIO.readImage(file);
    }
    
    public BufferedImage apply() {
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Iterate through the image pixels
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                // Compute new pixel value
                Color c = transform( new Color(image.getRGB(i, j)));
                image.setRGB(i, j, c.getRGB());
            }
        }
        return image;
    } 
    
    // Abstract method computes new pixel value. Each simple filter uses its own 
    // algorithm.
    protected abstract Color transform(Color color);

}