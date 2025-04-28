package org.example.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.models.App;
import org.example.models.User;

import java.io.File;
import java.util.ArrayList;

public class DBController {
    public static void loadUsers() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        App.setAllUsers(gson.fromJson(FileController.getTextOfFile
                        ("src/main/resources/users.json") ,
                new TypeToken<ArrayList<User>>(){}));
        if (App.getAllUsers() == null) {
            App.setAllUsers(new ArrayList<>());
        }
    }
    public static void saveUsers() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileController.writeTextToFile(gson.toJson(App.getAllUsers()) , "src/main/resources/users.json");
    }
    public static void loadCurrentUser() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        App.setCurrentUser(gson.fromJson(FileController.getTextOfFile("src/main/resources/users.json") ,
                User.class));
    }
    public static void saveCurrentUser() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileController.writeTextToFile(gson.toJson(App.getCurrentUser()) , "src/main/resources/users.json");
    }
}
