package org.example.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Farm {
    private List<Player> owner = new ArrayList<>();
    private HashMap<Food, Integer> refrigeratedFoods = new HashMap<>();
    private final ArrayList<FruitAndVegetable> crops = new ArrayList<>();
    private final ArrayList<Tree> trees = new ArrayList<>();
    private final int startX , startY, endX, endY ;
    private final ShippingBin shippingBin;



    public Farm (Player player, int startX, int startY){
        owner.add(player);
        this.startX = startX;
        this.startY = startY;
        endX = startX + 30;
        endY = startY + 30;
        shippingBin = new ShippingBin(startY, endX);
    }

    public ArrayList<FruitAndVegetable> getCrops() {
        return crops;
    }

    public void addCrop(FruitAndVegetable fruitAndVegetable){
        crops.add(fruitAndVegetable);
    }

    public boolean isInFarm(int x, int y) {
        return x >= startX && x <= endX && y >= startY && y <= endY;
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

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }
    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public static class ShippingBin {
        int startX, startY, endX, endY;

        public ShippingBin(int farmStartY, int farmEndX) {
            startX = farmEndX - 5;
            startY = farmStartY + 5;
            endX = startX + 2;
            endY = startY + 1;
        }
        public boolean isNear(int x, int y) {
            boolean nearX = (x >= startX - 1 && x <= endX + 1);
            boolean nearY = (y >= startY - 1 && y <= endY + 1);
            return nearX && nearY;
        }

        public void getCoordinates() {
            System.out.println(startX + "to" + endX + "," + startY + "to" + endY);
        }

    }


}
