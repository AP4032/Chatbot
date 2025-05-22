package org.aryanoor.GUI.Preferences;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;

public class Field extends JTextField {
    public Field() {
        setPreferredSize(new Dimension(200, 30));
        setFont(new Font("Arial", Font.PLAIN, 24));
    }
}
