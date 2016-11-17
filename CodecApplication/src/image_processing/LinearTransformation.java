/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Sergi Diaz, Fernando Padilla
 * 
 * Abstract class for linear transformations.
 */
public abstract class LinearTransformation {
    protected float[][] mask;
    protected BufferedImage image; 
    
    // Apply the transformation on the image and returns the image.
    public BufferedImage apply() {
        int width = image.getWidth();
        int height = image.getHeight();
        int radius = mask.length;
        
        // Iterate through the image pixels
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                // Get the boundaries of the mask
                int x0 = Integer.max(0, i - radius);        // left boundary
                int x1 = Integer.min(width, i + radius);    // right boundary
                int y0 = Integer.max(0, j - radius);        // upper boundary
                int y1 = Integer.min(height, j + radius);   // down boundary
                Color c = new Color(image.getRGB(i, j));    // pixel color
                float R = 0; float G = 0; float B = 0;
                // Iterate though neighboring pixels that lie inside the mask
                for (int x=x0; x<x1; x++) {
                    for (int y=y0; y<y1; y++) {
                        // Compute new pixel value
                        R += mask[x][y] * c.getRed();
                        G += mask[x][y] * c.getGreen();
                        B += mask[x][y] * c.getBlue();
                    }   
                }
                // Assign new value to the pixel
                image.setRGB(i, j, (new Color(R, G, B)).getRGB());
            }
        }
        return image;
    }
}
