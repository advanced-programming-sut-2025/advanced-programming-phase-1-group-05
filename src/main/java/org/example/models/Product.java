package org.example.models;

import org.example.models.Enums.BuildingType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.Season;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Product implements Item {
    private final String name;
    private final int price;
    private final int limit;
    private int soldToday = 0;
    private final BuildingType buildingType;
    private final List<Season> seasons = new ArrayList<>();

    public Product(String name, int price, int limit
    , BuildingType buildingType, List<Season> seasons) {
        this.name = name;
        this.price = price;
        this.limit = limit;
        this.buildingType = buildingType;
        this.seasons.addAll(seasons);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
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
}