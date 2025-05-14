package org.example.models;

import org.example.controllers.GameManager;
import org.example.models.Building.AnimalHouse;
import org.example.models.Enums.BackPackType;
import org.example.models.Enums.BuildingType;
import org.example.models.Enums.EnclosureType;
import org.example.models.Skills.*;
import org.example.models.Tool.*;

import java.util.*;

public class Player {
    private final User user;
    private Player spouse;
    private int x, y;
    private WateringCan wateringCan = new WateringCan();
    private final Farming farmingSkill = new Farming();
    private int energy;
    private int gold;
    private final AnimalCare animalCare = new AnimalCare();
    private final Crafting craftingSkill = new Crafting();
    private final Cooking cookingSkill = new Cooking();
    private final Fishing fishingSkill = new Fishing();
    private final Mining miningSkill = new Mining();
    private final Foraging foragingSkill = new Foraging();
    private final TrashCan trashCan = new TrashCan();
    private final BackPack backPack = new BackPack();
    private boolean unlimitedEnergy = false;
    private Item currentItem;
    private static final List<Friendship> friendships = new ArrayList<>();
    private int proposalRejectionDaysLeft = 0;
    private Farm farm;
    private SharedWallet sharedWallet = null;
    private Map.Entry<Integer, Integer> coordinates;
    private List<AnimalHouse> coopAndBarns = new ArrayList<>();
    private List<String> notifications = new ArrayList<>();
    private static int mapNum;

    public void setMapNum(int mapNum) {
        this.mapNum = mapNum;
    }

    public int getMapNum() {
        return this.mapNum;
    }

    public Player(User user) {
        this.user = user;
        this.energy = 200;
        //base player tools
        backPack.getInventory().put(new Hoe(), 1);
        backPack.getInventory().put(new Pickaxe(), 1);
        backPack.getInventory().put(new Scythe(), 1);
    }

    public void setFarm(int startX, int startY) {
        this.farm = new Farm(this, startX, startY);
    }
    public void addNotification(String message) {
        notifications.add(message);
    }

    public String getNotifications () {
        StringBuilder builder = new StringBuilder();
        builder.append("\n Your notifications: ");
        for (String notification : notifications) {
            builder.append(notification).append("\n");
        }
        notifications.clear();
        return builder.toString();
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addAnimalHouse(AnimalHouse animalHouse) {
        coopAndBarns.add(animalHouse);
    }

    public String getGender() {
        return user.gender;
    }

    public void faint() {
        //TODO use time controller
        TimeAndDate timeAndDate = new TimeAndDate();
        timeAndDate.advanceDay();
        //TODO waking up in the same spot // wait for map mechanism
        energy = energy * 3 / 2;
    }

    public void increaseEnergy(int amount) {
        if (amount < 0 && unlimitedEnergy) return;
        energy += amount;
        if (energy >= 200) energy = 200;
        if (energy <= 0) faint();
    }

    //reset energy every day
    public void resetEnergy() {
        energy = 200;
    }


    public AnimalHouse hasThisEnclosureType(BuildingType enclosureType) {
        for (AnimalHouse animalHouse : coopAndBarns) {
            if (animalHouse.getType().equals(enclosureType) &&
                    animalHouse.getAnimals().size() < animalHouse.getCapacity()) {
                return animalHouse;
            }
        }
        return null;
    }


    public boolean isEnergyUnlimited() {
        return unlimitedEnergy;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean hasEnoughEnergy(int required) {
        return false;
    }

    public Result waterTile(GameTile tile) {
        return null;
    }

    public Crafting getCraftingSkill() {
        return craftingSkill;
    }

    public Cooking getCookingSkill() {
        return cookingSkill;
    }

    public Farming getFarmingSkill() {
        return farmingSkill;
    }

    public Foraging getForagingSkill() {
        return foragingSkill;
    }

    public Fishing getFishingSkill() {
        return fishingSkill;
    }

    public Mining getMiningSkill() {
        return miningSkill;
    }

    public Map.Entry<Integer, Integer> getCoordinate() {
        return new AbstractMap.SimpleEntry<>(x, y);
    }

    public void setCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getItemQuantity(Item item) {
        return backPack.getInventory().getOrDefault(item, 0);
    }

    public void addGold(int amount) {
        if (sharedWallet == null)
            gold += amount;
        else sharedWallet.addGold(amount);
    }

    public int getGold() {
        if (sharedWallet == null)
            return gold;
        return sharedWallet.getGold();
    }

    public void setUnlimitedEnergy() {
        unlimitedEnergy = true;
    }

    public void setCurrentItem(Item item) {
        currentItem = item;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public AnimalCare getAnimalCare() {
        return animalCare;
    }

    public TrashCan getTrashCan() {
        return trashCan;
    }

    public BackPack getBackPack() {
        return backPack;
    }

    public String getName() {
        return user.nickName;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public WateringCan getWateringCan() {
        return wateringCan;
    }

    public List<AnimalHouse> getCoopsAndBarns() {
        return coopAndBarns;
    }

    public static void initializeFriendships(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player a = players.get(i);
                Player b = players.get(j);
                friendships.add(new Friendship(a, b));
            }
        }
    }

    private Friendship getFriendship(Player a, Player b) {
        for (Friendship f : friendships) {
            if (f.containsPlayer(a) && f.containsPlayer(b)) return f;
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

    public void setSpouse(Player spouse) {
        this.spouse = spouse;
        farm.addOwner(spouse);
        SharedWallet wallet = new SharedWallet(this.gold, spouse.gold);
        this.sharedWallet = wallet;
        spouse.sharedWallet = wallet;
    }

    public boolean isMarriedTo(Player a) {
        if (spouse == null) return false;
        return spouse.equals(a);
    }

    public boolean canAskMarriage(Player a) {
        Friendship friendship = getFriendship(a, this);
        if (friendship != null) return friendship.canAskMarriage();
        return false;
    }

    public void changeFriendshipXP(int xp, Player a) {
        Friendship friendship = getFriendship(this, a);
        if (friendship != null) friendship.addPoints(xp);
    }

    public int getProposalRejectionDaysLeft() {
        return proposalRejectionDaysLeft;
    }

    public void setProposalRejectionDaysLeft(int proposalRejectionDaysLeft) {
        this.proposalRejectionDaysLeft = proposalRejectionDaysLeft;
    }

    public void proposed(Player player) {
        Friendship friendship = getFriendship(this, player);
        if (friendship != null) friendship.Proposed();
    }

    public void decrementProposalRejectionDaysLeft() {
        proposalRejectionDaysLeft--;
    }

    public boolean hasProposed(Player player) {
        Friendship friendship = getFriendship(this, player);
        if (friendship == null) return false;
        return friendship.hasProposed;
    }

    public Farm getFarm() {
        return farm;
    }

    public Animal getAnimal(String name) {
        Animal animal = null;
        for (AnimalHouse house : coopAndBarns) {
            animal = house.getAnimal(name);
            if (animal != null) break;
        }
        return animal;
    }

    public void removeAnimal(Animal animal) {
        for (AnimalHouse house : coopAndBarns) {
            if (house.contains(animal)) {
                house.removeAnimal(animal);
                break;
            }
        }
    }

    private static class Friendship {
        private final Player player1;
        private final Player player2;
        private int xpPoints = 0;
        private int friendshipLevel = 0;
        private boolean hasProposed = false;

        public Friendship(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public boolean containsPlayer(Player a) {
            return player1.equals(a) && player2.equals(a);
        }

        public int getFriendshipLevel() {
            return friendshipLevel;
        }

        public void addPoints(int amount) {
            xpPoints += amount;
            if (xpPoints > 300) friendshipLevel = 2;
            else if (xpPoints > 100) friendshipLevel = 1;
        }

        public boolean canHug() {
            return friendshipLevel >= 2;
        }

        public void setLevel(int level) {
            friendshipLevel = level;
        }

        public boolean canGiveBouquet() {
            return xpPoints >= 600;
        }

        public boolean canGift() {
            return friendshipLevel >= 1;
        }

        public boolean canAskMarriage() {
            return xpPoints >= 1000;
        }

        public void Proposed() {
            hasProposed = true;
        }

        public boolean HasProposed() {
            return hasProposed;
        }
    }

    private static class SharedWallet {
        private int gold;

        public SharedWallet(int gold1, int gold2) {
            gold = gold1 + gold2;
        }

        public int getGold() {
            return gold;
        }

        public void addGold(int amount) {
            gold += amount;
        }

    }
}
