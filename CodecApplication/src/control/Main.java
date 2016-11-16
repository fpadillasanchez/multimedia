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
        ArgParser parser = new ArgParser();
        JCommander jCom = null;

        try {
            jCom = new JCommander(parser, args);
            
            if (parser.help) 
                jCom.usage();
            else if (parser.batch)
                System.out.println("Not implemented");
            else if (parser.decode)
                decode(parser.getInput(), parser.getOutput());
            else if (parser.encode)
                System.out.println("Not implemented"); 
            decode(parser.getInput(), parser.getOutput());
    
        } catch(ParameterException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Try --help for help.");
        }   
    }
    
    private static void decode(String input, String output) {
        VideoPlayer.OUTPUT_FOLDER = output;
        VideoPlayer.INPUT_ZIP = input;
        vp = new VideoPlayer();
        vp.setVisible(true);
    }
    
    private static void encode(String path) {
    }
    
    
    
}
