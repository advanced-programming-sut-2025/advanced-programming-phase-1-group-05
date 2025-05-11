package org.example.models;

import org.example.controllers.DBController;

import java.util.*;

public class UserDatabase {
    private static final Map<String, User> users = new HashMap<>();
    private static final Map<String, Boolean> userInGameStatus = new HashMap<>();

    public static void addUser(User user) {
        String usernameKey = user.getUsername();
        users.put(usernameKey, user);
        userInGameStatus.put(usernameKey, false);

        DBController.saveUsers();
        DBController.saveCurrentUser();
    }

    public static User getUserByUsername(String username) {
        return users.get(username);
    }

    public static void updateUser(User user) {
        String usernameKey = user.getUsername();
        users.put(usernameKey, user);
        DBController.saveUsers();
        DBController.saveCurrentUser();
    }

    public static boolean usernameExists(String username) {
        return users.containsKey(username);
    }

    public static boolean isUserInGame(String username) {
        return userInGameStatus.getOrDefault(username, false);
    }

    public static void setUserInGame(String username, boolean inGame) {
        userInGameStatus.put(username, inGame);
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }


    public static void clear() {
        users.clear();
        userInGameStatus.clear();
    }
}
