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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import utils.Buffer;

/***
 * 
 * @author Sergi Diaz
 */
public class Encoder {

    private Buffer buffer;

    public Encoder() {
    }
    /***
     * Performs encoding based on motion detection-compensation.
     * 
     * @param videoname
     * @throws IOException 
     */ 
    public void encode(String videoname) throws IOException {
        String output;          // path to encoded video

        ArrayList<String> encoded_files;        // encoded files
        String temp_dir;                        // directory used by the encoder to store temporary files
        int num_images = 1 + CodecConfig.gop;   // number of images per set
        int counter = 0;                        // image counter

        encoded_files = new ArrayList<>();
        temp_dir = CodecConfig.output + File.separator + CodecConfig.encoder_sub_directory;
        new File(temp_dir).mkdir();     // create temp directory

        MovementsData mov_data = new MovementsData();
        
        // Process buffered images
        while (!buffer.isEmpty()) {
            // Buffered images are divided in sets for compression purposes

            ArrayList<FrameData> frames;                            // frames set
            ArrayList<BufferedImage> images = new ArrayList<>();    // images set
            for (int i = 0; i < num_images; i++) {
                BufferedImage image = buffer.getImage();    // extract image from set
                if (image == null) // stop if buffer is empty
                {
                    break;
                }
                images.add(image);      // add image into the set             
            }

            // Perform motion detection-compensation
            frames = MotionDetector.motionDetection(mov_data, images, counter);

            for (FrameData frame : frames) {    // Store frames in temp directory
                // Apply averaging filter to the frame image before storing
                frame.setImage(FilterManager.average(frame.getImage(), 
                        CodecConfig.codec_avg_mask));   

                String file = temp_dir + File.separator + CodecConfig.encoder_sub_directory + counter;
                FileIO.writeImage(frame.getImage(), file);
                
                //FrameData.store(frame, file);
                encoded_files.add(file);
                counter++;
            }
        }
        
        // Store movements file
        MovementsData.store(mov_data, temp_dir + File.separator + "DATA");
        encoded_files.add(temp_dir + File.separator + "DATA");
        
        // Compress files in temp directory
        output = CodecConfig.output + File.separator + videoname;
        FileIO.compress(temp_dir, output);

        //(new File(temp_dir)).delete();  // delete temporary directory
    }
    
    /***
     * 
     * @throws IOException 
     */
    public void load() throws IOException {
        ArrayList<String> input_paths;  // paths to input images
        input_paths = FileIO.extractImages(CodecConfig.input, CodecConfig.output);  // extract images

        buffer = new Buffer(input_paths, CodecConfig.buffer_size);  // init buffer
        buffer.load();                                              // load buffer
    }

}
