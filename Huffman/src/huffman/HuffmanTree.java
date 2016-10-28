package huffman;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Sergi Diaz
 */

/*
    L'arbre implementat està pensat per rebre com a input cada element de les
    taules de probabilitats que es generen fent servir el algorisme de Huffman.
    Es passa cada paraula amb la seva probabilitat associada. L'ordre ha de ser
    de més a menys probable. No importa que alguns elements estiguin repetits.
 */
public class HuffmanTree {
    
    class Node {
        public String symbol; 
        public float prob;  // probability
        
        public Node parent, right, left;
        
        public Node (String symbol, float prob) {
            this.symbol = symbol;
            this.prob = prob;
            parent = null;
            right = null;
            left = null;    
        }
        
        public boolean isEqual (Node other) {
            if (other == null) {
                return false;
            }
            return this.symbol.equals(other.symbol);
        }
        
        public boolean isLeaf () {
            return this.symbol.length() < 2;
        }
    }
    
    private Node root;
    
    public HuffmanTree () {
        root = null;
    }
    
    // Add a node to the tree with symbol and probability given as parameters.
    public void addNode(String symbol, float prob) {
        Node node = new Node(symbol, prob);
        
        // Set new node as root if the tree is empty.
        if (root == null) {
            root = node;
            return;
        } 
        
        Node probe = root;
        while (true) { 
            // Abort insertion if the node is already in the tree.
            if (node.isEqual(probe)) {
                break;
            }
            // Set new node as current's right child if it has none.
            if (probe.right == null) {
                probe.right = node;
                node.parent = probe;
                break;
            } else {
                if (node.isEqual(probe.right)) { break; } // abort!
            }
            // Set new node as current's left child if it has none.
            if (probe.left == null) {
                probe.left = node;
                node.parent = probe;
                break;
            }else {
                if (node.isEqual(probe.left)) { break; } // abort!
            }
            // Move to left if right child is a leaf node.
            if (probe.right.isLeaf()) {
                probe = probe.left;
            } else {
                // Move to right otherwise.
                probe = probe.right;
            }
        }
    }
    
    // Returns a string with the encoding of a given symbol.
    public String getCode (String symbol) {
        String path = ""; 
        
        // Probability is irrelevant to perform the search.
        Node node = searchNode (new Node(symbol, 0));
        if (node == null) {
            return path; // node not found in the tree
        }
        
        // Code build by assigning binary values to the path from the root to
        // the node which holds the symbol.
        Node parent = node.parent;
        while (parent != null) {
            // Assignation is purely arbitrary.
            if (node.isEqual(parent.right)) {
                path += "1";
            } else {
                path += "0";
            }
            node = parent;
            parent = parent.parent;
        }
        // Ater doing this, we've got the path from the node to the root.
        // Since we want to return the path from the root to the node, we must
        // reverse the obtained string.
        
        StringBuilder builder=new StringBuilder(path); // reverse path
        return builder.reverse().toString();
    }
    
    // Searches in the tree for a node equal to the parameter. Returns null if not found.
    private Node searchNode (Node node) {
        if (root == null) {
            // Return null if the tree is empty
            return null;
        }    
        Node probe;
        
        // BFS algorithm
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()) {
            probe = q.remove();
            
            if (probe.isEqual(node)) {
                return probe;
            }
            if (probe.left != null) {
                q.add(probe.left);
            }
            if (probe.right != null) {
                q.add(probe.right);
            }
        }
        
        return null;
    }
    
    // For testing purpose. Returns some schematics about the content of the tree.
    @Override
    public String toString() {
        String str = "";
        if (root == null) {
            // Return null if the tree is empty
            return null;
        }    
        Node probe;
        
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()) {
            probe = q.remove();
            if (probe.parent == null) {
                str += (probe.symbol + " - parent: NaN" + "\n" );
            } else {
                str += (probe.symbol + "  - parent: " + probe.parent.symbol + "\n");
            }
            if (probe.right != null) {
                str += "\tright: " + probe.right.symbol + "\n";
            }
            if (probe.left != null) {
                str += "\tleft: " + probe.left.symbol + "\n";
            }
            
            if (probe.left != null) {
                q.add(probe.left);
            }
            if (probe.right != null) {
                q.add(probe.right);
            }
        }
        
        return str;
    }
    
    // Node n1 takes n2 position in the tree.
    private void replace (Node n1, Node n2) {
        if (n1 == null || n2 == null) {
            return;
        }
        // Node n1 takes over n2 right son if it exists
        if (n2.right != null) {
            n1.left = n2.right;
            n2.right.parent = n1;
            n2.right = null;
        }
        // Node n2 set as n1's right son.
        n1.parent = n2.parent;
        n2.parent = n1;
        // If n2 has still a son, it is placed to the right.
        if (n2.left != null) {
            n2.right = n2.left;
            n2.left = null;
        }
    }
}
