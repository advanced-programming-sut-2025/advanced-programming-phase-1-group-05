
package org.example.Server.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Client.FileController;
import org.example.Common.User;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

public class UserDatabase implements Serializable {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    private static final List<User> users = new ArrayList<>();
    private static final Map<String, Boolean> userInGameStatus = new HashMap<>();

    static {
        initDatabase();
        loadUsers();
    }



    private static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql =
                "CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, " +
                    "password TEXT, " +
                    "passwordHash TEXT, " +
                    "securityQuestion TEXT, " +
                    "securityAnswer TEXT" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User user) {
        if (!usernameExists(user.getUsername())) {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO users (username, passwordHash, securityQuestion, securityAnswer) VALUES (?, ?, ?, ?)"
                 )) {

                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getSecurityQuestion());
                ps.setString(4, user.getSecurityAnswer());
                ps.executeUpdate();

                users.add(user);

            } catch (SQLException e) {
                System.err.println("Error adding user: " + e.getMessage());
            }
        }
    }

    public static boolean usernameExists(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT 1 FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
        // Here we rewrite all users to DB (optional if addUser/update handles saving individually)
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT OR REPLACE INTO users (username, passwordHash, securityQuestion, securityAnswer) VALUES (?, ?, ?, ?)"
            )) {
                for (User user : users) {
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getSecurityQuestion());
                    ps.setString(4, user.getSecurityAnswer());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }


    public static void loadUsers() {
        users.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                User user = new User(
                    rs.getString("username"),
                    rs.getString("passwordHash"),
                    rs.getString("securityQuestion"),
                    rs.getString("securityAnswer")
                );
                users.add(user);
            }

        } catch (SQLException e) {
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

