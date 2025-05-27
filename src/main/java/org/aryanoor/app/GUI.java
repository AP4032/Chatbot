package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI extends JFrame {
    private JPanel currentPanel;
    private IAM iamUser;
    private OpenRouterChat chatBot;
    private String apiUrl;
    private String apiKey;
    private static final String CONFIG_FILE = "config.properties";

    public GUI() {
        setTitle("Chatbot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        currentPanel = new JPanel();
        this.add(currentPanel, BorderLayout.CENTER);

        try {
            loadConfig();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Configuration file missing or invalid.\nPlease create 'config.properties' with apiUrl and apiKey.",
                    "Config Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        showSignInWindow();
        setVisible(true);
    }

    private void loadConfig() throws IOException {
        java.util.Properties properties = new java.util.Properties();
        java.nio.file.Path configPath = java.nio.file.Paths.get(CONFIG_FILE);
        if (!java.nio.file.Files.exists(configPath)) {
            throw new IOException("config.properties not found.");
        }

        java.util.List<String> lines = java.nio.file.Files.readAllLines(configPath);
        for (String line : lines) {
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                String key = parts[0].trim().toLowerCase();
                String value = parts[1].trim();
                if (key.equals("apiurl")) {
                    apiUrl = value;
                } else if (key.equals("apikey")) {
                    apiKey = value;
                }
            }
        }

        if (apiUrl == null || apiKey == null) {
            throw new IOException("apiUrl or apiKey missing in config.properties.");
        }
    }

    private void showLogInWindow() {
        currentPanel.removeAll();
        currentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
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

        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
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
            String usernameInput = usernameField.getText().trim();
            String passwordInput = new String(passwordField.getPassword());

            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                IAM user = new IAM(usernameInput, passwordInput);
                boolean loginSuccess = user.login(usernameInput, passwordInput);
                if (!loginSuccess) {
                    JOptionPane.showMessageDialog(this,
                            "Username or password is incorrect.\nIf you don't have an account, please sign up first.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                iamUser = user;
                chatBot = new OpenRouterChat(apiUrl, apiKey);
                ShowChatBox();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error accessing user data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signupButton.addActionListener(e -> showSignInWindow());

        revalidate();
        repaint();
    }

    public void ShowChatBox() {
        currentPanel.removeAll();
        currentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.BLUE);
        titlePanel.setPreferredSize(new Dimension(this.getWidth(), 40));

        JLabel titleLabel = new JLabel("AI Chat Assistant");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.BLUE);
        logoutButton.setBackground(Color.WHITE);

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(logoutButton, BorderLayout.EAST);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - 100));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setPreferredSize(new Dimension(this.getWidth(), 40));
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(Color.BLUE);
        textPanel.add(textField, BorderLayout.CENTER);
        textPanel.add(sendButton, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(chatScroll, BorderLayout.CENTER);
        panel.add(textPanel, BorderLayout.SOUTH);

        currentPanel.add(panel, BorderLayout.CENTER);

        sendButton.addActionListener(e -> {
            String question = textField.getText().trim();
            if (question.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a question.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            chatArea.append("You: " + question + "\n");
            textField.setText("");
            textField.setEnabled(false);
            sendButton.setEnabled(false);

            new Thread(() -> {
                try {
                    String response = chatBot.sendChatRequest(question);
                    SwingUtilities.invokeLater(() -> {
                        chatArea.append("AI: " + response + "\n\n");
                        textField.setEnabled(true);
                        sendButton.setEnabled(true);
                        textField.requestFocus();
                    });
                } catch (IOException ex) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Error communicating with AI service:\n" + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        textField.setEnabled(true);
                        sendButton.setEnabled(true);
                    });
                }
            }).start();
        });

        logoutButton.addActionListener(e -> {
            iamUser = null;
            chatBot = null;
            showLogInWindow();
        });

        revalidate();
        repaint();
    }

    private void showSignInWindow() {
        currentPanel.removeAll();
        currentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Create Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(Color.BLUE);
        panel.add(registerButton);

        JPanel loginPanel = new JPanel();
        JLabel loginLabel = new JLabel("Already have an account?");
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
            String usernameInput = usernameField.getText().trim();
            String passwordInput = new String(passwordField.getPassword());

            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                IAM user = new IAM(usernameInput, passwordInput);
                user.signUp();
                JOptionPane.showMessageDialog(this, "Registration successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                showLogInWindow();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving user data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginButton.addActionListener(e -> showLogInWindow());

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
