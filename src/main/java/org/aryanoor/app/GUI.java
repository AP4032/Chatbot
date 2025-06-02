package org.aryanoor.app;
import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class GUI {
    private JFrame frame;
    private JPanel currentPanel;
    private OpenRouterChat chatBot;

    private Color primaryColor = new Color(0, 120, 215);
    private Color secondaryColor = new Color(240, 240, 240);
    private Color textColor = new Color(50, 50, 50);
    //login panel
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    // chat panel
    private JTextArea chatHistoryArea;
    private JTextField messageInputField;
    private JButton sendButton;
    private JButton logoutButton;
    // Configuration properties
    private String apiUrl;
    private String apiKey;
    private static final String CONFIG_FILE = "config.properties";

    public GUI() {
        frame = new JFrame("AI Chat Assistant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        try {
            loadConfig();
            chatBot = new OpenRouterChat(apiUrl, apiKey);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading configuration: " + e.getMessage()
                    + "\nPlease ensure 'config.properties' exists with 'apiUrl' and 'apiKey'.", "Configuration Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        showLoginPanel();
        frame.setVisible(true);
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
            if (apiUrl == null || apiKey == null || apiUrl.isEmpty() || apiKey.isEmpty()) {
                throw new IOException("Missing 'apiUrl' or 'apiKey' in 'config.properties'.");
            }
        } else {
            throw new IOException("Configuration file '" + CONFIG_FILE + "' not found.");
        }
    }
    private void showRegistrationPanel(){
        if(currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(secondaryColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel cyaLabel = new JLabel("Create Your Account");
        cyaLabel.setFont(new Font("Arial",Font.BOLD,24));
        cyaLabel.setForeground(primaryColor);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=2;
        registerPanel.add(cyaLabel,gbc);
        // Username label and field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial",Font.PLAIN,14));
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=1;
        registerPanel.add(usernameLabel,gbc);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial",Font.PLAIN,12));
        gbc.gridx=1;
        gbc.gridy=1;
        registerPanel.add(usernameField,gbc);
        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        registerPanel.add(passwordField, gbc);


        //Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(primaryColor);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.ipady = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(registerButton, gbc);


        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        linkPanel.setBackground(secondaryColor);

        JButton signupButton = new JButton("Sign in");
        signupButton.setFont(new Font("Arial", Font.PLAIN, 12));
        signupButton.setForeground(primaryColor);
        signupButton.setContentAreaFilled(false);
        signupButton.setBorderPainted(false);
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkPanel.add(signupButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.ipady = 0;
        registerPanel.add(linkPanel, gbc);

        //action listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                IAM user = new IAM(username,password);
                try {
                    user.signUp();
                    showLoginPanel();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               showLoginPanel();
            }
        });

        frame.add(registerPanel);
        currentPanel = registerPanel;
        frame.revalidate();
        frame.repaint();
    }


    private void showLoginPanel() {
        if (currentPanel != null) {
            frame.remove(currentPanel);
        }

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(secondaryColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Welcome Back
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(primaryColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(welcomeLabel, gbc);

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);

        //Password Label and Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(passwordField, gbc);


        //Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.ipady = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);


        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        linkPanel.setBackground(secondaryColor);

        createAccountButton = new JButton("Create an account");
        createAccountButton.setFont(new Font("Arial", Font.PLAIN, 12));
        createAccountButton.setForeground(primaryColor);
        createAccountButton.setContentAreaFilled(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkPanel.add(createAccountButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.ipady = 0;
        loginPanel.add(linkPanel, gbc);

        //action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                IAM user = new IAM(username, password);
                try {
                    user.login(username, password);
                    showChatPanel();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegistrationPanel();
            }
        });

        frame.add(loginPanel);
        currentPanel = loginPanel;
        frame.revalidate();
        frame.repaint();
    }


    private void showChatPanel() {

        if (currentPanel != null) {
            frame.remove(currentPanel);
        }

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(secondaryColor);

        //Title and Logout Button
        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setBackground(primaryColor);
        topBarPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("AI Chat Assistant");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        topBarPanel.add(titleLabel, BorderLayout.WEST);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(primaryColor);
        logoutButton.setFocusPainted(false);
        topBarPanel.add(logoutButton, BorderLayout.EAST);
        chatPanel.add(topBarPanel, BorderLayout.NORTH);

        //Chat History Area
        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        chatHistoryArea.setBackground(Color.WHITE);
        chatHistoryArea.setForeground(textColor);
        chatHistoryArea.setLineWrap(true);
        chatHistoryArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatHistoryArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chatPanel.add(scrollPane, BorderLayout.CENTER);


        chatHistoryArea.append("Welcome to AI Chat Assistant!\n");
        chatHistoryArea.append("Type your questions below and I'll do my best to help.\n");
        chatHistoryArea.append("You can type 'exit' to end the conversation.\n\n");
        chatHistoryArea.append("Bot: Hello! How can I assist you today?\n");

        //Text Field and Send Button
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        inputPanel.setBackground(secondaryColor);

        messageInputField = new JTextField();
        messageInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        inputPanel.add(messageInputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(primaryColor);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        inputPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        //Action Listeners
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });


        messageInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    showLoginPanel();
                }
            }
        });

        frame.add(chatPanel);
        currentPanel = chatPanel;
        frame.revalidate();
        frame.repaint();
        messageInputField.requestFocusInWindow();
    }


    private void sendMessage() {
        String userMessage = messageInputField.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }

        chatHistoryArea.append("You: " + userMessage + "\n");
        messageInputField.setText("");

        if (userMessage.equalsIgnoreCase("exit")) {
            chatHistoryArea.append("Bot: Goodbye!\n");
            sendButton.setEnabled(false);
            messageInputField.setEditable(false);
            return;
        }

        chatHistoryArea.append("Bot: Thinking...\n");

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return chatBot.sendChatRequest(userMessage);
            }

            @Override
            protected void done() {

                try {
                    String botResponse = get();
                    String currentText = chatHistoryArea.getText();
                    if (currentText.endsWith("Bot: Thinking...\n")) {
                        chatHistoryArea.replaceRange("", currentText.length() - "Bot: Thinking...\n".length(), currentText.length());
                    }

                    chatHistoryArea.append("Bot: " + botResponse + "\n\n");
                } catch (InterruptedException | ExecutionException ex) {

                    Throwable cause = ex.getCause();
                    chatHistoryArea.append("Bot: An error occurred: " + (cause != null ? cause.getMessage() : ex.getMessage()) + "\n\n");
                    ex.printStackTrace();
                } finally {
                    chatHistoryArea.setCaretPosition(chatHistoryArea.getDocument().getLength());
                }
            }
        }.execute();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}
