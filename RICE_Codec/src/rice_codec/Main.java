/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rice_codec;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SDP
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Rice.M = 8; // coeficient de compressio
        
        try {
            String code; // cadena de bits codificats
            int b; // mida en bits de codificacio binaria natural
            
            PrintWriter out = new PrintWriter("Rice_" + Rice.M + ".txt");
            for(int i=-1023; i<=1023; i++){
                code = Rice.encode(i);
                
                b = (int)Math.ceil( Rice.log2(Math.abs(i) +1) ) + 1;
                out.println(i + "\t" + code + "\t" + code.length());
            }
            out.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
