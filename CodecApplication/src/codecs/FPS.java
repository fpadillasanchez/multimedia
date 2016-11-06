/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codecs;
import java.awt.image.BufferedImage;
/**
 *
 * @author Fernando Padilla Sergi Díaz
 */
public class FPS {

    private int frame;
    private BufferedImage image;

    /***
     * 
     * @param frame
     * @param image 
     */
    public FPS(int frame, BufferedImage image) {
        this.frame = frame;
        this.image = image;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    
    
    
    
}
