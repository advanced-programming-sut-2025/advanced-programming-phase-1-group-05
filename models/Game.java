package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private static Player currentPlayer = null;
    private static ArrayList<Player> players = new ArrayList<Player>();
    private static List<NPC> npcs = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    public static void setCurrentPlayer(Player currentPlayer) {
        Game.currentPlayer = currentPlayer;
    }
    public static void addPlayer(Player player) {
        players.add(player);
    }
    public static List<Player> getAllPlayers() {
        return players;
    }
    public static void startTheGame() {
        initializeNPCs();
    }
    public static void initializeNPCs(){
        List<Item> favorites = new ArrayList<>();
        Map<Item, Integer> requests = new HashMap<>();
        Map<Item, Integer> rewards = new HashMap<>();

        favorites.add(getItemByName("wool"));
        favorites.add(getItemByName("pumpkinPie"));
        favorites.add(getItemByName("pizza"));
        requests.put(getItemByName("iron"), 50);
        requests.put(getItemByName("pumpkin"), 1);
        requests.put(getItemByName("stone"), 150);
        rewards.put(getItemByName("diamond"), 2);
        rewards.put(getItemByName("goldCoin"), 5000);
        rewards.put(getItemByName("quartz"), 50);
        npcs.add(new NPC("Sebastian", favorites, requests, rewards));
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

}
