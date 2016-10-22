/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Sergi Diaz
 */

// Sorted linked list of entries
public class HuffmanTable {
    // Stores word and probability
    class Entry {
        public String word; // might be a symbol or a chain of symbols
        public float prob;  // probability
        public Entry next;  // next entry
        
        public Entry (String word, float prob) {
            this.word = word;
            this.prob = prob;
            next = null;
        }
        
        public boolean isBefore(Entry other) {
            if (other == null) {
                return false;
            }
            return this.prob < other.prob;
        }
    }
    
    private Entry first;
    private int size;
    
    public HuffmanTable () {
        first = null;
        size = 0;
    }
    
    public static HuffmanTable setList(HashMap<String, Float> hash) {
        HuffmanTable list = new HuffmanTable();
        for (HashMap.Entry<String, Float> e : hash.entrySet()) {
            list.addEntry( e.getKey(), e.getValue() );
            list.size ++;
        }
        return list;
    }
    
    // Sorted insertion
    public void addEntry (String word, float prob) {
        Entry entry = new Entry(word, prob);
        size++;
        
        if (first == null) {
            first = entry;
            return;
        }
        if (entry.isBefore(first)) {
            entry.next = first;
            first = entry;
            return;
        }
        
        // Sorted from low to high probability
        Entry probe = first;
        while (probe.next != null) {
            if (entry.isBefore(probe.next)) {
                entry.next = probe.next;
                probe.next = entry;
                return;
            }
            probe = probe.next;
        }
        probe.next = entry;
    } 
    
    public void removeFirst () {
        if (first != null) {
            first = first.next;
            size--;
        }
    }
    
    public String getFirstWord() {
        if (first == null) {
            return null;
        }
        return first.word;
    }
    
    public float getLowestProbability() {
        if (first == null) {
            return 0f;
        }
        return first.prob;
    }
    
    public boolean isEmpty() {
        return first == null;
    }
    
    public int size () {
        return size;
    }
    
    public HashMap<String, Float> getTable() {
        if (first == null) {
            return null;
        }
        HashMap<String, Float> table = new HashMap<>();
        Entry entry = first;
        while (entry != null) {
            table.put(entry.word, entry.prob);
            entry = entry.next;
        }
        return table;
    }
    
    @Override
    public String toString() {
        String str = "";
        
        Entry entry = first;
        while (entry != null) {
            str += (entry.word + ": " + entry.prob + "\n");
            entry = entry.next;
        }
        return str;
    }
    
}
