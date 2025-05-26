package org.aryanoor.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JPanel currentPanel;

    public GUI() {
        setTitle("Chatbot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        currentPanel = new JPanel();
        this.add(currentPanel, BorderLayout.CENTER);

        showSignInWindow();
        setVisible(true);
    }

    private void showLogInWindow() {
        currentPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        panel.add(titleLabel);

        JPanel usernamePanel = new JPanel(new BorderLayout());
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField();
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        panel.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField();
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        panel.add(passwordPanel);

        JButton loginButton = new JButton("LogIn");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.BLUE);
        panel.add(loginButton);

        JPanel signupPanel = new JPanel();
        JLabel signupLabel = new JLabel("Don't have an account yet?");
        JButton signupButton = new JButton("Sign up");
        signupButton.setBorderPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setForeground(Color.BLUE);
        signupLabel.setForeground(Color.BLUE);
        signupPanel.add(signupLabel);
        signupPanel.add(signupButton);
        panel.add(signupPanel);

        currentPanel.add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if(username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else {

            }
        });

        signupButton.addActionListener(e -> showSignInWindow());

        revalidate();
        repaint();
    }

    private void showSignInWindow() {
        currentPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Create Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        panel.add(titleLabel);

        JPanel usernamePanel = new JPanel(new BorderLayout());
        JLabel usernameLabel = new JLabel("Username");
        JTextField usernameField = new JTextField();
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        panel.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField();
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        panel.add(passwordPanel);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(Color.BLUE);
        panel.add(registerButton);

        JPanel loginPanel = new JPanel();
        JLabel loginLabel = new JLabel("Already have an account? ");
        JButton loginButton = new JButton("Log in");
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setForeground(Color.BLUE);
        loginLabel.setForeground(Color.BLUE);
        loginPanel.add(loginLabel);
        loginPanel.add(loginButton);
        panel.add(loginPanel);

        currentPanel.add(panel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if(username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                showLogInWindow();
            }
        });

        loginButton.addActionListener(e -> showLogInWindow());

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}