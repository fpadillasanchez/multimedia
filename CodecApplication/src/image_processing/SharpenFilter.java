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
 * Sharpen filter
 * @author Sergi Diaz
 */
public class SharpenFilter extends LinearTransformation {
    /***
     * Class constructor
     * @param image 
     */
    public SharpenFilter(BufferedImage image) {
        this.image = image;
        setMask();
    }
    /***
     * Class constructor
     * @param image
     * @throws IOException 
     */
    public SharpenFilter(String image) throws IOException {
        this.image = ImageIO.read(new File(image));
        setMask();
    }
    
    @Override
    protected void setMask() {
        mask = new float[][]{{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
    }
    
}
