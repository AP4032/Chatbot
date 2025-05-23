package org.aryanoor.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Person {
    String username;
    String password;
    Person(String username, String password) {
        this.username = username;
        this.password = password;
    }
    String getUsername() {
        return username;
    }
    String getPassword() {
        return password;
    }
}
/**
 * The IAM (Identity and Access Management) class provides basic authentication services.
 * It allows users to sign up and log in using a hashed password mechanism.
 */
public class IAM {

    private final String name; // Stores the username
    private final String password; // Stores the hashed password
    private static final String FILE_PATH = "user.data.json"; // File to store user credentials
    private Gson gson = new Gson();
    private Map<String, String> users = new HashMap<>();

    /**
     * Constructor for IAM.
     *
     * @param name     The username of the user.
     * @param password The plaintext password, which will be hashed before storing.
     */
    public IAM(String name, String password) {
        this.name = name;
        this.password = password;
        loadUsers();
    }

    /**
     * Hashes the provided password using the SHA-256 algorithm.
     *
     * @param password The plaintext password.
     * @return The hashed password as a hexadecimal string.
     */
    /* private String hashPassword(String password) {
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
    }*/

    /**
     * Registers a new user by saving their credentials to a file.
     * If the file already exists, it will be overwritten with the new credentials.
     *
     * @throws IOException If an error occurs while writing to the file.
     */

    public void loadUsers() {
        try (Reader reader = new FileReader(FILE_PATH)){
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            users = gson.fromJson(reader, type);
            if (users == null) users = new HashMap<>();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean signUp(String username, String password) throws IOException {
        if (users.containsKey(username)) {
            return false; // User exists
        }
        users.put(username, password);
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Authenticates a user by verifying their name and password.
     * @return True if authentication is successful, false otherwise.
     * @throws IOException If an error occurs while reading from the file.
     */
    public boolean login(String username, String password) throws IOException {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}