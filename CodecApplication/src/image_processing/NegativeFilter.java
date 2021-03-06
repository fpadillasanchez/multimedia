/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * *
 * Negative filter.
 * @author Sergi Díaz
 */
public class NegativeFilter extends SimpleFilter {

    /***
     * 
     * @param image
     */
    public NegativeFilter(BufferedImage image) {
        super(image);
    }

    /***
     * Method that inverts each pixel's RGB color
     * @param image
     * @return
     */
    public BufferedImage invertImage(BufferedImage image) {

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgba = image.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                                255 - col.getGreen(),
                                255 - col.getBlue());
                image.setRGB(x, y, col.getRGB());
            }
        }
        return image;
    }
    /***
     * Method that changes the color of the image
     * @param color
     * @return 
     */
    @Override
    protected Color transform(Color color) {
        int r = 255 - color.getRed();
        int g = 255 - color.getGreen();
        int b = 255 - color.getBlue();

        return new Color(r, g, b);
    }
}
