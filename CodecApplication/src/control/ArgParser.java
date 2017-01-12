/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.beust.jcommander.Parameter;

/***
 * 
 * @author Sergi Diaz Fernando Padilla
 */
public class ArgParser {    
    // Compulsory arguments
    @Parameter(names = {"--input", "-i"}, 
            description = "Path to file.zip", required=true, validateWith = Validator.class)  
    private String input;
    
    @Parameter(names = {"--output", "-o"}, 
            description = "Path to file", required=true, validateWith = Validator.class)      
    private String output;
    
    // Optional arguments
    @Parameter(names = {"--encode", "-e"}, description = "Encode input file", 
            validateWith = Validator.class)
    public boolean encode = false;
    
    @Parameter(names = {"--decode", "-d"}, description = "Decode input file", 
            validateWith = Validator.class)
    public boolean decode = false;
    
    @Parameter(names = "--fps", description = "Frames per second", 
            validateWith = Validator.class)
    private Integer fps = 30;
    
    @Parameter(names = "--binarization", 
            description = "Binarization filtering using given threshold", 
            validateWith = Validator.class)
    private Integer bin = 1;
    
    @Parameter(names = "--negative", description = "Negative filtering", 
            validateWith = Validator.class)
    private boolean negative = false;
    
    @Parameter(names = "--averaging", 
            description = "Averaging filtering over zones of value x value", 
            validateWith = Validator.class)
    private Integer average = 3;
    
    @Parameter(names = "--emboss", description = "Emboss filtering", 
            validateWith = Validator.class)
    private boolean emboss = false;
    
    @Parameter(names = "--sharpen", description = "Sharpen filtering", 
            validateWith = Validator.class)
    private boolean sharpen = false;
    
    @Parameter(names = "--blur", description = "Blur filtering", 
            validateWith = Validator.class)
    private boolean blur = false;
    
    @Parameter(names = "--laplacian", description = "Laplacian filtering", 
            validateWith = Validator.class)
    private boolean laplacian = false;
    
    @Parameter(names = "--seekRange", 
            description = "Maximum range of the tile search", 
            validateWith = Validator.class)
    private Integer seekRange = 10;
    
    @Parameter(names = "--nTiles", description = "Number of tiles per image", 
            validateWith = Validator.class)
    private Integer nTiles;
    
    @Parameter(names = "--GOP", 
            description = "Number of images between two reference frames", 
            validateWith = Validator.class)
    private Integer gop = 1;
    
    @Parameter(names = "--quality", 
            description = "Determines when two tiles are considered coincident", 
            validateWith = Validator.class)
    private Integer quality = 1;
    
    @Parameter(names = {"--help", "-h"}, description = "Prints help message", 
            help=true, validateWith = Validator.class)
    public boolean help;
    
    @Parameter(names = {"--batch", "-b"}, description = "Execution through shell", 
            validateWith = Validator.class)
    public boolean batch = false;
    
    //-- GETTERS:  
    public String getInput() {
        return input;
    }
    
    public String getOutput() {
        return output;
    }
    
    public Integer getFPS() {
        return fps;
    }

    public Integer getSeekRange() {
        return seekRange;
    }

    public Integer getGop() {
        return gop;
    }

    public Integer getBin() {
        return bin;
    }

    public boolean isNegative() {
        return negative;
    }

    public Integer getnTiles() {
        return nTiles;
    }
    
    public boolean isSharpen() {
        return sharpen;
    }
    
    public boolean isBlur() {
        return blur;
    }
    
    public boolean isLaplacian() {
        return laplacian;
    }

    public Integer getAverage() {
        return average;
    }
    /***
     * Store current configuration
     */
    public void setConfig() {      
        CodecConfig.input = input;
        CodecConfig.output = output;
        
        CodecConfig.fps = fps;
        CodecConfig.gop = gop;
        CodecConfig.seekRange = seekRange;
        CodecConfig.quality = quality;
    }
    
}
