package org.aryanoor.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The OpenRouterChat class provides functionality to communicate with the
 * DeepSeek API.
 * It sends a user prompt to the API and retrieves a chatbot-generated response.
 */
public class OpenRouterChat {

    private final String apiUrl; // API endpoint URL
    private final String apiKey; // API key for authentication

    /**
     * Constructor for OpenRouterChat.
     *
     * @param apiUrl The API endpoint URL.
     * @param apiKey The API key for authentication.
     */
    public OpenRouterChat(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    /**
     * Sends a chat request to the DeepSeek API with a given user question.
     *
     * @param question The user's question to be sent to the chatbot.
     * @return The chatbot's response as a String.
     * @throws IOException If an error occurs during the network communication.
     */
    public String sendChatRequest(String question) throws IOException {
        HttpURLConnection conn;
        // Validate API URL first
        if (apiUrl == null || apiUrl.isBlank()) {
            throw new IOException("API URL is not configured");
        }

        try {
            URL url = URI.create(apiUrl).toURL(); // This will throw if URL is invalid
            conn = getHttpURLConnection(question, url);
            // ... rest of your existing code ...
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid API URL: " + apiUrl, e);
        }
        // Create a connection to the API URL

        // Read the API response
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // Extract and return the chatbot's response from the JSON
        return extractMessage(response.toString());
    }

    private HttpURLConnection getHttpURLConnection(String question, URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Construct the JSON request payload
        String jsonInputString = "{" +
                "\"model\": \"deepseek/deepseek-r1:free\"," +
                "\"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}]" +
                "}";

        // Send the request data
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }

    /**
     * Extracts the chatbot's response message from the JSON API response.
     *
     * @param jsonResponse The JSON response from the API.
     * @return The chatbot's response message as a String, or an error message if
     *         extraction fails.
     */
    private String extractMessage(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Check if the response contains an error message
            if (jsonObject.has("error")) {
                return "Error: " + jsonObject.getJSONObject("error").getString("message");
            }

            // Extract chatbot response from "choices" array
            JSONArray choicesArray = jsonObject.getJSONArray("choices");
            if (!choicesArray.isEmpty()) {
                return choicesArray.getJSONObject(0).getJSONObject("message").getString("content");
            }
        } catch (Exception e) {
            return "Error: Unable to parse JSON response. " + e.getMessage();
        }
        return "Error: No valid response received.";
    }

    /**
     * Handles the case where the chatbot response is null.
     * This method is intentionally left empty for students to implement.
     *
     * Assignment Task:
     * - If the chatbot response is null, the method should retry sending the
     * prompt.
     * - Consider setting a maximum number of retries to avoid infinite loops.
     * - Implement logging or console messages to indicate when a retry occurs.
     */
    private void nullResponseHandler() {
        // TODO: You should implement retry logic here.
        // This method will be called when the response is empty.
    }
}