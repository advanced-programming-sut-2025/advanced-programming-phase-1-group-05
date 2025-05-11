package org.example.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Farm {
    private List<Player> owner = new ArrayList<>();
    private HashMap<Food, Integer> refrigeratedFoods = new HashMap<>();
    private final ShippingBin shippingBin = new ShippingBin();
    private final ArrayList<FruitAndVegetable> crops = new ArrayList<>();
    //TODO the shipping bin coordinates should be in the farm
    private final int startX = 10, startY = 10, endX = startX + 80, endY = startY + 50;
    // TODO add the farm coordinates
    public Farm (Player player){
        owner.add(player);
    }

    public ArrayList<FruitAndVegetable> getCrops() {
        return crops;
    }

    public void addCrop(FruitAndVegetable fruitAndVegetable){
        crops.add(fruitAndVegetable);
    }

    public boolean isOwner(Player player) {
        return  owner.contains(player);
    }
    public void addOwner(Player player){
        owner.add(player);
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

    public boolean containsTile (int x, int y) {
        return (x >= startX && x <= endX) && (y >= startY && y <= endY);
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
