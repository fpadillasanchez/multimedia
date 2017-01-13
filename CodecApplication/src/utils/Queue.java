/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/***
 * Queue implemented as a FILO linked list.
 * @author Sergi Diaz Fernando Padilla
 */
public class Queue extends LinkedList {
    
    public Queue() {
        super();
    }
    /***
     * Return the first item of the queue and then it shorthens the queue by one
     * @return 
     */
    @Override
    public Object pop() {
        if (size == 0)
            return null;    // return null if list is empty
        
        Object obj = first.item;    // keep object in the first item of the list   
        first = first.next;         // remove first item from the list
        size--;
        
        return obj;
    }
    
    /***
     * Extracts the first item in the queue, enqueues the item at the end of the list and returns the object kept in that item
     * @return 
     */
    public Object popCircular() {
        Object obj = pop(); // pop first item in the list
        
        if (obj != null)    // add item at the end of the list if not null
            push(obj);
        
        return obj;         // return popped object.
    }
    
}
