package org.example.models.Tool;

import org.example.models.Craft;
import org.example.models.Enums.BackPackType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Game;
import org.example.models.Item;
import org.example.models.Result;
import org.example.models.Tool.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BackPack implements Tool <BackPackType>{
    BackPackType level = BackPackType.Normal;
    private final HashMap<Item, Integer> inventory = new HashMap<>();
    private final HashMap<Item, Integer> foragingItems = new HashMap<>();
    private final ArrayList<Craft> learntRecipes = new ArrayList<>();

    public HashMap<Item, Integer> getInventory() {
        return inventory;
    }

    public void addToInventory(Item item, int amount) {
        if(getInventoryCapacity() + amount <= level.getCapacity()) {
            if(!inventory.containsKey(item)) inventory.put(item, inventory.getOrDefault(item, 0) + amount);
            else inventory.put(item, inventory.get(item) + amount);
            System.out.println(item.getName() + " successfully added to your inventory");
        } else {
            System.out.println("You're backpack is full! Upgrade to store more items");
        }
    }


    public Result removeFromInventory(Item item, int amount) {
        if(!inventory.containsKey(item)) return new Result(false, "No such item in your inventory");
        inventory.put(item, inventory.get(item) - amount);
        return new Result(true, "Successfully removed " + amount + " of " + item.getName() + " from your inventory");
    }

    public int getInventoryCapacity() {
        int capacity = 0;
        for(Item i : inventory.keySet()) {
            if(inventory.get(i) > 0) {
                capacity += inventory.get(i);
            }
        }
        return capacity;
    }

    public boolean isInventoryFull() {
        return getInventoryCapacity() == level.getCapacity();
    }

    public void addForagingItem(Item item, int amount) {
        inventory.put(item, amount);
        foragingItems.put(item, amount);
    }

    public ArrayList<Craft> getLearntRecipes() {
        return learntRecipes;
    }

    public void addLearntRecipe(Craft recipe) {
        learntRecipes.add(recipe);
    }

    @Override
    public String getName() {
        return "BackPack";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public void use(Map.Entry<Integer, Integer> coordinates){


    }
    @Override
    public void reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        Game.getCurrentPlayer().increaseEnergy(-amount);
    }
    @Override
    public BackPackType getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.nextLevel();
            System.out.println(getName() + " upgraded to " + level.getName());
        }
    }
    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {
    }
    @Override
    public Map.Entry<Integer, Integer> getCoordinates() {
        return null;
    }
}