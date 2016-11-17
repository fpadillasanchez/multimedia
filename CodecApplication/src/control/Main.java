/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import io.FileIO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.VideoPlayer;

/**
 *
 * @author Fernando Padilla, Sergi Diaz
 */
public class Main {

    static VideoPlayer vp;  // GUI

    public static void main(String[] args) {
        ArgParser parser = new ArgParser();
        JCommander jCom = null;

        try {
            jCom = new JCommander(parser, args);

            if (parser.help) {
                jCom.usage();
            } else if (parser.batch) {
                System.out.println("Not implemented yet.");
            } else if (parser.decode) {
                decode(parser.getInput(), parser.getOutput(), parser.getFPS(), parser.getBin(), parser.isNegative(), parser.getAverage());
            } else if (parser.encode) {
                encode(parser.getInput(), parser.getOutput());
            }

        } catch (ParameterException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Try --help for help.");
        }
    }

    // Decodes ZIP and loades images into the videoplayer GUI.
    private static void decode(String input, String output, Integer fps) {
        // Set videoplayer parameters
        VideoPlayer.OUTPUT_FOLDER = output;
        VideoPlayer.INPUT_ZIP = input;
        vp = new VideoPlayer();
        try {
            vp.setFPS(fps);
            // Try to load image buffer
            vp.loadBuffer();
            // Visualize GUI
            vp.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // TODO: Implement encoding.
    private static void encode(String input, String output) {
        try {
            FileIO.formatedZip(input, output);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
