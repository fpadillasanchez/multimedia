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
 * @author SDP
 */
public abstract class SimpleFilter {
    
    protected BufferedImage image; 
    
    public SimpleFilter(BufferedImage image) {
        this.image = image;
    }
    
    public SimpleFilter(String file) throws IOException {
        image = FileIO.readImage(file);
    }
    
    public BufferedImage apply() {
        int width = image.getWidth();
        int height = image.getHeight();
        
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                
                Color c = transform( new Color(image.getRGB(i, j)));
                image.setRGB(i, j, c.getRGB());
            }
        }
        return image;
    } 
    
    protected abstract Color transform(Color color);
}