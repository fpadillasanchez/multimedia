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

/***
 * Motion Blur filter
 * 
 * @author Sergi Diaz
 */
public class MotionBlurFilter extends LinearTransformation {
    
    public MotionBlurFilter(BufferedImage image) {
        this.image = image;
        setMask();
    }
    /***
     * Clas constructor. Sets the image by the path
     * @param image
     * @throws IOException 
     */
    public MotionBlurFilter(String image) throws IOException {
        this.image = ImageIO.read(new File(image));
        setMask();
    }
    
    @Override
    protected void setMask() {
        mask = new float[][]{{1/9, 0, 0}, {0, 1/9, 0}, {0, 0, 1/9}};
    }
}
