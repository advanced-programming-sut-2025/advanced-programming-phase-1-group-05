package models;

import models.Skills.*;
//import src.models.Tool.TrashCan;
//import models.Tool.WateringCan;

import java.util.*;

public class Player {
    private User user;
    private int x, y;
//    private WateringCan wateringCan;
//    private Farming farmingSkill;
    private int energy;
    private int gold;
//    private final Crafting craftingSkill = new Crafting();
//    private final Cooking cookingSkill = new Cooking();
//    private final Fishing fishingSkill = new Fishing();
//    private final Foraging foragingSkill = new Foraging();
//    private final List<Item> inventory = new ArrayList<>();
//    private final TrashCan trashCan = new TrashCan();

    public void decreaseEnergy(int amount) {
        if(energy <= 0) faint();
    }

    public void faint() {
        //skip the rest of the day
        //set current coordinate
        energy *= 0.75;
    }

    public void increaseEnergy(int amount) {
        energy += amount;
        if(energy >= 200) energy = 200;
    }

    public boolean hasEnoughEnergy(int required) {
        return false;
    }
//    public Result waterTile(GameTile tile) {return null;}
//    public Crafting getCraftingSkill() {return craftingSkill;}
//    public Cooking getCookingSkill() {return cookingSkill;}
//    public Farming getFarmingSkill() {return farmingSkill;}
//    public Foraging getForagingSkill() {return foragingSkill;}
//    public Fishing getFishingSkill() {return fishingSkill;}
    public Map.Entry<Integer, Integer> getCoordinate() {
        return new AbstractMap.SimpleEntry<>(x, y);
    }
    public void setCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

//    public void addToInventory(Item item) {
//        inventory.add(item);
//    }
//    public List<Item> getInventory() {return inventory;}
    public void addGold(int amount) {gold += amount;}
    public int getGold() {return gold;}


}
