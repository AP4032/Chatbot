package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GUI extends JFrame {

    private CLI cli = new CLI();

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private String currentUser;


    private JTextArea chatArea;
    private JTextField chatInputField;
    private JScrollPane chatScrollPane;

    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color LIGHT_GRAY = new Color(245, 245, 245);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_GRAY = new Color(102, 102, 102);


    public GUI() throws IOException {
        initializeFrame();
        createPages();
        showRegisterPage();
    }

    private void initializeFrame() {
        setTitle("AI Chat Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }

    private void createPages() {
        mainPanel.add(createRegisterPage(), "REGISTER");
        mainPanel.add(createLoginPage(), "LOGIN");
        mainPanel.add(createChatPage(), "CHAT");
    }


    private JPanel createLoginPage() {
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(LIGHT_GRAY);
        loginPanel.setLayout(new BorderLayout());


        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_BLUE);
        titleLabel.setBorder(new EmptyBorder(70, 0, 40, 0));


        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(LIGHT_GRAY);
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(WHITE);
        formPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        formPanel.setPreferredSize(new Dimension(350, 320));


        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_GRAY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_GRAY);
        passwordLabel.setBorder(new EmptyBorder(20, 0, 5, 0));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);


        JButton loginButton = new JButton("Login");
        loginButton.setBackground(PRIMARY_BLUE);
        loginButton.setForeground(WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(new EmptyBorder(12, 0, 12, 0));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);


        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(LIGHT_GRAY);
        linkPanel.setBorder(new EmptyBorder(30, 0, 0, 0));


        JLabel createAccountLabel = new JLabel("Create an account");
        createAccountLabel.setForeground(PRIMARY_BLUE);
        createAccountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        createAccountLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(loginButton);

        linkPanel.add(createAccountLabel);

        centerPanel.add(formPanel);

        loginPanel.add(titleLabel, BorderLayout.NORTH);
        loginPanel.add(centerPanel, BorderLayout.CENTER);
        loginPanel.add(linkPanel, BorderLayout.SOUTH);


        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                IAM iam = new IAM(username, password);
                if (iam.login(username, password)) {
                    currentUser = username;
                    showChatPage();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error during login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        createAccountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showRegisterPage();
            }
        });


        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        return loginPanel;
    }


    private JPanel createRegisterPage() {
        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(LIGHT_GRAY);
        registerPanel.setLayout(new BorderLayout());


        JLabel titleLabel = new JLabel("Create Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_BLUE);
        titleLabel.setBorder(new EmptyBorder(70, 0, 40, 0));


        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(LIGHT_GRAY);
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));


        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(WHITE);
        formPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        formPanel.setPreferredSize(new Dimension(350, 300));


        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_GRAY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);


        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_GRAY);
        passwordLabel.setBorder(new EmptyBorder(20, 0, 5, 0));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);


        JButton registerButton = new JButton("Register");
        registerButton.setBackground(PRIMARY_BLUE);
        registerButton.setForeground(WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(new EmptyBorder(12, 0, 12, 0));
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);


        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(LIGHT_GRAY);
        linkPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        JLabel signInLabel = new JLabel("Already have an account? Sign in");
        signInLabel.setForeground(PRIMARY_BLUE);
        signInLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        signInLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(registerButton);

        linkPanel.add(signInLabel);
        centerPanel.add(formPanel);

        registerPanel.add(titleLabel, BorderLayout.NORTH);
        registerPanel.add(centerPanel, BorderLayout.CENTER);
        registerPanel.add(linkPanel, BorderLayout.SOUTH);


        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                IAM iam = new IAM(username, password);
                iam.signUp();
                JOptionPane.showMessageDialog(this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                showLoginPage();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error during registration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        signInLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showLoginPage();
            }
        });


        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerButton.doClick();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        return registerPanel;
    }

    private JPanel createChatPage() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(WHITE);


        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setPreferredSize(new Dimension(0, 50));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("AI Chat Assistant");
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(WHITE);
        logoutButton.setForeground(PRIMARY_BLUE);
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(new EmptyBorder(5, 15, 5, 15));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);


        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setBackground(WHITE);
        chatArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.setBorder(null);


        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                new EmptyBorder(15, 15, 15, 15)
        ));

        chatInputField = new JTextField();
        chatInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        chatInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));


        JButton sendButton = new JButton("Send");
        sendButton.setBackground(PRIMARY_BLUE);
        sendButton.setForeground(WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 12));
        sendButton.setFocusPainted(false);
        sendButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        sendButton.setPreferredSize(new Dimension(80, 40));

        inputPanel.add(chatInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.AFTER_LINE_ENDS);

        chatPanel.add(headerPanel, BorderLayout.NORTH);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);


        appendMessage("Welcome to AI Chat Assistant!\n\nType your questions below and I'll do my best to help.\n");


        logoutButton.addActionListener(e -> {
            currentUser = null;
            chatArea.setText("");
            appendMessage("Welcome to AI Chat Assistant!\n\nType your questions below and I'll do my best to help.\n");
            showLoginPage();
        });


        chatInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        return chatPanel;
    }


    private void appendMessage(String message) {
        chatArea.append(message);
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void sendMessage() {

        String message = chatInputField.getText().trim();
        if (message.equalsIgnoreCase("exit")) {
            appendMessage("Bot: Goodbye! Thanks for chatting.\n");
            System.exit(0);
            return;
        }

        if (!message.isEmpty()) {
            appendMessage("You:" + message + "\n" + "Thinking..." + "\n");
            chatInputField.setText("");
            chatInputField.setEnabled(false);
        }

        OpenRouterChat chatBot = new OpenRouterChat(cli.getApiUrl(), cli.getApiKey());
//            SwingUtilities.invokeLater(() -> {
        try {
            String response = chatBot.sendChatRequest(message);
            appendMessage("AI: " + response + "\n");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }





        chatInputField.setEnabled(true);


//        });

    }

    private void showRegisterPage() {
        cardLayout.show(mainPanel, "REGISTER");
    }

    private void showLoginPage() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void showChatPage() {
        cardLayout.show(mainPanel, "CHAT");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                new GUI().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}