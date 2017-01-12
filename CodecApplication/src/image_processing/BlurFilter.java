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
 * 
 * @author gondu
 */
public class BlurFilter extends LinearTransformation {
    
    public BlurFilter(BufferedImage image) {
        this.image = image;
        setMask();
    }
    
    public BlurFilter(String image) throws IOException {
        this.image = ImageIO.read(new File(image));
        setMask();
    }
    
    @Override
    protected void setMask() {
        float[][] blur = {{0, 0.2f, 0}, {0.2f, 0.2f, 0.2f}, {0, 0.2f, 0}};
        mask = blur;
    }
}
