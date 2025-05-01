package org.example.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Farm {
    private Player owner;
    // TODO make this a list of owners for after marriage
    private HashMap<Food, Integer> refrigeratedFoods = new HashMap<>();
    private final ShippingBin shippingBin = new ShippingBin();
    //TODO the shipping bin coordinates should be in the farm
//    private final int startX, startY, endX, endY;


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

    public ShippingBin getShippingBin() {
        return shippingBin;
    }

    public static class ShippingBin {
        int startX, startY, endX = startX + 2, endY = startY + 1;

//        public ShippingBin(int farmStartY, int farmEndX) {
//            startX = farmEndX - 10;
//            startY = farmStartY - 5;
//        }
        public boolean isNear(int x, int y) {
            boolean nearX = (x >= startX - 1 && x <= endX + 1);
            boolean nearY = (y >= startY - 1 && y <= endY + 1);
            return nearX && nearY;
        }
    }

}
