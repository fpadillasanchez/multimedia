package utils;

import io.FileIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/***
 * BufferedImage buffer implemented as a FILO linked list
 * @author Sergi Diaz
 */
public class Buffer extends Queue {
    
    protected int maxSize = 10; // maximum number of images the buffer can hold
    protected Queue files;      // image paths to be loaded into the buffer
    
    /***
     * Create buffer {mazSize, files}
     * @param files
     * @param maxSize 
     */ 
    public Buffer(ArrayList<String> files, int maxSize) {
        this.maxSize = maxSize;
        this.files = new Queue();
        
        
        for (String path : files) {
            this.files.push(path);
        }
    }
    
    /***
     * Load images directed by paths at files queue into the buffer
     * @throws IOException 
     */
    public void load() throws IOException {
        int counter = 0;
        int numFiles = files.size();
        
        while (counter < maxSize && counter < numFiles) {
            loadImage();        // load image from files
            counter++;
        }
    }
    
    /***
     * Get image from the buffer
     * @return
     * @throws IOException 
     */
    public BufferedImage getImage() throws IOException {
        if (this.isEmpty()) {
            if (files.isEmpty())    // no more images to give 
                return null;          
            loadImage();   // load from files if empty
        }
 
        return (BufferedImage) pop();
    }
    
    /***
     * Removes all images from the buffer
     */ 
    public void flush() {
        removeAll();
    }
    
    /***
     * Extracts image path form files, loads image and pushes it into the buffer queue
     * @throws IOException 
     */
    protected void loadImage() throws IOException {
        this.push(FileIO.readImage((String) files.pop()));
    }
    
}
