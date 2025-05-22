package org.aryanoor.GUI.Preferences;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class Button extends JButton {
    public Button(String label) {
        setText(label);
        setBackground(new Color(51, 77, 173));
        setForeground(Color.white);
        setPreferredSize(new Dimension(100, 40));
        setFocusable(false);
        setBorder(null);
    }
}
