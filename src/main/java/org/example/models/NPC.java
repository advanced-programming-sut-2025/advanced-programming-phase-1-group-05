package org.example.models;

import org.example.models.Building.Building;

import java.util.*;

public class NPC {
    private final String name;
    private Map<Player, Integer> friendshipPoints = new HashMap<>();
    private final List<Item> favorites = new ArrayList<>();
    private Map<Item, Integer> requests = new LinkedHashMap<>();
    private Map<Item, Integer> rewards = new LinkedHashMap<>();
    private Building residence; // will implement this after the building class is done
    private Map<Player, Integer> unlockedQuests = new LinkedHashMap<>();
    private final int daysToUnlockThirdQuest;
    public NPC(String name, List<Item> favorites, Map<Item, Integer> requests,
               Map<Item, Integer> rewards) {
        this.name = name;
        this.favorites.addAll(favorites);
        this.requests.putAll(requests);
        this.rewards.putAll(rewards);
        for (Player player : Game.getAllPlayers()){
            friendshipPoints.put(player, 0);
            unlockedQuests.put(player, 1);
        }
        Random rand = new Random();
        daysToUnlockThirdQuest = rand.nextInt(10, 29);
        // this.residence = residence
    }

    public String getName() {
        return name;
    }

    public int getDaysToUnlockThirdQuest() {
        return daysToUnlockThirdQuest;
    }
    public int getNumOfUnlockedQuests(Player player){
        return unlockedQuests.get(player);
    }
    public void recieveGift(Item gift, Player player) {
        // this method will check if the gift is a favorite
        // and will add to friendshipLevel of the player accordingly
    }
    public boolean isFavorite(Item item) {
        return favorites.contains(item);
    }
    public void addFriendShipPoints(Player player, int points) {
        friendshipPoints.merge(player, points, Integer::sum);
        if (getFriendshipLevel(player) == 1){
            unlockedQuests.put(player, 2);
        }
    }
    public void unlockThirdQuest(Player player) {
        unlockedQuests.put(player, 3);
    }
    public int getFriendshipLevel(Player player){
        return Math.min(799, (int) Math.floor(friendshipPoints.get(player)/200.0));
    }
    public void finishQuest(Player player, Item item) {



        Random rand = new Random();
        int index = 0;
        Item rewardItem = null;
        for (Map.Entry<Item, Integer> entry : rewards.entrySet()) {
            rewardItem = entry.getKey();
            break;
        }
        if (rewardItem != null) {
            int quantity = rewards.get(rewardItem);
            if (getFriendshipLevel(player) >= 2) quantity *= 2;
            player.addToInventory(rewardItem, quantity);
            System.out.println("You got " + quantity+ " " + rewardItem.getName() + "(s) from " + name + " for completing this quest.");
            rewards.remove(rewardItem);
        }

    }
    public Map<Item, Integer> getRequests() {
        return requests;
    }
    public void sendGift(Player player){
        Random rand = new Random();
        Item gift = favorites.get(rand.nextInt(favorites.size()));
        player.addToInventory(gift, rand.nextInt(1, 5));

        int messageNumber = rand.nextInt(3);
        if (messageNumber == 0){
            System.out.println("Hey " + player.getName() + "! I saw this " + gift.getName() + " and thought of you. Hope it makes your day sweeter. -" + name);
        }
        else if (messageNumber == 1) {
            System.out.println("Thought you could use a little boost today "+ player.getName() +". Enjoy this "+ gift.getName() + ". -" + name);
        }
        else {
            System.out.println("The weather reminded me of you today, "+ player.getName() +". Don't ask why. Just enjoy this " + gift.getName() + ". -" + name);
        }

    }
}