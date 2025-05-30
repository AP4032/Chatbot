package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class GUI  {
    JFrame frame;
    GridBagConstraints FrameGbc;
    JLabel Welcome;
    private String apiUrl;
    private String apiKey;
    private static final String CONFIG_FILE = "config.properties";
    public GUI(){
     frame=new JFrame();
     frame.setTitle("chatbot");
     frame.setSize(700,700);
     frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
     frame.setResizable(false);
     frame.setLocationRelativeTo(null);
     frame.setLayout(new GridBagLayout());
     frame.getContentPane().setBackground(new Color(230, 240, 255));

     FrameGbc=new GridBagConstraints();
     FrameGbc.gridx=0; FrameGbc.gridy=0;
     FrameGbc.insets=new Insets(10,10,10,10);

     Welcome=new JLabel("Welcome");
     Welcome.setFont(new Font("Arial", Font.BOLD, 24));

     FrameGbc.anchor=GridBagConstraints.NORTH;
     frame.add(Welcome,FrameGbc);
     LoginPanel();

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
        } else {
            System.out.println("Configuration file not found. Please create 'config.properties' with apiUrl and apiKey.");
            System.exit(1);
        }
    }

    public void LoginPanel (){
        JPanel LoginPanel=new JPanel();
        LoginPanel.setPreferredSize(new Dimension(350, 300));
        Border raised = BorderFactory.createRaisedBevelBorder();
        Border lowered = BorderFactory.createLoweredBevelBorder();
        Border compound = BorderFactory.createCompoundBorder(raised, lowered);
        LoginPanel.setBorder(compound);
        LoginPanel.setLayout(new GridBagLayout());

        FrameGbc.anchor=GridBagConstraints.CENTER;
        FrameGbc.gridx=0;  FrameGbc.gridy=1;

        frame.add(LoginPanel,FrameGbc);

        GridBagConstraints panelGbc=new GridBagConstraints();
        panelGbc.insets=new Insets(10,10,10,10);
        panelGbc.gridx=0;  panelGbc.gridy=0;

        JLabel Username = new JLabel("Username");
        panelGbc.gridx=0; panelGbc.gridy=0;
        panelGbc.anchor = GridBagConstraints.WEST;
        LoginPanel.add(Username,panelGbc);

        JTextField FieldUsername=new JTextField();
        FieldUsername.setPreferredSize(new Dimension(300,30 ));
        panelGbc.gridx=0; panelGbc.gridy=1;
        panelGbc.anchor = GridBagConstraints.CENTER;
        LoginPanel.add(FieldUsername,panelGbc);

        JLabel Password= new JLabel("Password");
        panelGbc.gridx=0; panelGbc.gridy=2;
        panelGbc.anchor= GridBagConstraints.WEST;
        LoginPanel.add(Password,panelGbc);

        JPasswordField passwordField= new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300,30));
        panelGbc.gridx=0; panelGbc.gridy=3;
        panelGbc.anchor=GridBagConstraints.CENTER;
        LoginPanel.add(passwordField,panelGbc);

        ActionListener DetailsButton = e -> {
            String username = FieldUsername.getText();
            String password = new String(passwordField.getPassword());
            IAM user = new IAM(username, password);
            try {
                if (user.login(username, password)) {
                frame.getContentPane().removeAll();
                frame.repaint();
                frame.revalidate();
                ChatPanel();
                } else {
                    JOptionPane.showMessageDialog(null,"Invalid credentials.","Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };

        JButton button=new JButton("Login");
        button.addActionListener(DetailsButton);
        button.setBackground(new Color(1,100,200));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(300,30));
        panelGbc.gridx=0;  panelGbc.gridy=4;
        LoginPanel.add(button,panelGbc);


        JLabel SignUp =new JLabel("Sign Up");
        SignUp.setForeground(Color.BLUE);
        SignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        FrameGbc.gridx=0; FrameGbc.gridy=2;
        FrameGbc.anchor=GridBagConstraints.WEST;
        frame.add(SignUp,FrameGbc);


        SignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginPanel.remove(button);
                frame.remove(SignUp);
                SignUp(FieldUsername,passwordField,LoginPanel,panelGbc);
            }
        });


        frame.setVisible(true);
    }

    public void SignUp(JTextField FieldUsername,JPasswordField passwordField,JPanel LoginPanel,GridBagConstraints panelGbc){
        ActionListener DetailsButton= e -> {
            String userName=FieldUsername.getText();
            String userPassword=new String(passwordField.getPassword());
            IAM newUser = new IAM(userName, userPassword);
            try {
                newUser.signUp();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.revalidate();
            FrameGbc.gridx=0; FrameGbc.gridy=0;
            FrameGbc.anchor=GridBagConstraints.NORTH;
            frame.add(Welcome,FrameGbc);
            LoginPanel();
        };

        JButton button=new JButton("SignUp");
        button.addActionListener(DetailsButton);
        button.setBackground(new Color(1, 100, 200));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(300,30));
        panelGbc.gridx=0;  panelGbc.gridy=4;
        LoginPanel.add(button,panelGbc);

        JLabel labelExit = new JLabel("Do You have Account? Login");
        labelExit.setForeground(Color.BLUE);
        labelExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        FrameGbc.gridx=0; FrameGbc.gridy=2;
        FrameGbc.anchor=GridBagConstraints.WEST;
        frame.add(labelExit,FrameGbc);

        labelExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.getContentPane().removeAll();
                frame.repaint();
                frame.revalidate();
                FrameGbc.gridx=0; FrameGbc.gridy=0;
                FrameGbc.anchor=GridBagConstraints.NORTH;
                frame.add(Welcome,FrameGbc);
                LoginPanel();
            }
        });

       frame.setVisible(true);

    }

    public void ChatPanel(){
        frame.setLayout(new GridBagLayout());

        JPanel panelLogout = new JPanel(new BorderLayout());
        panelLogout.setBackground(new Color(120, 240, 255));

        JLabel ChatDeepSeek=new JLabel("  DeepSeek Chat");
        ChatDeepSeek.setFont(new Font("Arial", Font.BOLD, 18));
        ChatDeepSeek.setForeground(Color.WHITE);
        panelLogout.add(ChatDeepSeek,BorderLayout.WEST);

        ActionListener DetailsButtonLogout = e -> {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.revalidate();
            LoginPanel();
        };

        JButton buttonLogout=new JButton("Logout");
        buttonLogout.setBackground(new Color(120, 240, 255));
        buttonLogout.setForeground(Color.WHITE);
        buttonLogout.addActionListener(DetailsButtonLogout);
        panelLogout.add(buttonLogout,BorderLayout.EAST);

        FrameGbc.gridx=0; FrameGbc.gridy=0;
        FrameGbc.anchor=GridBagConstraints.NORTH;
        FrameGbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(panelLogout,FrameGbc);

        JTextArea chatArea =new JTextArea();
        chatArea.setEnabled(false);
        chatArea.setBackground(new Color(1,100,200));
        chatArea.setFont(new Font("Arial", Font.BOLD, 18));
        chatArea.setDisabledTextColor(new Color(120, 240, 255));;
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setPreferredSize(new Dimension(500,500));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        FrameGbc.gridx=0; FrameGbc.gridy=1;
        FrameGbc.anchor=GridBagConstraints.CENTER;
        frame.add(scrollPane,FrameGbc);

        JPanel panelTextAndButton=new JPanel();
        panelTextAndButton.setBackground(new Color(120, 240, 255));

        JTextField textField=new JTextField();
        textField.setPreferredSize(new Dimension(500,30));
        panelTextAndButton.add(textField);

        try {
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        OpenRouterChat chatBot = new OpenRouterChat(apiUrl, apiKey);

        ActionListener DetailsButtonSend= e -> {
        String Text = textField.getText();
            try {
                String response = chatBot.sendChatRequest(Text);
                chatArea.append("You: " +Text+ "\n" +"\n");
                chatArea.append("Bot:" + response + "\n"+"\n");
                textField.setText("");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        };

        JButton button=new JButton("Send");
        button.setPreferredSize(new Dimension(100,30));
        button.addActionListener(DetailsButtonSend);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(120, 240, 255));
        panelTextAndButton.add(button);

        FrameGbc.anchor=GridBagConstraints.SOUTH;
        FrameGbc.gridx=0; FrameGbc.gridy=2;
        frame.add(panelTextAndButton,FrameGbc);

        frame.setVisible(true);
    }


public static void main(String [] args){
        new GUI();
}

}
