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
 * Emboss filter.
 * @author Sergi Díaz
 */
public class EmbossFilter extends LinearTransformation {
    
    public EmbossFilter(BufferedImage image) {
        this.image = image;
        setMask();
    }
    
    public EmbossFilter(String image) throws IOException {
        this.image = ImageIO.read(new File(image));
        setMask();
    }
    
    @Override
    protected void setMask() {
        
        mask = new float[][]{{-1, -1, 0}, {-1, 0, 1}, {0, 1, 1}}; //emboss mask
    }
    
}
