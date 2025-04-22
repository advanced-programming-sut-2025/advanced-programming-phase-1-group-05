package org.example.models;

import java.util.HashMap;

public class App {
    private static HashMap<String, User> users = new HashMap<>();
    private static Player currentPlayer = null;
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

}
