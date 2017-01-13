/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import image_processing.FilterManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

/***
 * Class used for loading images from files. Includes a format validator. At
 * this moment, only JPEG and PNG images are allowed.
 * @author Sergi Diaz Fernando Padilla
 */
public class FileIO {
    
    /***
     * 
     * @param files
     * @return 
     */
    private static ArrayList<String> convert(List<String> files) {
        ArrayList<String> newFiles = new ArrayList<String>(files);
        return newFiles;   
    }

    /***
     * Supported image formats. Used for unzipping duties
     */
    public static enum SupportedFormats {
        JPEG("jpg"), PNG("png"), GIF("gif"), TIFF("tiff");

        public String value;

        SupportedFormats(String value) {
            this.value = value;
        }
    };

    /***
     * Compress all files inside input directory into an output zip video
     * @param input
     * @param output
     * @throws IOException 
     */ 
    public static void compress(String input, String output) throws IOException {
        File folder;                    // input must be a valid directory
        File zipfile;                   // output zip file
        byte[] buf = new byte[1024];    // buffer for reading the files

        folder = new File(input);
        if (!folder.isDirectory()) {
            throw new IOException(input + " is not a valid directory.");
        }
        zipfile = new File(output);

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                FileInputStream in = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(file.getName())); // add ZIP entry to output stream

                int len;
                while ((len = in.read(buf)) > 0) { // transfer bytes from the file to the ZIP file
                    out.write(buf, 0, len);
                }
                // complete the entry
                out.closeEntry();
                in.close();

                file.delete();  // delete file
            }
        }
        // ZIP file completed
        out.close();
    }

    /***
     * Decompress entries from a zip video into an output directory
     * @param input
     * @param output
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static ArrayList<String> decompress(String input, String output) throws FileNotFoundException, IOException {
        ArrayList<String> files = new ArrayList<>();    // output files paths array
        byte[] buffer = new byte[1024];
        int lenght;

        File outputDirectory = new File(output);        // output must be a valid directory
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }

        // Try to extract entries in the zip
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(input))) {
            ZipEntry entry = zis.getNextEntry();

            FileOutputStream fos;
            while (entry != null) {
                String path;    // location of the new file where we are storing the extracted information

                path = output + File.separator + entry.getName();
                File file = new File(path); // create new File

                // Store the info hold in the zip entry into the new file
                fos = new FileOutputStream(file);
                while ((lenght = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, lenght);
                }
                fos.close();

                files.add(path);            // insert path into the array
                entry = zis.getNextEntry(); // move to next entry
            }
            zis.closeEntry();   // extraction completed

        }
        return files;
    }

    /***
     * Extracts images from zip and returns an array of paths to those images.
     * @param input
     * @param output
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static ArrayList<String> extractImages(String input, String output) throws FileNotFoundException, IOException {
        List<String> files = new ArrayList<>();    // output files paths array
        byte[] buffer = new byte[1024];
        int length;

        File outputDirectory = new File(output);        // output must be a valid directory
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(input))) {

            ZipEntry entry = zis.getNextEntry();

            FileOutputStream fos;
            while (entry != null) {
                String fileName = entry.getName();
                if (entry.isDirectory()) {
                    new File(outputDirectory.getName()+"/"+entry.getName()).mkdirs();
                    entry = zis.getNextEntry();
                    continue;
                }
                if (validateExtension(entry.getName())) {
                    // Create temporary file containing the info of the entry
                    File file = new File(output + fileName);
                    
                    fos = new FileOutputStream(file);
                    // Store the info hold in the zip entry into the temporary file
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    fos.close();

                    // Store a copy of the image temporary file using default format JPEG
                    files.add(storeImage(file.getAbsolutePath(), SupportedFormats.JPEG).getAbsolutePath());
                    // Delete temporary file
                    file.delete();
                }
                entry = zis.getNextEntry(); // extraction completed
            }

            zis.closeEntry();
        }
        Collections.sort(files, String.CASE_INSENSITIVE_ORDER);
        return convert(files);
    }

    /***
     * Loads image if its format is supported.
     * @param file
     * @return
     * @throws IOException 
     */
    public static BufferedImage readImage(String file) throws IOException {
        if (!validateExtension(file)) {
            return null;
        }
        return ImageIO.read(new File(file));
    }

    /***
     * Stores given buffered image into a new file. Stored as a JPEG image.
     * @param image
     * @param output
     * @throws IOException 
     */
    public static void writeImage(BufferedImage image, String output) throws IOException {
        String format = SupportedFormats.JPEG.toString();
        ImageIO.write(image, format, new File(output + "." + format));
    }

    /***
     * Format validator.
     * @param fileName
     * @return 
     */
    private static boolean validateExtension(String fileName) {
        int dot = fileName.lastIndexOf(".");
        if (dot == -1 || dot == fileName.length()) {
            return false;
        }
        String ext = fileName.substring(dot + 1);
        for (SupportedFormats sf : SupportedFormats.values()) {
            if (ext.equals(sf.value)) {
                return true;
            }
        }
        return true;

    }

    /***
     * Takes the image directed by a given path and stores a copy in the same
     * directory using given supported format.
     * @param file
     * @param format
     * @return
     * @throws IOException 
     */ 
    private static File storeImage(String file, SupportedFormats format) throws IOException {
        File outputFile = new File(stripExtension(file) + "." + format.value);

        // Filtration process done during storing
        ImageIO.write(FilterManager.filtrate(ImageIO.read(new File(file))), format.value, outputFile);
        return outputFile;
    }

    /***
     * Return input string without extension, that being a path to a file
     * @param file
     * @return 
     */
    
    public static String stripExtension(String file) {
        if (file == null) {
            return null;
        }
        int pos = file.lastIndexOf(".");
        if (pos == -1) {
            return file;
        }
        return file.substring(0, pos);
    }
    
    /***
     * Return extendion from input string, that being the path to a file
     * @param file
     * @return 
     */
    public static String getExtension(String file) {
        String ext = "";
        
        int pos = file.lastIndexOf(".");
        if (pos == -1) {
            return ext;
        }
        return file.substring(pos);
        
    }
    

}
