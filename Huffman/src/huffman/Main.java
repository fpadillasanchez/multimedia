package huffman;

import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Random ran = new Random();
        char[] abecedario = createArray();
        float prob[][] = {
            {0.3f, 0.3f, 0.2f, 0.1f, 0.1f},
            {0.6f, 0.2f, 0.1f, 0.05f, 0.05f},
            {0.5f, 0.2f, 0.1f, 0.05f, 0.05f},
            {0.4f, 0.3f, 0.2f, 0.05f, 0.05f}
        };

        HuffmanTree tree = new HuffmanTree();
        
        /*
        
        Yo evitaria usar este método con random, porque la suma de probabilidades no siempre es 1.
        
        
        //This is gonna be random
        HuffmanTable table = new HuffmanTable();
        for (int i = 0; i < 5; i++) {
            System.out.println(String.valueOf(abecedario[i]) + " = " + prob[ran.nextInt(4)][i]);
            table.setList(String.valueOf(abecedario[i]), prob[ran.nextInt(4)][i]);
        }
        */
        
        System.out.println("Abecedario: "); 
        HuffmanTable table = new HuffmanTable();
        for (int i = 0; i < 5; i++) {
            System.out.println(String.valueOf("\t" + abecedario[i]) + " = " + prob[3][i]); // muestra valores iniciales
            table.setList(String.valueOf(abecedario[i]), prob[3][i]);
        }
        table.asdf();
        table.constructTree(tree);
        
        // Muestra la codificación obtenida:
        System.out.println("\nCodificacion: ");
        for (int i=0; i < 5; i++) {
            System.out.println("\t" + abecedario[i] + " = " + tree.getCode("" + abecedario[i]));
        }
        
        /*
        table.asdf();

        TreeMap<String, Float> dictionary = table.buildTree(tree);
    
        NavigableMap<String,Float> nset = dictionary.descendingMap();

        for(Entry<String, Float> entry: nset.entrySet()){
            System.out.println(entry.getKey() + " = " + entry.getValue());
            tree.addNode(entry.getKey(),entry.getValue());
        } 
        */
    }

    /**
     * Create an array filled with the letter from the alphabet
     *
     * @return
     */
    private static char[] createArray() {
        char[] s = new char[26];
        for (int letra = 0; letra < 26; letra++) {
            s[letra] = (char) ('A' + letra);
        }
        return s;
    }

}
