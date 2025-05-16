package org.example.models;

import org.example.models.Building.Building;

import java.util.*;

public class NPC {
    // npc coordinates not initialized!!!!
    int x, y;
    private final String name;
    private Map<Player, Integer> friendshipPoints = new HashMap<>();
    private final List<String> favorites = new ArrayList<>();
    private Map<String, Integer> requests = new LinkedHashMap<>();
    private Map<String, Integer> rewards = new LinkedHashMap<>();
    private Map<Integer, Boolean> questsStatus = new LinkedHashMap<>();
    private Building residence; // will implement this after the building class is done
    private Map<Player, Integer> unlockedQuests = new LinkedHashMap<>();
    private final int daysToUnlockThirdQuest;
    public NPC(String name, List<String> favorites, Map<String, Integer> requests,
               Map<String, Integer> rewards) {
        this.name = name;
        this.favorites.addAll(favorites);
        this.requests.putAll(requests);
        this.rewards.putAll(rewards);
        setXandY();
        for(int i = 1; i < 4; i++){
            questsStatus.put(i, false);
        }
        Random rand = new Random();
        daysToUnlockThirdQuest = rand.nextInt(19) + 10;
        // this.residence = residence
    }

    public void setXandY () {
        if (name.equalsIgnoreCase("Sebastian")){
            x = 73; y= 46;
        }
        else if (name.equalsIgnoreCase("Abigail")) {
            x = 43; y = 19;
        }
        else  if (name.equalsIgnoreCase("Harvey")) {
            x = 72; y = 44;
        }
        else if (name.equalsIgnoreCase("Leah")) {
            x = 13; y= 39;
        }
        else if (name.equalsIgnoreCase("Robin")) {
            x = 1; y = 54;
        }
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

    public void initializeFriendships() {
        for (Player player : Game.getAllPlayers()){
            friendshipPoints.put(player, 0);
            unlockedQuests.put(player, 1);
        }
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
    public boolean isFavorite(String itemName) {
        for (String favorite : favorites ) {
            if (favorite.equalsIgnoreCase(itemName)) return true;
        }
        return false;
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
    public int getFriendshipPoints(Player player){
        return friendshipPoints.get(player);
    }
    public Map.Entry<String, Integer> getQuest(int questIndex){
        questIndex --;
        int index = 0;
        for (Map.Entry<String, Integer> entry : requests.entrySet()) {
            if (questIndex == index) return entry;
            index ++;
        }
        return null;
    }
    public Map.Entry<String, Integer> finishQuest(Player player, int questIndex) {
        questsStatus.put(questIndex, true);
        String rewardItem = "";
        int quantity;
        int index = 1;
        for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
            if (index == questIndex)  rewardItem = entry.getKey();
            index++;
        }

        quantity = rewards.get(rewardItem);
        if (getFriendshipLevel(player) >= 2) quantity *= 2;
        player.getBackPack().addToInventory(new BasicItem(rewardItem, 100), quantity);


        return new AbstractMap.SimpleEntry<>(rewardItem, quantity);
    }
    public Map<String, Integer> getRequests() {
        return requests;
    }
    public void sendGift(Player player){
        Random rand = new Random();
        String giftName = favorites.get(rand.nextInt(favorites.size()));
        Item gift = Game.getDatabase().getItem(giftName);
        player.getBackPack().addToInventory(gift, rand.nextInt(4)+1);

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