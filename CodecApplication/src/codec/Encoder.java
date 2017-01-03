/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codec;

import image_processing.FilterManager;
import io.FileIO;
import io.ImageBuffer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
    
    public Encoder(String input, String output, int gop, int[] nTiles) {
        setGOP(gop);
        setTiling(nTiles);
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
        int i = (int)Math.sqrt(size);
        nTiles_x = i;
        nTiles_y = i;
    }
    
    // Performs encoding over images in input directory:
    //      input: adress to the input zip
    //      output: directory where the output video will be stored
    //      videoname: name of the output file
    //
    public void encode(String input, String output, String videoname) throws IOException {
        if (images  == null)    // abort if buffer is not initialized
            return;
        
        // Temporary directory, where temporary images get stored
        String temp = output + File.separator + "temp";     // TODO: manage the existance of a temp folder in output dir
        new File(temp).mkdir();
        
        /*
        String dir = output.substring(
                output.lastIndexOf(File.separator) + 1); // output directory, without file name
        */
        ArrayList<File> outImg = new ArrayList<>();

        int counter = 0;
        while (!images.isEmpty()) { // iterate until the buffer has been emptied

            // Obtain the reference image
            BufferedImage reference = images.getImage(false);
            ArrayList<BufferedImage> set = new ArrayList<>(); // interframes
                   
            for (int i=0; i<gop; i++) {
                set.add(images.getImage(false));
            }
            MotionCompensator mot = new MotionCompensator(reference, set, nTiles_x, nTiles_y);   
            mot.motionDetection();
            
            for (BufferedImage img : mot.getImages()) {                         // store images in temp files
                outImg.add(FileIO.storeImage(FilterManager.average(img, 3),     // average image before storing
                        temp + File.separator +  "img_" + counter, FileIO.SupportedFormats.JPEG));
                counter++;
            }
        }

        FileIO.formatedZip(outImg, output + File.separator + videoname);    // compress     
        (new File(temp)).delete();  // delete temporary directory    
    }
    
    public void loadBuffer (ArrayList<String> files) throws IOException {
        images.loadBuffer(files);
    }
    
    public void loadBuffer(String input, String output) throws FileNotFoundException, IOException {
        images.loadBuffer(FileIO.unZip(input, output));   // Load buffer
    }
    
    

}
