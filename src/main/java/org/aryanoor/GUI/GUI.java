package org.aryanoor.GUI;

import javax.swing.*;

import org.aryanoor.app.CLI;
import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import java.awt.*;
import java.io.IOException;

public class GUI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private OpenRouterChat routerChat;

    // Colors
    private final Color PRIMARY_COLOR = new Color(56, 84, 199);
    private final Color SECONDARY_COLOR = new Color(230, 231, 235);
    private final Color TEXT_COLOR = new Color(51, 51, 51);

    public GUI(CLI cli) {
        routerChat = new OpenRouterChat(cli.getApiUrl(), cli.getApiKey());
        initializeFrame();
        createCards();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("ZHE(ژ) Chat Assistant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setBackground(SECONDARY_COLOR);
        frame.setResizable(false);
        ImageIcon icon = new ImageIcon("zhe.png");
        frame.setIconImage(icon.getImage());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        frame.add(cardPanel, BorderLayout.CENTER);
    }

    private void createCards() {
        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();
        JPanel chatPanel = createChatPanel();

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(registerPanel, "REGISTER");
        cardPanel.add(chatPanel, "CHAT");

        // show login panel on opening
        cardLayout.show(cardPanel, "LOGIN");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Header
        JLabel header = new JLabel("Welcome Back", SwingConstants.CENTER);
        styleHeader(header);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(header);
        panel.add(Box.createVerticalStrut(30));

        // Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(SECONDARY_COLOR);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 300));

        // Username
        JLabel usernameLabel = new JLabel("Username");
        styleFormLabel(usernameLabel);
        JTextField usernameField = createTextField();

        // Password
        JLabel passwordLabel = new JLabel("Password");
        styleFormLabel(passwordLabel);
        JPasswordField passwordField = createPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(10));

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(20));

        // Login button
        JButton loginBtn = createPrimaryButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                IAM user = new IAM(username, password);
                if (user.login(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful!", "Login", JOptionPane.PLAIN_MESSAGE);
                    cardLayout.show(cardPanel, "CHAT");
                } else {
                    JOptionPane.showMessageDialog(frame, "Wrong credentials");
                    usernameField.setText("");
                    passwordField.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Login error: " + ex.getMessage());
            }
        });
        panel.add(loginBtn);
        panel.add(Box.createVerticalStrut(15));

        // Links
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        linksPanel.setBackground(SECONDARY_COLOR);

        JLabel createAccount = createLinkLabel("Create an account");

        createAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(cardPanel, "REGISTER");
            }
        });

        linksPanel.add(createAccount);

        panel.add(linksPanel);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Header
        JLabel header = new JLabel("Create Your Account", SwingConstants.CENTER);
        styleHeader(header);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(header);
        panel.add(Box.createVerticalStrut(30));

        // Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(SECONDARY_COLOR);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 300));

        // Username
        JLabel usernameLabel = new JLabel("Username");
        styleFormLabel(usernameLabel);
        JTextField usernameField = createTextField();

        // Password
        JLabel passwordLabel = new JLabel("Password");
        styleFormLabel(passwordLabel);
        JPasswordField passwordField = createPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(20));

        // Register button
        JButton registerBtn = createPrimaryButton("Register");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                IAM user = new IAM(username, password);
                user.signUp();
                JOptionPane.showMessageDialog(frame, "Registration successful!");
                cardLayout.show(cardPanel, "LOGIN");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
        panel.add(registerBtn);
        panel.add(Box.createVerticalStrut(15));

        // Links
        JLabel signInLabel = createLinkLabel("Already have an account? Sign in");
        signInLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(cardPanel, "LOGIN");
            }
        });
        panel.add(signInLabel);

        return panel;
    }

    private JPanel createChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);

        // Header
        JLabel header = new JLabel("Zhe(ژ) Chat Assistant", SwingConstants.CENTER);
        styleHeader(header);
        panel.add(header, BorderLayout.NORTH);

        // Chat area
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        inputPanel.setBackground(SECONDARY_COLOR);

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton sendButton = createPrimaryButton("Send");
        sendButton.addActionListener(e -> handleUserInput(inputField, chatArea));

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleUserInput(JTextField inputField, JTextArea chatArea) {
        String question = inputField.getText().trim();
        if (question.isEmpty())
            return;

        // Add user message to chat
        chatArea.append("You: " + question + "\n\n");
        inputField.setText("");

        chatArea.append("AI is thinking...\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());

        // Process in background thread to keep UI responsive
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                try {
                    return routerChat.sendChatRequest(question);
                } catch (IOException ex) {
                    return "Error: " + ex.getMessage();
                }
            }

            @Override
            protected void done() {
                try {
                    String currentText = chatArea.getText();
                    chatArea.setText(currentText.substring(0, currentText.lastIndexOf("AI is thinking...\n")));

                    String response = get(); // Get the result from doInBackground
                    chatArea.append("ZHE: " + response + "\n\n");
                } catch (Exception ex) {
                    chatArea.append("ZHE: Error processing response\n\n");
                }
                chatArea.setCaretPosition(chatArea.getDocument().getLength());
            }
        }.execute();
    }

    // >---------------<styles>---------------<
    private void styleHeader(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(PRIMARY_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    }

    private void styleFormLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private JLabel createLinkLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(PRIMARY_COLOR);
        return label;
    }
}