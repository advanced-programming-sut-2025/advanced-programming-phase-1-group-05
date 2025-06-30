//package org.example.models;
//
//import org.example.controllers.DBController;
//
//import java.util.*;
//
//public class UserDatabase {
//    private static final Map<String, User> users = new HashMap<>();
//    private static final Map<String, Boolean> userInGameStatus = new HashMap<>();
//
//    public static void addUser(User user) {
//        String usernameKey = user.getUsername();
//        users.put(usernameKey, user);
//        userInGameStatus.put(usernameKey, false);
//
//        DBController.saveUsers();
//        DBController.saveCurrentUser();
//    }
//
//    public static User getUserByUsername(String username) {
//        return users.get(username);
//    }
//
//    public static void updateUser(User user) {
//        String usernameKey = user.getUsername();
//        users.put(usernameKey, user);
//        DBController.saveUsers();
//        DBController.saveCurrentUser();
//    }
//
//    public static boolean usernameExists(String username) {
//        return users.containsKey(username);
//    }
//
//    public static boolean isUserInGame(String username) {
//        return userInGameStatus.getOrDefault(username, false);
//    }
//
//    public static void setUserInGame(String username, boolean inGame) {
//        userInGameStatus.put(username, inGame);
//    }
//
//    public static List<User> getAllUsers() {
//        return new ArrayList<>(users.values());
//    }
//
//
//    public static void clear() {
//        users.clear();
//        userInGameStatus.clear();
//    }
//
//}
package org.example.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.controllers.FileController;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class UserDatabase {
    private static final String USER_FILE = "users.json";
    private static final List<User> users = new ArrayList<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Boolean> userInGameStatus = new HashMap<>();

    static {
        loadUsers();
    }

    public static void addUser(User user) {
        if (!usernameExists(user.getUsername())) {
            users.add(user);
            saveUsers();
        }
    }

    public static boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    public static User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public static List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    public static void saveUsers() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(users); // must be a List<User>
            FileController.writeTextToFile(json, USER_FILE);
        } catch (Exception e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static void loadUsers() {
        try (FileReader reader = new FileReader(USER_FILE)) {
            Type listType = new TypeToken<ArrayList<User>>() {}.getType();
            List<User> loaded = gson.fromJson(reader, listType);
            if (loaded != null) {
                users.clear();
                users.addAll(loaded);
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }
    public static boolean isUserInGame(String username) {
        return userInGameStatus.getOrDefault(username, false);
    }
        public static void setUserInGame(String username, boolean inGame) {
        userInGameStatus.put(username, inGame);
    }
}

