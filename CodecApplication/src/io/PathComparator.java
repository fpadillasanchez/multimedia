/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import control.CodecConfig;
import java.util.Comparator;

/**
 *
 * @author SDP
 */
public class PathComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        String key = CodecConfig.decoder_sub_directory;
        
        String sub_s1 = s1.substring(s1.lastIndexOf(key)+key.length(), s1.lastIndexOf('.'));
        String sub_s2 = s2.substring(s2.lastIndexOf(key)+key.length(), s2.lastIndexOf('.'));
          
        return Integer.parseInt(sub_s1) - Integer.parseInt(sub_s2);
    }
}
