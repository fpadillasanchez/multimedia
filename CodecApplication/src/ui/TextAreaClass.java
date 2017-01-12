/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/***
 * 
 * @author gondu
 */
public class TextAreaClass extends OutputStream {
    private final JTextArea textArea;
    private final StringBuilder sb = new StringBuilder();

    public TextAreaClass(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == '\r'){
            return;
        }
        
        if (b == '\n'){
            final String text = sb.toString() + "\n";
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    textArea.append(text);
                }
            });
            sb.setLength(0);
        }
        sb.append((char)b);
    }
    
    
}
