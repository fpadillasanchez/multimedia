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
import utils.Queue;

/***
 * 
 * @author Sergi Diaz
 */
public class Decoder {

    // Perform the decoding of a video directed by input. Place the images at output directory.
    public static void decode(String input, String output) throws IOException, FileNotFoundException, ClassNotFoundException, Exception {
        // Abort if input is not a video generated using this codec
        if (!FileIO.getExtension(input).equals(CodecConfig.video_format)) {
            throw new Exception("input file format not supported!");
        }

        File temp_dir;  // directory used for storing temporary files

        temp_dir = new File(output + File.separator + CodecConfig.decoder_sub_directory);
        temp_dir.mkdir();         // create temporary directory

        FileIO.decompress(input, temp_dir.getAbsolutePath()); // extract frame data into the tenp directory

        Queue paths = new Queue();
        for (File file : temp_dir.listFiles()) {
            /*
            // Read frame data from the extracted temporary file
            FrameData frame = FrameData.load(file.getAbsolutePath());
            frame.setTileMap(MotionDetector.tesselate(frame.getImage()));   // compute tilemap

            // TODO: extend decoding 
            String image_path = output + File.separator + Integer.toString(frame.getId());
            FileIO.writeImage(retrieveImage(frame), image_path);

            file.delete();  // delete temporary file
            */
            
            paths.push(file.getAbsolutePath()); // paths to temporary data files
        }
        
        while (!paths.isEmpty()) {
            String referencePath = (String) paths.pop();
            
            // Get reference frame
            FrameData refer = FrameData.load(referencePath);
            refer.setTileMap(MotionDetector.tesselate(refer.getImage()));   // compute tilemap
                 
            // Compute set of frames, which takes previous frame as a reference
            for (int j=0; j<CodecConfig.gop; j++) {
                String path = (String) paths.pop();
                
                FrameData other = FrameData.load(path);
                other.setTileMap(MotionDetector.tesselate(other.getImage()));   // compute tilemap       
                
                // Retrieve image in current frame
                BufferedImage retrieved = retrieveImage(refer, other);
                // Store image 
                String image_path = output + File.separator + Integer.toString(other.getId());
                FileIO.writeImage(retrieved, image_path);
                
                // Delete temporary file
                (new File(path)).delete();
            }
            
            // Delete reference temp file
            (new File(referencePath)).delete();    
        }
        

        temp_dir.delete();  // delete temporary directory
    }

    /***
     * Retrieve images using movements and tiling matrixes
     * @param data
     * @return 
     */
        
    public static BufferedImage retrieveImage(FrameData data) {
        return data.getImage();
    }


    /***
     * Retrieve an image using image placed at other as the subimage, image placed in reference as the reference
     * and the movements matrix placed in other.
     * @param reference
     * @param other
     * @return 
     */
    public static BufferedImage retrieveImage(FrameData reference, FrameData other) {
        // Image size in pixels
        int img_w = other.getImage().getWidth();
        int img_h = other.getImage().getHeight();
        
        // Tile size in pixels
        int tile_w = img_w / CodecConfig.n_tiles_x;
        int tile_h = img_h / CodecConfig.n_tiles_y;
        
        // Traverse through all the pixels in the image of other frame
        for (int i=0; i < img_w; i++) {
            for (int j=0; j < img_h; j++) {
                try {
                    // Access to the movements matrix of other frame
                    int mov[] = other.getMovements()[i / tile_w][j / tile_h];   
                    /*
                    First component of movement vector specifies if the (i,j) pixel
                    is to be taken from reference. If component is 1, pixel in other 
                    will maintain its original value. If component is 0, pixel will
                    be set to match the value of a pixel in reference, taken using
                    the other components in movement as a motion vector from i, j to 
                    the place of the pixel in reference.         
                    */
                    if (mov[0] == 1){
                        continue;   // pixel maintains its value
                    }
                    
                    int color = reference.getImage().getRGB(i + mov[1], j + mov[2]); 
                    other.getImage().setRGB(i, j, color);  
                    
                }catch (ArrayIndexOutOfBoundsException ex) {
                    // Out of bounds
                }
            }
        }
     
        return other.getImage();    // return the retrieved image
    }

}
