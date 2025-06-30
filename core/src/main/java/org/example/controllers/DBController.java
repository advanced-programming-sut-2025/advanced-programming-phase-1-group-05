//package org.example.controllers;
//
//import com.google.gson.*;
//import com.google.gson.reflect.TypeToken;
//import org.example.models.*;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.Writer;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DBController {
//    private static final String USERS_FILE = "src/main/resources/saveusers.json";
//    private static final String CURRENT_USER_FILE = "src/main/resources/current_user.json";
//    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//

////    public static void loadUsers() {
////        try {
////            String json = FileController.getTextOfFile(USERS_FILE);
////
////            if (json == null || json.trim().isEmpty()) {
////                App.setAllUsers(new ArrayList<>());
////                return;
////            }
////
////            ArrayList<User> loadedUsers = gson.fromJson(json, new TypeToken<ArrayList<User>>(){}.getType());
////
////            if (loadedUsers == null) {
////                App.setAllUsers(new ArrayList<>());
////                return;
////            }
////
////            App.setAllUsers(loadedUsers);
////
////            for (User user : loadedUsers) {
////                UserDatabase.addUser(user);
////            }
////
////        } catch (Exception e) {
////            System.err.println("Error loading users: " + e.getMessage());
////            App.setAllUsers(new ArrayList<>());
////        }
//
//
//    public static void saveUsers() {
//        try {
//            List<User> allUsers = UserDatabase.getAllUsers();
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String json = gson.toJson(allUsers);
//            FileController.writeTextToFile(json, "src/main/resources/users.json");
//        } catch (Exception e) {
//            System.err.println("Error saving users: " + e.getMessage());
//        }
//    }
//
//    public static void saveGameState() {
//        try {
//            Map<String, Object> gameState = new HashMap<>();
//
//            gameState.put("ownerUsername", RegisterMenuController.currentUser.getUsername());
//
//            List<Map<String, Object>> playersInfo = new ArrayList<>();
//            for (Player player : GameMenuController.selectedPlayers) {
//                Map<String, Object> playerInfo = new HashMap<>();
//                playerInfo.put("username", player.getUsername());
//                playerInfo.put("energy", player.getEnergy());
//                playersInfo.add(playerInfo);
//            }
//
//            gameState.put("players", playersInfo);
//
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String json = gson.toJson(gameState);
//            FileController.writeTextToFile(json, "src/main/resources/game_state.json");
//
//        } catch (Exception e) {
//            System.err.println("Error saving game state: " + e.getMessage());
//        }
//    }
//
//    public static void loadGameState() {
//        try {
//            String json = FileController.getTextOfFile("src/main/resources/game_state.json");
//            if (json == null || json.trim().isEmpty()) return;
//
//            Gson gson = new Gson();
//            JsonObject gameState = JsonParser.parseString(json).getAsJsonObject();
//
//            String ownerUsername = gameState.get("ownerUsername").getAsString();
//            JsonArray playersArray = gameState.getAsJsonArray("players");
//
//            for (JsonElement element : playersArray) {
//                JsonObject playerObj = element.getAsJsonObject();
//                String username = playerObj.get("username").getAsString();
//                int energy = playerObj.get("energy").getAsInt();
//
//                Player player = Game.getPlayerByUsername(username);
//                if (player != null) {
//                    player.setEnergy(energy);
//                    // اگه نیاز بود لیست بازیکنان رو دوباره بسازی:
//                    if (!GameMenuController.selectedPlayers.contains(player))
//                        GameMenuController.selectedPlayers.add(player);
//                }
//            }
//
//            Game.setCurrentPlayer(Game.getPlayerByUsername(ownerUsername));
//
//        } catch (Exception e) {
//            System.err.println("Error loading game state: " + e.getMessage());
//        }
//    }
//
//
//
//    public static void loadCurrentUser() {
//        try {
//            String json = FileController.getTextOfFile(CURRENT_USER_FILE);
//            User currentUser = json != null ? gson.fromJson(json, User.class) : null;
//            App.setCurrentUser(currentUser);
//        } catch (Exception e) {
//            System.err.println("Error loading current user: " + e.getMessage());
//            App.setCurrentUser(null);
//        }
//    }
//
//    public static void saveCurrentUser() {
//        try {
//            FileController.writeTextToFile(
//                    gson.toJson(RegisterMenuController.currentUser),
//                    "src/main/resources/current_user.json"
//            );
//        } catch (Exception e) {
//            System.err.println("Error saving current user: " + e.getMessage());
//        }
//    }
//}
package org.example.controllers;

import com.google.gson.*;
import org.example.models.Game;
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
        public static void loadGameState() {
        try {
            String json = FileController.getTextOfFile("src/main/resources/game_state.json");
            if (json == null || json.trim().isEmpty()) return;

            Gson gson = new Gson();
            JsonObject gameState = JsonParser.parseString(json).getAsJsonObject();

            String ownerUsername = gameState.get("ownerUsername").getAsString();
            JsonArray playersArray = gameState.getAsJsonArray("players");

            for (JsonElement element : playersArray) {
                JsonObject playerObj = element.getAsJsonObject();
                String username = playerObj.get("username").getAsString();
                int energy = playerObj.get("energy").getAsInt();

                Player player = Game.getPlayerByUsername(username);
                if (player != null) {
                    player.setEnergy(energy);
                    // اگه نیاز بود لیست بازیکنان رو دوباره بسازی:
                    if (!GameMenuController.selectedPlayers.contains(player))
                        GameMenuController.selectedPlayers.add(player);
                }
            }

            Game.setCurrentPlayer(Game.getPlayerByUsername(ownerUsername));

        } catch (Exception e) {
            System.err.println("Error loading game state: " + e.getMessage());
        }
    }
        public static void saveGameState() {
        try {
            Map<String, Object> gameState = new HashMap<>();

            gameState.put("ownerUsername", RegisterMenuController.currentUser.getUsername());

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
            FileController.writeTextToFile(json, "src/main/resources/game_state.json");

        } catch (Exception e) {
            System.err.println("Error saving game state: " + e.getMessage());
        }
    }
}
