package org.example.models.Tool;

import org.example.models.Enums.BackPackType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Game;
import org.example.models.Item;
import org.example.models.Tool.Tool;

import java.util.HashMap;
import java.util.Map;

public class BackPack implements Tool <BackPackType>{
    BackPackType level = BackPackType.Normal;
    private final HashMap<Item, Integer> inventory = new HashMap<>();
    private final HashMap<Item, Integer> foragingItems = new HashMap<>();

    public HashMap<Item, Integer> getInventory() {
        return inventory;
    }

    public void addToInventory(Item item, int amount) {
        if(getInventoryCapacity() + amount <= level.getCapacity()) {
            inventory.put(item, inventory.getOrDefault(item, 0) + amount);
            System.out.println(item.getName() + " successfully added to your inventory");
        } else {
            System.out.println("You're backpack is full! Upgrade to store more items");
        }
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

    public void addForagingItem(Item item, int amount) {
        inventory.put(item, amount);
        foragingItems.put(item, amount);
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
        Game.getCurrentPlayer().addEnergy(-amount);
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
}