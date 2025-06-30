package org.example.models.Tool;

import org.example.models.Craft;
import org.example.models.Enums.BackPackType;
import org.example.models.Enums.CookingRecipeType;
import org.example.models.Enums.CraftType;
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
    private final ArrayList<CraftType> learntRecipes = new ArrayList<>();
    private final ArrayList<CookingRecipeType> learntCookingRecipe = new ArrayList<>();
    {
        //learntCookingRecipe.add(CookingRecipeType.FriedEgg);
        //learntCookingRecipe.add(CookingRecipeType.BakedFish);
        //learntCookingRecipe.add(CookingRecipeType.Salad);
    }
    private boolean isStorageUnlimited = true;

    public void setBackPackType(BackPackType type) {
        this.level = type;
        if(type == BackPackType.Deluxe) isStorageUnlimited = true;
    }
    public HashMap<Item, Integer> getInventory() {
        return inventory;
    }


    public boolean hasThisCraft(CraftType craft) {
        for (Item item : inventory.keySet()) {
            if (item instanceof Craft) {
                if (((Craft) item).getType() == craft)
                    return true;
            }
        }
        return false;
    }
    public Result addToInventory(Item item, int amount) {
        if(isStorageUnlimited) {
            for(Item stuff : inventory.keySet()) {
                if(stuff.getName().equals(item.getName())) {
                    inventory.put(stuff, inventory.get(stuff) + amount);
                    return new Result(true, item.getName() + " successfully added to your inventory");
                }
            }

            inventory.put(item, inventory.getOrDefault(item, 0) + amount);
            return new Result(true, item.getName() + " successfully added to your inventory");

        } else {
            if (getInventoryCapacity() + amount <= level.getCapacity()) {
                if (!inventory.containsKey(item)) inventory.put(item, inventory.getOrDefault(item, 0) + amount);
                else inventory.put(item, inventory.get(item) + amount);
                return new Result(true, "**" + amount + " of " + item.getName() + " successfully added to your inventory**");
            } else {
                return new Result(false, "Your backpack is full! Upgrade to store more items");
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
        if(inventory.get(item) - amount <=0) {
            inventory.remove(item);
            return new Result(true, "Successfully removed " + item.getName() + " from your inventory");
        }
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

    public Item getToolFromInventory(String name) {
        for(Item i : inventory.keySet()) {
            if(i.getName().equals(name)) return i;
        }
        return null;
    }

    public boolean isInventoryFull() {
        if(isStorageUnlimited) return false;
        return getInventoryCapacity() == level.getCapacity();
    }

    public void addForagingItem(Item item, int amount) {
        inventory.put(item, amount);
        foragingItems.put(item, amount);
    }

    public ArrayList<CraftType> getLearntRecipes() {
        return learntRecipes;
    }

    public void addLearntRecipe(CraftType recipe) {
        learntRecipes.add(recipe);
    }
    public ArrayList<CookingRecipeType> getLearntCookingRecipe() {
        return learntCookingRecipe;
    }
    public void addLearntCookingRecipe(CookingRecipeType recipe) {
        learntCookingRecipe.add(recipe);
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
        }
    }

}