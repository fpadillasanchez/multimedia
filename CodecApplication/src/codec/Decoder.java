/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codec;

import control.CodecConfig;
import io.FileIO;
import io.FrameData;
import java.awt.Color;
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

    public static BufferedImage retrieveImage(FrameData reference, FrameData other) {

        // TODO: Move this segment outside to avoid tiling reference multiple times
        // Process frame tilemaps
        int refer_tilemap[][];      // reference tilemap
        int other_tilemap[][];      // other frame tilemap

        refer_tilemap = MotionCompensator.tesselate(reference.getImage());
        other_tilemap = MotionCompensator.tesselate(other.getImage());
        reference.setTileMap(refer_tilemap);
        other.setTileMap(other_tilemap);
        // ------------------------------------------------------------------------

        // Tile size in pixels
        /*
        int w = other.getImage().getWidth() / CodecConfig.n_tiles_x;
        int h = other.getImage().getHeight() / CodecConfig.n_tiles_y;

        
        for (int i = 0; i < CodecConfig.n_tiles_x; i++) {
            for (int j = 0; j < CodecConfig.n_tiles_y; j++) {
                // Obtain movements associated to the tile
                int mov[] = other.getMovements()[i][j];
                // Displace tile in reference and move into the other frame
                
                if (mov[0] == 0 || true) {
                    
                    for (int x = (i * w); x < ((i + 1) * w); x++) {
                        for (int y = (j * h); y < ((j + 1) * h); y++) {
                            try {
                                int color = reference.getImage().getRGB(x, y);
                                other.getImage().setRGB(x, y, color);
                            } catch (Exception ex) {
                                // Out of range
                            }
                        }
                    }
                }
            }
        }
        */
        int img_w = other.getImage().getWidth();
        int img_h = other.getImage().getHeight();
        int tile_w = img_w / CodecConfig.n_tiles_x;
        int tile_h = img_h / CodecConfig.n_tiles_y;
        
        for (int i=0; i < img_w; i++) {
            for (int j=0; j < img_h; j++) {
                /*
                try {
                    System.out.println("(i,j)= (" + i + ", " + j + "), " + "mov= (" 
                        + other.getMovements()[i / tile_w][j / tile_h][0] + ", " 
                        + other.getMovements()[i / tile_w][j / tile_h][1] + ")");
                    
                }catch (ArrayIndexOutOfBoundsException ex) {
                    //System.out.println("exception: " + (i / tile_w) + ", " + (j / tile_h));
                } */
                try {
                    int color = reference.getImage().getRGB(i, j);
                    
                    int mov[] = other.getMovements()[i / tile_w][j / tile_h];
                    
                    other.getImage().setRGB(i+mov[0], j+mov[1], color);
                    
                }catch (ArrayIndexOutOfBoundsException ex) {
                    
                }
            }
        }

        return other.getImage();
    }

}
