/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.beust.jcommander.Parameter;

/**
 *
 * @author sdiazpla7.alumnes
 */
public class ArgParser {
    /*
    ARG PARSER
    
    // Definim quina sintaxi segueix el nostre argument.
    @Parameter(names = "--debug", description = "Enters debug mode");
    private boolean debug = false;
    // Tal com esta aqui, si la comanda conte --debug la variable sera true. Si es privat cal
    // afegir funcio get.
    
    // Parametre d'ajuda. Not's com podem assignar mes d'1 nom.
    @Parameter(names = "--help", "-h", description = "Prints help message", help=true);
    public boolean help;
    
    //Podem tambe afegir arguments obligatoris:
    @Parameter(names..., required=true, description...) //flag required, per defecte false
    variable
    
    // + flags:
        validateWith = FileExists.class  // solicita a la classe indicada que comprovi si el parametre es valid
                                         // si no es posa s'usa el validador estandar
    
        Podem construir el nostre propi validador, preveient el tipus d'error que poden
        sorgir en processar aquet tipus de parametre.
    
        class <validador> implements IParameterValidator {
            public void validate(String name, String value) {
                 ...
            }
        }
    */
    
    // Arguments obligatoris
    @Parameter(names = {"--input", "-i"}, 
            description = "Path to file.zip", required=true)  // TODO: required = true
    private String input;
    
    @Parameter(names = {"--output", "-o"}, 
            description = "Path to file", required=true)      // TODO: required = true
    private String output;
    
    // Arguments opcionals
    @Parameter(names = {"--encode", "-e"}, description = "Encode input file")
    public boolean encode = false;
    
    @Parameter(names = {"--decode", "-d"}, description = "Decode input file")
    public boolean decode = false;
    
    @Parameter(names = "--fps", description = "Frames per second")
    private Integer fps = 10;
    
    @Parameter(names = "--binarization", 
            description = "Binarization filtering using given threshold")
    private Integer bin = null;
    
    @Parameter(names = "--negative", description = "Negative filtering")
    private boolean negative = false;
    
    @Parameter(names = "--averaging", 
            description = "Averaging filtering over zones of value x value")
    private Integer average = null;
    
    @Parameter(names = "--seekRange", 
            description = "Maximum range of the tile search")
    private Integer seekRange = 10;
    
    @Parameter(names = "--nTiles", description = "Number of tiles per image")
    private Integer[] nTiles;
    
    @Parameter(names = "--GOP", 
            description = "Number of images between two reference frames")
    private Integer gop = 1;
    
    @Parameter(names = "--quality", 
            description = "Determines when two tiles are considered coincident")
    private Integer quality = 1;
    
    @Parameter(names = {"--help", "-h"}, description = "Prints help message", help=true)
    public boolean help;
    
    @Parameter(names = {"--batch", "-b"}, description = "Execution through shell")
    public boolean batch = false;
    
    
    public String getInput() {
        return input;
    }
    
    public String getOutput() {
        return output;
    }
}
