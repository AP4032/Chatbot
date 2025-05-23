package org.aryanoor.app;

import org.aryanoor.services.IAM;
import org.aryanoor.services.OpenRouterChat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * The CLI class provides a command-line interface for user authentication and
 * chatbot interaction.
 * It loads API configurations, manages user login, and facilitates chatbot
 * communication.
 */
public class CLI {

    private String apiUrl; // API URL for chatbot
    private String apiKey; // API key for authentication
    private static final String CONFIG_FILE = "config.properties"; // Configuration file path

    /**
     * Constructor for CLI.
     * Loads the API configuration from the properties file.
     *
     * @throws IOException If an error occurs while reading the configuration file.
     */
    public CLI() throws IOException {
        loadConfig();
    }

    /**
     * Loads API configuration (apiUrl and apiKey) from a properties file.
     *
     * @throws IOException If an error occurs while reading the file.
     */
    private void loadConfig() throws IOException {
        Properties properties = new Properties();
        Path configPath = Paths.get(CONFIG_FILE);

        if (Files.exists(configPath)) {
            try (InputStream input = Files.newInputStream(configPath)) {
                properties.load(input);
                apiUrl = properties.getProperty("apiUrl");
                apiKey = properties.getProperty("apiKey");

                // Validation
                if (apiUrl == null || apiUrl.isBlank()) {
                    throw new IOException("apiUrl is missing in config.properties");
                }
                if (apiKey == null || apiKey.isBlank()) {
                    throw new IOException("apiKey is missing in config.properties");
                }

                System.out.println("Config loaded successfully");
                System.out.println("API URL: " + apiUrl);
                System.out.println("API Key: " + (apiKey != null ? "*****" : "null"));
            }
        } else {
            throw new FileNotFoundException("config.properties not found at: " + configPath.toAbsolutePath());
        }
    }

    /**
     * Runs the CLI for user authentication and chatbot interaction.
     *
     * @throws IOException If an error occurs during input/output operations.
     */
    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // If no user is registered, prompt for registration
        if (!Files.exists(Paths.get("user.data"))) {
            System.out.print("No registered user found. Please register.\nEnter username: ");
            String username = reader.readLine();
            System.out.print("Enter password: ");
            String password = reader.readLine();
            IAM newUser = new IAM(username, password);
            newUser.signUp();
        }

        System.out.println(">Hello Again! Let's login to your account");

        // Login process
        while (true) {
            System.out.print("Enter username: ");
            String username = reader.readLine();
            System.out.print("Enter password: ");
            String password = reader.readLine();
            IAM user = new IAM(username, password);
            if (user.login(username, password)) {
                break;
            }
            System.out.println("Try again.");
        }

        OpenRouterChat chatBot = new OpenRouterChat(apiUrl, apiKey);
        System.out.println("Welcome to the chatbot! Type 'exit' to quit.");

        // Chat loop
        while (true) {
            System.out.print("> Enter your question: ");
            String question = reader.readLine();

            if (question.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                break;
            }

            System.out.println("Thinking...");
            String response = chatBot.sendChatRequest(question);
            System.out.println(response);
            System.out.println(">---------------------------------<");
        }
    }

    /**
     * Processes the user input to ensure proper formatting.
     *
     * Assignment Task:
     * - Capitalizes the first letter of each sentence.
     * - Ensures the sentence ends with a question mark.
     *
     * @param prompt The original user input.
     * @return The formatted user input.
     */
    private String promptPreprocess(String prompt) {
        // TODO: Implement the preprocessing logic
        return null;
    }

    /**
     * Counts the number of words in a given string.
     *
     * @param input The original string.
     * @return The number of words in the string.
     */
    private int countWords(String input) {
        // TODO: Implement the countWords and use it for counting question and response
        // words.
        return 0;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
}
