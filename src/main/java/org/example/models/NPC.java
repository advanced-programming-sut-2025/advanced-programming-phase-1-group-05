package org.example.models;

import org.example.models.Building.Building;

import java.util.*;

public class NPC {
    // npc coordinates not initialized!!!!
    int x, y;
    private final String name;
    private Map<Player, Integer> friendshipPoints = new HashMap<>();
    private final List<Item> favorites = new ArrayList<>();
    private Map<Item, Integer> requests = new LinkedHashMap<>();
    private Map<Item, Integer> rewards = new LinkedHashMap<>();
    private Map<Integer, Boolean> questsStatus = new LinkedHashMap<>();
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
        for(int i = 1; i < 4; i++){
            questsStatus.put(i, false);
        }
        Random rand = new Random();
        daysToUnlockThirdQuest = rand.nextInt(10, 29);
        // this.residence = residence
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
    public Map.Entry<Item, Integer> getQuest(int questIndex){
        questIndex --;
        int index = 0;
        for (Map.Entry<Item, Integer> entry : requests.entrySet()) {
            if (questIndex == index) return entry;
            index ++;
        }
        return null;
    }
    public Map.Entry<Item, Integer> finishQuest(Player player, int questIndex) {
        questsStatus.put(questIndex, true);
        Item rewardItem = null;
        int quantity = 0;
        for (Map.Entry<Item, Integer> entry : rewards.entrySet()) {
            rewardItem = entry.getKey();
            break;
        }
        if (rewardItem != null) {
            quantity = rewards.get(rewardItem);
            if (getFriendshipLevel(player) >= 2) quantity *= 2;
            player.addToInventory(rewardItem, quantity);
            rewards.remove(rewardItem);
        }
        return new AbstractMap.SimpleEntry<>(rewardItem, quantity);
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
    public boolean isCompleted(int questIndex){
        return questsStatus.get(questIndex);
    }
}