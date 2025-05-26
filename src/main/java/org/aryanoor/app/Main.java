package org.aryanoor.app;

import java.io.IOException;

import javax.swing.SwingUtilities;

/**
 * The Main class serves as the entry point for the chatbot application.
 * It initializes the CLI and starts the chatbot interaction.
 */
public class Main {

    /**
     * The main method starts the application by creating an instance of the CLI class.
     *
     * @param args Command-line arguments (not used in this application).
     * @throws IOException If an error occurs during input or file operations.
     */
    public static void main(String[] args) throws IOException {
        // Start CLI in its own thread
        new Thread(() -> {
            try {
                new CLI().run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Start GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new GUI();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
}