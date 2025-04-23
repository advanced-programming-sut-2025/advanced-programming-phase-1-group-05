package org.example.models;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private static final Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUsername().toLowerCase(), user);
    }

    public static User getUserByUsername(String username) {
        return users.get(username.toLowerCase());
    }

    public static boolean usernameExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    public static void updateUser(User user) {
        users.put(user.getUsername(), user);
    }
}