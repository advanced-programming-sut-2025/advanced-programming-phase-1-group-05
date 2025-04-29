package org.example.models;

import org.example.models.Enums.ItemLevel;

import java.util.Map;

public class Product implements Item{
    private final String name;
    private final String description;
    private final int price;
    private final int limit;
    private ItemLevel level = ItemLevel.Normal;
    private int stock;

    public Product(String name, String description, int price, int limit) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.limit = limit;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
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

    public int getLimit() {
        return limit;
    }
    public boolean isAvailable() {
        return stock > 0;
    }
    public void decrementStock(int amount) {
        stock -= amount;
    }
}