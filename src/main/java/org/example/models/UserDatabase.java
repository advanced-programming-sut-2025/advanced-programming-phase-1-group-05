package org.example.models;

import org.example.controllers.DBController;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private static final Map<String, User> users = new HashMap<>();
    private static final Map<String, Boolean> userInGameStatus = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUsername().toLowerCase(), user);
        userInGameStatus.put(user.getUsername().toLowerCase(), false);
        DBController.saveUsers();
        DBController.saveCurrentUser();
    }

    public static User getUserByUsername(String username) {
        return users.get(username.toLowerCase());
    }

    public static void updateUser(User user) {
        users.put(user.getUsername().toLowerCase(), user);
        DBController.saveUsers();
        DBController.saveCurrentUser();
    }

    public static boolean usernameExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    public static boolean isUserInGame(String username) {
        return userInGameStatus.getOrDefault(username.toLowerCase(), false);
    }

    public static void setUserInGame(String username, boolean inGame) {
        userInGameStatus.put(username.toLowerCase(), inGame);
    }

    public static Map<String, User> getAllUsers() {
        return new HashMap<>(users); // Return a copy to prevent external modification
    }
}