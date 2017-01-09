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

/**
 *
 * @author SDP
 */
public class FrameData implements Serializable {

    private int id;                 // frame position in the timeline
    private BufferedImage image;    // image

    // Params relevant to motion detection
    private int[][] tilemap = null;
    private int[][][] movements = null;

    public FrameData(int id, BufferedImage image) {
        this.id = id;
        this.image = image;
    }

    public FrameData(int id, BufferedImage image, int[][] tilemap, int[][][] movements) {
        this.id = id;
        this.image = image;
        this.movements = movements;
        this.tilemap = tilemap;
    }

    // Setter
    public void setTileMap(int[][] tilemap) {
        if (tilemap == null) {
            this.tilemap = new int[CodecConfig.n_tiles_x][CodecConfig.n_tiles_y];
        } else {
            this.tilemap = tilemap;
        }

    }

    public void setMovements(int[][][] mov) {
        if (mov == null) {
            movements = new int[CodecConfig.n_tiles_x][CodecConfig.n_tiles_y][2];
        } else {
            this.movements = mov;
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    // Getters
    public int[][] getTileMap() {
        return tilemap;
    }

    public int[][][] getMovements() {
        return movements;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    // Given the position (x, y) of the tilemap, returns a matrix with the values of the pixels inside the tile
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
                Color color = new Color(image.getRGB(i + x, j + y));

                fragment[i - x][j - y][0] = color.getRed();
                fragment[i - x][j - y][1] = color.getGreen();
                fragment[i - x][j - y][2] = color.getBlue();
            }
        }

        return fragment;
    }

    // Given a matrix containing pixel values, uses that values to color a fraction of the image
    public void setImageFragment(int[][][] fragment, int x, int y) {
        int w = fragment.length;        // fragment width in pixels
        int h = fragment[0].length;     // fragment height in pixels

        // Set image color to match the given fragment
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Color color = new Color(fragment[i][j][0], fragment[i][j][1], fragment[i][j][2]);
                image.setRGB(i + x, j + y, color.getRGB());
            }
        }
    }

    // Return true if frame has movement associated with tile (x, y)
    public boolean hasMovement(int x, int y) {
        int mov[] = movements[x][y];

        return (mov[0] + mov[1] != 0);
    }

    // Store given frame data
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
            }
        }

        ImageIO.write(data.image, "png", out);  // store image

        out.close();        // close streams
        fileOut.close();
    }

    // Retrieve stored frame data
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
