/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codec;

import control.CodecConfig;
import io.FrameData;
import io.MovementsData;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * *
 *
 * @author Sergi Diaz
 */
public class MotionDetector {

    public static ArrayList<FrameData> motionDetection(ArrayList<BufferedImage> images, int counter) {
        ArrayList<FrameData> frames = new ArrayList<>();

        // Compute reference frame
        FrameData reference = new FrameData(counter, images.remove(0)); // take reference from set 
        reference.setTileMap(tesselate(reference.getImage()));          // tesselate reference
        reference.setMovements(null);                                   // empty movement matrix
        counter++;
        frames.add(reference);

        // Compute non-referencial frames 
        while (!images.isEmpty()) {
            FrameData frame = new FrameData(counter, images.remove(0)); // take image from set
            frame.setTileMap(tesselate(frame.getImage()));          // tesselate frame
            frame.setMovements(getMovements(reference, frame));         // compute movements   

            counter++;
            frames.add(frame);
        }

        return frames;
    }
    
    public static ArrayList<FrameData> motionDetection_2(MovementsData data, ArrayList<BufferedImage> images, int counter) {
        ArrayList<FrameData> frames = new ArrayList<>();
        
         // Compute reference frame
        FrameData reference = new FrameData(counter, images.remove(0)); // take reference from set 
        reference.setTileMap(tesselate(reference.getImage()));          // tesselate reference
        
        int mov[][][] = new int[1][1][3];                               // empty movement matrix
        data.push(mov);
        
        frames.add(reference);
        counter++;

        // Compute non-referencial frames 
        while (!images.isEmpty()) {
            FrameData frame = new FrameData(counter, images.remove(0)); // take image from set
            frame.setTileMap(tesselate(frame.getImage()));          // tesselate frame         
            
            data.push(getMovements(reference, frame));  // compute movements and add them to movements data

            frames.add(frame);
            counter++;
        }
        
        return frames;
    }
            

    /**
     * *
     * Tesselate given image. Return tesselation matrix
     *
     * @param image
     * @return
     */
    public static int[][] tesselate(BufferedImage image) {
        int[][] tilemap;                // tile matrix, aka tilemap
        int x = CodecConfig.n_tiles_x;  // matrix size
        int y = CodecConfig.n_tiles_y;

        tilemap = new int[x][y];

        // Compute each tile value using 'evaluate' function
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                tilemap[i][j] = evaluate(image, i, j);
            }
        }

        return tilemap; // return the tilemap
    }

    /**
     * *
     * Sets value of (x,y) tile as the average color value in the tile.
     *
     * @param image
     * @param x
     * @param y
     * @return
     */
    private static int evaluate(BufferedImage image, int x, int y) {
        int r = 0;  // red component of color
        int g = 0;  // green component
        int b = 0;  // blue component

        // Tile size in pixels
        int w = Math.min(Math.max(1, (image.getWidth() / CodecConfig.n_tiles_x)), CodecConfig.n_tiles_x);
        int h = Math.min(Math.max(1, (image.getHeight() / CodecConfig.n_tiles_y)), CodecConfig.n_tiles_y);

        // Iterate through pixels inside the tile
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color color = new Color(image.getRGB(i + x * w, j + y * h));
                r += color.getRed();
                g += color.getGreen();
                b += color.getBlue();
            }
        }
        // Obtain average RGB values
        int size = w * h;
        r /= size;
        g /= size;
        b /= size;

        return (new Color(r, g, b).getRGB());   // return RGB
    }

    /**
     * *
     *
     * @param refer_frame
     * @param other_frame
     * @return
     */
    public static int[][][] getMovements(FrameData refer_frame, FrameData other_frame) {
        int[][] refer_tilemap = tesselate(refer_frame.getImage());   // tilemaps
        int[][] other_tilemap = tesselate(other_frame.getImage());

        int[][][] movements;                               // movements matrix
        boolean[][] visited;                               // visited tiles

        int w = CodecConfig.n_tiles_x;                      // matrix size;
        int h = CodecConfig.n_tiles_y;
        float tolerance = CodecConfig.quality;      // tolerance

        /* Matrix holds movement vectors for each tile in the original image (this).
         * Such vectors point to destination position in the matrix and use current
         * position as a reference.
         */
        movements = new int[w][h][3];
        visited = new boolean[w][h];

        // Init visited matrix
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                visited[i][j] = false;
            }
        }

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (visited[i][j]) {
                    continue;
                }

                if (Math.abs(refer_tilemap[i][j] - other_tilemap[i][j]) < tolerance) {
                    // Delete tile if there is coincidence
                    deleteTile(other_frame, i, j);
                    movements[i][j][0] = 0;
                    movements[i][j][1] = 0;
                    movements[i][j][2] = 0;
                } else {
                    movements[i][j] = searchTile(refer_tilemap, other_tilemap, i, j, tolerance);

                    // Tile can be found in the reference
                    if (movements[i][j][0] == 0) {
                        // Delete tile in other which is placed at the destination of the movement
                        deleteTile(other_frame, i + movements[i][j][1], j + movements[i][j][2]);
                    }
                }

                visited[i][j] = true;
            }
        }

        return movements;
    }

    /**
     * *
     * Search for the (x,y) tile from reference into another tilemap. Return
     * movement vector
     *
     * @param refer_tilemap
     * @param other_tilemap
     * @param x
     * @param y
     * @param tol
     * @return
     */
    private static int[] searchTile(int[][] refer_tilemap, int[][] other_tilemap, int x, int y, float tol) {
        int[] vector = {1, 0, 0};    // not found

        int counter = CodecConfig.seekRange;
        for (int i = 0; i < CodecConfig.n_tiles_x; i++) {
            for (int j = 0; j < CodecConfig.n_tiles_y; j++) {
                if (counter == 0 && false) {
                    break;  // stop if exceeding search range
                }

                if (Math.abs(refer_tilemap[x][y] - other_tilemap[i][j]) < tol) {
                    vector[0] = 0;      // ressembance found
                    vector[1] = i - x;
                    vector[2] = j - y;
                    return vector;      // return if coincidence found
                }

                counter--;
            }
        }
        return vector;
    }

    /**
     * *
     * Call this function for deleting both the tile and the set of pixels that
     * belong to that tile
     *
     * @param frame
     * @param x
     * @param y
     */
    private static void deleteTile(FrameData frame, int x, int y) {
        BufferedImage image = frame.getImage();

        // Tile size in pixels
        int tileWidth = Math.max(1, (int) Math.ceil((float) image.getWidth() / CodecConfig.n_tiles_x));
        int tileHeight = Math.max(1, (int) Math.ceil((float) image.getHeight() / CodecConfig.n_tiles_y));

        for (int i = 0; i < tileWidth; i++) {
            for (int j = 0; j < tileHeight; j++) {
                try {
                    image.setRGB(i + x * tileWidth, j + y * tileHeight, frame.getRGB());
                } catch (Exception e) {
                    // Color out of range
                }
            }
        }
        int[][] tesselation = frame.getTileMap();
        frame.setTileMap(tesselation);
    }
}
