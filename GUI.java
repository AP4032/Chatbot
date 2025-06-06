package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JFrame implements ActionListener {

    private OpenRouterChat router;
    private JPanel mainPanel = new JPanel();
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel chatPanel;
    private CLI cli;
    private JLabel label;
    private JTextField loginUsername;
    private JPasswordField loginPassword;
    private JTextField registerUsername;
    private JPasswordField registerPassword;
    private JTextField chatInput;
    private JTextArea chatArea;

    public GUI() {

        label = new JLabel("Chatbot");
        label.setHorizontalAlignment(SwingConstants.CENTER);


        this.loginPanel = createLoginPanel();
        this.registerPanel = createRegisterPanel();
        this.chatPanel = createChatPanel();


        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);


        setTitle("AI Chat Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        add(mainPanel);
        setVisible(true);
        setResizable(false);

    }

    public static void main(String[] args) {
        new GUI();
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);


        JLabel usernameLabel = new JLabel("Username");
        registerUsername = new JTextField(20);
        usernameLabel.setForeground(new Color(87, 115, 207));


        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(new Color(87, 115, 207));
        registerPassword = new JPasswordField(20);


        JButton registerButton = new JButton("Register");
        registerButton.setActionCommand("register");
        registerButton.addActionListener(this);

        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back");
        backButton.addActionListener(this);


        gc.gridx = 0; gc.gridy = 0;
        panel.add(usernameLabel, gc);

        gc.gridx = 1;
        panel.add(registerUsername, gc);

        gc.gridx = 0; gc.gridy = 1;
        panel.add(passwordLabel, gc);

        gc.gridx = 1;
        panel.add(registerPassword, gc);

        gc.gridx = 1; gc.gridy = 3;
        panel.add(backButton, gc);

        gc.gridx = 1; gc.gridy = 2;
        panel.add(registerButton, gc);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);


        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(new Color(87, 115, 207));
        loginUsername = new JTextField(20);


        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(new Color(87, 115, 207));
        loginPassword = new JPasswordField(20);


        JButton loginButton = new JButton("Login");
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this);

        JButton registerButton = new JButton("Register");
        registerButton.setActionCommand("first_register");
        registerButton.addActionListener(this);


        gc.gridx = 0; gc.gridy = 0;
        panel.add(usernameLabel, gc);

        gc.gridx = 1;
        panel.add(loginUsername, gc);

        gc.gridx = 0; gc.gridy = 1;
        panel.add(passwordLabel, gc);

        gc.gridx = 1;
        panel.add(loginPassword, gc);

        gc.gridx = 1 ; gc.gridy = 2;
        panel.add(loginButton, gc);

        gc.gridx = 1; gc.gridy = 3;
        panel.add(registerButton, gc);

        return panel;
    }

    private JPanel createChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());


        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFocusable(false);
        chatArea.setBackground(new Color(182, 182, 182));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);


        JPanel inputPanel = new JPanel(new BorderLayout());
        chatInput = new JTextField();

        inputPanel.setBackground(new Color(87, 80, 165, 69));
        chatInput.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));


        inputPanel.setPreferredSize(new Dimension(100, 50));
        JButton sendButton = new JButton("Send");
        sendButton.setActionCommand("send");
        sendButton.addActionListener(this);
        sendButton.setPreferredSize(new Dimension(100, 25));
        sendButton.setFocusable(false);
        sendButton.setFocusPainted(false);
        sendButton.setBackground(new Color(107, 117, 109));

        JButton logout = new JButton("Logout");
        logout.setActionCommand("logout");
        logout.addActionListener(this);
        logout.setPreferredSize(new Dimension(100, 25));
        logout.setFocusable(false);
        logout.setFocusPainted(false);
        logout.setBackground(new Color(107, 117, 109));

        inputPanel.add(chatInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(logout, BorderLayout.WEST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "first_register":
                mainPanel.remove(loginPanel);
                mainPanel.add(registerPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                break;

            case "back":
                mainPanel.remove(registerPanel);
                mainPanel.add(loginPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                break;

            case "login":
                String username = loginUsername.getText().trim();
                String password = loginPassword.getText().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username or password is empty");
                    break;
                }
                try {
                    IAM user = new IAM(username, password);
                    if(user.login(loginUsername.getText(), new String(loginPassword.getPassword()))) {
                        cli = new CLI();
                        router = new OpenRouterChat(cli.getApiUrl(), cli.getApiKey());
                        mainPanel.remove(loginPanel);
                        mainPanel.add(chatPanel, BorderLayout.CENTER);


                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                revalidate();
                repaint();
                break;

            case "register":
                String regUser = registerUsername.getText().trim();
                String regPass = new String(registerPassword.getPassword()).trim();
                if (regUser.isEmpty() || regPass.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username or password cannot be empty", "Input Error", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                try {
                    IAM user = new IAM(regUser, regPass);
                    user.signUp();
                    JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    mainPanel.remove(registerPanel);
                    mainPanel.add(loginPanel, BorderLayout.CENTER);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                revalidate();
                repaint();
                break;


            case "send":
                String message = chatInput.getText();
                chatInput.setText("");
                chatArea.append("You: " + message + "\n");
                chatArea.append("AI is thinking.... \n");


                new Thread(() -> {
                    try {
                        String response = router.sendChatRequest(message);
                        SwingUtilities.invokeLater(() -> chatArea.append("Bot: " + response + "\n"));
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> chatArea.append("Error sending message\n"));
                    }
                }).start();
                break;

                case "logout":
                    mainPanel.remove(chatPanel);
                    mainPanel.add(loginPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                    break;

        }
    }
}