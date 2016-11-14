/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author SDP
 */
public class NegativeFilter extends SimpleFilter{

    public NegativeFilter(BufferedImage image) {
        super(image);
    }

    @Override
    protected Color transform(Color color) {
        int r = 255 - color.getRed();
        int g = 255 - color.getGreen();
        int b = 255 - color.getBlue();
        
        return new Color(r, g, b);
    }
}
