package org.aryanoor.services;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * The IAM (Identity and Access Management) class provides basic authentication services.
 * It allows users to sign up and log in using a hashed password mechanism.
 */
public class IAM {

    private final String name; // Stores the username
    private final String password; // Stores the hashed password
    private static final String DATA_FILE = "user.data"; // File to store user credentials

    /**
     * Constructor for IAM.
     *
     * @param name     The username of the user.
     * @param password The plaintext password, which will be hashed before storing.
     */
    public IAM(String name, String password) {
        this.name = name;
        this.password = hashPassword(password);
    }

    /**
     * Hashes the provided password using the SHA-256 algorithm.
     *
     * @param password The plaintext password.
     * @return The hashed password as a hexadecimal string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Registers a new user by saving their credentials to a file.
     * If the file already exists, it will be overwritten with the new credentials.
     *
     * @throws IOException If an error occurs while writing to the file.
     */
    public void signUp() throws IOException {
        String userData = name + "," + password;
        Files.write(Paths.get(DATA_FILE), userData.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        JOptionPane.showMessageDialog(null,"User registered successfully!");
    }

    /**
     * Authenticates a user by verifying their name and password.
     *
     * @param name           The username to log in.
     * @param enteredPassword The plaintext password entered by the user.
     * @return True if authentication is successful, false otherwise.
     * @throws IOException If an error occurs while reading from the file.
     */
    public boolean login(String name, String enteredPassword) throws IOException {
        // Check if the user data file exists
        if (!Files.exists(Paths.get(DATA_FILE))) {
            System.out.println("No registered user found.");
            return false;
        }

        // Read stored credentials from file
        List<String> lines = Files.readAllLines(Paths.get(DATA_FILE));
        if (lines.isEmpty()) return false;

        String[] userData = lines.get(0).split(",");
        if (userData.length != 2) return false;

        String storedName = userData[0];
        String storedHashedPassword = userData[1];
        String enteredHashedPassword = hashPassword(enteredPassword);

        // Validate user credentials
        if (storedHashedPassword.equals(enteredHashedPassword) && storedName.equals(name)) {
            System.out.println("Login successful!\n");
            return true;
        } else {
            System.out.println("Invalid credentials.");
            return false;
        }
    }
}