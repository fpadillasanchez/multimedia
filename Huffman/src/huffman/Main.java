package huffman;

import java.util.Random;

/**
 *
 * @author Sergi Diaz, Fernando Padilla
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        HuffmanTable table = new HuffmanTable();
        Random ran = new Random();
        char[] abecedario = createArray();
        float prob[][] = {
            {0.3f, 0.3f, 0.2f, 0.1f, 0.1f},
            {0.6f, 0.2f, 0.1f, 0.05f, 0.05f},
            {0.5f, 0.2f, 0.1f, 0.05f, 0.05f},
            {0.4f, 0.3f, 0.2f, 0.05f, 0.05f}
        };

        HuffmanTree tree = new HuffmanTree();
                
        System.out.println("Abecedario: "); 
        //every time we run the program a different prob will be loaded from the prob array
        for (int i = 0; i < 5; i++) {
            System.out.println(String.valueOf("\t" + abecedario[i]) + " = " + prob[ran.nextInt(4)][i]); // muestra valores iniciales
            table.setList(String.valueOf(abecedario[i]), prob[3][i]);
        }
        table.fillList();
        table.constructTree(tree);
        
        // It show the codification obtained by the HuffMan Tree:
        System.out.println("\nCodificacion: ");
        for (int i=0; i < 5; i++) {
            System.out.println("\t" + abecedario[i] + " = " + tree.getCode("" + abecedario[i]));
        }

    }

    /**
     * Create an array filled with each letter of the alphabet
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
