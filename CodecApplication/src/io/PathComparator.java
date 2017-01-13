/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.util.Comparator;

/**
 * Class used for sorting codec's file paths. Such paths are expected to contain some sort of numerical identificator.
 * @author Sergi Díaz
 */
public class PathComparator implements Comparator<String> {
    String key = "";
    
    public PathComparator(String key) {
        this.key = key;
    }
     
    @Override
    public int compare(String s1, String s2) {
        try {
            // Use key to obtain relevant fragment from path
            String sub_s1 = s1.substring(s1.lastIndexOf(key)+key.length(), s1.lastIndexOf('.'));
            String sub_s2 = s2.substring(s2.lastIndexOf(key)+key.length(), s2.lastIndexOf('.'));
            // Such fragment is expected to be a number, return then a numerical comparison
            return Integer.parseInt(sub_s1) - Integer.parseInt(sub_s2);
            
        } catch (Exception ex) {
            // Use default comparison if previous one failed
            return s1.compareTo(s2);
        }
        
          
        
    }
}
