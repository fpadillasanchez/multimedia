/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import codec.Decoder;
import codec.Encoder;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import image_processing.FilterManager;
import io.FileIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * *
 * Main class. It reads the parameters from args
 *
 * @author Sergi Diaz Fernando Padilla
 */
public class Main {

    public static void main(String[] args) throws IOException {

        ArgParser parser = new ArgParser();
        JCommander jCom = null;

        try {
            jCom = new JCommander(parser, args);

            // Make filter selection
            for (String arg : args) { // following filters require input values
                if (arg.equals("--binarization")) {
                    FilterManager.setFilter(FilterManager.SupportedFilters.Binary,
                            parser.getAverage(), parser.getBin());
                    break;
                }
                if (arg.equals("--averaging")) {
                    FilterManager.setFilter(FilterManager.SupportedFilters.Average,
                            parser.getAverage(), parser.getBin());
                    break;
                }
            }

            // Following inputs do not require input values
            if (parser.isNegative()) // negative filtering
            {
                FilterManager.setFilter(FilterManager.SupportedFilters.Negative);
            } else if (parser.isBlur()) // blur filtering
            {
                FilterManager.setFilter(FilterManager.SupportedFilters.Blur);
            } else if (parser.isSharpen()) // sharpen filtering
            {
                FilterManager.setFilter(FilterManager.SupportedFilters.Sharpen);
            } else if (parser.isLaplacian()) // laplacian filtering
            {
                FilterManager.setFilter(FilterManager.SupportedFilters.Laplacian);
            } else if (parser.isEmboss()) { // emboss filtering
                FilterManager.setFilter(FilterManager.SupportedFilters.Emboss);
            }

            parser.setConfig();     // set codec configuration

            if (parser.help) {
                jCom.usage();
            } else if (parser.decode) {
                decode(!parser.batch, parser.getFPS(), parser.getnTiles(), parser.getSeekRange(), parser.getGop()); // do not show GUI if batch
            } else if (parser.encode) {
                encode("my_video.zip", parser.getnTiles(), parser.getSeekRange(), parser.getGop());
            }

        } catch (ParameterException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Try --help for help.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Try --help for help.");
        }
        /*
        
         */
    }

    /**
     * *
     * Decodes ZIP and loades images into the videoplayer GUI.
     *
     * @param visible
     * @param input
     * @param output
     * @param fps
     * @throws IOException
     */
    private static void decode(boolean visible, int fps, int tiles, int seekRange, int gop) throws IOException, ClassNotFoundException, Exception {
        if (tiles != 0) {
            CodecConfig.n_tiles_x = tiles;
            CodecConfig.n_tiles_y = tiles;
        }

        if (seekRange != 0) {
            CodecConfig.seekRange = seekRange;
        }

        if (gop != 0) {
            CodecConfig.gop = gop;
        }
        Decoder.decode(CodecConfig.input, CodecConfig.output);
        if (visible) {
            ArrayList<String> files = new ArrayList<>();

            File output_dir = new File(CodecConfig.output);
            for (String path : output_dir.list()) {
                files.add(path);
            }

            visualize(files, fps);
        }

    }

    /**
     * *
     * ZIP compression
     *
     * @param videoname
     */
    private static void encode(String videoname, int tiles, int seekRange, int gop) {
        float pesoOriginal = 0;
        float pesoNuevo = 0;

        if (tiles != 0) {
            CodecConfig.n_tiles_x = tiles;
            CodecConfig.n_tiles_y = tiles;
        }

        if (seekRange != 0) {
            CodecConfig.seekRange = seekRange;
        }

        if (gop != 0) {
            CodecConfig.gop = gop;
        }

        try {
            String input = CodecConfig.input;
            String output = CodecConfig.output;

            long t1, t2;    // used for measuring encoding nelapsed time
            ArrayList<String> inputFiles = getImageFiles(input, output);

            visualize(inputFiles, 30);

            // -- ENCODING --
            t1 = System.nanoTime();
            Encoder e = new Encoder();  // initalize encoder.
            e.load();
            e.encode(videoname);
            t2 = System.nanoTime();
            // --------------
            double elapsedTime = (t2 - t1) / 1000000000.0;
            System.out.println("Encoder time: " + elapsedTime + " sec");
            ZipFile zf;
            ZipFile zf2;
            try {
                zf = new ZipFile(input);
                Enumeration ee = zf.entries();
                while (ee.hasMoreElements()) {
                    ZipEntry ze = (ZipEntry) ee.nextElement();
                    long compressedSize = ze.getCompressedSize();
                    pesoOriginal = pesoOriginal + compressedSize;

                }
                System.out.println("Original zip: " + pesoOriginal);
            } catch (IOException ex) {
                Logger.getLogger(VideoPlayerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                zf2 = new ZipFile(output + File.separator + videoname);
                Enumeration ee = zf2.entries();
                while (ee.hasMoreElements()) {
                    ZipEntry ze = (ZipEntry) ee.nextElement();
                    long compressedSize = ze.getCompressedSize();
                    pesoNuevo = pesoNuevo + compressedSize;

                }
                System.out.println("New compressed zip after encoding: " + pesoNuevo);
            } catch (IOException ex) {
                Logger.getLogger(VideoPlayerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Factor de compresion: " + (1-(pesoNuevo / pesoOriginal))*100 + "%");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * *
     * Manages video visualization
     *
     * @param imageFiles
     * @param input
     * @param output
     * @param fps
     * @throws IOException
     */
    private static void visualize(ArrayList<String> imageFiles, int fps) throws IOException {

        // Set videoplayer parameters
        VideoPlayerController.fps = fps;
        VideoPlayerController.imageFiles = imageFiles;
        VideoPlayerController vpc = new VideoPlayerController();
        (new Thread(vpc)).start(); // video displayed on another thread
    }

    /**
     * *
     * Gets all the images from a zip file and extracts them in a output folder
     *
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    private static ArrayList<String> getImageFiles(String input, String output) throws IOException {
        return FileIO.extractImages(input, output);
    }

}
