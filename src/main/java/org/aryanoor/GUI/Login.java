package org.aryanoor.GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//preferences for text fields and submit buttons
import org.aryanoor.GUI.Preferences.Button;
import org.aryanoor.GUI.Preferences.Field;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {
    private Field username, password;
    private Button submit;

    Login() {
        setLayout(new FlowLayout());
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        username = new Field();
        password = new Field();
        submit = new Button("Submit");
        submit.addActionListener(this);
        JPanel usernamPanel = new JPanel();
        usernamPanel.setLayout(new FlowLayout());
        usernamPanel.add(new JLabel("username: "));
        usernamPanel.add(username);
        JPanel pass = new JPanel();
        pass.setLayout(new FlowLayout());
        pass.add(new JLabel("password: "));
        pass.add(password);
        add(usernamPanel);
        add(pass);
        add(submit);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            System.out.println(username.getText() + " my beautiful " + password.getText());
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}