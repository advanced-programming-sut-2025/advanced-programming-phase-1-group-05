package org.example.models;

import org.example.models.Enums.BackPackType;
import org.example.models.Skills.*;
import org.example.models.Tool.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

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
    private final HashMap<Item, Integer> inventory = new HashMap<>();
    private final TrashCan trashCan = new TrashCan();
    private final BackPack backPack = new BackPack();
    private boolean unlimitedEnergy = false;
    private Item currentItem;

    public Player(User user) {
        this.user = user;
        this.energy = 200;
        //base player tools
        inventory.put(new Hoe(), 1);
        inventory.put(new Pickaxe(), 1);
        inventory.put(new Scythe(), 1);
    }
    public void addEnergy(int amount) {
        energy += amount;
        if(energy <= 0) faint();
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

    public void addToInventory(Item item, int quantity) {
        inventory.put(item, quantity);
    }
    public HashMap<Item, Integer> getInventory() {return inventory;}
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

}
