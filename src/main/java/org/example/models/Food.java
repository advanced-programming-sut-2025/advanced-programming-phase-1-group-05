package org.example.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Food implements Item{
    private String name;
    private HashMap<Food, Integer> ingredients;
    private int energy;
    //buff??
    private int price;

    public HashMap<Food, Integer> getIngredients() {
        return ingredients;
    }
    public int getEnergy() {
        return energy;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getPrice() {
        return price;
    }
    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {
        return;
    }
    @Override
    public Map.Entry<Integer, Integer> getCoordinates() {
        return null;
    }
}
