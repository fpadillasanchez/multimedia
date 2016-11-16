/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Fernando Padilla, Sergi Diaz
 * 
 * Class used for loading images from files. Includes a format validator. At 
 * this moment, only JPEG and PNG images are allowed.
 */
public class FileIO {
    
    public static enum SupportedFormats {
        JPEG("jpg"), PNG("png");
        
        public String value;
        SupportedFormats(String value) {
            this.value = value;
        }
    };
    
    // Loads image if its format is supported.
    public static BufferedImage readImage(String file) throws IOException {
        if (!validateExtension(file))
            return null;
        return ImageIO.read(new File(file));
    }
    
    // Extracts images from zip, stores them at an output directory and returns
    // an array of paths to those images.
    public static ArrayList<String> unZip(String input, String output) throws FileNotFoundException, IOException {
        ArrayList<String> files = new ArrayList<>();
        byte[] buffer = new byte[1024];
        int lenght;

        File outputDirectory = new File(output);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(input))) {
            ZipEntry entry = zis.getNextEntry();
            
            FileOutputStream fos;
            while (entry != null) {
                if(validateExtension(entry.getName())) {
                    File file = new File(output + File.separator + entry.getName());
                    fos = new FileOutputStream(file);
                    
                    while ((lenght = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, lenght);
                    }
                    fos.close();
                    files.add(file.getAbsolutePath());
                }
                entry = zis.getNextEntry();
            }
            
            zis.closeEntry();
        }
        return files;
    }
    
    // TODO: zip images from directory.
    public static void zip() {
        
    }
    
    // Format validator.
    private static boolean validateExtension(String fileName) {
        int dot = fileName.lastIndexOf(".");
        if (dot == -1 || dot == fileName.length()) 
            return false;
        String ext = fileName.substring(dot+1);
        for (SupportedFormats sf : SupportedFormats.values()) {
            if (ext.equals(sf.value))
                return true;
        }
        return true;
   
    }
}
