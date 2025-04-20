package models;

import java.util.ArrayList;
import java.util.HashMap;

public class App {
    private static HashMap<String, User> users = new HashMap<>();

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }


}
