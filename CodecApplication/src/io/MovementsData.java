/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import utils.Queue;

/**
 *
 * @author Sergi Diaz
 */
public class MovementsData extends Queue implements Serializable {
    
    public MovementsData() {
        super();
    }
    
    public void add(int[][][] movements) {
        push(movements);
    }
    
    public int[][][] get() {
        int [][][] mov = (int [][][]) pop();      
        return mov;
    }
    
    public static void store(MovementsData data, String path) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        
        // Store the number of movement matrixes in data
        out.writeInt(data.size);
        
        // Traverse through al the matrixes in data
        while (data.size != 0) {
            int [][][] mov = data.get();
            
            // movement matrix size
            int w = mov.length;
            int h = mov[0].length;
            // store size of matrix
            out.writeInt(w);
            out.writeInt(h);
            
            // store matrix
            for (int i=0; i<w; i++) {
                for (int j=0; j<h; j++) {
                    out.writeInt(mov[i][j][0]);
                    out.writeInt(mov[i][j][1]);
                    out.writeInt(mov[i][j][2]);
                }
            }
        }
        
        out.close();        // close streams
        fileOut.close();
    }
    
    public static MovementsData load(String path) throws FileNotFoundException, IOException {
        int size;   // number of matrixes to be read
        MovementsData data = new MovementsData();

        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        size = in.readInt();          // read id
        
        // Obtain all the matrixes in the file
        while (size != 0) {
            // read matrix size
            int w = in.readInt();
            int h = in.readInt();
            
            // read the matrix itself
            int [][][] mov = new int[w][h][3];
            for (int i=0; i<w; i++) {
                for (int j=0; j<h; j++) {
                    mov[i][j][0] = in.readInt();
                    mov[i][j][1] = in.readInt();
                    mov[i][j][2] = in.readInt();
                }
            }
            
            // add read matrix into data
            data.push(mov);
            size--;
        }
        
        return data;
    }
    
    
    
}
