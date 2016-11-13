/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author sdiazpla7.alumnes
 */
public class Validator implements IParameterValidator{

    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            switch(name) {
                case "negative":
                    int i = Integer.parseInt(value);
                    if(i < 0 || i > 1) {
                        throw new NumberFormatException("Allowed values: 0, 1.");
                    };
                    break;
                // cases: fps, nTiles, quality, GOP, seekRange
                default:
                    if(Integer.parseInt(value) <= 0) {
                        throw new NumberFormatException("Value must be over 0.");
                    };
                    break;
            }
            
        } catch (NumberFormatException ex) {
            throw ex;
        }
    }
    
}
