package org.aryanoor.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class IAM {
    private final String username;
    private final String hashedPassword;
    private static final String DATA_FILE = "user.data";
    private static final int SALT_LENGTH = 16;

    public IAM(String username, String password) {
        this.username = username;
        this.hashedPassword = hashPassword(password, generateSalt());
    }

    private String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean signUp() throws IOException {
        if (userExists(username)) {
            return false;
        }

        String userData = username + "," + hashedPassword + "\n";
        Files.write(Paths.get(DATA_FILE), userData.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        return true;
    }

    public boolean login(String username, String password) throws IOException {
        if (!Files.exists(Paths.get(DATA_FILE))) {
            return false;
        }

        List<String> lines = Files.readAllLines(Paths.get(DATA_FILE));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2 && parts[0].equals(username)) {
                String storedHash = parts[1];
                String computedHash = hashPassword(password, parts[1]);
                return storedHash.equals(computedHash);
            }
        }
        return false;
    }

    private boolean userExists(String username) throws IOException {
        if (!Files.exists(Paths.get(DATA_FILE))) {
            return false;
        }
        return Files.lines(Paths.get(DATA_FILE))
                .anyMatch(line -> line.startsWith(username + ","));
    }
}