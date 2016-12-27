/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.image.BufferedImage;

/**
 *
 * @author Sergi Diaz, Fernando Padilla
 * 
 * This class provides the appropriate filter to outsiders.
 */
public class FilterManager {
    public enum SupportedFilters {
        Negative, Binary, Average, Emboss, Sharpen, Blur, Laplacian
    }
    
    private static SupportedFilters filter;
    // Average
    private static int maskSize;
    // Binary
    private static int threshold;
    
    public static BufferedImage filtrate(BufferedImage image) {
        /*
        Return the image after being filtered using the desired transformation.
        Note that if somehow more than one filter has been selected the conflict
        is solved by the order of the comprovations. Such order is purely arbitrary.      
        */
        switch(filter) {
            case Negative:
                return (new NegativeFilter(image)).apply();
            case Binary:
                return (new BinarizationFilter(image, threshold)).apply();
            case Average:
                return (new AverageFilter(image, maskSize)).apply();
            case Emboss:
                return (new EmbossFilter(image)).apply();
            case Sharpen:
                return (new SharpenFilter(image)).apply();
            case Blur:
                return (new BlurFilter(image)).apply();
            case Laplacian:
                return (new LaplacianFilter(image)).apply();
            default:
                return image;
        }
    }
    
    public static void setFilter(SupportedFilters f, int size, int thr) {
        filter = f;
        maskSize = size;
        threshold = thr;
    }
    
    public static void setFilter(SupportedFilters f) {
        filter = f;
    }
    
    
}
