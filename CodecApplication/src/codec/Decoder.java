/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codec;

import control.CodecConfig;
import io.FileIO;
import io.FrameData;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author SDP
 */
public class Decoder {
    
    public static void decode(String input, String output) throws IOException, FileNotFoundException, ClassNotFoundException {
        // Abort if input is not a video generated using this codec
        /*
        if (!FileIO.stripExtension(input).equals(CodecConfig.video_format)) {
            // TODO: throw custom codec exception
            return;
        }*/
    
        File temp_dir;  // directory used for storing temporary files
        
        temp_dir = new File(output + File.separator + CodecConfig.decoder_sub_directory);
        temp_dir.mkdir();         // create temporary directory
        
        FileIO.decompress(input, temp_dir.getAbsolutePath()); // extract frame data into the tenp directory
        
        for (File file : temp_dir.listFiles()) {
            // Read frame data from the extracted temporary file
            FrameData frame = FrameData.load(file.getAbsolutePath()); 
            
            // TODO: extend decoding 
            String image_path = output + File.separator + Integer.toString(frame.getId());
            FileIO.writeImage(retrieveImage(frame), image_path);
            
            file.delete();  // delete temporary file
        }
        
        temp_dir.delete();  // delete temporary directory
    }
    
    // TODO: Retrieve images using movements and tiling matrixes
    public static BufferedImage retrieveImage(FrameData data) {
        return data.getImage();
    }
    
    
}
