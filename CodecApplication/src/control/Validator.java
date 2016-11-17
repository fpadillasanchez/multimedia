/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import java.io.File;

/**
 *
 * @author Sergi Diaz, Fernando Padilla
 * 
 * Custom parameter validator.
 */
public class Validator implements IParameterValidator{

    @Override
    public void validate(String name, String value) throws ParameterException {
        
        try {
            switch(name) {
                case "--input":
                case "-i":
                    System.out.println(name);
                    fileValidation(name, value);
                    break;
                case "--output":
                case "-o":
                    directoryValidation(name, value);
                    break;
                case "--fps":
                    integerValidation(name, value, 1, 100);
                    break;
                case "--bin":
                    integerValidation(name, value, 0, 1);
                    break;
                case "--average":
                    integerValidation(name, value, 1, 255);
                    break;
                case "--seekRange":
                    integerValidation(name, value, 1, 100);
                    break;
                case "--gop":
                    integerValidation(name, value, 1, 100);
                    break;
                case "--quality":
                    integerValidation(name, value, 0, 100);
            }
            
        } catch (ParameterException ex) {
            throw ex;
        }
    }
    
    // Validation for integer arguments. Value must be parseable and be among a specified range.
    private void integerValidation(String name, String value, int minValue, int maxValue) throws ParameterException {
        try {
            int i = Integer.parseInt(value);
            if (i < minValue || i > maxValue)
                throw new ParameterException(name + " must be in range [" + minValue + ", " + maxValue + "].");
        } catch (NumberFormatException ex) {
            throw new ParameterException("Invalid " + name);
        }
    }
    
    // Validation for file arguments (aka input). Path must adress a file, which can't be a directory.
    private void fileValidation(String name, String value) throws ParameterException {
        File f = new File(value);
        if (!f.exists()) 
            throw new ParameterException(name + " file does not exist.");
        else
            if (f.isDirectory())
                throw new ParameterException(name + " is not a file.");
    }
    
    // Validation for directory arguments (aka output). Path must adress a directory.
    private void directoryValidation(String name, String value) throws ParameterException {
        File d = new File(value);
        if (!d.exists())
            throw new ParameterException(name + " directory does not exist.");
        else
            if (!d.isDirectory())
                throw new ParameterException(name + " is not a directory.");
    }
    
}
