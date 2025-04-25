package org.example.models;

import org.example.controllers.DBController;

import java.util.ArrayList;
import java.util.HashMap;

public class App {
    private static HashMap<String, User> users = new HashMap<>();
    private static Player currentPlayer = null;
    private static User currentUser;
    private static ArrayList<User> allUsers;
    public static HashMap<String, User> getUsers() {
        return users;
    }
    //next line is for when time functionality is fixed
    //public static TimeAndDate currentTime = new TimeAndDate();
    public static void addUser(User user) {
        users.put(user.getUsername(), user);
    }
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    public static void setCurrentPlayer(Player currentPlayer) {
        App.currentPlayer = currentPlayer;
    }


    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static void setAllUsers(ArrayList<User> allusers) {
        allUsers = allusers;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    public static boolean isStayLoggedIn() {
        return currentUser != null;
    }

    //maybe need!
    public static Game getCurrentGame() {
        return null;
    }

    public static void setCurrentGame(Game currentGame) {

    }

}
