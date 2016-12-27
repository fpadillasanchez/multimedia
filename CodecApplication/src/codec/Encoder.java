/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codec;

import image_processing.FilterManager;
import image_processing.FilterManager.SupportedFilters;
import io.FileIO;
import io.ImageBuffer;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SDP
 */
public class Encoder {    
    //private String input, output;   // input and output files
    
    private int gop = 1;
    
    // Tiling
    private int nTiles_x = 4;
    private int nTiles_y = 4;
    
    // Images
    ImageBuffer images = null;
    
    public Encoder() {
        images = new ImageBuffer();
    }
    
    /*
    public Encoder(String input, String output) {
        this.input = input;
        this.output = output;
    }
    
    public Encoder(String input, String output, int gop, int[] nTiles) {
        this.input = input;
        this.output = output;
        setGOP(gop);
        setTiling(nTiles);
    }
    */
    
    public void debugStore() {
        if (images == null)
            return;
        
        String path = "C:\\Users\\SDP\\Documents\\GitHub\\multimedia\\CodecApplication\\src\\unzip\\DEBUG";
        int counter = images.size();
        while (counter > 0) {
            try {
                //FileIO.storeImage(image, path, FileIO.SupportedFormats.JPEG)
                BufferedImage img = images.getImage(true);
                FileIO.storeImage(img, path + "\\image" + counter + ".jpg", FileIO.SupportedFormats.JPEG);
                counter--;
            } catch (IOException ex) {
                Logger.getLogger(Encoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // Set number of images between two reference images
    public void setGOP(int gop) {
        this.gop = gop;
    }
    
    // Set number of tiles on the X and y axis.
    public void setTiling(int[] args) {
        if (args.length != 2)
            return;
        
        nTiles_x = args[0];
        nTiles_y = args[1];
    }
    
    // Set number of tiles by giving the desired size in pixels as a parameter
    public void setTiling(int size) {
        /*
        int i = (int)Math.sqrt(size);
        nTiles_x = i;
        nTiles_y = i;
        */
    }
    
    public void loadBuffer(String input, String output) throws FileNotFoundException, IOException {
        images.loadBuffer(FileIO.unZip(input, output));      
        average();
        //debugStore();
    }
    
    // Apply averaging filter to all images in buffer
    private void average() {
        int counter = images.size();
        
        while (counter > 0) {
            BufferedImage img = FilterManager.average(images.getImage(false), 3);
            images.pushImage(img);
            counter--;
        }
    }
    
}
