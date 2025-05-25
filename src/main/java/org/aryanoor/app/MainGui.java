package org.aryanoor.app;

import com.formdev.flatlaf.FlatDarculaLaf;
import java.io.IOException;

import javax.swing.*;

public class MainGui {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(()-> {
            try {
                new GUI();
            } catch (IOException ex) {
            }
        });
    }
}
