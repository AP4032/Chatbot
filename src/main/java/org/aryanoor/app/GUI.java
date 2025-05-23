package org.aryanoor.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class GUI extends JFrame {
    private String apiUrl; // API URL for chatbot
    private String apiKey; // API key for authentication
    private OpenRouterChat chatBot;
    private static final String CONFIG_FILE = "config.properties"; // Configuration file path
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public GUI() throws IOException {
        loadConfig();
        setTitle("Hooshang the Professional");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(loginPanel(), "login");
        mainPanel.add(registerPanel(), "register");
        mainPanel.add(chatPanel(), "chat");

        add(mainPanel);
        if (Files.exists(Paths.get("user.data.json"))){
            cardLayout.show(mainPanel, "login");
        }
        else {
            JOptionPane.showMessageDialog(null, "no registered user found. Please register.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
            cardLayout.show(mainPanel, "register");
        }
        setVisible(true);
    }

    private JPanel loginPanel() {
        JTextField usernameField;
        JPasswordField passwordField;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Welcome to the Hooshang zone", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, gbc);

        // Username
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = usernameField.getText().trim();
                    String password = String.valueOf(passwordField.getPassword());
                    IAM user = new IAM(username, password);
                    if (user.login(username,password)) {
                        cardLayout.show(mainPanel, "chat");
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"User not found",
                                "Error",JOptionPane.ERROR_MESSAGE);
                        cardLayout.show(mainPanel, "login");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panel.add(loginButton, gbc);

        // Sign up button
        gbc.gridy = 4;
        JButton signUpButton = new JButton("Create New Account");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });
        panel.add(signUpButton, gbc);

        return panel;
    }

    private JPanel registerPanel() {
        JTextField newUsernameField;
        JPasswordField newPasswordField;
        JPasswordField confirmPasswordField;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Create New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("New Username:"), gbc);

        gbc.gridx = 1;
        newUsernameField = new JTextField(15);
        panel.add(newUsernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        newPasswordField = new JPasswordField(15);
        panel.add(newPasswordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        panel.add(confirmPasswordField, gbc);

        // Account button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton createButton = new JButton("Create Account");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = newUsernameField.getText().trim();
                    String password = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());

                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter both username and password",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        cardLayout.show(mainPanel, "register");
                    }

                    else if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(null, "Passwords do not match",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        cardLayout.show(mainPanel, "register");
                    }
                    else {
                        IAM newUser = new IAM(username, password);
                        newUser.signUp(username,password);
                        cardLayout.show(mainPanel, "login");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panel.add(createButton, gbc);

        JButton backButton = new JButton("Cancel");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });
        panel.add(backButton, gbc);
        return panel;
    }

    private JPanel chatPanel() {
        chatBot = new OpenRouterChat(apiUrl, apiKey);
        JPanel panelChat = new JPanel(new BorderLayout());
        JPanel topPage = new JPanel(new FlowLayout());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel,"login");
            }
        });
        topPage.add(logoutButton);

        panelChat.add(topPage,BorderLayout.NORTH);

        JTextArea chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setLineWrap(true);
        chatBox.setWrapStyleWord(true);
        panelChat.add(chatBox, BorderLayout.CENTER);

        JPanel bottomPage = new JPanel(new FlowLayout());
        JTextField messageField = new JTextField(40);
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Color.BLUE);
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText().trim();
                try {
                    sendMessage(message, chatBox);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                messageField.setText("");
            }
        });

        bottomPage.add(messageField);
        bottomPage.add(sendButton);
        panelChat.add(bottomPage, BorderLayout.SOUTH);
        return panelChat;
    }


    private void loadConfig() throws IOException {
        Properties properties = new Properties();
        if (Files.exists(Paths.get(CONFIG_FILE))) {
            List<String> lines = Files.readAllLines(Paths.get(CONFIG_FILE));
            for (String line : lines) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    if (parts[0].trim().equalsIgnoreCase("apiUrl")) {
                        apiUrl = parts[1].trim();
                    } else if (parts[0].trim().equalsIgnoreCase("apiKey")) {
                        apiKey = parts[1].trim();
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "config file not found",
                                        "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void sendMessage(String message, JTextArea chatBox) throws IOException {
        if (!message.isEmpty()) {
            chatBox.append("You: "+message+"\n");
        }
        String responce = chatBot.sendChatRequest(message);
        if (responce != null) {
            chatBox.append( "Hooshang: "+responce+"\n");
        }

    }
}
