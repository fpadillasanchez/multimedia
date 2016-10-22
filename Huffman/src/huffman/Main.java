package huffman;

import java.util.HashMap;


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        HashMap<String, Float> lang = new HashMap<>(); // llenguatge  a codificar
        lang.put("A", 0.4f);    // A = 0.4
        lang.put("B", 0.4f);    // B = 0.4
        lang.put("C", 0.1f);    // C = 0.1
        lang.put("D", 0.05f);   // D = 0.05
        lang.put("E", 0.05f);   // E = 0.05
        
        HuffmanTree tree = new HuffmanTree();
        /*
        L'arbre implementat està pensat per rebre com a input cada element de les
        taules de probabilitats que es generen fent servir el algorisme de Huffman.
        Es passa cada paraula amb la seva probabilitat associada. L'ordre ha de ser
        de més a menys probable. No importa que alguns elements estiguin repetits.
        
        Aquesta part encara no tinc clar com fer-la, he estat provant amb llistes
        enllaçades (classe HuffmanTable).
        */
        tree.addNode("ABCDE", 1f);
        // 3a iteració
        tree.addNode("BCDE", 0.6f);
        tree.addNode("A", 0.4f);
        // 2a iteració
        tree.addNode("A", 0.4f);
        tree.addNode("B", 0.4f);
        tree.addNode("CDE", 0.2f);
        // 1a iteració
        tree.addNode("A", 0.4f);
        tree.addNode("B", 0.4f); 
        tree.addNode("C", 0.1f);
        tree.addNode("DE", 0.1f);
        // inici
        tree.addNode("A", 0.4f);
        tree.addNode("B", 0.4f); 
        tree.addNode("C", 0.1f);
        tree.addNode("D", 0.05f);
        tree.addNode("E", 0.05f);
        
        // Mostra per pantalla el codi de cada lletra del llenguatge:
        for (String symbol : lang.keySet()) {
            System.out.println(symbol + " = " + tree.getCode(symbol, 0));
        }
        
    }
    
}
