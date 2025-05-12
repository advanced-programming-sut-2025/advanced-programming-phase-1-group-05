package org.example.models;

import org.example.controllers.GameManager;
import org.example.models.Enums.BuildingType;
import org.example.models.Enums.GameMenuCommands;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.Season;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product implements Item {
    private final String name;
    private final int price;
    private final int limit;
    private int soldToday = 0;
    private final BuildingType buildingType;
    private final List<Season> seasons = new ArrayList<>();
    private final Map<Item, Integer> costs = new HashMap<>();
    private ItemLevel itemLevel = ItemLevel.Normal;


    public Product(String name, int price, int limit
    , BuildingType buildingType, List<Season> seasons, Map<Item, Integer> costs) {
        this.name = name;
        this.price = price;
        this.limit = limit;
        this.buildingType = buildingType;
        this.seasons.addAll(seasons);
        this.setItemLevel();
        this.costs.putAll(costs);
    }

    public void setItemLevel() {
        String[] parts = name.split("\\s+");
        try {
            itemLevel = ItemLevel.valueOf(parts[0]);
        }
        catch (IllegalArgumentException e) {
            itemLevel = ItemLevel.Normal;
        }

    }

    public BuildingType getBuildingType() {
        return buildingType;
    }
    public void setItemLevel(ItemLevel itemLevel) {
        this.itemLevel = itemLevel;
    }
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        if (!seasons.contains(GameManager.getSeason())) {
            return (int) (price * 3 / 2.0 * itemLevel.getPriceCoefficient());
        }
        return (int) (price * itemLevel.getPriceCoefficient());
    }

    public boolean isInSeason(Store store) {
        if (store.getStoreName().equals("Joja Mart")) {
            return seasons.contains(GameManager.getSeason());
        }
        return true;
    }
    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {
        //booooo not necessary
    }

    @Override
    public Map.Entry<Integer, Integer> getCoordinates() {
        return null;
    }


    public void setSoldToday(int soldToday) {
        this.soldToday = soldToday;
    }

    public void addSold(int amount) {
        soldToday += amount;
    }
    public int getRemainingForToday(){
        return limit - soldToday;
    }

    public boolean isAvailable(){
        return getRemainingForToday() > 0;
    }

    public Map<Item, Integer> getCosts() {
        return costs;
    }
}