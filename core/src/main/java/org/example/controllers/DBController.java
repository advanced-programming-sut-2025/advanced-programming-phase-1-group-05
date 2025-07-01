package org.example.controllers;

import com.google.gson.*;
import org.example.models.MyGame;
import org.example.models.Player;
import org.example.models.User;
import org.example.models.UserDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBController {

    public static void registerUser(User user) {
        UserDatabase.addUser(user);
    }

    public static boolean usernameExists(String username) {
        return UserDatabase.usernameExists(username);
    }

    public static User getUserByUsername(String username) {
        return UserDatabase.getUserByUsername(username);
    }

    public static void saveAllUsers() {
        UserDatabase.saveUsers();
    }

    public static void loadAllUsers() {
        UserDatabase.loadUsers();
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }

//    public static void loadGameState() {
//        try {
//            String json = FileController.getTextOfFile("game_state.json");
//            if (json == null || json.trim().isEmpty()) return;
//
//            Gson gson = new Gson();
//            JsonObject gameState = JsonParser.parseString(json).getAsJsonObject();
//
//            String ownerUsername = gameState.get("ownerUsername").getAsString();
//            JsonArray playersArray = gameState.getAsJsonArray("players");
//
//            GameMenuController.selectedPlayers.clear();
//            MyGame.getAllPlayers().clear();
//
//            for (JsonElement element : playersArray) {
//                JsonObject playerObj = element.getAsJsonObject();
//                String username = playerObj.get("username").getAsString();
//                int energy = playerObj.get("energy").getAsInt();
//
//                User user = UserDatabase.getUserByUsername(username);
//                if (user == null) continue;
//
//                Player player = new Player(user);
//                player.setEnergy(energy);
//
//                GameMenuController.selectedPlayers.add(player);
//                MyGame.addPlayer(player);
//                DBController.savePlayersToFile();
//            }
//
//            MyGame.setCurrentPlayer(MyGame.getPlayerByUsername(ownerUsername));
//
//        } catch (Exception e) {
//            System.err.println("Error loading game state: " + e.getMessage());
//        }
//    }

//    public static void loadGameState() {
//        try {
//            // مرحله 1: بارگذاری فایل
//            String json = FileController.getTextOfFile("game_state.json");
//            if (json == null || json.trim().isEmpty()) {
//                System.err.println("game_state.json is empty or missing.");
//                return;
//            }
//
//            System.out.println("Raw game_state.json:\n" + json);
//
//            // مرحله 2: پارس کردن JSON
//            JsonObject gameState = JsonParser.parseString(json).getAsJsonObject();
//            String ownerUsername = gameState.get("ownerUsername").getAsString();
//            JsonArray playersArray = gameState.getAsJsonArray("players");
//
//            // مرحله 3: پاک کردن لیست‌ها
//            GameMenuController.selectedPlayers.clear();
//            MyGame.getAllPlayers().clear();
//
//            // مرحله 4: ساختن بازیکن‌ها
//            for (JsonElement element : playersArray) {
//                JsonObject playerObj = element.getAsJsonObject();
//                String username = playerObj.get("username").getAsString();
//                int energy = playerObj.get("energy").getAsInt();
//
//                User user = UserDatabase.getUserByUsername(username);
//                if (user == null) {
//                    System.err.println("User not found: " + username);
//                    continue;
//                }
//
//                Player player = new Player(user);
//                player.setEnergy(energy);
//
//                GameMenuController.selectedPlayers.add(player);
//                MyGame.addPlayer(player);
//            }
//
//            // مرحله 5: تعیین بازیکن فعلی
//            Player ownerPlayer = MyGame.getPlayerByUsername(ownerUsername);
//            if (ownerPlayer != null) {
//                MyGame.setCurrentPlayer(ownerPlayer);
//                System.out.println("Current player set to: " + ownerPlayer.getUsername());
//            } else {
//                System.err.println("Owner username not found among players: " + ownerUsername);
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error loading game state: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public static void loadGameState() {
        try {
            String json = FileController.getTextOfFile("game_state.json");

            if (json == null || json.trim().isEmpty()) {
                System.err.println("game_state.json is empty or missing.");
                return;
            }

            System.out.println("Raw game_state.json:\n" + json);

            JsonElement root = JsonParser.parseString(json);
            if (!root.isJsonObject()) {
                System.err.println("Error: Expected JSON Object but got: " + root);
                return;
            }

            JsonObject gameState = root.getAsJsonObject();

            if (!gameState.has("ownerUsername") || !gameState.has("players")) {
                System.err.println("Error: Missing required fields in game_state.json");
                return;
            }

            String ownerUsername = gameState.get("ownerUsername").getAsString();
            JsonArray playersArray = gameState.getAsJsonArray("players");

            GameMenuController.selectedPlayers.clear();
            MyGame.getAllPlayers().clear();

            for (JsonElement element : playersArray) {
                JsonObject playerObj = element.getAsJsonObject();
                String username = playerObj.get("username").getAsString();
                int energy = playerObj.get("energy").getAsInt();

                User user = UserDatabase.getUserByUsername(username);
                if (user == null) {
                    System.err.println("User not found in database: " + username);
                    continue;
                }

                Player player = new Player(user);
                player.setEnergy(energy);

                GameMenuController.selectedPlayers.add(player);
                MyGame.addPlayer(player);
            }

            Player ownerPlayer = MyGame.getPlayerByUsername(ownerUsername);
            if (ownerPlayer != null) {
                MyGame.setCurrentPlayer(ownerPlayer);
                System.out.println("✅ Current player set to: " + ownerPlayer.getUsername());
            } else {
                System.err.println("❌ Owner username not found among loaded players: " + ownerUsername);
            }

        } catch (Exception e) {
            System.err.println("Error loading game state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveGameState() {
        try {
            Map<String, Object> gameState = new HashMap<>();

            if (MyGame.getCurrentPlayer() == null) {
                System.err.println("Error: No current player set for saving state.");
                return;
            }

            gameState.put("ownerUsername", MyGame.getCurrentPlayer().getUsername());

            List<Map<String, Object>> playersInfo = new ArrayList<>();
            for (Player player : GameMenuController.selectedPlayers) {
                Map<String, Object> playerInfo = new HashMap<>();
                playerInfo.put("username", player.getUsername());
                playerInfo.put("energy", player.getEnergy());
                playersInfo.add(playerInfo);
            }

            gameState.put("players", playersInfo);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(gameState);

            System.out.println("Saving game state:");
            System.out.println(json);

            FileController.writeTextToFile2(json);

        } catch (Exception e) {
            System.err.println("Error saving game state: " + e.getMessage());
        }
    }

    public static void savePlayersToFile() {
        try {
            List<Map<String, Object>> playersInfo = new ArrayList<>();
            for (Player player : MyGame.getAllPlayers()) {
                Map<String, Object> playerInfo = new HashMap<>();
                playerInfo.put("username", player.getUsername());
                playerInfo.put("energy", player.getEnergy());
                // اطلاعات بیشتر بازیکن رو اگر خواستی اضافه کن
                playersInfo.add(playerInfo);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(playersInfo);
            FileController.writeTextToFile(json, "players.json");

        } catch (Exception e) {
            System.err.println("Error saving players: " + e.getMessage());
        }
    }

    public static void loadPlayersFromFile() {
        try {
            String json = FileController.getTextOfFile("players.json");
            if (json == null || json.trim().isEmpty()) return;

            Gson gson = new Gson();
            JsonArray playersArray = JsonParser.parseString(json).getAsJsonArray();

            MyGame.getAllPlayers().clear(); // پاکسازی لیست قبلی

            for (JsonElement element : playersArray) {
                JsonObject playerObj = element.getAsJsonObject();
                String username = playerObj.get("username").getAsString();
                int energy = playerObj.get("energy").getAsInt();

                User user = UserDatabase.getUserByUsername(username);
                if (user != null) {
                    Player player = new Player(user);
                    player.setEnergy(energy);
                    MyGame.addPlayer(player);
//                    DBController.savePlayersToFile();
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading players: " + e.getMessage());
        }
    }

}
