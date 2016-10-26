/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sergi Diaz
 */
// Sorted linked list of entries
public class HuffmanTable {

    private HashMap<String, Float> list = new HashMap<>();
    private Entry first;
    private int size;

    // Stores word and probability
    class Entry {

        public String word; // might be a symbol, number or a chain of symbols
        public float prob;  // probability
        public Entry next;  // next entry pointer

        public Entry(String word, float prob) {
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

    public HuffmanTable() {
        first = null;
        size = 0;
    }

    public void setList(String word, float prob) {
        list.put(word, prob);
    }

    public void asdf() {
        for (Map.Entry<String, Float> e : list.entrySet()) {
            addEntry(e.getKey(), e.getValue());
            size++;
        }
    }

    public void buildTree(HuffmanTree tree) {
        //estructura de datos temporal para guardar los nodos
        HashMap<String, Float> dictionary = new HashMap<>();
        Entry probe = first;
        dictionary = enterNodes(dictionary); //a,b,c,d and e (first iteration)
        while (probe.next != null) {
            // Nodos unidos tienen la misma probabilidad, los unimos
            if (probe.prob == probe.next.prob) {
                double probabilitat = probe.prob;
                String word = probe.word;
                probe.next.prob += probabilitat;
                probe.next.word += word;
                probe.next.word = sortString(probe.next.word);
                dictionary.put(probe.next.word, probe.next.prob);
                removeFirst();
                //una vez avanzado el proceso y nos queden dos nodos, 0.6 y 0.4 
                //por ejemplo, los unimos en el nodo root del árbol
            } else if (probe.prob != probe.next.prob && size == 2) {
                double probabilitat = probe.prob;
                String word = probe.word;
                probe.next.prob += probabilitat;
                probe.next.word += word;
                probe.next.word = sortString(probe.next.word);
                dictionary.put(probe.next.word, probe.next.prob);
                removeFirst();
            } else {
                probe = probe.next;
            }
        }
    }

    // Sorted insertion
    public void addEntry(String word, float prob) {
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

    public void removeFirst() {
        if (first != null) {
            first = first.next;
            size--;
        }
    }

    public Entry removeLast() {
        Entry probe = first;
        Entry previousProbe = null;
        while (probe.next != null) {
            if (probe.next == null) {
                previousProbe.next = null; //one entry removed from last position
                size--;
                return probe;
            }
            previousProbe = probe;
            probe = probe.next;

        }

        return probe;
    }

    private HashMap<String, Float> enterNodes(HashMap<String, Float> dictionary) {
        Entry probe = first;
        while (probe.next != null) {
            dictionary.put(probe.word, probe.prob);
            probe = probe.next;
        }
        return dictionary;
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

    public int size() {
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

    /*
        Sorted (char) string array
     */
    private String sortString(String word) {

        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return String.valueOf(chars);
    }

}
