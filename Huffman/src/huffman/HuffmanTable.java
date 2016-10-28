/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Sergi Diaz, Fernando Padilla
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

    /*
        El objetivo de builTree es formar la tabla de Huffman tal y como la solucionamos en papel
        El procedimiento es el siguiente: la lista enlazada creada en esta clase
        es ascendiente, por lo que podemos empezar de izquierda a derecha para concatenar strings
        Cuando unamos las letras con menos prob, E,D -> DE, lo añadimos al diccionario junto
        con el resto de letras sin juntar.
     */
    
    /*
    Esta funcion suele quedarse pillada en el bucle while.
    Debajo he añadido una version alternativa que funciona, aunque su implementación
    es bastante fea.
    */
    
    public TreeMap<String, Float> buildTree(HuffmanTree tree) {
        //estructura de datos temporal para guardar los nodos
        TreeMap<String, Float> dictionary = new TreeMap<>();
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
                //añadimos el nuevo nodo, formado de la unión de dos nodos con la misma prob
                //dictionary.put(probe.next.word, probe.next.prob);
                removeFirst();
                probe.next = first;
                //falta añadir el resto de nodos que todavía no se han juntado iteración n: DE,C,B,A   
                dictionary = enterNodes(dictionary);
                
                //una vez avanzado el proceso y nos queden dos nodos, 0.6 y 0.4 
                //por ejemplo, los unimos en el nodo root del árbol
            } else if (probe.prob != probe.next.prob && size == 2) {
                double probabilitat = probe.prob;
                String word = probe.word;
                probe.next.prob += probabilitat;
                probe.next.word += word;
                probe.next.word = sortString(probe.next.word);
                dictionary.put(probe.next.word, probe.next.prob);
                //falta añadir el resto de nodos que todavía no se han juntado iteración n: DE,C,B,A
                removeFirst();
                probe.next = first;
                dictionary = enterNodes(dictionary);
            } else {
                probe.next = first;  
            } 
        }
        return dictionary;
    }    
    
    /*
        Construct tree, como su nombre indica, se encarga de construir el arbol de 
        Huffman de la tabla siguiendo el algoritmo de Huffman. No retorna nada,
        pues la misma función se encarga de añadir los nodos en el arbol.
    */
    public void constructTree(HuffmanTree tree) {
        //estructura de datos temporal para guardar los nodos
        ArrayList<Entry> dictionary = new ArrayList<>();
        
        // Insert current table in the array.
        dictionary.addAll(getArray());
        
        Entry entry = first;
        while (entry != null) {
            // Stop when only one element remains in the table.
            if (entry.next == null) 
                break;
 
            // Add new entry, such entry set as a union of the 2 elements with
            // the lowest probability.
            addEntry(entry.word + entry.next.word, entry.prob + entry.next.prob);
            // Remove the united elements.
            removeFirst();      
            removeFirst();  
            entry = first;
            
            // Insert current table in the array.
            dictionary.addAll(getArray());
        }  
        // Build tree.
        for(int i=dictionary.size()-1; i>=0; i--) { 
            tree.addNode(dictionary.get(i).word, dictionary.get(i).prob);
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

    private TreeMap<String, Float> enterNodes(TreeMap<String, Float> dictionary) {
        Entry probe = first;
        while (probe != null) {
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
    
    private ArrayList<Entry> getArray() {
        if (first == null) {
            return null;
        }
        ArrayList<Entry> table = new ArrayList<>();
        Entry entry = first;
        while (entry != null) {
            table.add(new Entry(entry.word, entry.prob));
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