package org.example.models;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    ArrayList<FruitAndVegetable> plantDatabase = new ArrayList<>();
    ArrayList<Tree> treeDatabase = new ArrayList<>();
    ArrayList<Item> itemDatabase = new ArrayList<>();
    ArrayList<Seed> seedDatabase = new ArrayList<>();
    ArrayList<Craft> craftingRecipeDatabase = new ArrayList<>();
    ArrayList<Food> foodDatabase = new ArrayList<>();
    Map<String, NPC> NPCs = new HashMap<>();

    //start when the game starts
    //put this somewhere where everything is initialized
    public void initializePlantDatabase(){
        try {
            Gson gson = new Gson();

            FileReader reader = new FileReader("plants.json");

            Type plantListType = new TypeToken<ArrayList<FruitAndVegetable>>(){}.getType();

            ArrayList<FruitAndVegetable> plantList = gson.fromJson(reader, plantListType);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public FruitAndVegetable getFruitAndVegetable(String name){
        FruitAndVegetable plant = null;
        for(FruitAndVegetable f : plantDatabase){
            if(f.getName().equals(name)){
                plant = f;
            }
        }
        return plant;
    }

    public ArrayList<FruitAndVegetable> getFruitAndVegetables(){
        return plantDatabase;
    }

    public void initializeAllItems(){
        itemDatabase.add(new basicItem("friendshipPoints", 0));
        // this item will only be used as a gift for incrementing the friendship points between two players or an npc
        //can't be bought or added to inventory
    }

    public Item getItem(String name){
        for(Item i : itemDatabase){
            if(i.getName().equals(name)){
                return i;
            }
        }
        return null;
    }
    public ArrayList<Tree> getTreeDatabase(){
        return treeDatabase;
    }
    public ArrayList<Seed> getSeedDatabase(){
        return seedDatabase;
    }


    public ArrayList<Craft> getCraftingRecipeDatabase(){
        return craftingRecipeDatabase;
    }
    public ArrayList<Item> getItemDatabase(){
        return itemDatabase;
    }

    public void loadNPCs() {
        String json;
        try {
             json = new String(Files.readAllBytes(Paths.get("npcInfos.json")));
        }
        catch (IOException e){
            System.out.println("can't get json.");
            return;
        }
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray npcsArray = jsonObject.getAsJsonArray("npcs");

        for (JsonElement npcElement : npcsArray) {
            JsonObject npcObject = npcElement.getAsJsonObject();

            String name = npcObject.get("name").getAsString();
            JsonArray favoritesArray = npcObject.getAsJsonArray("favorites");
            JsonObject requestsObject = npcObject.getAsJsonObject("requests");
            JsonObject rewardsObject = npcObject.getAsJsonObject("rewards");

            List<Item> favorites = new ArrayList<>();
            for (JsonElement favoriteElement : favoritesArray) {
                favorites.add(getItem(favoriteElement.getAsString()));
            }

            Map<Item, Integer> requests = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : requestsObject.entrySet()) {
                Item item = getItem(entry.getKey());
                int quantity = entry.getValue().getAsInt();
                requests.put(item, quantity);
            }

            Map<Item, Integer> rewards = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : rewardsObject.entrySet()) {
                Item item = getItem(entry.getKey());
                int quantity = entry.getValue().getAsInt();
                rewards.put(item, quantity);
            }

            NPC npc = new NPC(name, favorites, requests, rewards);
            NPCs.put(name, npc);
        }
    }
    public Map<String, NPC> getNPCs(){
        return NPCs;
    }
    public ArrayList<Food> getFoodDatabase(){
        return foodDatabase;
    }
}