/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author SDP
 */
public class LaplacianFilter extends LinearTransformation {
    
    public LaplacianFilter(BufferedImage image) {
        this.image = image;
        setMask();
        grayScale(); // <- comment this line for preventing grayscaling
    }
    
    public LaplacianFilter(String image) throws IOException {
        this.image = ImageIO.read(new File(image));
        setMask();
        grayScale(); // <- comment this line for preventing grayscaling
    }

    @Override
    protected void setMask() {
        /** Uses following Laplace mask:
         * 
         *  [ -1 -1 -1 ]
         *  [ -1  8 -1 ]
         *  [ -1 -1 -1 ] 
         */
        float[][] laplacian = {{-1f, -1f, -1}, {-1f, 8f, -1f}, {-1f, -1f, -1f}};
        mask = laplacian;
    }
    
    // Transforms current image from RGB to grayscale
    private void grayScale() {
        for (int i=0; i<image.getWidth(); i++) {
            for (int j=0; j<image.getHeight(); j++) {
                Color c = new Color(image.getRGB(i, j));
                int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                image.setRGB(i, j, (new Color(gray, gray, gray)).getRGB());
            }
        }
    }    
}
