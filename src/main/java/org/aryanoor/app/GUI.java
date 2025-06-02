package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class GUI extends JFrame {

    JPanel loginPanel, registerPanel, recoverPanel, chatPanel;

    public GUI() {
        setTitle("AI ChatBot Application");
        setSize(510, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // ========== Login Panel ==========
        loginPanel = new JPanel();
        loginPanel.setBounds(0, 0, 500, 400);
        loginPanel.setLayout(new GridLayout(7, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JTextField loginUsername = new JTextField();
        JPasswordField loginPassword = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton goToRegisterBtn = new JButton("Create Account");
        JButton goToRecoverBtn = new JButton("Forgot Password");

        loginBtn.setBackground(Color.BLUE);
        loginBtn.setForeground(Color.WHITE);

        loginBtn.addActionListener(e -> {
            IAM iam = new IAM(loginUsername.getText(), new String(loginPassword.getPassword()));
            try {
                if (iam.login(loginUsername.getText(), new String(loginPassword.getPassword()))) {
                    showOnly(chatPanel);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Login Failed",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        goToRegisterBtn.addActionListener(e -> showOnly(registerPanel));
        goToRecoverBtn.addActionListener(e -> showOnly(recoverPanel));

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(loginUsername);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(loginPassword);
        loginPanel.add(loginBtn);
        loginPanel.add(goToRegisterBtn);
        loginPanel.add(goToRecoverBtn);

        add(loginPanel);

        // ========== Register Panel ==========
        registerPanel = new JPanel();
        registerPanel.setBounds(0, 0, 500, 400);
        registerPanel.setLayout(new GridLayout(5, 1, 10, 10));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JTextField registerUsername = new JTextField();
        JPasswordField registerPassword = new JPasswordField();
        JButton registerBtn = new JButton("Register");
        JButton toLoginBtn = new JButton("Back to Login");


        registerBtn.setBackground(Color.GREEN);
        registerBtn.setForeground(Color.BLACK);

        registerBtn.addActionListener(e -> {
            IAM iam = new IAM(registerUsername.getText(), new String(registerPassword.getPassword()));
            try {
                if(registerUsername.getText().isEmpty() || Arrays.toString(registerPassword.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please fill all the fields",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    iam.signUp();
                    JOptionPane.showMessageDialog(this,
                            "Registered Successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    showOnly(loginPanel);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        toLoginBtn.addActionListener(e -> {showOnly(loginPanel);});

        registerPanel.add(new JLabel("Username:"));
        registerPanel.add(registerUsername);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(registerPassword);
        registerPanel.add(registerBtn);
        registerPanel.add(toLoginBtn);

        add(registerPanel);

        // ========== Recover Panel ==========
        recoverPanel = new JPanel();
        recoverPanel.setBounds(0, 0, 500, 400);
        recoverPanel.setLayout(new GridLayout(5, 1, 10, 10));
        recoverPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JTextField recoverUsername = new JTextField();
        JPasswordField newPasswordField = new JPasswordField();
        JButton recoverBtn = new JButton("Set Password");
        JButton backToLoginBtn = new JButton("Back to Login");

        recoverBtn.setBackground(Color.GREEN);

        recoverBtn.addActionListener(e -> {
            String username = recoverUsername.getText().trim();
            String newPassword = new String(newPasswordField.getPassword()).trim();
            Path userFile = Paths.get("user.data");

            if(username.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username or Password Required",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            if (!Files.exists(userFile)) {
                JOptionPane.showMessageDialog(this,
                        "No user data found. Please register first.",
                        null, JOptionPane.ERROR_MESSAGE);
                showOnly(registerPanel);
                return;
            }
            try {
                List<String> lines = Files.readAllLines(userFile);

                if (lines.isEmpty() || !lines.get(0).startsWith(username + ",")) {
                    JOptionPane.showMessageDialog(this,
                            "User not found. Please register first.",
                            null, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Now proceed to update the password
                IAM iam = new IAM(username, newPassword);
                String newData = username + "," + iam.hashPassword(newPassword);
                Files.write(userFile, newData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

                JOptionPane.showMessageDialog(this,
                        "Password updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                showOnly(loginPanel);

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error while accessing user data.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backToLoginBtn.addActionListener(e -> showOnly(loginPanel));

        recoverPanel.add(new JLabel("Username:"));
        recoverPanel.add(recoverUsername);
        recoverPanel.add(new JLabel("New Password:"));
        recoverPanel.add(newPasswordField);
        recoverPanel.add(recoverBtn);
        recoverPanel.add(backToLoginBtn);

        add(recoverPanel);

        // ========== Chat Panel ==========
        chatPanel = new JPanel();
        chatPanel.setBounds(0, 0, 500, 400);
        chatPanel.setLayout(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatArea);

        JTextField chatInput = new JTextField();
        JButton sendBtn = new JButton("Send");
        JButton logoutBtn = new JButton("Log Out");

        sendBtn.setBackground(Color.BLUE);
        sendBtn.setForeground(Color.WHITE);

        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);

        sendBtn.addActionListener(e -> {
            String msg = chatInput.getText().trim();
            if (!msg.isEmpty()) {
                chatArea.append("You: " + msg + "\n");
                chatInput.setText("");
                chatArea.append("Bot is typing...\n");

                new Thread(() -> {
                    try {
                        OpenRouterChat chatBot = new OpenRouterChat(
                        "https://openrouter.ai/api/v1/chat/completions",
                        "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");
                        String response = chatBot.sendChatRequest(msg);
                        SwingUtilities.invokeLater(() -> {
                            String text = chatArea.getText().replace("Bot is typing...\n", "");
                            chatArea.setText(text);
                            chatArea.append("Bot: " + response + "\n");
                        });
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> {
                            String text = chatArea.getText().replace("Bot is typing...\n", "");
                            chatArea.setText(text);
                            chatArea.append("Bot: Failed to send request. Check connection.\n");
                        });
                        ex.printStackTrace();
                    }
                }).start();
            }
        });

        logoutBtn.addActionListener(e -> {
            chatArea.setText("");
            showOnly(loginPanel);
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(chatInput, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);

        chatPanel.add(chatScroll, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        chatPanel.add(logoutBtn, BorderLayout.NORTH);

        add(chatPanel);

        // ==== Show Login Page First ====
        loginPanel.setVisible(true);
        registerPanel.setVisible(false);
        recoverPanel.setVisible(false);
        chatPanel.setVisible(false);

        setVisible(true);
    }

    private void showOnly(JPanel panelToShow) {
        loginPanel.setVisible(false);
        registerPanel.setVisible(false);
        recoverPanel.setVisible(false);
        chatPanel.setVisible(false);
        panelToShow.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI();
        });
    }
}
