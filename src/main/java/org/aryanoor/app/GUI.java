package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class GUI extends JFrame {
    Boolean responcebot = false;
    private static final String CONFIG_FILE = "config.properties";
    private OpenRouterChat chatBot;
    private String apiKey;
    private String apiUrl;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public GUI() throws IOException {
        loadConfig();
        setTitle("Chatbot");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.gray);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(buildLoginPanel(), "login");
        mainPanel.add(buildRegisterPanel(), "register");
        mainPanel.add(buildChatPanel(), "chat");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    private JPanel buildLoginPanel() {
        JLabel titleLabel = new JLabel("Welcome to Chatbot", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI",Font.BOLD,22));
        titleLabel.setForeground(new Color(33,150,243));
//        titleLabel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
//        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));



        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(SwingConstants.CENTER);
        usernameField.setMaximumSize(new Dimension(400,50));
//        usernameField.setBounds(10, 10, 10, 10);

        JLabel passwordLabel = new JLabel("Password:");


        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(400,50));

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.white);


        JButton goToRegisterButton = new JButton("Register");
        goToRegisterButton.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        goToRegisterButton.setForeground(new Color(66, 133, 244));




        panel.add(usernameLabel);
        panel.add(usernameField);
//        panel.add(Box.createHorizontalStrut(10));
        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));
//        panel.add(Box.createHorizontalStrut(10));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
//        panel.add(Box.createHorizontalStrut(10));
        panel.add(goToRegisterButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            IAM user = new IAM(username, password);
            try {
                if (user.login(username, password)) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    cardLayout.show(mainPanel, "chat");
                } else {
                    JOptionPane.showMessageDialog(this, "Login failed!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during login.");
            }
        });

        goToRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        return panel;
    }

    private JPanel buildRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(submitButton);
        panel.add(cancelButton);

        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            } else {
                IAM newUser = new IAM(username, password);
                try {
                    newUser.signUp();
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    cardLayout.show(mainPanel, "login");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error during registration.");
                }
            }
        });

        cancelButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        return panel;
    }

    private JPanel buildChatPanel() throws IOException {
         chatBot = new OpenRouterChat(apiUrl, apiKey);


        JPanel panelChat = new JPanel(new BorderLayout());

        JPanel topPage = new JPanel(new FlowLayout());
        topPage.setBackground(Color.GRAY);
        JLabel titleLabel = new JLabel("AI Chat Assistant");
        topPage.add(titleLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(Color.GRAY);
        logoutButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "login");
        });
        topPage.add(logoutButton);

        panelChat.add(topPage, BorderLayout.NORTH);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panelChat.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPage = new JPanel(new FlowLayout());
        JTextField messageField = new JTextField(30);
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Color.BLUE);
        sendButton.setForeground(Color.WHITE);


            sendButton.addActionListener(e -> {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    chatArea.append("You: " + message + "\n");
                    messageField.setText("");
                    try {
                        String response = chatBot.sendChatRequest(message);
                        chatArea.append("Bot: " + response + "\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        chatArea.append("Error communicating with chatbot.\n");
                    }
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
            System.out.println("Configuration file not found. Please create 'config.properties' with apiUrl and apiKey.");
            System.exit(1);
        }
    }
    private void sendMessage(String message, JTextArea chatArea) throws IOException {
        if (!message.isEmpty()) {
            chatArea.append("You: "+message+"\n"+"Thinking...\n");
        }
    }
}
