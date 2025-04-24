package org.example.models;

import org.example.models.Enums.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private static GameMap gameMap = new GameMap();
    private static Player currentPlayer = null;
    private static ArrayList<Player> players = new ArrayList<Player>();
    private static Map<String, NPC> npcs = new HashMap<>();
    private static List<Message> messages = new ArrayList<>();
    private static Weather currentWeather = Weather.Sunny;
    private static Database database = new Database();
    // TODO a method for changing the weather
    // TODO make the game methods nonstatic
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    public static void setCurrentPlayer(Player currentPlayer) {
        Game.currentPlayer = currentPlayer;
    }
    public static void addPlayer(Player player) {
        players.add(player);
    }
    public static GameMap getGameMap() {
        return gameMap;
    }
    public static List<Player> getAllPlayers() {
        return players;
    }
    public static void startTheGame() {
        database.initializeAllItems();
        database.initializePlantDatabase();
        initializeNPCs();
    }
    public static List<NPC> getAllNPCs() {
        List<NPC> allNPCs = new ArrayList<>();
        for (Map.Entry<String, NPC> entry : npcs.entrySet()) {
            allNPCs.add(entry.getValue());
        }
        return allNPCs;
    }
    public static void initializeNPCs(){
        List<Item> favorites = new ArrayList<>();
        Map<Item, Integer> requests = new HashMap<>();
        Map<Item, Integer> rewards = new HashMap<>();

        favorites.add(database.getItem("wool"));
        favorites.add(database.getItem("pumpkinPie"));
        favorites.add(database.getItem("pizza"));
        requests.put(database.getItem("iron"), 50);
        requests.put(database.getItem("pumpkinPie"), 1);
        requests.put(database.getItem("stone"), 150);
        rewards.put(database.getItem("diamond"), 2);
        rewards.put(database.getItem("goldCoin"), 5000);
        rewards.put(database.getItem("quartz"), 50);
        npcs.put("Sebastian", new NPC("Sebastian", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();

        favorites.add(database.getItem("stone"));
        favorites.add(database.getItem("ironOre"));
        favorites.add(database.getItem("coffee"));
        requests.put(database.getItem("goldBar"), 1);
        requests.put(database.getItem("pumpkin"), 1);
        requests.put(database.getItem("wheat"), 50);
        rewards.put(database.getItem("friendshipPoints"), 1);
        rewards.put(database.getItem("goldCoin"), 500);
        rewards.put(database.getItem("AutomaticSprinkler"), 1);
        npcs.put("Abigail", new NPC("Abigail", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();


        favorites.add(database.getItem("coffee"));
        favorites.add(database.getItem("pickle"));
        favorites.add(database.getItem("wine"));
        requests.put(database.getItem("dandelion"), 12);
        requests.put(database.getItem("salmon"), 1);
        requests.put(database.getItem("wine"), 1);
        rewards.put(database.getItem("friendshipPoints"), 1);
        rewards.put(database.getItem("goldCoin"), 750);
        rewards.put(database.getItem("salad"), 5);
        npcs.put("Harvey", new NPC("Harvey", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();

        favorites.add(database.getItem("salad"));
        favorites.add(database.getItem("grape"));
        favorites.add(database.getItem("wine"));
        requests.put(database.getItem("hardWood"), 10);
        requests.put(database.getItem("salmon"), 1);
        requests.put(database.getItem("wood"), 200);
        rewards.put(database.getItem("dinnerSalmonRecipe"), 1);
        rewards.put(database.getItem("goldCoin"), 500);
        rewards.put(database.getItem("ScareCrow"), 5); // deluxe?
        npcs.put("Leah", new NPC("Leah", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();

        favorites.add(database.getItem("spaghetti"));
        favorites.add(database.getItem("wood"));
        favorites.add(database.getItem("ironBar"));
        requests.put(database.getItem("spaghetti"), 80);
        requests.put(database.getItem("ironBar"), 10);
        requests.put(database.getItem("wood"), 1000);
        rewards.put(database.getItem("beeHouse"), 3);
        rewards.put(database.getItem("goldCoin"), 1000);
        rewards.put(database.getItem("coin"), 25000);
        npcs.put("Robin", new NPC("Robin", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();
    }
    public static NPC getNPCByName(String npcName) {
        return npcs.get(npcName);
    }
    public static Weather getCurrentWeather() {
        return currentWeather;
    }
    public static Player getPlayerByUsername(String username){
        for (Player player : players){
            if (player.getUsername().equals(username)) return player;
        }
        return null;
    }
    public static Database getDatabase() {
        return database;
    }
    public static void addMessage(Message message) {
        messages.add(message);
    }
    public static List<Message> getMessages(Player player1, Player player2) {
        List<Message> commonMessages = new ArrayList<>();
        for (Message message : messages) {
            if ((message.getSender().equals(player1) && message.getReceiver().equals(player2))
            || (message.getSender().equals(player2) && message.getReceiver().equals(player1)) )
                commonMessages.add(message);
        }
        return commonMessages;
    }
}
