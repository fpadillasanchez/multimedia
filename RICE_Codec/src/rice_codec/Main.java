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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int n = 1089;
        System.out.println(n + " -> " + Rice.encode(n));
    }
    
}
