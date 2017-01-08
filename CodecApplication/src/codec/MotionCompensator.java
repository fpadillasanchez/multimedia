/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codec;

import control.CodecConfig;
import io.FrameData;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author SDP
 */
public class MotionCompensator {

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
            frame.setTileMap(tesselate(reference.getImage()));          // tesselate frame
            frame.setMovements(getMovements(reference, frame));         // compute movements   

            counter++;
            frames.add(frame);
        }

        return frames;
    }

    // Tesselate given image. Return tesselation matrix
    public static int[][] tesselate(BufferedImage image) {
        int[][] tilemap;                // tile matrix
        int w = CodecConfig.n_tiles_x;  // matrix size;
        int h = CodecConfig.n_tiles_y;

        tilemap = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                // Use average color as a metric for tile evaluation
                tilemap[i][j] = evaluate(image, i, j);
            }
        }

        return tilemap;
    }

    // Sets value of (x,y) tile as the average color value in the tile.
    private static int evaluate(BufferedImage image, int x, int y) {
        // Tile size in pixels
        int tileWidth = Math.max(1, (int) Math.ceil((float) image.getWidth() / CodecConfig.n_tiles_x));
        int tileHeight = Math.max(1, (int) Math.ceil((float) image.getHeight() / CodecConfig.n_tiles_y));

        int size = tileWidth * tileHeight;
        int r = 0;  // average red value
        int g = 0;  // average green value
        int b = 0;  // average blue value
        for (int i = 0; i < tileWidth; i++) {
            for (int j = 0; j < tileHeight; j++) {
                try {
                    Color color = new Color(image.getRGB(i + tileWidth * x, j + tileHeight * y));
                    r += color.getRed();
                    g += color.getGreen();
                    b += color.getBlue();
                } catch (Exception ex) {
                    // pixel coord out of range
                }
            }
        }

        return (new Color(r / size, g / size, b / size)).getRGB();
    }

    private static int[][][] getMovements(FrameData refer_frame, FrameData other_frame) {
        int[][] refer_tilemap = refer_frame.getTileMap();   // tilemaps
        int[][] other_tilemap = other_frame.getTileMap();

        int[][][] movements;                               // movements matrix

        int w = CodecConfig.n_tiles_x;                      // matrix size;
        int h = CodecConfig.n_tiles_y;
        float tolerance = CodecConfig.quality * w * h;      // tolerance

        /* Matrix holds movement vectors for each tile in the original image (this).
         * Such vectors point to destination position in the matrix and use current
         * position as a reference.
         */
        movements = new int[w][h][2];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                // Don't bother to search for the tile in the other image if tile values are similar.
                if (Math.abs(refer_tilemap[i][j] - other_tilemap[i][j]) < tolerance) {
                    movements[i][j][0] = 0; // does not require movement
                    movements[i][j][1] = 0;

                    // Delete tile if coincidence
                    deleteTile(other_frame, i, j);
                } else {
                    movements[i][j] = searchTile(refer_tilemap, other_tilemap, i, j, tolerance);
                }
            }
        }

        return movements;
    }

    // Search for the (x,y) tile from reference into another tilemap. Return movement vector
    private static int[] searchTile(int[][] refer_tilemap, int[][] other_tilemap, int x, int y, float tol) {
        int[] vector = {0, 0};

        for (int i = 0; i < CodecConfig.n_tiles_x; i++) {
            for (int j = 0; j < CodecConfig.n_tiles_y; j++) {
                if (Math.abs(refer_tilemap[x][y] - other_tilemap[i][j]) < tol) {
                    vector[0] = i - x;
                    vector[1] = j - y;
                    return vector;      // return if coincidence found
                }
            }
        }
        return vector;
    }

    // Call this function for deleting both the tile and the set of pixels that
    // belong to that tile
    private static void deleteTile(FrameData frame, int x, int y) {
        BufferedImage image = frame.getImage();

        // Tile size in pixels
        int tileWidth = Math.max(1, (int) Math.ceil((float) image.getWidth() / CodecConfig.n_tiles_x));
        int tileHeight = Math.max(1, (int) Math.ceil((float) image.getHeight() / CodecConfig.n_tiles_y));
        Color color = new Color(0, 0, 0);

        for (int i = 0; i < tileWidth; i++) {
            for (int j = 0; j < tileHeight; j++) {
                try {
                    image.setRGB(i + x * tileWidth, j + y * tileHeight, color.getRGB());
                } catch (Exception e) {
                    // Color out of range
                }
            }
        }
        int[][] tesselation = frame.getTileMap();
        frame.setTileMap(tesselation);
    }
}
