package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GUI extends JFrame implements ActionListener {
    JLabel label;
    JPanel loginPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel regesterPanel = new JPanel();
    JPanel mainChatPanel = new JPanel();
    JTextField loginUsernameField;
    JPasswordField loginPasswordField;
    JTextField regesterUsernameField;
    JPasswordField regesterPasswordField;
    IAM newUser;
    OpenRouterChat chatBot;
    CLI cli;

    JTextArea textArea;
    JTextField mainTextField;
    public GUI() {
        label = new JLabel("Chat Bot");
        label.setFont(new Font("Tahoma", Font.PLAIN, 50));
        label.setForeground(new Color(0x1212A5));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        /// /////////////////////////////////////////
        loginUsernameField = new JTextField(15);
        loginUsernameField.setPreferredSize(new Dimension(150, 30));
        loginPasswordField = new JPasswordField(15);
        loginPasswordField.setPreferredSize(new Dimension(150, 30));
        /// /////////////////////////////////////////////////////

        titlePanel.add(label);
        titlePanel.setBackground(new Color(0x292828));
        titlePanel.setPreferredSize(new Dimension(100, 100));
        /// //////////////////////////////////////////////
        this.regesterPanel=createRegisterPanel();
        this.loginPanel=createLoginPanel();
        this.mainChatPanel=createChatPanel();
        /// ////////////////////////////
        setTitle("Chatbot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }



    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(0x292828));
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setPreferredSize(new Dimension(500, 400));
        /// ////////////////////////////////////////////////////
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        /// /////////////////////////////////////////////////
        /// //////////////////////////////////////////////////
        JButton loginButton = new JButton("Login");
        loginButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        loginButton.setFocusable(false);
        loginButton.setBackground(new Color(0x111185));
        loginButton.setForeground(Color.WHITE);
        //loginButton.setOpaque(true);
        loginButton.setPreferredSize(new Dimension(100, 50));
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this);
        /// ///////////////////////////////////////////////////
        JButton registerButton = new JButton("Register");
        registerButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        registerButton.setFocusable(false);
        registerButton.setBackground(new Color(0x111185));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(100, 50));
        registerButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        registerButton.setActionCommand("firstRegister");
        registerButton.addActionListener(this);
        /// //////////////////////////////////////////////////
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        usernameLabel.setForeground(new Color(240, 240, 240));
        /// //////////////////////////////////////////////////
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        passwordLabel.setForeground(new Color(240, 240, 240));
        /// /////////////////////////////////////////////////////
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(loginUsernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(loginPasswordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(loginButton, gbc);
        gbc.gridx = 1;
        loginPanel.add(registerButton, gbc);
        return loginPanel;



    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(new Color(0x292828));
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setPreferredSize(new Dimension(500, 400));
        /// ////////////////////////////////////////////////////
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        /// /////////////////////////////////////////////////

        /// //////////////////////////////////////////////////
        JButton backButton = new JButton("Back");
        backButton.setFocusable(false);
        backButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        backButton.setBackground(new Color(0x111185));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 50));
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        backButton.setActionCommand("Back");
        backButton.addActionListener(this);
        /// ///////////////////////////////////////////////////
        JButton registerButton = new JButton("Register");
        registerButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        registerButton.setFocusable(false);
        registerButton.setBackground(new Color(0x111185));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(100, 50));
        registerButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        registerButton.setActionCommand("secondRegister");
        registerButton.addActionListener(this);
        /// //////////////////////////////////////////////////
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        usernameLabel.setForeground(new Color(240, 240, 240));
        /// //////////////////////////////////////////////////
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        passwordLabel.setForeground(new Color(240, 240, 240));
        /// /////////////////////////////////////////////////////
        regesterUsernameField= new JTextField(15);
        regesterUsernameField.setPreferredSize(new Dimension(150, 30));
        regesterPasswordField = new JPasswordField(15);
        regesterPasswordField.setPreferredSize(new Dimension(150, 30));
        ///  //////////////////////////////////////
        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        registerPanel.add(regesterUsernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        registerPanel.add(regesterPasswordField, gbc);
        gbc.gridy = 2;
        gbc.gridx = 1;
        registerPanel.add(registerButton, gbc);
        gbc.gridx = 0;
        registerPanel.add(backButton, gbc);
        return registerPanel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            //JPanel panel = gui.createLoginPanel();
            //gui.add(panel, BorderLayout.CENTER);

        });
    }

    private JPanel createChatPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setBackground(new Color(0x292828));
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(500, 350));
        /// //////////////////////////////////////////////

        ///  ////////////////////////////////////////////////
        textArea = new JTextArea();
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setBackground(new Color(240, 240, 240));
        textArea.setForeground(Color.BLACK);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textArea.setMargin(new Insets(5, 50, 5, 5));
        textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        textArea.setPreferredSize(new Dimension(500,300));

        textArea.setText("Hi, Welcome to Chatbot!\n");

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setPreferredSize(new Dimension(500, 350));
        textPanel.add(scrollPane, BorderLayout.CENTER);
        textPanel.add(textArea,BorderLayout.CENTER);
        chatPanel.add(textPanel, BorderLayout.CENTER);
        /// ///////////////////////////////////////////////
        JPanel textfieldPanel = new JPanel();
        mainTextField= new JTextField();
        mainTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        mainTextField.setPreferredSize(new Dimension(300, 50));

        JButton submitButton = new JButton("Send");
        submitButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
        submitButton.setFocusable(false);
        submitButton.setBackground(new Color(0x111185));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        submitButton.setActionCommand("Send");
        submitButton.addActionListener(this);
        submitButton.setPreferredSize(new Dimension(90, 50));
        textfieldPanel.add(mainTextField);
        textfieldPanel.add(submitButton);

        chatPanel.add(textfieldPanel, BorderLayout.SOUTH);

        return chatPanel;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "firstRegister":
                remove(loginPanel);
                add(regesterPanel, BorderLayout.CENTER);
                pack();
                revalidate();
                repaint();
                break;
            case "login":
                if (loginUsernameField.getText().isBlank() || loginPasswordField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(this, "Please enter all the fields correctly.", "Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                IAM user = new IAM(loginUsernameField.getText(),loginPasswordField.getText());
                try {
                    if(!user.login(loginUsernameField.getText(),loginPasswordField.getText())){
                        JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    cli=new CLI();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                String apiUrl=cli.getApiUrl();
                String apiKey=cli.getApiKey();
                chatBot = new OpenRouterChat(apiUrl, apiKey);


                titlePanel.setPreferredSize(new Dimension(100,50));
                titlePanel.setBackground(new Color(240, 240, 240));

                label.setFont(new Font("Tahoma", Font.PLAIN, 20));

                remove(loginPanel);
                add(mainChatPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                pack();
                break;

            case "secondRegister":
                if (regesterUsernameField.getText().isBlank() || regesterPasswordField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(this, "Please enter all the fields correctly.", "Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    IAM newUser = new IAM(regesterUsernameField.getText(), regesterPasswordField.getText());
                    this.newUser = newUser;
                    try {
                        this.newUser.signUp();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(this, "You have successfully sign up.", "Success",JOptionPane.INFORMATION_MESSAGE);
                }

                remove(regesterPanel);
                add(loginPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                pack();
                break;
            case "Send":
                String question = mainTextField.getText();
                mainTextField.setText("");
                textArea.append("You :"+question + "\n");
                textArea.append("Thinking....\n");

                revalidate();
                repaint();
                String response;
                try {
                    response = chatBot.sendChatRequest(question);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                textArea.append("Bot :"+response+"\n");

                revalidate();
                repaint();
                pack();
                break;
            case "Back":
                remove(regesterPanel);
                add(loginPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                pack();
                break;
        }
    }

}
