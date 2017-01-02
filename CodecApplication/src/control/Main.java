/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import codec.Encoder;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import image_processing.FilterManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.VideoPlayer;

/**
 *
 * @author Fernando Padilla, Sergi Diaz
 * 
 * 
 */
public class Main {

    static VideoPlayer vp;  // GUI

    public static void main(String[] args) {   
        
        ArgParser parser = new ArgParser();
        JCommander jCom = null;
        
        try {
            jCom = new JCommander(parser, args);
            
            // Make filter selection
            for (String arg : args) { // following filters require input values
                if (arg.equals("--binarization")) {
                    FilterManager.setFilter(FilterManager.SupportedFilters.Binary, 
                            parser.getAverage(), parser.getBin());
                    break;
                }
                if (arg.equals("--averaging")) {
                    FilterManager.setFilter(FilterManager.SupportedFilters.Average, 
                            parser.getAverage(), parser.getBin());
                    break;
                }  
            } // following inputs do not require input values
            if (parser.isNegative())       // negative filtering
                FilterManager.setFilter(FilterManager.SupportedFilters.Negative);
            else if (parser.isBlur())      // blur filtering
                FilterManager.setFilter(FilterManager.SupportedFilters.Blur);
            else if (parser.isSharpen())   // sharpen filtering
                FilterManager.setFilter(FilterManager.SupportedFilters.Sharpen);
            else if (parser.isLaplacian()) // laplacian filtering
                FilterManager.setFilter(FilterManager.SupportedFilters.Laplacian);
            
            if (parser.help) {
                jCom.usage();
            } else if (parser.decode) {
                decode(!parser.batch, parser.getInput(), parser.getOutput(), parser.getFPS()); // do not show GUI if batch
            } else if (parser.encode) {
                encode(parser.getInput(), parser.getOutput(), "my_video.zip");
            }

        } catch (ParameterException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Try --help for help.");
        }
        
        
        /*
        String input = "C:\\Users\\SDP\\Documents\\GitHub\\multimedia\\CodecApplication\\src\\zips\\Imagenes.zip";
        String output = "C:\\Users\\SDP\\Documents\\GitHub\\multimedia\\CodecApplication\\src\\unzip";

        encode(input, output, "my_video.zip");   
        */
    }

    // Decodes ZIP and loades images into the videoplayer GUI.
    private static void decode(boolean visible, String input, String output, int fps) {
        // Set videoplayer parameters
        // TODO: Take these parameters, held at CodecConfig, from VideoPlayer
        VideoPlayer.OUTPUT_FOLDER = output;
        VideoPlayer.INPUT_ZIP = input;
        vp = new VideoPlayer();
        try {
            vp.setFPS(fps);
            // Try to load image buffer
            vp.loadBuffer();
            // Visualize GUI
            vp.setVisible(visible);
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ZIP compression
    private static void encode(String input, String output, String videoname) {
        long t1, t2;    // used for measuring elapsed time
         
        t1 = System.nanoTime();
        try {
            Encoder e = new Encoder();  // initalize encoder.
            e.encode(input, output, videoname);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        t2 = System.nanoTime();
        
        double elapsedTime = (t2 - t1) * 000000.1;
        
        System.out.println("Elapsed time: " + elapsedTime + " ms");
    }
}
