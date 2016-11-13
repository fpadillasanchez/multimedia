/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zipextractor;

/**
 *
 * @author Fernando Padilla
 */
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;

public class UnZip {

    List<String> fileList;
    private static final String INPUT_ZIP_FILE = "C:/Users/gondu/Desktop/cambios_locales.zip";
    private static final String OUTPUT_FOLDER = "C:/Users/gondu/Desktop/outputzip";
    final byte[] buffer = new byte[1024];
    int count = 0;

    public static void main(String[] args) throws IOException {
        UnZip unZip = new UnZip();
        unZip.unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
    }

    /**
     * Unzip it
     *
     * @param zipFile input zip file
     * @param outputFolder
     * @throws java.io.FileNotFoundException
     */
    public void unZipIt(String zipFile, String outputFolder) throws FileNotFoundException, IOException {

        try {
            //create output directory is not exists
            File folder = new File(OUTPUT_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zipped file list entry
            try ( //get the zip file content
                    ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
                //get the zipped file list entry
                ZipEntry ze = zis.getNextEntry();
                
                while (ze != null) {
                    
                    String fileName = ze.getName();
                    File newFile = new File(OUTPUT_FOLDER + File.separator + fileName);
                    
                    System.out.println("file unzip : " + newFile.getCanonicalPath());
                    
                    //create all non exists folders
                    //else you will hit FileNotFoundException for compressed folder
                    if (ze.isDirectory()) {
                        new File(newFile.getParent()).mkdirs();
                    } else {
                        FileOutputStream fos = null;
                        
                        new File(newFile.getParent()).mkdirs();
                        File output2 = new File("C:/Users/gondu/Desktop/outputzip/output" + count + ".jpeg");
                        if (newFile.getName().endsWith("jpeg")) {
                            output2 = newFile;
                            fos = new FileOutputStream(output2);
                        } else if (newFile.getName().endsWith("png")) {
                            fos = new FileOutputStream(output2);
                            saveImage(zis, fos);
                            
                        } else if (newFile.getName().endsWith("bmp")) {
                            fos = new FileOutputStream(output2);
                            saveImage(zis, fos);
                            
                        } else if (newFile.getName().endsWith("gif")) {
                            
                            fos = new FileOutputStream(output2);
                            saveImage(zis, fos);
                        } else if (newFile.getName().endsWith("tiff")) {
                            
                            fos = new FileOutputStream(output2);
                            saveImage(zis, fos);
                            
   
                        } else {
                            return;
                        }
                        
                        fos.close();
                    }
                    
                    ze = zis.getNextEntry();
                }
                
                zis.closeEntry();
            }

            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveImage(ZipInputStream zis, FileOutputStream fos) throws FileNotFoundException, IOException {

        count++;
        int len;
        while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
    }
}
