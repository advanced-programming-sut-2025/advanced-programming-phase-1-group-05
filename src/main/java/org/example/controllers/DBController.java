package org.example.controllers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.models.*;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBController {
    private static final String USERS_FILE = "src/main/resources/saveusers.json";
    private static final String CURRENT_USER_FILE = "src/main/resources/current_user.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

//    public static void loadUsers() {
//        try {
//            String json = FileController.getTextOfFile(USERS_FILE);
//
//            if (json == null || json.trim().isEmpty()) {
//                App.setAllUsers(new ArrayList<>());
//                return;
//            }
//
//            ArrayList<User> loadedUsers = gson.fromJson(json, new TypeToken<ArrayList<User>>(){}.getType());
//
//            if (loadedUsers == null) {
//                App.setAllUsers(new ArrayList<>());
//                return;
//            }
//
//            App.setAllUsers(loadedUsers);
//
//            for (User user : loadedUsers) {
//                UserDatabase.addUser(user);
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error loading users: " + e.getMessage());
//            App.setAllUsers(new ArrayList<>());
//        }

    public static void loadUsers() {
        try {
            // ایجاد فایل اگر وجود ندارد
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // ذخیره آرایه خالی
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
                return;
            }

            String json = FileController.getTextOfFile(USERS_FILE);

            // اگر فایل خالی است با آرایه خالی شروع کن
            if (json == null || json.trim().isEmpty()) {
                json = "[]";
            }

            // پارس کردن JSON به صورت قطعی
            JsonElement element = JsonParser.parseString(json);
            JsonArray usersArray;

            if (element.isJsonArray()) {
                usersArray = element.getAsJsonArray();
            } else {
                // اگر آبجکت است، آن را به آرایه تبدیل کن
                usersArray = new JsonArray();
                if (element.isJsonObject()) {
                    usersArray.add(element);
                }
                // فایل را با فرمت صحیح ذخیره کن
                try (FileWriter writer = new FileWriter(USERS_FILE)) {
                    gson.toJson(usersArray, writer);
                }
            }

            // تبدیل به لیست کاربران
            List<User> loadedUsers = new ArrayList<>();
            for (JsonElement userElement : usersArray) {
                User user = gson.fromJson(userElement, User.class);
                loadedUsers.add(user);
            }

            // بارگذاری در UserDatabase
            UserDatabase.clear();
            for (User user : loadedUsers) {
                UserDatabase.addUser(user);
            }

        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            UserDatabase.clear();
        }
    }

//    public static void saveUsers() {
//        try {
//            List<User> allUsers = UserDatabase.getAllUsers();
//            System.out.println("Saving " + allUsers.size() + " users:");
//            for (User user : allUsers) {
//                System.out.println(" - " + user.getUsername());
//            }
//
//            File file = new File(USERS_FILE);
//            file.getParentFile().mkdirs();
//
//            // همیشه به صورت آرایه ذخیره کن
//            try (FileWriter writer = new FileWriter(file)) {
//                gson.toJson(allUsers, writer);
//            }
//        } catch (Exception e) {
//            System.err.println("Error saving users: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
    public static void saveUsers() {
        try {
            List<User> allUsers = UserDatabase.getAllUsers();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(allUsers);
            FileController.writeTextToFile(json, "src/main/resources/users.json");
        } catch (Exception e) {
            System.err.println("Error saving users: " + e.getMessage());
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

            GameMenuController.currentPlayer = Game.getPlayerByUsername(ownerUsername);

        } catch (Exception e) {
            System.err.println("Error loading game state: " + e.getMessage());
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
                    gson.toJson(RegisterMenuController.currentUser),
                    "src/main/resources/current_user.json"
            );
        } catch (Exception e) {
            System.err.println("Error saving current user: " + e.getMessage());
        }
    }
}
