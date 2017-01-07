/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

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
public class FrameData implements Serializable{
    
    private int id;                 // frame position in the timeline
    private BufferedImage image;    // image
    
    // Params relevant to motion detection
    private int [][][] tilemap = null;
    private int [][][] movements = null;
    
    public FrameData(int id, BufferedImage image) {
        this.id = id;
        this.image = image;
    } 
    
    public FrameData(int id, BufferedImage image, int[][][] tilemap, int[][][] movements) {
        this.id = id;
        this.image = image;
        this.movements = movements;
        this.tilemap = tilemap;
    }
    
    // Setter
    public void setTileMap(int [][][] tilemap) {
        this.tilemap = tilemap;
    }
    
    public void setMovements(int [][][] mov) {
        this.movements = mov;
    } 
    
    // Getters
    public int[][][] getTileMap() {
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
    
    // Store given frame data
    public static void store(FrameData data, String path) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
       
        out.writeInt(data.id);                  // store image id
        
        // Store tilemap             
        int w = data.tilemap.length;    // Store tilemap size
        int h = data.tilemap[0].length;
        out.writeInt(w);  
        out.writeInt(h);
        
        for (int i=0; i<w; i++) {       // Store tilemap data
            for (int j=0; j<h; j++) {
                out.writeInt(data.tilemap[i][j][0]);
                out.writeInt(data.tilemap[i][j][1]);
                out.writeInt(data.tilemap[i][j][2]);
            }
        }  
     
        // Store movements matrix
        w = data.movements.length;      // Store matrix size
        h = data.movements[0].length;
        out.writeInt(w);  
        out.writeInt(h);
        
        for (int i=0; i<w; i++) {       // Store tilemap data
            for (int j=0; j<h; j++) {
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
        int id, tilemap[][][], movements[][][];
        
        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        
        id = in.readInt();          // read id
                
        // Read tilemap
        int w = in.readInt();       // read tilemap size
        int h = in.readInt();
        
        tilemap = new int[w][h][3];
        
        for (int i=0; i<w; i++) {       // read tilemap data
            for (int j=0; j<h; j++) {
                tilemap[i][j][0] = in.readInt();
                tilemap[i][j][1] = in.readInt();
                tilemap[i][j][2] = in.readInt();
            }
        } 
        
        // Read movements matrix
        w = in.readInt();       // read movements size
        h = in.readInt();
        movements = new int[w][h][2];
        
        for (int i=0; i<w; i++) {       // read movements data
            for (int j=0; j<h; j++) {
                movements[i][j][0] = in.readInt();
                movements[i][j][1] = in.readInt();
            }
        } 
        
        img = ImageIO.read(in);     // read image
        
        in.close();     // close streams
        fileIn.close();
        
        return new FrameData(id, img, tilemap, movements);
    }
    
}
