/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

/**
 *
 * @author SDP
 *
 * Codec configuration, OS independent
 */
public class CodecConfig {

    public static String input;         // input zip
    public static String output;        // output directory

    // Sub-directories, where encoder places processed images
    public static String encoder_sub_directory = "temp";
    public static String decoder_sub_directory = "temp";

    //--------- Codec stats -------------
    public static int buffer_size = 100;     // max number of images per buffer
    public static int fps;                   // frames per second
    public static int gop = 1;               // num of images between two reference images
    public static int quality = 1;           // used to determine when two tiles are coincident
    public static int seekRange = 1;         // max distance in tiles when searching for coincidences
    public static int n_tiles_x = 100;       // number of tiles on the x axis
    public static int n_tiles_y = 100;       // number of tiles on the y axis
    public static int codec_avg_mask = 3;    // size of the averaging filter mask used when coding

    // Formats
    public static String data_format = ".data";
    public static String video_format = ".zip";
}
