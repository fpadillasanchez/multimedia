/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author SDP
 */
public class FileIO {
    
    public static enum SupportedFormats {
        JPEG("jpg"), PNG("png");
        
        public String value;
        SupportedFormats(String value) {
            this.value = value;
        }
    };
    
    public static String[] unZip(String input) throws FileNotFoundException, IOException {
        ArrayList<String> files = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(input))) {
            ZipEntry entry = zis.getNextEntry();
            
            while (entry != null) {
                if(validateExtension(entry.getName()))
                    files.add(input + File.separator + entry.getName());
                entry = zis.getNextEntry();
            } 
            zis.closeEntry();
        }
        
        return (String[]) files.toArray();
    }
    
    public static String[] unZip(String input, String output) throws FileNotFoundException, IOException {
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
                    files.add(output + File.separator + entry.getName());
                }
                entry = zis.getNextEntry();
            }
            
            zis.closeEntry();
        }
        return (String[]) files.toArray();
    }
    
    public static void zip() {
        
    }
    
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
