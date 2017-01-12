/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image_processing;

import java.awt.image.BufferedImage;

/***
 * 
 * @author gondu
 */
public class FilterManager {
    public enum SupportedFilters {
        Negative, Binary, Average, Emboss, Sharpen, Blur, Laplacian, NONE
    }
    
    private static SupportedFilters filter = SupportedFilters.NONE;
    // Average
    private static int maskSize;
    // Binary
    private static int threshold;
    /***
     * 
     * @param image
     * @return 
     */
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
    /***
     * 
     * @param f
     * @param image
     * @return 
     */
    public static BufferedImage filtrate(SupportedFilters f, BufferedImage image) {
        switch(f) {
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
    /***
     * 
     * @param f
     * @param size
     * @param thr 
     */
    public static void setFilter(SupportedFilters f, int size, int thr) {
        filter = f;
        maskSize = size;
        threshold = thr;
    }
    /***
     * 
     * @param f 
     */
    public static void setFilter(SupportedFilters f) {
        filter = f;
    }
    /***
     * 
     * @param image
     * @param maskSize
     * @return 
     */
    public static BufferedImage average(BufferedImage image, int maskSize) {
        return (new AverageFilter(image, maskSize)).apply();
    }
    
    
}
