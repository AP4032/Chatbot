package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.awt.Cursor;

public class GUI extends JFrame {
    public CLI cli ;
    public JTextArea textArea;
    public JTextField nameField;
    public JPasswordField passwordField;
    public JTextField usernameField;
    public JPasswordField passwordField2;
    public JTextField inputField;

    public GUI() throws IOException {
        textArea = new JTextArea();
inputField = new JTextField();
        setTitle("Chatbot");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void run() {
        getContentPane().removeAll();
        JLabel label = new JLabel("Welcome to Chatbot", SwingConstants.CENTER);
        label.setForeground(Color.BLUE);
        label.setFont(new Font("Tahoma", Font.BOLD, 20));
        label.setBorder(new EmptyBorder(25, 25, 25, 25));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(label, BorderLayout.NORTH);

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.orange);

        JLabel nameLabel = new JLabel("UserName: ");
        nameLabel.setBorder(new EmptyBorder(25, 25, 25, 25));
        nameField = new JTextField(25);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setBorder(new EmptyBorder(25, 25, 25, 25));
        passwordField = new JPasswordField(25);

        JCheckBox rememberCheckBox = new JCheckBox("Remember me");
        rememberCheckBox.setBorder(new EmptyBorder(25, 25, 25, 25));
        rememberCheckBox.setSelected(true);
        rememberCheckBox.setBackground(Color.orange);

        JButton loginButton = new JButton("Login");
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(new EmptyBorder(25, 25, 25, 25));
        loginButton.setBackground(Color.BLUE);
        JLabel signupLabel = new JLabel("<Html><U>Signup</U></Html>");
        signupLabel.setForeground(Color.BLUE);
        signupLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signup();
            }
        });

        panel2.add(nameLabel);
        panel2.add(nameField);
        panel2.add(passwordLabel);
        panel2.add(passwordField);
        panel2.add(rememberCheckBox);
        panel2.add(loginButton);
        panel2.add(signupLabel);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setBackground(Color.WHITE);
        panel3.setBorder(new EmptyBorder(100, 150, 50, 150));
        panel3.add(panel2, BorderLayout.CENTER);

        panel.add(panel3, BorderLayout.CENTER);
        add(panel);

        revalidate();
        repaint();
        loginButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            IAM user = new IAM(nameField.getText(), password);
            try {
                if (user.login(nameField.getText(), password)) {
                    chatpanel();
                }else {
                    eror();
                }
            } catch (IOException ex) {

                throw new RuntimeException(ex);

            }
        });


    }

    public void chatpanel() {

        getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.BLUE);

        JLabel titleLabel = new JLabel("  AI Chat Assistant");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBorder(new EmptyBorder(25, 25, 25, 25));
        logoutButton.setForeground(Color.BLUE);
        logoutButton.setBackground(Color.GRAY);

        logoutButton.addActionListener(e -> {
            run();
        });

        headerPanel.add(logoutButton, BorderLayout.EAST);


        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.GRAY);

        inputField = new JTextField(45);
        inputField.setEditable(true);
        inputField.setBackground(Color.WHITE);
        inputField.setForeground(Color.BLACK);

        JButton sendbutton = new JButton("Send");
        sendbutton.setForeground(Color.WHITE);
        sendbutton.setBackground(Color.BLUE);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendbutton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
        sendbutton.addActionListener(e -> {
            try {
                cli();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


    }

    public void signup() {
        getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel nameLabel = new JLabel("Create Your Account", SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(Color.CYAN);
        formPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(25);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField2 = new JPasswordField(25);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField2);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.BLUE);
        registerButton.setForeground(Color.WHITE);

        registerButton.addActionListener(e -> {
            String password = new String(passwordField2.getPassword());
            IAM newUser = new IAM(usernameField.getText(),password );
            try {
                run();
                newUser.signUp();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });



        JLabel backLabel = new JLabel("<HTML><U>Back to Login</U></HTML>");
        backLabel.setForeground(Color.BLUE);
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.setHorizontalAlignment(SwingConstants.CENTER);

        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                run();
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(50, 150, 50, 150));

        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(registerButton, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(backLabel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void eror() {
        JOptionPane.showMessageDialog(this, "Invalid credentials. Try again!", "Error", JOptionPane.ERROR_MESSAGE);
        run();
    }


    public void cli() throws IOException {
        cli.chat();


    }
}
