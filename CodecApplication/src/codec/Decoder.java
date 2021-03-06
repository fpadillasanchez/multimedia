/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codec;

import control.CodecConfig;
import image_processing.FilterManager;
import io.FileIO;
import io.FrameData;
import io.MovementsData;
import io.PathComparator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/***
 * 
 * @author Sergi Diaz
 */
public class Decoder {

    // Perform the decoding of a video directed by input. Place the images at output directory.
    public static void decode(String input, String output) throws IOException, FileNotFoundException, ClassNotFoundException, Exception {
        // Abort if input is not a video generated using this codec
        if (!FileIO.getExtension(input).equals(CodecConfig.video_format)) {
            System.out.println("input file format not supported!");
            throw new Exception("input file format not supported!");
        }

        ArrayList<String> image_files;  // paths to image files
        MovementsData mov_data = null;  // movements data from all frames
        File temp_dir;                  // directory used for storing temporary files

        temp_dir = new File(output + File.separator + CodecConfig.decoder_sub_directory);
        temp_dir.mkdir();         // create temporary directory

        FileIO.decompress(input, temp_dir.getAbsolutePath()); // extract frame data into the tenp directory
        
        // Classify images bewteen DATA file and images
        image_files = new ArrayList<>();
        for (File file : temp_dir.listFiles()) {
            if (file.getName().equals("DATA")) {    // file is DATA
                mov_data = MovementsData.load(file.getAbsolutePath());
            } else {                                // file is an image
                image_files.add(file.getAbsolutePath());
            }
        }
        if (mov_data == null) {
            throw new Exception("DATA file could not be found");
        }
        image_files.sort(new PathComparator(CodecConfig.encoder_sub_directory));
        
        int counter = 0;
        while(!image_files.isEmpty()) {
            String refer_path = image_files.remove(0);  // reference image
            
            FrameData refer_data = new FrameData(counter, FileIO.readImage(refer_path));
            refer_data.setTileMap(MotionDetector.tesselate(refer_data.getImage()));
            refer_data.setMovements((int[][][]) mov_data.get());
            
            // Store reference image after applying desired filter and move on
            FileIO.writeImage(FilterManager.filtrate(refer_data.getImage()), 
                    output + File.separator + counter);
            counter++;
            
            // Delete temp file
            (new File(refer_path)).delete();
            
            // Compute each frame between references
            for (int i=0; i<CodecConfig.gop; i++) {
                String path = image_files.remove(0);  // next frame
                 
                FrameData frame_data = new FrameData(counter, FileIO.readImage(path));
                frame_data.setTileMap(MotionDetector.tesselate(frame_data.getImage()));
                frame_data.setMovements((int[][][]) mov_data.get());

                // Retrieve original image associated to this frame
                BufferedImage retrieved = retrieveImage(refer_data, frame_data);

                // Store retrieved image after applying desired filter and move on
                FileIO.writeImage(FilterManager.filtrate(retrieved), output + File.separator + counter);
                counter++;
                
                // Delete temp file
                (new File(path)).delete();
            }
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
