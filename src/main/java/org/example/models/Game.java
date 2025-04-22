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
    private static List<Item> items = new ArrayList<>();
    private static Weather currentWeather = Weather.Sunny;
    // a method for changing the weather

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
        initializeItems();
        initializeNPCs();
    }
    public static void initializeItems(){
        items.add(new basicItem("friendshipPoints", 0));
        // this item will only be used as a gift for incrementing the friendship points between two players or an npc
        //can't be bought or added to inventory
    }
    public static void initializeNPCs(){
        List<Item> favorites = new ArrayList<>();
        Map<Item, Integer> requests = new HashMap<>();
        Map<Item, Integer> rewards = new HashMap<>();

        favorites.add(getItemByName("wool"));
        favorites.add(getItemByName("pumpkinPie"));
        favorites.add(getItemByName("pizza"));
        requests.put(getItemByName("iron"), 50);
        requests.put(getItemByName("pumpkinPie"), 1);
        requests.put(getItemByName("stone"), 150);
        rewards.put(getItemByName("diamond"), 2);
        rewards.put(getItemByName("goldCoin"), 5000);
        rewards.put(getItemByName("quartz"), 50);
        npcs.put("Sebastian", new NPC("Sebastian", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();

        favorites.add(getItemByName("stone"));
        favorites.add(getItemByName("ironOre"));
        favorites.add(getItemByName("coffee"));
        requests.put(getItemByName("goldBar"), 1);
        requests.put(getItemByName("pumpkin"), 1);
        requests.put(getItemByName("wheat"), 50);
        rewards.put(getItemByName("friendshipPoints"), 1);
        rewards.put(getItemByName("goldCoin"), 500);
        rewards.put(getItemByName("AutomaticSprinkler"), 1);
        npcs.put("Abigail", new NPC("Abigail", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();


        favorites.add(getItemByName("coffee"));
        favorites.add(getItemByName("pickle"));
        favorites.add(getItemByName("wine"));
        requests.put(getItemByName("dandelion"), 12);
        requests.put(getItemByName("salmon"), 1);
        requests.put(getItemByName("wine"), 1);
        rewards.put(getItemByName("friendshipPoints"), 1);
        rewards.put(getItemByName("goldCoin"), 750);
        rewards.put(getItemByName("salad"), 5);
        npcs.put("Harvey", new NPC("Harvey", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();

        favorites.add(getItemByName("salad"));
        favorites.add(getItemByName("grape"));
        favorites.add(getItemByName("wine"));
        requests.put(getItemByName("hardWood"), 10);
        requests.put(getItemByName("salmon"), 1);
        requests.put(getItemByName("wood"), 200);
        rewards.put(getItemByName("dinnerSalmonRecipe"), 1);
        rewards.put(getItemByName("goldCoin"), 500);
        rewards.put(getItemByName("ScareCrow"), 5); // deluxe?
        npcs.put("Leah", new NPC("Leah", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();

        favorites.add(getItemByName("spaghetti"));
        favorites.add(getItemByName("wood"));
        favorites.add(getItemByName("ironBar"));
        requests.put(getItemByName("spaghetti"), 80);
        requests.put(getItemByName("ironBar"), 10);
        requests.put(getItemByName("wood"), 1000);
        rewards.put(getItemByName("beeHouse"), 3);
        rewards.put(getItemByName("goldCoin"), 1000);
        rewards.put(getItemByName("coin"), 25000);
        npcs.put("Robin", new NPC("Robin", favorites, requests, rewards));
        favorites.clear(); requests.clear(); rewards.clear();
    }
    public static Item getItemByName(String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }
    public static NPC getNPCByName(String npcName) {
        return npcs.get(npcName);
    }
    public static Weather getCurrentWeather() {
        return currentWeather;
    }

}
