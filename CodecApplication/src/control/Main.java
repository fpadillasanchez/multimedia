/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import ui.VideoPlayer;

/**
 *
 * @author SDP
 */
public class Main {
    static VideoPlayer vp;
    
    public static void main(String[] args) {
        vp = new VideoPlayer();
        
        String[] args2 = {"-d"};
    
        ArgParser parser = new ArgParser();
        JCommander jCom = null;
    
        try {
            jCom = new JCommander(parser, args2);
            
            if (parser.help) 
                jCom.usage();
            if (parser.batch)
                System.out.println("Not implemented");
            if (parser.decode)
                System.out.println("Not implemented");
            if (parser.encode)
                System.out.println("Not implemented");
            playVideo();
    
        } catch(ParameterException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Try --help for help.");
        }
    }
    
    private static void decode(String path) {
    }
    
    private static void encode(String path) {
    }
    
    private static void playVideo() { 
    }
    
    
    
}
