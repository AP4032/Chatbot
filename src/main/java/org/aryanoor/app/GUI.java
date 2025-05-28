//Aria razavi

package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class GUI extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout); // دسترسی به چندین پنل
    private OpenRouterChat chatBot;

    private final JTextArea chatArea = new JTextArea();
    private final JTextField inputField = new JTextField();

    public GUI() {
        setTitle("AI Chat Assistant");
        setSize(700, 600); // تنظیمات اولیه پنجره اصلی
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel loginPanel = createLoginPanel();
        JPanel signUpPanel = createSignUpPanel();
        JPanel chatPanel = createChatPanel();

        // اضافه کردن پنل ها به پنل اصلی
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(signUpPanel, "SIGNUP");
        mainPanel.add(chatPanel, "CHAT");

        // اضافه کردن پنل اصلی به فریم برنامه
        add(mainPanel);

        // بررسی وجود یوزر
        if (Files.exists(Paths.get("user.data"))) {
            cardLayout.show(mainPanel, "LOGIN");
        } else {
            cardLayout.show(mainPanel, "SIGNUP");
        }
    }
// ساخت پنل لاگین
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(200, 235, 235));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // تایتل
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        panel.add(title, gbc);


        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField userField = new JTextField(15);
        panel.add(userField, gbc);
        gbc.gridy = 2;
        JPasswordField passField = new JPasswordField(15);
        panel.add(passField, gbc);

        // دکمه ها
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 51, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);

        JButton goToSignUpButton = new JButton("Create an account");
        buttonPanel.add(loginButton);
        buttonPanel.add(goToSignUpButton);
        panel.add(buttonPanel, gbc);

        // رفتار ها
        loginButton.addActionListener(e -> handleLogin(userField.getText(), new String(passField.getPassword())));
        goToSignUpButton.addActionListener(e -> cardLayout.show(mainPanel, "SIGNUP"));
        passField.addActionListener(e -> loginButton.doClick());

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(200, 235, 235)); // یک رنگ خاکستری روشن
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Create Your Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        panel.add(title, gbc);


        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField userField = new JTextField(15);
        panel.add(userField, gbc);
        gbc.gridy = 2;
        JPasswordField passField = new JPasswordField(15);
        panel.add(passField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerButton = new JButton("Register");
        JButton goToLoginButton = new JButton("Already have an account?");
        buttonPanel.add(registerButton);
        buttonPanel.add(goToLoginButton);
        panel.add(buttonPanel, gbc);


        registerButton.addActionListener(e -> handleSignUp(userField.getText(), new String(passField.getPassword())));
        goToLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        passField.addActionListener(e -> registerButton.doClick());

        return panel;
    }


    private JPanel createChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(200, 235, 235)); // یک رنگ خاکستری روشن

        // هدر
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(26, 115, 232)); // Blue background
        header.setBorder(new EmptyBorder(8, 12, 8, 12)); // Padding

        JLabel appTitle = new JLabel("AI Chat Assistant");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");

        header.add(appTitle, BorderLayout.WEST);
        header.add(logoutButton, BorderLayout.EAST);


        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.add(header, BorderLayout.CENTER);
        headerContainer.add(new JSeparator(), BorderLayout.SOUTH); // Separator line

        panel.add(headerContainer, BorderLayout.NORTH);

        // ناحیه چت
        chatArea.setEditable(false);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // دریافت ورودی
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);


        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        return panel;
    }


    private void handleLogin(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            showError("Username and password cannot be empty.");
            return;
        }
        try {
            if (new IAM(username, password).login(username, password)) {
                initChatBot(); // Load config and prepare chatbot
                cardLayout.show(mainPanel, "CHAT"); // Switch to chat panel
            } else {
                showError("Invalid username or password.");
            }
        } catch (IOException e) {
            showError("Login Error: " + e.getMessage());
        }
    }

    private void handleSignUp(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            showError("Username and password cannot be empty.");
            return;
        }
        try {
            new IAM(username, password).signUp();
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "LOGIN"); // Switch to login panel
        } catch (IOException e) {
            showError("Signup Error: " + e.getMessage());
        }
    }

    private void initChatBot() {
        if (chatBot == null) {
            try {
                Properties properties = new Properties();
                properties.load(Files.newInputStream(Paths.get("config.properties")));
                this.chatBot = new OpenRouterChat(properties.getProperty("apiUrl"), properties.getProperty("apiKey"));
                // Add initial welcome message
                chatArea.setText("Welcome to AI Chat Assistant!\n\nType your questions below and I'll do my best to help.\n\n");
            } catch (IOException e) {
                showError("Could not load chatbot configuration: " + e.getMessage());
                cardLayout.show(mainPanel, "LOGIN");
            }
        }
    }

    private void sendMessage() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        chatArea.append("You: " + userText + "\n");
        inputField.setText("");
        inputField.setEnabled(false);

        new SwingWorker<String, Void>() {
            protected String doInBackground() throws Exception {
                return chatBot.sendChatRequest(userText);
            }
            protected void done() {
                try {
                    chatArea.append("Bot: " + get() + "\n\n");
                } catch (Exception e) {
                    chatArea.append("Bot: Error - " + e.getMessage() + "\n\n");
                } finally {
                    inputField.setEnabled(true);
                    inputField.requestFocus();
                }
            }
        }.execute();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}