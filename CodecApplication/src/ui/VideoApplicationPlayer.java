/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.CardLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author gondu
 */
public class VideoApplicationPlayer extends javax.swing.JFrame {

    private JFrame frame;
    private JTextField textFieldUserName;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuItem;

    public VideoApplicationPlayer() throws HeadlessException {
        initConstructor();

    }

    private void initConstructor() {
        this.frame = new JFrame("Demo program for JFrame");
        this.menuBar = new JMenuBar();
        this.menuFile = new JMenu("File");
        this.menuItem = new JMenuItem("Exit");
        this.textFieldUserName = new JTextField(50);

        frame.setLayout(new CardLayout());
        frame.add(textFieldUserName);
        menuBar.add(menuItem);
        menuFile.add(menuItem);
        // adds menu bar to the frame
        frame.setJMenuBar(menuBar);
        //Make sure we set size for the frame before making it visible
        frame.setSize(300, 200);
        frame.setVisible(true);
        //Hide the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setBounds(100, 100, 300, 400);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VideoApplicationPlayer().setVisible(true);
            }
        });

    }

}
