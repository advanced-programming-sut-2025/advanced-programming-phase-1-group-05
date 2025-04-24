package org.example.models;

import org.example.models.Enums.BackPackType;
import org.example.models.Skills.*;
import org.example.models.Tool.*;

import java.util.*;

public class Player {
    private User user;
    private int x, y;
    private WateringCan wateringCan = new WateringCan();
    private Farming farmingSkill;
    private int energy;
    private int gold;
    private final AnimalCare animalCare = new AnimalCare();
    private final Crafting craftingSkill = new Crafting();
    private final Cooking cookingSkill = new Cooking();
    private final Fishing fishingSkill = new Fishing();
    private final Foraging foragingSkill = new Foraging();
    private final TrashCan trashCan = new TrashCan();
    private final BackPack backPack = new BackPack();
    private boolean unlimitedEnergy = false;
    private Item currentItem;
    private static List<Friendship> friendships = new ArrayList<>();

    public Player(User user) {
        this.user = user;
        this.energy = 200;
        //base player tools
        backPack.getInventory().put(new Hoe(), 1);
        backPack.getInventory().put(new Pickaxe(), 1);
        backPack.getInventory().put(new Scythe(), 1);
    }
    public void addEnergy(int amount) {
        energy += amount;
        if(energy <= 0) faint();
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getGender(){
        return user.gender;
    }
    public void faint() {
        //skip the rest of the day
        //set current coordinate
        //waiting for time functionality
        energy *= 0.75;
    }

    public void increaseEnergy(int amount) {
        if(amount < 0 && unlimitedEnergy) return;
        energy += amount;
        if(energy >= 200) energy = 200;
    }
    public int getEnergy() {return energy;}
    public boolean hasEnoughEnergy(int required) {
        return false;
    }
    public Result waterTile(GameTile tile) {return null;}
    public Crafting getCraftingSkill() {return craftingSkill;}
    public Cooking getCookingSkill() {return cookingSkill;}
    public Farming getFarmingSkill() {return farmingSkill;}
    public Foraging getForagingSkill() {return foragingSkill;}
    public Fishing getFishingSkill() {return fishingSkill;}
    public Map.Entry<Integer, Integer> getCoordinate() {
        return new AbstractMap.SimpleEntry<>(x, y);
    }
    public void setCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getItemQuantity(Item item) {
        return backPack.getInventory().get(item);
    }
    public void addGold(int amount) {gold += amount;}
    public int getGold() {return gold;}
    public void setUnlimitedEnergy() {
        unlimitedEnergy = true;
    }
    public void setCurrentItem(Item item) {
        currentItem = item;
    }
    public Item getCurrentItem() {return currentItem;}
    public AnimalCare getAnimalCare() {return animalCare;}
    public TrashCan getTrashCan() {return trashCan;}
    public BackPack getBackPack() {return backPack;}

    public String getName() {
        return user.nickName;
    }
    public String getUsername(){
        return  user.getUsername();
    }
    public WateringCan getWateringCan() {return wateringCan;}

//    private Friendship getFriendship(Player otherPlayer){
//        return friendships.computeIfAbsent(otherPlayer.getUsername(), k -> new Friendship(this, otherPlayer));
//    }

    public static void initializeFriendships(List<Player> players){
        for (int i = 0; i< players.size(); i++){
            for (int j = i +1; j < players.size(); j++){
                Player a = players.get(i);
                Player b = players.get(j);
                friendships.add(new Friendship(a, b));
            }
        }
    }

    private Friendship getFriendship(Player a, Player b) {
        for (Friendship f : friendships) {
            if(f.containsPlayer(a) && f.containsPlayer(b)) return f;
        }
        return null;
    }
    public int getFriendshipLevel(Player a) {
        Friendship friendship = getFriendship(this, a);
        if (friendship == null) return 0;
        return friendship.getFriendshipLevel();
    }

    public void changeLevel(Player a, int level) {
        Friendship friendship = getFriendship(a, this);
        if (friendship != null)
            friendship.setLevel(level);
    }

    public boolean canAskMarriage(Player a){
        Friendship friendship = getFriendship(a, this);
        if( friendship != null ) return friendship.canAskMarriage();
        return false;
    }
    public void changeFriendshipXP(int xp, Player a){
        Friendship friendship = getFriendship(this, a);
        if (friendship != null) friendship.addPoints(xp);
    }
    private static class Friendship {
        private Player player1;
        private Player player2;
        private int xpPoints = 0;
        private int friendshipLevel = 0;
        private boolean bouquetGifted = false;
        public Friendship(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public boolean containsPlayer(Player a){
            return  player1.equals(a) && player2.equals(a);
        }

        public int getFriendshipLevel(){
            return friendshipLevel;
        }

        public void addPoints(int amount){
            xpPoints += amount;
            if (xpPoints > 300) friendshipLevel = 2;
            else if (xpPoints > 100) friendshipLevel = 1;
        }
        public boolean canHug(){
            return friendshipLevel >= 2;
        }

        public void setLevel(int level){
            friendshipLevel = level;
        }

        public boolean canGiftFlower(){
            return xpPoints >= 600;
        }

        public boolean canAskMarriage(){
            return xpPoints >= 1000;
        }
    }
}
