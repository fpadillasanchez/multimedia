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
    // TODO: Optimization
    public BufferedImage apply() {      
        int width = image.getWidth();
        int height = image.getHeight();
        int radius = mask.length / 2;
        
        // Stores computed pixel value
        int pixel[][][] = new int[width][height][3];
        
        // Iterate through the image pixels
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                // Get the boundaries of the mask
                int x0 = Integer.max(0, i - radius);        // left boundary
                int x1 = Integer.min(width, i + radius);    // right boundary
                int y0 = Integer.max(0, j - radius);        // upper boundary
                int y1 = Integer.min(height, j + radius);   // down boundary
                float R = 0; float G = 0; float B = 0;
                // Iterate though neighboring pixels that lie inside the mask
                for (int x=x0; x<x1; x++) {
                    for (int y=y0; y<y1; y++) {
                        Color c = new Color(image.getRGB(x0, y0));    // neighbor color
                        // Compute new pixel value
                        R += c.getRed() * mask[x - x0][y - y0];
                        G += c.getGreen() * mask[x - x0][y - y0];
                        B += c.getBlue() * mask[x - x0][y - y0];
                    }   
                }
                // Store new value
                pixel[i][j][0] = (int)R; pixel[i][j][1] = (int)G; pixel[i][j][2] = (int)B; 
            }         
        }
        
        // Reassign pixel values
        for (int i=0; i<width; i++)
            for (int j=0; j<height; j++)
                image.setRGB(i, j, new Color(pixel[i][j][0], pixel[i][j][1], pixel[i][j][2]).getRGB());
        
        return image;
        
    }
}
