package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


import static java.awt.Font.BOLD;


public class GUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel panel;
    private CLI cli = new CLI();
    private OpenRouterChat chatBot;

    public GUI() throws IOException {
        setTitle("ChatBot");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);
        panel.add(showLogInPanel(),"Login");
        panel.add(showRegistrationPanel(),"Register");
        panel.add(showChatPanel(),"chat");
        add(panel);
        cardLayout.show(panel,"Register");
        setVisible(true);
    }

    private JPanel showLogInPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Arial", BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(nameLabel, gbc);

        JTextField nameFiled = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(nameFiled, gbc);

        JLabel passwordLabel = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String userName = nameFiled.getText();
            String password = String.valueOf(passwordField.getPassword());

            IAM user = new IAM(userName, password);
            try {
                if (user.login(userName, password)) {
                    JOptionPane.showMessageDialog(GUI.this, "Login successful!");
                    cardLayout.show(panel, "chat");
                } else {
                    JOptionPane.showMessageDialog(GUI.this, "Login failed!");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return loginPanel;
    }

    private JPanel showRegistrationPanel() {
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Arial", BOLD, 18));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registrationPanel.add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        registrationPanel.add(nameLabel, gbc);

        JTextField nameFiled = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        registrationPanel.add(nameFiled, gbc);

        JLabel passwordLabel = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        registrationPanel.add(passwordField, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registrationPanel.add(registerButton, gbc);


        registerButton.addActionListener(e -> {
            String userName = nameFiled.getText();
            String password = String.valueOf(passwordField.getPassword());

            IAM user = new IAM(userName, password);
            if (userName.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(GUI.this, "Please fill in the fields carefully!");
            } else {
                try {
                    user.signUp();
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    cardLayout.show(panel, "Login");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return registrationPanel;
    }

    private JPanel showChatPanel(){
        chatBot = new OpenRouterChat(cli.apiUrl,cli.apiKey);
        JPanel chatPanel = new JPanel(new BorderLayout());
        JPanel logoutPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("AI Chat Assistant");
        label.setFont(new Font("Arial",BOLD,18));
        label.setBackground(Color.BLUE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(Color.BLUE);
        logoutButton.addActionListener(e -> {
            cardLayout.show(panel, "Login");
        });

        logoutPanel.add(label);
        logoutPanel.add(logoutButton);
        chatPanel.add(logoutPanel,BorderLayout.NORTH);

        JTextArea textArea = new JTextArea("Welcome to AI Chat Assistant!\n\n");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        chatPanel.add(textArea, BorderLayout.CENTER);
        //chatPanel.add(scrollPane,BorderLayout.CENTER);

        JPanel textPanel = new JPanel(new FlowLayout());
        JTextField messageField = new JTextField(70);
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Color.WHITE);
        sendButton.setForeground(Color.BLUE);
        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            try {
                if(!message.isEmpty()) {
                    textArea.append("You:"+ message + "\n" + "Thinking..." + "\n");
                }
                String request = chatBot.sendChatRequest(message);
                if(!request.isEmpty()){
                    textArea.append("DeepSeek:"+ request + "\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            messageField.setText("");
        });
        textPanel.add(messageField);
        textPanel.add(sendButton);

        chatPanel.add(textPanel, BorderLayout.SOUTH);

        return chatPanel;
        }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new GUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
