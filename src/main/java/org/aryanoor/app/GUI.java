package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GUI {
    private JFrame frame;
    private JPanel currentPanel;
    private CLI cli;
    private OpenRouterChat chatBot;
    private Color primaryColor = new Color(0, 120, 215);
    private Color secondaryColor = new Color(240, 240, 240);
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public GUI() {
        try {
            initialize();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to initialize application: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initialize() throws IOException {
        cli = new CLI();
        chatBot = new OpenRouterChat(cli.getApiUrl(), cli.getApiKey());
        mainFrame();
         if (!Files.exists(Paths.get("user.data"))) {
             showRegisterPanel();
        } else {
             loginPanel();
        }
    }

    private void mainFrame() {
        frame = new JFrame("Chatbot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        currentPanel = new JPanel(new BorderLayout());
        frame.add(currentPanel);
    }

    private void loginPanel() {
      currentPanel.removeAll();
      currentPanel.setLayout(new GridBagLayout());
      currentPanel.setOpaque(true);
      currentPanel.setBackground(secondaryColor);
      currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.fill = GridBagConstraints.HORIZONTAL;
      JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
      titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
      titleLabel.setForeground(primaryColor);
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 2;
      currentPanel.add(titleLabel, gbc);
      JLabel userLabel = new JLabel("Username:");
      userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      gbc.gridy = 1;
      gbc.gridwidth = 1;
      currentPanel.add(userLabel, gbc);
      usernameField = new JTextField(20);
      usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      gbc.gridy = 2;
      currentPanel.add(usernameField, gbc);
      JLabel passLabel = new JLabel("Password:");
      passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      gbc.gridy = 3;
      currentPanel.add(passLabel, gbc);
      passwordField = new JPasswordField(20);
      passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
      gbc.gridy = 4;
      currentPanel.add(passwordField, gbc);
      JButton loginButton = createStyledButton("Login", primaryColor, Color.WHITE);
      gbc.gridy = 5;
      currentPanel.add(loginButton, gbc);
      JLabel registerLabel = new JLabel("Don't have an account? Register here");
      registerLabel.setForeground(primaryColor);
      registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      registerLabel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              showRegisterPanel();
          }
      });
      gbc.gridy = 6;
      currentPanel.add(registerLabel, gbc);
      loginButton.addActionListener(e -> {
          try {
              performLogin();
          } catch (IOException ex) {
              ex.printStackTrace();
          }
      });
      usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
      passwordField.addActionListener(e -> {
          try {
              performLogin();
          } catch (IOException ex) {
              ex.printStackTrace();
          }
      });
      frame.revalidate();
      frame.repaint();
      frame.setVisible(true);
    }

    private void showRegisterPanel() {
        currentPanel.removeAll();
        currentPanel.setLayout(new GridBagLayout());
        currentPanel.setOpaque(true);
        currentPanel.setBackground(secondaryColor);
        currentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(primaryColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        currentPanel.add(titleLabel, gbc);
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        currentPanel.add(userLabel, gbc);
        JTextField regUserField = new JTextField(20);
        regUserField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 2;
        currentPanel.add(regUserField, gbc);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 3;
        currentPanel.add(passLabel, gbc);
        JPasswordField regPassField = new JPasswordField(20);
        regPassField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4;
        currentPanel.add(regPassField, gbc);
        JButton registerButton = createStyledButton("Register", primaryColor, Color.WHITE);
        gbc.gridy = 5;
        currentPanel.add(registerButton, gbc);
        JLabel backLabel = new JLabel("Already have an account? Login here");
        backLabel.setForeground(primaryColor);
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginPanel();
            }
        });
        gbc.gridy = 6;
        currentPanel.add(backLabel, gbc);
        registerButton.addActionListener(e -> {
            String username = regUserField.getText();
            String password = new String(regPassField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and password can't be empty",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            IAM newUser = new IAM(username, password);
            try {
                newUser.signUp();
                JOptionPane.showMessageDialog(frame, "Registration successful!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                loginPanel();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Registration failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        regUserField.addActionListener(e -> regPassField.requestFocusInWindow());
        regPassField.addActionListener(e -> registerButton.doClick());

        frame.revalidate();
        frame.repaint();
    }
    private void showChatPanel() {
        currentPanel.removeAll();
        currentPanel.setLayout(new BorderLayout());
        currentPanel.setBackground(Color.WHITE);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel titleLabel = new JLabel("ChatBot", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(primaryColor);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                loginPanel();
            }
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        currentPanel.add(headerPanel, BorderLayout.NORTH);
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setMargin(new Insets(10, 15, 10, 15));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        currentPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(secondaryColor);
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryColor, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        sendButton = createStyledButton("Send", primaryColor, Color.WHITE);
        sendButton.setPreferredSize(new Dimension(100, 40));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        currentPanel.add(inputPanel, BorderLayout.SOUTH);
        chatArea.append("ChatBot\n\n");
        chatArea.append("Welcome! You can start chatting now.\n");
        chatArea.append("Type 'exit' to quit the conversation.\n\n");
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        frame.revalidate();
        frame.repaint();
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void performLogin() throws IOException {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        IAM user = new IAM(username, password);
        if (user.login(username, password)) {
            showChatPanel();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password",
                    "Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (message.isEmpty()) return;
        sendButton.setEnabled(false);
        inputField.setEnabled(false);
        chatArea.append("You: " + message + "\n");
        inputField.setText("");
        if (message.equalsIgnoreCase("exit")) {
            frame.dispose();
            return;
        }
        chatArea.append("Bot: Thinking...\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return chatBot.sendChatRequest(message);
            }
            @Override
            protected void done() {
                try {
                    String text = chatArea.getText();
                    chatArea.setText(text.replace("Bot: Thinking...\n", ""));
                    String response = get();
                    chatArea.append("Bot: " + response + "\n\n");
                } catch (Exception e) {
                    String text = chatArea.getText();
                    chatArea.setText(text.replace("Bot: Thinking...\n", ""));
                    chatArea.append("Error: " + e.getMessage() + "\n\n");
                } finally {
                    sendButton.setEnabled(true);
                    inputField.setEnabled(true);
                    inputField.requestFocus();
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI();
        });
    }
}