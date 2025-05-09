package org.example.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.models.App;
import org.example.models.User;
import org.example.models.UserDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBController {
    private static final String USERS_FILE = "src/main/resources/users.json";
    private static final String CURRENT_USER_FILE = "src/main/resources/current_user.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void loadUsers() {
        try {
            String json = FileController.getTextOfFile(USERS_FILE);

            if (json == null || json.trim().isEmpty()) {
                App.setAllUsers(new ArrayList<>());
                return;
            }

            ArrayList<User> loadedUsers = gson.fromJson(json, new TypeToken<ArrayList<User>>(){}.getType());

            if (loadedUsers == null) {
                App.setAllUsers(new ArrayList<>());
                return;
            }

            App.setAllUsers(loadedUsers);

            for (User user : loadedUsers) {
                UserDatabase.addUser(user);
            }

        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            App.setAllUsers(new ArrayList<>());
        }
    }
    public static void saveUsers() {
        try {
            FileController.writeTextToFile(
                    gson.toJson(App.getAllUsers()),
                    USERS_FILE
            );
        } catch (Exception e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static void loadCurrentUser() {
        try {
            String json = FileController.getTextOfFile(CURRENT_USER_FILE);
            User currentUser = json != null ? gson.fromJson(json, User.class) : null;
            App.setCurrentUser(currentUser);
        } catch (Exception e) {
            System.err.println("Error loading current user: " + e.getMessage());
            App.setCurrentUser(null);
        }
    }

    public static void saveCurrentUser() {
        try {
            FileController.writeTextToFile(
                    gson.toJson(App.getCurrentUser()),
                    CURRENT_USER_FILE
            );
        } catch (Exception e) {
            System.err.println("Error saving current user: " + e.getMessage());
        }
    }
}
