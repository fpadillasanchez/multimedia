/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author SDP
 * 
 * Linked list. Extraction policy specified by its subclasses.
 */
public abstract class LinkedList {
    
    class Item {    // List item class
        Object item;    // data contained by the item
        Item prev;      // previous item
        Item next;      // next item
        
        public Item(Object item) {
            this.item = item;
            prev = null;
            next = null;
        }
        
        // Setters:
        
        public void setNext(Item next) {
            this.next = next;
        }
        
        public void setPrev(Item prev) {
            this.prev = prev;
        }
        
        // Getters: 
        
        public Item getNext() {
            return next;
        }
        
        public Item getPrev() {
            return prev;
        }
    } 
    
    protected Item first;  // fisrt item in the list
    protected Item last;   // last item in the list 
    protected int size;    // number of items in the list
    
    // Create stack
    public LinkedList() {
        first = null;
        size = 0;
    }
    
    // Returns number of items in the stack
    public int size() {
        return size;
    }
    
    // Returns true if there are no items in the list
    public boolean isEmpty() {
        return size == 0;
    }
    
    // Push new object into the list
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
    
    // Removes all items from the list
    public void removeAll() {
        first = null;
        last = null;
        size = 0;
    }
    
    // Pop item from the list
    public abstract Object pop();
      
}
