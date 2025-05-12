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
    private boolean isStorageUnlimited = false;

    public void setBackPackType(BackPackType type) {
        this.level = type;
        if(type == BackPackType.Deluxe) isStorageUnlimited = true;
    }
    public HashMap<Item, Integer> getInventory() {
        return inventory;
    }

    public Result addToInventory(Item item, int amount) {
        if(isStorageUnlimited) {
            if(!inventory.containsKey(item)) inventory.put(item, inventory.getOrDefault(item, 0) + amount);
            else inventory.put(item, inventory.get(item) + amount);
            return new Result(true, item.getName() + " successfully added to your inventory");

        } else {
            if (getInventoryCapacity() + amount <= level.getCapacity()) {
                if (!inventory.containsKey(item)) inventory.put(item, inventory.getOrDefault(item, 0) + amount);
                else inventory.put(item, inventory.get(item) + amount);
                return new Result(true, item.getName() + " successfully added to your inventory");
            } else {
                return new Result(false, "You're backpack is full! Upgrade to store more items");
            }
        }
    }

    public Item getFromInventory(String name) {
        for (Map.Entry<Item, Integer> entry : inventory.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(name)) return entry.getKey();
        }
        return null;
    }
    public Result removeFromInventory(Item item, int amount) {
        if(!inventory.containsKey(item)) return new Result(false, "No such item in your inventory");
        inventory.put(item, inventory.get(item) - amount);
        return new Result(true, "Successfully removed " + amount + " of " + item.getName() + " from your inventory");
    }

    public int getInventoryCapacity() {
        if(isStorageUnlimited) return level.getCapacity();
        int capacity = 0;
        for(Item i : inventory.keySet()) {
            if(inventory.get(i) > 0) {
                capacity += inventory.get(i);
            }
        }
        return capacity;
    }

    public boolean isInventoryFull() {
        if(isStorageUnlimited) return false;
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
    public Result use(Map.Entry<Integer, Integer> coordinates){
        return new Result(true, "");
    }
    @Override
    public boolean reduceEnergy(int amount){
        return true;
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