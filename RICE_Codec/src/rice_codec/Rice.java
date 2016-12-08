/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rice_codec;

/**
 *
 * @author SDP
 */
public class Rice {
    
    public static int M = 256;  // parametre de compressio
    
    /*
    RICE:
        codi de N = <signe><quocient><residu>
        
        <signe>: 1 positiu, 0 negatiu
        <quocient>: Q vegades 1 i un 0 al final ( Q=N/M )
        <resiudu>: residu com a binari natural (log(M) bits)
    */
    public static String encode(int n) {
        String output, R;  // cadena de sortida, residu
        int Q;       // quocient
        
        
        // Assignem bit de signe
        if (n >= 0)
            output = "1"; // primer bit es 1 si el nombre es positiu
        else
            output = "0"; // segon bit es 0 si el nombre es negatiu
        
        R = Integer.toBinaryString(Math.abs(n) % M);        // residu de n/M
        Q = Math.abs(n) / M;  // quocient de n/M
        for(int i=0; i<Q; i++) {
            output += "1";
        }
        
        // Allarga R posant 0 davant fins a tenir log(M) bits
        int bits = (int) log2(M);
        String aux = "";
        for(int j=0; j<(bits - R.length()); j++) {
            aux += "0";
        }
        R = aux + R;
        
        output += "0" + R; // <signe><quocient><residu>      
        return output; // retorna cadena de sortida amb el nombre codificat
    }
    
    public static double log2(int num) {
        if (num < 0) {
            return Double.NaN;
        }
        if (num == 0) {
            return 1;
        }
        return Math.log(num) / Math.log(2);
    }
}
