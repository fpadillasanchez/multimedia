/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import codec.Decoder;
import codec.Encoder;
import codec.MotionDetector;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import image_processing.FilterManager;
import io.FileIO;
import io.FrameData;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

/**
 *
 * @author Fernando Padilla, Sergi Diaz
 *
 *
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
        }

        parser.setConfig();     // set codec configuration

        if (parser.help) {
        jCom.usage();
        } else if (parser.decode) {
        decode(!parser.batch, parser.getInput(), parser.getOutput(), parser.getFPS()); // do not show GUI if batch
        } else if (parser.encode) {
        encode("my_video.zip");
        }

        } catch (ParameterException ex) {
        System.out.println(ex.getMessage());
        System.out.println("Try --help for help.");
        }

        /*
        //CodecConfig.input = "C:\\Users\\SDP\\Documents\\GitHub\\multimedia\\CodecApplication\\src\\zips\\Imagenes.zip";
        //CodecConfig.output = "C:\\Users\\SDP\\Documents\\GitHub\\multimedia\\CodecApplication\\src\\unzip";
        CodecConfig.input = "C:\\Users\\SDP\\Documents\\GitHub\\multimedia\\CodecApplication\\src\\unzip\\my_video.zip";
        CodecConfig.output = "C:\\Users\\SDP\\Documents\\GitHub\\multimedia\\CodecApplication\\src\\unzip";
        //Encoder encoder = new Encoder();


        try {
        Decoder.decode(CodecConfig.input, CodecConfig.output);
        //encoder.load();
        //encoder.encode("my_video");
        } catch (IOException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
         
        
        /*
        try {
            DEBUG();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
    }

    // Decodes ZIP and loades images into the videoplayer GUI.
    private static void decode(boolean visible, String input, String output, int fps) throws IOException {

        if (visible) {
            visualize(getImageFiles(input, output), input, output, fps);
        }

    }

    // ZIP compression
    private static void encode(String videoname) {
        long pesoOriginal = 0;
        long pesoNuevo = 0;

        try {
            String input = CodecConfig.input;
            String output = CodecConfig.output;

            long t1, t2;    // used for measuring encoding nelapsed time
            ArrayList<String> inputFiles = getImageFiles(input, output);

            visualize(inputFiles, input, output, 30);

            // -- ENCODING --
            t1 = System.nanoTime();
            Encoder e = new Encoder();  // initalize encoder.
            e.load();
            e.encode(videoname);
            t2 = System.nanoTime();
            // --------------
            double elapsedTime = (t2 - t1) * 000000.1;
            System.out.println("Encoder time: " + elapsedTime + " ms");
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
                zf2 = new ZipFile(output + File.separator + videoname + CodecConfig.video_format);

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
            System.out.println("Factor de compresion: " + pesoOriginal / pesoNuevo * 100 + "%");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Manages video visualization
    private static void visualize(ArrayList<String> imageFiles, String input, String output, int fps) throws IOException {

        // Set videoplayer parameters
        VideoPlayerController.fps = fps;
        VideoPlayerController.imageFiles = imageFiles;
        VideoPlayerController vpc = new VideoPlayerController();
        (new Thread(vpc)).start();              // video displayed on another thread
    }

    private static ArrayList<String> getImageFiles(String input, String output) throws IOException {
        return FileIO.extractImages(input, output);
    }
    
    private static void DEBUG() throws IOException {
        /*
        
        CodecConfig.n_tiles_x = 4;
        CodecConfig.n_tiles_y = 4;
        
        String in_refer = "C:\\Users\\SDP\\Desktop\\test_0.png";
        String in_other = "C:\\Users\\SDP\\Desktop\\test_1.png";
        
        ArrayList<BufferedImage> images = new ArrayList<>();
        ArrayList<FrameData> frames;
        
        BufferedImage img_refer = ImageIO.read(new File(in_refer));
        BufferedImage img_other = ImageIO.read(new File(in_other));
        
        images.add(img_refer);
        images.add(img_other);
        
        frames = MotionDetector.motionDetection(images, 0);
        
        FrameData refer = frames.get(0);
        FrameData other = frames.get(1);
        
        refer.setImage(FilterManager.average(refer.getImage(), 3));
        other.setImage(FilterManager.average(other.getImage(), 3));
        
        FileIO.writeImage(refer.getImage(), "C:\\Users\\SDP\\Desktop\\test_refer.jpg");
        FileIO.writeImage(other.getImage(), "C:\\Users\\SDP\\Desktop\\test_other.jpg");
        
        FileIO.writeImage(Decoder.retrieveImage(refer, other), "C:\\Users\\SDP\\Desktop\\test_retrieved.jpg");
        
        
        int mov[][][] = other.getMovements();
        for (int i=0; i<mov.length; i++) {
            for (int j=0; j<mov[0].length; j++) {
                System.out.println(i + ", " + j + " --> " + mov[i][j][0] + "; " + mov[i][j][1] + ", " + mov[i][j][2]);
            }
        }
        */
        
        
        /*
        CodecConfig.n_tiles_x = 4;
        CodecConfig.n_tiles_y = 4;
        
        BufferedImage image = FileIO.readImage("C:\\Users\\SDP\\Desktop\\test_0.png");
        
        int[][] tessel = MotionDetector.tesselate(image);
        
        // Tile size in pixels
        int w = Math.min(Math.max(1, (image.getWidth() / CodecConfig.n_tiles_x)), CodecConfig.n_tiles_x);
        int h = Math.min(Math.max(1, (image.getHeight() / CodecConfig.n_tiles_y)), CodecConfig.n_tiles_y);
        
        for (int i=0; i<CodecConfig.n_tiles_x; i++) {
            for (int j=0; j<CodecConfig.n_tiles_y; j++) {

                for (int x=0; x<w; x++) {
                    for (int y=0; y<w; y++) {
                        try {
                            image.setRGB(x + i * w, y + j * h, tessel[i][j]);
                            
                        } catch(Exception ex) {
                            System.out.println("Exception");
                        }
                    }
                }
            }
        }
        
        FileIO.writeImage(image, "C:\\Users\\SDP\\Desktop\\tessel");
        */
        
        /*
        CodecConfig.n_tiles_x = 4;
        CodecConfig.n_tiles_y = 4;
        
        String in_refer = "C:\\Users\\SDP\\Desktop\\test_0.png";
        String in_other = "C:\\Users\\SDP\\Desktop\\test_1.png";
        
        ArrayList<BufferedImage> images = new ArrayList<>();
        ArrayList<FrameData> frames;
        
        BufferedImage img_refer = ImageIO.read(new File(in_refer));
        BufferedImage img_other = ImageIO.read(new File(in_other));
        
        images.add(img_refer);
        images.add(img_other);
        
        frames = MotionDetector.motionDetection(images, 0);
        */
        
        
        CodecConfig.n_tiles_x = 100;
        CodecConfig.n_tiles_y = 100;
        
        String in_refer = "C:\\Users\\SDP\\Desktop\\test_0.png";
        String in_other = "C:\\Users\\SDP\\Desktop\\test_1.png";
        
        ArrayList<BufferedImage> images = new ArrayList<>();
        ArrayList<FrameData> frames;
        
        BufferedImage img_refer = ImageIO.read(new File(in_refer));
        BufferedImage img_other = ImageIO.read(new File(in_other));
        
        int[][] tilemap_refer = MotionDetector.tesselate(img_refer);
        int[][] tilemap_other = MotionDetector.tesselate(img_other);     

        int w = Math.min(Math.max(1, (img_refer.getWidth() / CodecConfig.n_tiles_x)), CodecConfig.n_tiles_x);
        int h = Math.min(Math.max(1, (img_refer.getHeight() / CodecConfig.n_tiles_y)), CodecConfig.n_tiles_y);
        
        FrameData refer;
        FrameData other;

        images.add(img_refer);
        images.add(img_other);
        
        frames = MotionDetector.motionDetection(images, 0);
        
        refer = frames.get(0);
        other = frames.get(1);
        
        FileIO.writeImage(refer.getImage(), "C:\\Users\\SDP\\Desktop\\test_0_refer.png");
        FileIO.writeImage(other.getImage(), "C:\\Users\\SDP\\Desktop\\test_0_other.png");
        
        
        BufferedImage retrieved = Decoder.retrieveImage(refer, other);
        FileIO.writeImage(retrieved, "C:\\Users\\SDP\\Desktop\\retrieved.png");
    }

}
