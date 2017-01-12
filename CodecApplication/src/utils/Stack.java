/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/***
 * Stack implemented as a FIFO linked list.
 * @author Sergi Diaz
 */
public class Stack extends LinkedList {    

    // Create stack
    public Stack() {
        super();
    }
    
    /***
     * 
     * @return 
     */
    @Override
    public Object pop() {
        if (size == 0) 
            return null;
        
        Object obj = last.item; // keep object hold at the last item in the list
        last = last.prev;       // remove last item from the list
        size--;
        
        return obj;
    }
}
