/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author SDP
 */
public class ImageBuffer {

    class Buffer {

        class BufferItem {

            public BufferedImage item;
            public BufferItem next = null;

            public BufferItem(BufferedImage item) {
                this.item = item;
            }
        }

        public int size;
        BufferItem first, last, current;

        public Buffer() {
            size = 0;
            first = null;
            last = null;
        }

         public void push(BufferedImage image) {
            size++;
            BufferItem item  = new BufferItem(image);
            if (first == null) {
                first = item;            
                return;
            }
            if (last == null) {
                last = item;
                first.next = last;
                return;
            }          
            last.next = item;
            last = item;
         }

        public BufferedImage pop() {
            if (size == 0) {
                return null;
            }
            size--;
            BufferedImage image = first.item;
            first = first.next;
            return image;
        }
    }

    // TODO: limit the ammount of images in the buffer
    private Buffer buffer;

    public ImageBuffer() {
        buffer = new Buffer();
    }

    public int size() {
        return buffer.size;
    }

    public void loadBuffer(String input) throws IOException {

    }

    public void loadBuffer(ArrayList<String> files) throws IOException {
        for (String file : files) {
            BufferedImage image = FileIO.readImage(file);
            if (image != null) {
                buffer.push(image);
            }
        }
    }
/*
    public void loadImages(String[] files) throws IOException {
        for (String file : files) {
            buffer.push(loadImage(file));
        }
    }
*/
    public void loadImagesArrayList(ArrayList<BufferedImage> files) throws IOException {
        Iterator<BufferedImage> iter = files.iterator();
        while (iter.hasNext()) {
            buffer.push(iter.next());
        }

    }

    public BufferedImage getImage(boolean circular) {
        if (buffer.size == 0) {
            return null;
        }
        BufferedImage image = buffer.pop();
        
        if (circular) {
            buffer.push(image);
        }

        return image;

    }
    
    public BufferedImage getImage2() {

        return buffer.pop();
    }
    
    public void pushImage(BufferedImage image){
        buffer.push(image);
    }
/*
    private BufferedImage loadImage(String file) throws IOException {
        return ImageIO.read(new File(file));
    }
*/
}
