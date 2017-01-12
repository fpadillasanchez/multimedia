/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/***
 * 
 * @author Sergi Diaz
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
    
    /***
     * Return pixel color after filtering, input pixel defined by (x,y) coordinates.
     * @param y
     * @param x
     * @return 
     */
    protected Color getFilteredValue(int y, int x) {
        int radius = mask.length / 2;
        
        int r = 0, g = 0, b = 0;
        for (int j = 0; j < mask.length; j++) {
            for (int k = 0; k < mask[j].length; k++) {
                try {
                    r += (mask[j][k] * (new Color(image.getRGB(x - radius + k, 
                            y - radius + j))).getRed());
                    g += (mask[j][k] * (new Color(image.getRGB(x - radius + k, 
                            y - radius + j))).getGreen());
                    b += (mask[j][k] * (new Color(image.getRGB(x - radius + k, 
                            y - radius + j))).getBlue());
                    
                    // Bound RGB values
                    r = Math.min(Math.max(0, r), 255);
                    g = Math.min(Math.max(0, g), 255);
                    b = Math.min(Math.max(0, b), 255);
                }catch(ArrayIndexOutOfBoundsException ex) {
                }
            }
        }
        try {
            return new Color(r, g, b);
        } catch (IllegalArgumentException e) {
            return new Color(0, 0, 0);
        }
        
    }
    
    protected abstract void setMask();
}
