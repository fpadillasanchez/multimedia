/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import control.CodecConfig;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

/***
 * 
 * @author Sergi Diaz
 */
public class FrameData implements Serializable {

    private int id;                 // frame position in the timeline
    private BufferedImage image;    // image
    private int rgb;              // average value calculated from the whole set of pixels

    /***
     * Params relevant to motion detection
     */
    private int[][] tilemap = null;
    private int[][][] movements = null;
    /* About movements matrix:
    *       - size w * h * 3, where (w, h) are the number of tiles in the x and y axis
    *       - each (i,j) position in the matrix holds a 3D vector (a, b, c):
    *           a = 1 if movement is NULL
    *           b = x component of the movement
    *           c = y component of the movement
    */
    /***
     * 
     * @param id
     * @param image 
     */
    public FrameData(int id, BufferedImage image) {
        this.id = id;
        this.image = image;
        
        rgb = computeColor(); // average color in the image
    }
    /***
     * 
     * @param id
     * @param image
     * @param tilemap
     * @param movements 
     */
    public FrameData(int id, BufferedImage image, int[][] tilemap, int[][][] movements) {
        this.id = id;
        this.image = image;
        this.movements = movements;
        this.tilemap = tilemap;
        
        rgb = computeColor(); // average color in the image
    }

    /***
     * 
     * @param tilemap 
     */
    public void setTileMap(int[][] tilemap) {
        if (tilemap == null) {
            this.tilemap = new int[CodecConfig.n_tiles_x][CodecConfig.n_tiles_y];
        } else {
            this.tilemap = tilemap;
        }

    }
    /***
     * 
     * @param mov 
     */
    public void setMovements(int[][][] mov) {
        if (mov == null) {
            movements = new int[CodecConfig.n_tiles_x][CodecConfig.n_tiles_y][3];
        } else {
            this.movements = mov;
        }
    }
    /***
     * 
     * @param image 
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /***
     * 
     * @return 
     */
    public int[][] getTileMap() {
        return tilemap;
    }
    /***
     * 
     * @return 
     */
    public int[][][] getMovements() {
        return movements;
    }
    /***
     * 
     * @return 
     */
    public BufferedImage getImage() {
        return image;
    }
    /***
     * 
     * @return 
     */
    public int getId() {
        return id;
    }
    /***
     * 
     * @return 
     */    
    public int getRGB() {
        return rgb;
    }

    /***
     * Given the position (x, y) of the tilemap, returns a matrix with the values of the pixels inside the tile
     * @param x
     * @param y
     * @return 
     */
    public int[][][] getImageFragment(int x, int y) {
        int w = image.getWidth() / CodecConfig.n_tiles_x;   // segment width in pixels
        int h = image.getHeight() / CodecConfig.n_tiles_y;  // segment height in pixels

        int fragment[][][] = new int[w][h][3];     // fragment of the image associated with tile (x, y)
        if (image == null || tilemap == null) {
            return fragment;     // return empty segment if it cannot be computed
        }

        // Fill fragment with pixel values inside the tile
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                try {
                    Color c = new Color(image.getRGB(i + x * w, j + y * h));
                    
                    fragment[i][j][0] = c.getRed();
                    fragment[i][j][1] = c.getGreen();
                    fragment[i][j][2] = c.getBlue();
                    
                } catch (ArrayIndexOutOfBoundsException ex) {
                    // out of range
                }
            }
        }

        return fragment;
    }

    /***
     * Given a matrix containing pixel values, uses that values to color a fraction of the image
     * @param fragment
     * @param x
     * @param y 
     */
    public void setImageFragment(int[][][] fragment, int x, int y) {
        int w = fragment.length;        // fragment width in pixels
        int h = fragment[0].length;     // fragment height in pixels

        // Set image color to match the given fragment
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color c = new Color(fragment[i][j][0], fragment[i][j][1], fragment[i][j][2]);
                image.setRGB(i + x * w, j + y * h, c.getRGB());
            }
        }
    }

    /***
     * Return true if frame has movement associated with tile (x, y)
     * @param x
     * @param y
     * @return 
     */ 
    public boolean hasMovement(int x, int y) {
        int mov[] = movements[x][y];

        return (mov[0] + mov[1] != 0);
    }
    
    /***
     * Calculates the average RGB value of the image
     * @return 
     */ 
    public int computeColor() {
        int r = 0;  // average red component
        int g = 0;  // average green 
        int b = 0;  // average blue
        
        // Traverse through all the pixels in the image
        for (int i=0; i<image.getWidth(); i++) {
            for (int j=0; j<image.getHeight(); j++) {
                Color c = new Color(image.getRGB(i, j));
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        
        int size = image.getWidth() * image.getHeight();
        r /= size;
        g /= size;
        b /= size;

        return (new Color(r, g, b)).getRGB();   // return color RGB int
    }
    

    /***
     * Store given frame data
     * @param data
     * @param path
     * @throws FileNotFoundException
     * @throws IOException 
     */ 
    public static void store(FrameData data, String path) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeInt(data.id);                  // store image id

        // We are not storing the tilemap, the image will be re-tesselated during decompression
        // Store movements matrix
        int w = data.movements.length;      // Store matrix size
        int h = data.movements[0].length;
        out.writeInt(w);
        out.writeInt(h);

        for (int i = 0; i < w; i++) {       // Store tilemap data
            for (int j = 0; j < h; j++) {
                out.writeInt(data.movements[i][j][0]);
                out.writeInt(data.movements[i][j][1]);
                out.writeInt(data.movements[i][j][2]);
            }
        }

        ImageIO.write(data.image, "png", out);  // store image

        out.close();        // close streams
        fileOut.close();
    }

    /***
     * Retrieve stored frame data
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */ 
    public static FrameData load(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        BufferedImage img;
        int id, movements[][][];

        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        id = in.readInt();          // read id

        // Read movements matrix
        int w = in.readInt();       // read movements size
        int h = in.readInt();
        movements = new int[w][h][2];

        for (int i = 0; i < w; i++) {       // read movements data
            for (int j = 0; j < h; j++) {
                movements[i][j][0] = in.readInt();
                movements[i][j][1] = in.readInt();
                movements[i][j][2] = in.readInt();
            }
        }

        img = ImageIO.read(in);     // read image

        in.close();     // close streams
        fileIn.close();

        FrameData data = new FrameData(id, img);
        data.setMovements(movements);
        return data;
        //return new FrameData(id, img, tilemap, movements);
    }

}
