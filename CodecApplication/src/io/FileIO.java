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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
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
        JPEG("jpg"), PNG("png"), GIF("gif");
        
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
    
    // Extracts images from zip and returns an array of paths to those images.
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
    
    // Compresses a list of files into an output zip. Default format: JPEG.
    // TODO: Allow other formats.
    public static void formatedZip(ArrayList<File> files, String output) throws IOException {
        ArrayList<File> newFiles = new ArrayList<>();
        for (File file : files) {
            // For each file store a temporary formated copy.
            newFiles.add(storeImage(ImageIO.read(file), file.getCanonicalPath(), SupportedFormats.JPEG));
        }
        zip(newFiles, output); // zip newly created files.
        
        // Delete the copies
        for (File newFile : newFiles)
            newFile.delete();
    }
    
    // Compresses files in input directory into an output zip. Default format: JPEG.
    // TODO: Allow other formats.
    public static void formatedZip(String input, String output) throws IOException {
        File folder = new File(input);
        if (!folder.isDirectory())
            throw new IOException(input + " is not a valid directory.");
        
        ArrayList<File> files = new ArrayList<>();
        // Get array of files in input directory.
        for (File file : folder.listFiles()) {
            if (file.isFile())
                if (validateExtension(file.getAbsolutePath())) // check if is an imatge with supported format
                    files.add(file);
        }   
        formatedZip(files, output);
    }
    
    // Creates the zip file.
    private static void zip(ArrayList<File> files, String output) throws IOException {
        File zipfile = new File(output);
        byte[] buf = new byte[1024]; // buffer for reading the files
        
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
        // compress the files
        for(int i=0; i<files.size(); i++) {
            FileInputStream in = new FileInputStream(files.get(i).getCanonicalPath());  
            out.putNextEntry(new ZipEntry(files.get(i).getName())); // add ZIP entry to output stream

            int len;
            while((len = in.read(buf)) > 0) { // transfer bytes from the file to the ZIP file
                out.write(buf, 0, len);
            }
            // complete the entry
            out.closeEntry();
            in.close();
        }
        // complete the ZIP file
        out.close();
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
    
    // Given a path and a supported format, the method stores there an image.
    // Returns the path to the new file.
    private static File storeImage(BufferedImage image, String file, SupportedFormats format) throws IOException {
        File outputFile = new File(stripExtension(file) + "." + format.value);
        ImageIO.write(image, format.value, outputFile);
        
        return outputFile;
    }
    
    private static String stripExtension(String file) {
        if (file == null) 
            return null;
        int pos = file.lastIndexOf(".");
        if (pos == -1) 
            return file;
        return file.substring(0, pos);
    }
}
