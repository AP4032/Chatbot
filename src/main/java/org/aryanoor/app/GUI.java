package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GUI extends JFrame {
    private String apiUrl;
    private String apiKey;
    private static final String CONFIG_FILE = "config.properties";

    private JPanel currentPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private OpenRouterChat chatBot;
    private final Color BUTTON_BLUE = new Color(0, 122, 255); // رنگ آبی دقیق مانند تصویر

    public GUI() {
        try {
            loadConfig();
            initUI();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading configuration: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void loadConfig() throws IOException {
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
            JOptionPane.showMessageDialog(this,
                    "Configuration file not found. Please create 'config.properties' with apiUrl and apiKey.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initUI() {
        setTitle("Chatbot");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create all panels
        createLoginPanel();
        createRegistrationPanel();
        createSuccessPanel();
        createChatPanel();

        // Add panels to main panel
        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createRegistrationPanel(), "register");
        mainPanel.add(createSuccessPanel(), "success");
        mainPanel.add(createChatPanel(), "chat");

        add(mainPanel);

        // Show appropriate initial panel
        if (!Files.exists(Paths.get("user.data"))) {
            cardLayout.show(mainPanel, "register");
        } else {
            cardLayout.show(mainPanel, "login");
        }

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title
        JLabel titleLabel = new JLabel("Welcome Back", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, gbc);

        // Username
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Username"), gbc);

        gbc.gridx++;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password"), gbc);

        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Remember me checkbox
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JCheckBox rememberMe = new JCheckBox("Remember me");
        panel.add(rememberMe, gbc);

        // Login button
        gbc.gridy++;
        JButton loginButton = createStyledButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            IAM user = new IAM(username, password);
            try {
                if (user.login(username, password)) {
                    cardLayout.show(mainPanel, "chat");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        panel.add(loginButton, gbc);

        // Links
        gbc.gridy++;
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JLabel forgotPassword = new JLabel("<html><u>Forgot password?</u></html>");
        forgotPassword.setForeground(BUTTON_BLUE);
        forgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(GUI.this,
                        "Please contact support to reset your password",
                        "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel createAccount = new JLabel("<html><u>Create an account</u></html>");
        createAccount.setForeground(BUTTON_BLUE);
        createAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "register");
            }
        });

        linkPanel.add(forgotPassword);
        linkPanel.add(createAccount);
        panel.add(linkPanel, gbc);

        return panel;
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title
        JLabel titleLabel = new JLabel("Create Your Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, gbc);

        // Username
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Username"), gbc);

        gbc.gridx++;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password"), gbc);

        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Register button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton registerButton = createStyledButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            IAM newUser = new IAM(username, password);
            try {
                newUser.signUp();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            cardLayout.show(mainPanel, "success");
        });
        panel.add(registerButton, gbc);

        // Link
        gbc.gridy++;
        JLabel loginLink = new JLabel("<html>Already have an account? <u>Sign in</u></html>", JLabel.CENTER);
        loginLink.setForeground(BUTTON_BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "login");
            }
        });
        panel.add(loginLink, gbc);

        return panel;
    }

    private JPanel createSuccessPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Title
        JLabel titleLabel = new JLabel("Create Your Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, gbc);

        // Success message
        gbc.gridy++;
        JLabel successLabel = new JLabel("Success", JLabel.CENTER);
        successLabel.setForeground(Color.GREEN);
        successLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(successLabel, gbc);

        gbc.gridy++;
        JLabel messageLabel = new JLabel("Registration successful!", JLabel.CENTER);
        panel.add(messageLabel, gbc);

        // Continue button
        gbc.gridy++;
        JButton continueButton = createStyledButton("Continue");
        continueButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "login");
        });
        panel.add(continueButton, gbc);

        // Link
        gbc.gridy++;
        JLabel loginLink = new JLabel("<html>Already have an account? <u>Sign in</u></html>", JLabel.CENTER);
        loginLink.setForeground(BUTTON_BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "login");
            }
        });
        panel.add(loginLink, gbc);

        return panel;
    }

    private JPanel createChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Chat area
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField inputField = new JTextField();
        JButton sendButton = createStyledButton("Send");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        // Chat bot initialization
        chatBot = new OpenRouterChat(apiUrl, apiKey);
        chatArea.append("Welcome to the chatbot! Type your questions below.\n");

        // Send message action
        ActionListener sendAction = e -> {
            String question = inputField.getText().trim();
            inputField.setText("");

            if (question.isEmpty()) {
                return;
            }

            chatArea.append("You: " + question + "\n");
            chatArea.append("Chatbot is thinking...\n");

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return chatBot.sendChatRequest(question);
                }

                @Override
                protected void done() {
                    try {
                        String response = get();
                        chatArea.append("Chatbot: " + response + "\n");
                    } catch (Exception ex) {
                        chatArea.append("Error: " + ex.getMessage() + "\n");
                    }
                }
            }.execute();
        };

        inputField.addActionListener(sendAction);
        sendButton.addActionListener(sendAction);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GUI();
        });
    }
}