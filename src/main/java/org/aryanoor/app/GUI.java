package org.aryanoor.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.aryanoor.services.OpenRouterChat;

public class GUI extends JFrame {
    private JPanel mainPanel, loginPanel, registerPanel, chatRoomPanel;
    private JLabel registerAccount, forgotPass;
    private CardLayout cardLayout;

    private static final String DATA_FILE = "user.data";
    private JTextField registerNameInput, registerPassInput;
    private JButton backToLogin;
    private JTextArea messagePanel;
    private JTextField userInput;
    private JButton sendButton;
    private JButton logoutButton;
    private JTextField nameInput, passInput;
    private JButton loginBtn;
    private JCheckBox rememberUser;

    private Color color_one = new Color(51, 52, 70);
    private Color color_two = new Color(127, 140, 170);
    private Color color_three = new Color(184, 207, 206);
    private Color color_four = new Color(234, 239, 239);

    private String apiUrl;
    private String apiKey;
    private static final String CONFIG_FILE = "config.properties";

    private OpenRouterChat chatBot;

    public GUI() throws IOException {
        setTitle("ChatBot");
        setSize(1000, 900);
        setBackground(color_four);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initLoginPanel();
        initRegisterPanel();
        initChatRoomPanel();

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(chatRoomPanel, "chat");

        this.loadConfig();

        add(mainPanel);
        showLoginPage();
        setVisible(true);
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
            JOptionPane.showMessageDialog(this, "Configuration file not found. Please create 'config.properties' with apiUrl and apiKey.", "Missing Config", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        chatBot = new OpenRouterChat(apiUrl, apiKey);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(color_one);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);

        loginPanel.add(title, gbc);

        forgotPass = new JLabel("Forgot your pass?");
        registerAccount = new JLabel("Create an account");

        JPanel inputsField = new JPanel();
        JLabel username = new JLabel("Username");
        JLabel password = new JLabel("Password");
        username.setFont(new Font("Arial", Font.BOLD, 18));
        password.setFont(new Font("Arial", Font.BOLD, 18));

        nameInput = new JTextField(15);
        passInput = new JTextField(15);

        nameInput.setPreferredSize(new Dimension(0, 60));
        passInput.setPreferredSize(new Dimension(0, 60));

        nameInput.setBorder(new EmptyBorder(0, 15, 0, 0));
        passInput.setBorder(new EmptyBorder(0, 15, 0, 0));

        loginBtn = new JButton("Login");

        rememberUser = new JCheckBox("Remember me for later");
        rememberUser.setFont(new Font("SansSerif", Font.BOLD, 17));

        nameInput.setFont(new Font("SansSerif", Font.BOLD, 20));
        passInput.setFont(new Font("SansSerif", Font.BOLD, 20));

        inputsField.setLayout(new BoxLayout(inputsField, BoxLayout.Y_AXIS));

        inputsField.add(username);
        inputsField.add(Box.createRigidArea(new Dimension(0, 25)));
        inputsField.add(nameInput);
        inputsField.add(Box.createRigidArea(new Dimension(0, 25)));
        inputsField.add(password);
        inputsField.add(Box.createRigidArea(new Dimension(0, 25)));
        inputsField.add(passInput);
        inputsField.add(Box.createRigidArea(new Dimension(0, 25)));
        inputsField.add(rememberUser);
        inputsField.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 25));
        loginBtn.setPreferredSize(new Dimension(500, 60));
        loginBtn.setBackground(color_one);
        loginBtn.setForeground(color_four);

        buttonWrapper.add(loginBtn);
        styleAsLink(forgotPass);
        styleAsLink(registerAccount);

        JPanel linkWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        linkWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        linkWrapper.add(forgotPass);
        linkWrapper.add(registerAccount);

        inputsField.add(Box.createRigidArea(new Dimension(0, 10)));
        inputsField.add(buttonWrapper);
        inputsField.add(linkWrapper);

        gbc.gridy = 1;
        loginPanel.add(inputsField, gbc);

        loginBtn.addActionListener(e -> {
            try {
                if (login(nameInput.getText(), passInput.getText())) {
                    showChatRoomPage();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error during login.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerAccount.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });
    }

    private void initRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());

        JLabel title = new JLabel("Create an Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        registerPanel.add(title, gbc);

        JPanel inputsField = new JPanel();
        inputsField.setLayout(new BoxLayout(inputsField, BoxLayout.Y_AXIS));

        JLabel username = new JLabel("Username");
        JLabel password = new JLabel("Password");
        username.setFont(new Font("Arial", Font.BOLD, 18));
        password.setFont(new Font("Arial", Font.BOLD, 18));

        registerNameInput = new JTextField(20);
        registerPassInput = new JTextField(20);

        registerNameInput.setFont(new Font("SansSerif", Font.BOLD, 20));
        registerPassInput.setFont(new Font("SansSerif", Font.BOLD, 20));
        registerNameInput.setPreferredSize(new Dimension(160, 60));
        registerPassInput.setPreferredSize(new Dimension(160, 60));

        registerNameInput.setBorder(new EmptyBorder(0, 15, 0, 0));
        registerPassInput.setBorder(new EmptyBorder(0, 15, 0, 0));

        inputsField.add(username);
        inputsField.add(Box.createRigidArea(new Dimension(0, 25)));
        inputsField.add(registerNameInput);
        inputsField.add(Box.createRigidArea(new Dimension(0, 25)));
        inputsField.add(password);
        inputsField.add(Box.createRigidArea(new Dimension(0, 25)));
        inputsField.add(registerPassInput);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            try {
                String name = registerNameInput.getText();
                String pass = registerPassInput.getText();
                String hashed = hashPassword(pass);
                Files.write(Paths.get(DATA_FILE), (name + "," + hashed).getBytes());
                JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                showLoginPage();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        inputsField.add(registerButton);

        gbc.gridy = 1;
        registerPanel.add(inputsField, gbc);

        backToLogin = new JButton("Back to Login");
        backToLogin.addActionListener(e -> showLoginPage());
        registerPanel.add(backToLogin);
    }

    private void initChatRoomPanel() {
        chatRoomPanel = new JPanel();
        chatRoomPanel.setLayout(new BorderLayout());

        messagePanel = new JTextArea();
        userInput = new JTextField();
        sendButton = new JButton("Send");
        logoutButton = new JButton("Logout");

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(color_one);
        headerPanel.setPreferredSize(new Dimension(1000, 40));

        JLabel headerTitle = new JLabel("AI Chat Assistant");
        headerTitle.setForeground(color_four);
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(color_four);
        logoutButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.addActionListener(e -> logOut());

        headerPanel.add(headerTitle, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        messagePanel.setFont(new Font("Arial", Font.PLAIN, 18));
        messagePanel.setForeground(color_one);
        messagePanel.setBackground(color_four);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messagePanel.setText("Welcome to chat bot application.\n");
        messagePanel.setEditable(false);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sendButton.setPreferredSize(new Dimension(80, 30));
        sendButton.setBackground(color_one);
        sendButton.setForeground(color_four);
        sendButton.setFont(new Font("Arial", Font.BOLD, 15));

        sendButton.addActionListener(e -> {
            String message = userInput.getText();
            if (message.trim().isEmpty()) return;
            messagePanel.append("You: " + message + "\n");
            userInput.setText("");
            String response = null;
            try {
                response = chatBot.sendChatRequest(message);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            messagePanel.append("Bot: " + response + "\n");
        });

        inputPanel.add(userInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatRoomPanel.add(headerPanel, BorderLayout.NORTH);
        chatRoomPanel.add(new JScrollPane(messagePanel), BorderLayout.CENTER);
        chatRoomPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    private void showLoginPage() {
        cardLayout.show(mainPanel, "login");
    }

    private void showChatRoomPage() {
        cardLayout.show(mainPanel, "chat");
    }

    private void styleAsLink(JLabel label) {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setForeground(color_one);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setText("<html>" + label.getText() + "</html>");
    }

    public boolean login(String name, String enteredPassword) throws IOException {
        if (!Files.exists(Paths.get(DATA_FILE))) return false;
        List<String> lines = Files.readAllLines(Paths.get(DATA_FILE));
        if (lines.isEmpty()) return false;
        String[] userData = lines.get(0).split(",");
        if (userData.length != 2) return false;
        String storedName = userData[0];
        String storedHashedPassword = userData[1];
        String enteredHashedPassword = hashPassword(enteredPassword);
        return storedHashedPassword.equals(enteredHashedPassword) && storedName.equals(name);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private void logOut() {
        showLoginPage();
    }

    public static void main(String[] args) throws IOException {
        new GUI();
    }
}