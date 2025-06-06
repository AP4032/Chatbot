package org.aryanoor.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class IAM {
    private final String username;
    private final String password; // This will store the plaintext temporarily
    private static final String DATA_FILE = "user.data";

    public IAM(String username, String password) {
        this.username = username;
        this.password = password; // Store plaintext to hash later
    }

    // Consistent hashing method
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

    public void signUp() throws IOException {
        String hashedPassword = hashPassword(password);
        String userData = username + ":" + hashedPassword + "\n"; // Using colon as delimiter
        Files.write(Paths.get(DATA_FILE), userData.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    public boolean login(String username, String inputPassword) throws IOException {
        if (!Files.exists(Paths.get(DATA_FILE))) {
            return false;
        }

        List<String> lines = Files.readAllLines(Paths.get(DATA_FILE));
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2 && parts[0].equals(username)) {
                String storedHash = parts[1].trim();
                String inputHash = hashPassword(inputPassword);
                return storedHash.equals(inputHash);
            }
        }
        return false;
    }
}