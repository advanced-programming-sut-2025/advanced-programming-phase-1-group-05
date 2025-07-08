package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;
import org.example.models.Enums.BuildingType;
import org.example.models.Enums.Season;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    ArrayList<Item> itemDatabase = new ArrayList<>();
    ArrayList<Craft> craftingRecipeDatabase = new ArrayList<>();
    List<Store> stores = new ArrayList<>();
    List<NPC> NPCs = new ArrayList<>();

    public Database() {
        initializeStoresAndItems();
        loadNPCs();
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
        itemDatabase.add(new BasicItem("Truffle", 625));
        String json;
        FileHandle file = Gdx.files.internal("jsonFiles/stores.json");
        json = file.readString();
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
                String buildingType = safeGetAsString(storeProductObject, "buildingRequired");
                JsonArray seasons = storeProductObject.has("season") && !storeProductObject.get("season").isJsonNull()
                    ? storeProductObject.get("season").getAsJsonArray()
                    : null;
                Map<String, Integer> costs = new HashMap<>();
                List<Season> seasonsInStock = new ArrayList<>();
                if (seasons != null) {
                    for (JsonElement season : seasons) {
                        seasonsInStock.add(Season.fromString(season.getAsString()));
                    }
                }
                JsonObject costObject = storeProductObject.getAsJsonObject("cost");
                if (costObject != null) {
                    for (Map.Entry<String, JsonElement> entry : costObject.entrySet()) {
                        String itemName = entry.getKey();
                        int quantity = entry.getValue().getAsInt();

                        costs.put(itemName, quantity);
                    }
                }
                Product product = new Product(productName, price, limit, BuildingType.fromString(buildingType), seasonsInStock, costs);
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

    public List<Store> getStores() {
        return stores;
    }

    public void loadNPCs() {
        String json;
        FileHandle file = Gdx.files.internal("jsonFiles/npcInfos.json");
        json = file.readString();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray npcsArray = jsonObject.getAsJsonArray("npcs");

        for (JsonElement npcElement : npcsArray) {
            JsonObject npcObject = npcElement.getAsJsonObject();

            String name = npcObject.get("name").getAsString();
            JsonArray favoritesArray = npcObject.getAsJsonArray("favorites");
            JsonObject requestsObject = npcObject.getAsJsonObject("requests");
            JsonObject rewardsObject = npcObject.getAsJsonObject("rewards");

            List<String> favorites = new ArrayList<>();
            for (JsonElement favoriteElement : favoritesArray) {
                favorites.add(favoriteElement.getAsString());
            }

            List<String> requestItems = new ArrayList<>();
            List<Integer> requestAmounts = new ArrayList<>();
            for (Map.Entry<String, JsonElement> entry : requestsObject.entrySet()) {
                String item = entry.getKey();
                int quantity = entry.getValue().getAsInt();
                requestItems.add(item);
                requestAmounts.add(quantity);
            }

            List<String> rewardItems = new ArrayList<>();
            List<Integer> rewardAmounts = new ArrayList<>();
            for (Map.Entry<String, JsonElement> entry : rewardsObject.entrySet()) {
                String item = entry.getKey();
                int quantity = entry.getValue().getAsInt();
                rewardItems.add(item);
                rewardAmounts.add(quantity);
            }

            List<Mission> missions = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String itemRequired = requestItems.get(i);
                int amountRequired = requestAmounts.get(i);
                Mission mission = new Mission("Deliver " + amountRequired + " " + itemRequired + "(s)",
                    itemRequired, amountRequired, rewardItems.get(i), rewardAmounts.get(i));
                if (i != 0) {
                    mission.setStatus(Mission.Status.LOCKED);
                }
                missions.add(mission);
            }
            NPC npc = new NPC(name, favorites, missions);
            NPCs.add(npc);
        }
    }

    public List<NPC> getNPCs() {
        return NPCs;
    }

    public static String safeGetAsString(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        return el != null && !el.isJsonNull() ? el.getAsString() : null;
    }


}
