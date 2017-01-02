/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codec;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author SDP
 */
public class MotionCompensator {

    public class SubImage {

        private BufferedImage image;            // image
        private boolean delete;                 // deletion flag 

        private int[][] tesselation;            // tile matrix
        private int tileWidth, tileHeight;      // tile size in pixels

        public SubImage(BufferedImage image, int numTiles_x, int numTiles_y) {
            this.image = image;
                
            tesselation = new int[numTiles_x][numTiles_y];

            // Compute tile size. Keep into account that the last tiles might contain
            // less pixels than the others. It is a way to ensure that all pixels in
            // the image are procesed.
            tileWidth = Math.max(1, (int) Math.ceil((float) image.getWidth() / numTiles_x));
            tileHeight = Math.max(1, (int) Math.ceil((float) image.getHeight() / numTiles_y));

            tesselate();
        }

        // Compute tile matrix
        private void tesselate() {
            // Size of tile in pixels       
            for (int i = 0; i < tesselation.length; i++) {
                for (int j = 0; j < tesselation[0].length; j++) {
                    evaluate(i, j);
                }
            }
        }

        // Sets value of (x,y) tile as the average value in the tile.
        private void evaluate(int x, int y) {
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
            tesselation[x][y] = (new Color(r / size, g / size, b / size)).getRGB();
        }

        // Movement detection-compensation
        private int[][][] compare(SubImage other, float tol) {
            float tolerance = tol * tileWidth * tileHeight;
            int numTiles_x = tesselation.length;
            int numTiles_y = tesselation[0].length;

            // Matrix holds movement vectors for each tile in the original image (this).
            // Such vectors point to destination position in the matrix and use current
            // position as a reference.
            int movements[][][] = new int[numTiles_x][numTiles_y][2];   // 2D vectors

            for (int i = 0; i < numTiles_x; i++) {
                for (int j = 0; j < numTiles_y; j++) {
                    // Don't bother to search for the tile in the other image if tile values are similar.
                    if (Math.abs(tesselation[i][j] - other.tesselation[i][j]) < tolerance) {
                        movements[i][j][0] = 0; // does not require movement
                        movements[i][j][1] = 0;

                        // Delete tile if coincidence
                        //other.tesselation[i][j] = 0;
                        other.deleteTile(i, j);
                    } else {
                        movements[i][j] = searchTile(other, i, j, tolerance);
                    }
                }
            }

            return movements;
        }

        // Returns a 2D vector that contains the displacement necessary for the 
        // (x, y) tile to match that of the other image.
        private int[] searchTile(SubImage other, int x, int y, float tol) {
            int[] vector = {0, 0};

            for (int i = 0; i < other.tesselation.length; i++) {
                for (int j = 0; j < other.tesselation[0].length; j++) {
                    if (Math.abs(tesselation[x][y] - other.tesselation[i][j]) < tol) {
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
        private void deleteTile(int x, int y) {
            Color color = new Color(0, 0, 0);

            for (int i = 0; i < tileWidth; i++) {
                for (int j = 0; j < tileHeight; j++) {
                    try {
                        image.setRGB(i + x * tileWidth, j + y * tileHeight, color.getRGB());
                    } catch (Exception e) {
                    }
                }
            }
            tesselation[x][y] = 0;
        }

        public BufferedImage getImage() {
            return image;
        }

        // FOR TESTING PURPOSE: Stores tiled image into a file with the given path.
        public void DEBUG_Image(String path) {
            BufferedImage debug = image;

            // Apply colors from tesselation
            for (int i = 0; i < debug.getWidth(); i++) {
                for (int j = 0; j < debug.getHeight(); j++) {
                    int x = Math.min(i / tileWidth, tesselation.length - 1);
                    int y = Math.min(j / tileHeight, tesselation[0].length - 1);
                    Color color = new Color(tesselation[x][y]);
                    debug.setRGB(i, j, color.getRGB());
                }
            }

            // Try to store image in a new file
            try {
                File outputfile = new File(path);
                ImageIO.write(debug, "jpg", outputfile);
            } catch (IOException e) {
            }
        }
    }

    private SubImage reference;
    private ArrayList<SubImage> imageSet = new ArrayList<>();

    private SubImage DEBUG_image;

    public MotionCompensator(ArrayList<BufferedImage> images, int tileSize_x, int tileSize_y) {
        for (BufferedImage image : images) {
            imageSet.add(new SubImage(image, tileSize_x, tileSize_y));
        }
    }

    public MotionCompensator(BufferedImage debug_image, int x, int y) {
        DEBUG_image = new SubImage(debug_image, x, y);

        DEBUG_image.tesselate();
    }

    public MotionCompensator(BufferedImage reference, ArrayList<BufferedImage> images, int tileSize_x, int tileSize_y) {
        this.reference = new SubImage(reference, tileSize_x, tileSize_y);
        for (BufferedImage image : images) {
            imageSet.add(new SubImage(image, tileSize_x, tileSize_y));
        }
    }

    public void DEBUG_TesselatedImage(String path) {
        DEBUG_image.DEBUG_Image(path);
    }
    
    public void motionDetection() {
        for (SubImage sub : imageSet) {
            reference.compare(sub, 2500);
        }
    }
    
    public ArrayList<BufferedImage> getImages() {
        ArrayList<BufferedImage> images = new ArrayList<>();
        
        // List composed by reference + set:
        images.add(reference.image);
        for (SubImage sub : imageSet) {
            images.add(sub.image);
        }
        return images;
    } 
    
}
