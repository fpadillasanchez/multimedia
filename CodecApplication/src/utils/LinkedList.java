/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * *
 * Linked list. Extraction policy specified by its subclasses.
 *
 * @author Sergi Diaz
 */
public abstract class LinkedList {

    protected Item first;  // first item in the list
    protected Item last;   // last item in the list 
    protected int size;    // number of items in the list

    /**
     * Class item
     *
     */
    class Item {    // List item class

        Object item;    // data contained by the item
        Item prev;      // previous item
        Item next;      // next item

        /**
         * Constructor of each Item. Contains an item, the previous item and the one
         * @param item
         */
        public Item(Object item) {
            this.item = item;
            prev = null;
            next = null;
        }

        /**
         * Set method that points and links to the next item
         *
         * @param next
         */
        public void setNext(Item next) {
            this.next = next;
        }

        /**
         * *
         *
         * @param prev
         */
        public void setPrev(Item prev) {
            this.prev = prev;
        }

        /**
         * *
         *
         * @return
         */
        public Item getNext() {
            return next;
        }

        /***
         * 
         * @return 
         */
        public Item getPrev() {
            return prev;
        }
    }

    /***
     * 
     */
    public LinkedList() {
        first = null;
        size = 0;
    }

    /***
     * 
     * @return 
     */
    
    public int size() {
        return size;
    }

    /***
     * 
     * @return 
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /***
     * 
     * @param object 
     */
    public void push(Object object) {
        Item item = new Item(object);

        if (size == 0) {    // list is empty
            first = item;
            last = item;
        } else {            // insert item at the end of the list
            last.next = item;
            item.prev = last;
            last = item;
        }
        size++;             // increment size counter
    }

    /***
     * 
     */
    public void removeAll() {
        first = null;
        last = null;
        size = 0;
    }

    /***
     * 
     * @return 
     */
    public abstract Object pop();

}
