package org.example.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Farm {
    private Player owner;
    private HashMap<Food, Integer> refrigeratedFoods = new HashMap<>();


    public Player getOwner() {
        return owner;
    }
    public HashMap<Food, Integer> getRefrigeratedFoods() {
        return refrigeratedFoods;
    }
    public void addRefrigeratedFood(Food food, int amount) {
        refrigeratedFoods.put(food, amount);
    }

    public void removeRefrigeratedFood(Food food, int amount) {
        refrigeratedFoods.put(food, refrigeratedFoods.get(food) - amount);
    }


}
