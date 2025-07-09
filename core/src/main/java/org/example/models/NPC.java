package org.example.models;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.example.models.Building.Building;

import java.util.*;

public class NPC {
    float x, y;
    private final String name;
    private Map<Player, Integer> friendshipPoints = new HashMap<>();
    private final List<String> favorites = new ArrayList<>();
    List<Mission> missions = new ArrayList<>();

    private final int daysToUnlockThirdQuest;
    public NPC(String name, List<String> favorites, List<Mission> missionList) {
        this.name = name;
        this.favorites.addAll(favorites);
        this.missions.addAll(missionList);
        setXandY();
        Random rand = new Random();
        daysToUnlockThirdQuest = rand.nextInt(19) + 10;
    }

    public void setXandY () {
        if (name.equalsIgnoreCase("Sebastian")){
            x = 6493.93f; y= 4108;
        }
        else if (name.equalsIgnoreCase("Abigail")) {
            x = 4470.5f; y = 4468.5f;
        }
        else  if (name.equalsIgnoreCase("Harvey")) {
            x = 6404.52f; y = 3929.28f;
        }
        else if (name.equalsIgnoreCase("Leah")) {
            x = 1129.33f; y= 3902.43f;
        }
        else if (name.equalsIgnoreCase("Robin")) {
            x = 56.41f; y = 4823;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public String getName() {
        return name;
    }

    public void initializeFriendships() {
        for (Player player : MyGame.getAllPlayers()){
            friendshipPoints.put(player, 0);
        }
    }

    public int getDaysToUnlockThirdQuest() {
        return daysToUnlockThirdQuest;
    }
    public int getNumOfUnlockedQuests(Player player){
        int i =0;
        for (Mission mission : missions) {
            if (mission.getStatus() != Mission.Status.LOCKED)
                i ++;
        }
        return i;
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
            missions.get(1).setStatus(Mission.Status.NOT_STARTED);
        }
    }
    public void unlockThirdQuest(Player player) {
        missions.get(2).setStatus(Mission.Status.NOT_STARTED);
    }
    public int getFriendshipLevel(Player player){
        return Math.min(799, (int) Math.floor(friendshipPoints.get(player)/200.0));
    }
    public int getFriendshipPoints(Player player){
        return friendshipPoints.get(player);
    }
    public Map.Entry<String, Integer> getQuest(int questIndex){
//        questIndex --;
//        int index = 0;
//        for (Map.Entry<String, Integer> entry : requests.entrySet()) {
//            if (questIndex == index) return entry;
//            index ++;
//        }
        return null;
    }
    public Map.Entry<String, Integer> finishQuest(Player player, int questIndex) {
//        questsStatus.put(questIndex, true);
//        String rewardItem = "";
//        int quantity;
//        int index = 1;
//        for (Map.Entry<String, Integer> entry : rewards.entrySet()) {
//            if (index == questIndex)  rewardItem = entry.getKey();
//            index++;
//        }
//
//        quantity = rewards.get(rewardItem);
//        if (getFriendshipLevel(player) >= 2) quantity *= 2;
//        player.getBackPack().addToInventory(new BasicItem(rewardItem, 100), quantity);
//
//
//        return new AbstractMap.SimpleEntry<>(rewardItem, quantity);
        return null;
    }
    public Map<String, Integer> getRequests() {
//        return requests;
        return null;
    }
    public void sendGift(Player player){
        Random rand = new Random();
        String giftName = favorites.get(rand.nextInt(favorites.size()));
        Item gift = MyGame.getDatabase().getItem(giftName);
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
        Mission mission = missions.get(questIndex - 1);
        return !mission.getPlayerUsername().isEmpty();
    }

    public List<Mission> getMissions() {
        return missions;
    }



}
