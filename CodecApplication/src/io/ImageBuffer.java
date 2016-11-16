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
 * @author Sergi Diaz, Fernando Padilla
 * 
 * 
 * The puspose of this class is to hold images to be reproduced. We intend to 
 * reduce the ammount of images loaded during reproduction by loading them before
 * the video starts. At this point, the buffer is pretty simplistic. Our objective
 * is to improve its performance before the final release of the codec.
 */
public class ImageBuffer {

    // Linked queue
    class Buffer {
        // Queue item
        class BufferItem {

            public BufferedImage item;      // image hold by the item
            public BufferItem next = null;  // next item in the queue

            public BufferItem(BufferedImage item) { // Constructor
                this.item = item;
            }
        }

        public int size;                    // size of the queue
        BufferItem first, last, current;    // first, last and current items

        public Buffer() {   // Constructor
            size = 0;
            first = null;
            last = null;
        }

        // Add new image at the end of the queue.
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

        // Remove the first image in the queue.
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

    // Given an array of file paths, buffer gets loaded by images directed by those paths.
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

    // Return first image in the buffer. If 'circular' is true, the removed image
    // is inserted again at the end of the queue.
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
