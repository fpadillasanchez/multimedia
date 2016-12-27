/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Sergi Diaz, Fernando Padilla
 * 
 * TODO: Consider to integrate this feature inside its superclass.
 */
public class AverageFilter extends LinearTransformation {   
    
    private int maskSize;
    
    // Basic builder. Both buffered image and mask size required.
    public AverageFilter(BufferedImage image, int maskSize) {
        this.maskSize = maskSize;
        
        this.image = image;
        setMask();
    }
    
    // Load image from file. Mask size still required.
    public AverageFilter(String image, int maskSize) throws IOException {
        this.image = ImageIO.read(new File(image));
        this.maskSize = maskSize;
        
        setMask();
    }
    
    // Non-loader builder with default mask size 3.
    public AverageFilter(BufferedImage image) {
        this.image = image;
        this.maskSize = 3;
        setMask();
    }
    
    // Loader builder with default mask size 3.
    public AverageFilter(String image) throws IOException {
        this.image = ImageIO.read(new File(image));
        this.maskSize = 3;
        setMask();
    }

    // Compute averaging mask.
    @Override
    protected void setMask() {
        mask = new float[maskSize][maskSize];
        
        float value = 1f / (maskSize * maskSize);
        for (int i=0; i<maskSize; i++) {
            for (int j=0; j<maskSize; j++) {
                mask[i][j] = value;
            }
        } 
    }
}
