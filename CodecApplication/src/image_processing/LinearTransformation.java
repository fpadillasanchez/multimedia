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
 
        for (int y = 1; y + 1 < image.getHeight(); y++) {
            for (int x = 1; x + 1 < image.getWidth(); x++) {
                Color tempColor = getFilteredValue(y, x);
                image.setRGB(x, y, tempColor.getRGB());
            }
        }
        return image;
    }
    
    // Return pixel color after filtering, input pixel defined by (x,y) coordinates.
    private Color getFilteredValue(int y, int x) {
        int radius = mask.length / 2;
        
        int r = 0, g = 0, b = 0;
        for (int j = 0; j < mask.length; j++) {
            for (int k = 0; k < mask[j].length; k++) {
                try {
                    r += (mask[j][k] * (new Color(image.getRGB(x - radius - k, 
                            y - radius + j))).getRed());
                    g += (mask[j][k] * (new Color(image.getRGB(x - radius + k, 
                            y - radius + j))).getGreen());
                    b += (mask[j][k] * (new Color(image.getRGB(x - radius + k, 
                            y - radius + j))).getBlue());
                }catch(ArrayIndexOutOfBoundsException ex) {
                }
            }
        }
        return new Color(r, g, b);
    }
}
