package org.example.models;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.models.Enums.BuildingType;
import org.example.models.Enums.CropType;
import org.example.models.Enums.Season;
import org.example.models.BasicItem;
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
    ArrayList<Item> itemDatabase = new ArrayList<>();
    ArrayList<Craft> craftingRecipeDatabase = new ArrayList<>();
    List<Store> stores = new ArrayList<>();
    Map<String, NPC> NPCs = new HashMap<>();

    //start when the game starts
    //put this somewhere where everything is initialized
    public void initializePlantDatabase() {
        try {
            Gson gson = new Gson();

            FileReader reader = new FileReader("plants.json");

            Type plantListType = new TypeToken<ArrayList<FruitAndVegetable>>() {
            }.getType();

            ArrayList<FruitAndVegetable> plantList = gson.fromJson(reader, plantListType);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public FruitAndVegetable getFruitAndVegetable(String name) {
        FruitAndVegetable plant = null;
        return plant;
    }


    public void initializeStoresAndItems() {
        itemDatabase.add(new BasicItem("friendshipPoints", 0));
        itemDatabase.add(new BasicItem("Egg", 50));
        itemDatabase.add(new BasicItem("Large egg", 95));
        itemDatabase.add(new BasicItem("Duck egg", 95));
        itemDatabase.add(new BasicItem("Duck feather", 50));
        itemDatabase.add(new BasicItem("Wool", 340));
        itemDatabase.add(new BasicItem("Rabbit's foot", 565));
        itemDatabase.add(new BasicItem("Dinosaur egg", 350));
        itemDatabase.add(new BasicItem("Milk", 125));
        itemDatabase.add(new BasicItem("Large milk", 190));
        itemDatabase.add(new BasicItem("Goat milk", 225));
        itemDatabase.add(new BasicItem("Large goat milk", 345));
        itemDatabase.add(new BasicItem("Wool", 340));
        itemDatabase.add(new BasicItem("Truffle", 625));
        String json;
        try {
            json = new String(Files.readAllBytes(Paths.get("stores.json")));
        } catch (IOException e) {
            System.out.println("can't get json.");
            return;
        }
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray storesArray = jsonObject.get("stores").getAsJsonArray();
        for (JsonElement store : storesArray) {
            JsonObject storeObject = store.getAsJsonObject();
            String storeName = storeObject.get("name").getAsString();
            int xStart = storeObject.get("xStart").getAsInt();
            int xEnd = storeObject.get("xEnd").getAsInt();
            int yStart = storeObject.get("yStart").getAsInt();
            int yEnd = storeObject.get("yEnd").getAsInt();
            int openingTime = storeObject.get("openingTime").getAsInt();
            int closingTime = storeObject.get("closingTime").getAsInt();
            List<Product> products = new ArrayList<>();
            JsonArray storeProducts = storeObject.get("products").getAsJsonArray();
            for (JsonElement storeProduct : storeProducts) {
                JsonObject storeProductObject = storeProduct.getAsJsonObject();
                String productName = storeProductObject.get("name").getAsString();
                int price = storeProductObject.get("price").getAsInt();
                int limit = storeProductObject.get("limit").getAsInt();
                String buildingType = storeProductObject.get("buildingType").getAsString();
                JsonArray seasons = storeProductObject.get("seasons").getAsJsonArray();
                Map<Item, Integer> costs = new HashMap<>();
                List<Season> seasonsInStock = new ArrayList<>();
                for (JsonElement season : seasons) {
                    seasonsInStock.add(Season.valueOf(season.getAsString()));
                }
                JsonObject costObject = storeProductObject.getAsJsonObject("cost");
                for (Map.Entry<String, JsonElement> entry : costObject.entrySet()) {
                    String itemName = entry.getKey();
                    int quantity = entry.getValue().getAsInt();
                    Item item = Game.getDatabase().getItem(itemName);
                    if (item == null) {
                        continue;
                    }
                    costs.put(item, quantity);
                }
                Product product = new Product(productName, price, limit, BuildingType.valueOf(buildingType), seasonsInStock, costs);
                products.add(product);
                itemDatabase.add(new BasicItem(productName, product.getPrice()));
            }
            stores.add(new Store(storeName, products, xStart, xEnd, yStart, yEnd, openingTime, closingTime));
        }
    }
    public Item getItem(String name) {
        for (Item i : itemDatabase) {
            if (i.getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return null;
    }



    public ArrayList<Craft> getCraftingRecipeDatabase() {
        return craftingRecipeDatabase;
    }

    public ArrayList<Item> getItemDatabase() {
        return itemDatabase;
    }

    public List<Store> getStores() {
        return stores;
    }
    public void loadNPCs() {
        String json;
        try {
            json = new String(Files.readAllBytes(Paths.get("npcInfos.json")));
        } catch (IOException e) {
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

    public Map<String, NPC> getNPCs() {
        return NPCs;
    }

}