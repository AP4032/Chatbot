package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GUI extends JFrame {
    private static final String CONFIG_FILE = "config.properties";
    private OpenRouterChat chatBot;
    private String apiKey, apiUrl;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    public GUI() throws IOException {
        loadConfiguration();
        initializeWindow();
        initializePanels();
        setVisible(true);
    }

    private void initializeWindow() {
        setTitle("AI Chatbot");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(mainPanel);
    }

    private void initializePanels() {
        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createRegisterPanel(), "register");
        mainPanel.add(createChatPanel(), "chat");
        cardLayout.show(mainPanel, "login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = createFormPanel(4, 2);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> handleLogin(usernameField, passwordField));
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        panel.add(styledLabel("Username:"));
        panel.add(usernameField);
        panel.add(styledLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = createFormPanel(4, 2);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        submitButton.addActionListener(e -> handleRegister(usernameField, passwordField));
        cancelButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        panel.add(styledLabel("Username:"));
        panel.add(usernameField);
        panel.add(styledLabel("Password:"));
        panel.add(passwordField);
        panel.add(submitButton);
        panel.add(cancelButton);

        return panel;
    }

    private JPanel createChatPanel() {
        chatBot = new OpenRouterChat(apiUrl, apiKey);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.black);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.black);
        JLabel title = styledLabel("AI Chat Assistant");
        header.add(title);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        header.add(logout);
        panel.add(header, BorderLayout.NORTH);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(chatArea);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout());
        JTextField messageField = new JTextField(45);
        JButton send = new JButton("Send");

        send.addActionListener(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                chatArea.append("You: " + message + "\n");
                try {
                    String response = chatBot.sendChatRequest(message);
                    chatArea.append("Bot: " + (response != null ? response : "No response") + "\n");
                } catch (IOException ex) {
                    chatArea.append("Bot: Error while sending message\n");
                }
                messageField.setText("");
            }
        });

        footer.add(messageField);
        footer.add(send);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private void handleLogin(JTextField userField, JPasswordField passField) {
        String username = userField.getText().trim();
        String password = String.valueOf(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        try {
            IAM user = new IAM(username, password);
            if (user.login(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                cardLayout.show(mainPanel, "chat");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Login error: " + e.getMessage());
        }
    }

    private void handleRegister(JTextField userField, JPasswordField passField) {
        String username = userField.getText().trim();
        String password = String.valueOf(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        try {
            IAM newUser = new IAM(username, password);
            newUser.signUp();
            JOptionPane.showMessageDialog(this, "Registration successful!");
            cardLayout.show(mainPanel, "login");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Registration error: " + e.getMessage());
        }
    }

    private JPanel createFormPanel(int rows, int cols) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(Color.black);
        return panel;
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.white);
        return label;
    }

    private void loadConfiguration() throws IOException {
        if (!Files.exists(Paths.get(CONFIG_FILE))) {
            System.err.println("Missing config.properties file.");
            System.exit(1);
        }

        ArrayList<String> lines =new ArrayList<>(Files.readAllLines(Paths.get(CONFIG_FILE)));
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
    }
}