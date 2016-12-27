/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author SDP
 */
public class BinarizationFilter extends SimpleFilter {
    
    private int threshold = -1;

    public BinarizationFilter(BufferedImage image, int threshold) {
        super(image);
        setThreshold(threshold);
    }
    
    public BinarizationFilter(String file, int threshold) throws IOException {
        super(file);
        setThreshold(threshold);
    }
    
    public void setThreshold(int threshold) {
        if(threshold < 0)
            this.threshold= 0;
        else if(threshold > 255)
            this.threshold = 255;
        else
            this.threshold = threshold;
    }

    @Override
    protected Color transform(Color color) {
        if (threshold == -1)
            return color;
        
        // Grayscale value
        int gray = average(color.getRed(), color.getGreen(), color.getBlue());
        
        // Set color as white if value surpasses threshold. Set as black otherwise
        
        if (gray > threshold) {
            gray = 0;
        } else {
            gray = 255;
        }
        
        // Return new value
        return new Color(gray, gray, gray);
    }
    
    private int average(int R, int G, int B) {
        return (R + G + B) / 3;
    }   
}