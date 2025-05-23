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

public class GUI extends JFrame {
    private static final String CONFIG_FILE = "config.properties";
    private OpenRouterChat chatBot;
    private String apiKey;
    private String apiUrl;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public GUI() throws IOException {
        loadConfig();
        setTitle("Chatbot");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(buildLoginPanel(), "login");
        mainPanel.add(buildRegisterPanel(), "register");
        mainPanel.add(buildChatPanel(), "chat");

        add(mainPanel);
        cardLayout.show(mainPanel, "login"); // start with login
        setVisible(true);
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton goToRegisterButton = new JButton("Register");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
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

    private JPanel buildChatPanel() {
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

//        JPanel chatPanel = new JPanel();

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panelChat.add(chatArea, BorderLayout.CENTER);

        JPanel bottomPage = new JPanel(new FlowLayout());
        JTextField messageField = new JTextField(50);
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Color.BLUE);
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            try {
                sendMessage(message,chatArea);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            messageField.setText("");
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
            chatArea.append("You: "+message+"\n");
        }
        String responce = chatBot.sendChatRequest(message);
        if (responce != null) {
            chatArea.append( "Bot : "+responce+"/n");
        }

    }
}
