package org.example.models;


import com.badlogic.gdx.math.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Farm {
    private List<String> owner = new ArrayList<>();
    private HashMap<Food, Integer> refrigeratedFoods = new HashMap<>();
    private final ArrayList<FruitAndVegetable> crops = new ArrayList<>();
    private final ArrayList<Tree> trees = new ArrayList<>();
    //private final int startX , startY, endX, endY ;
    private final ShippingBin shippingBin;
    private Rectangle bounds;


    public Farm (Player player, Rectangle bounds){
        owner.add(player.getUsername());
        this.bounds = bounds;
        shippingBin = new ShippingBin( bounds.y + bounds.height, bounds.x);
    }

    public Rectangle getBounds() {
        return bounds;
    }
    public float getStartX() {
        return bounds.x;
    }
    public float getStartY() {
        return bounds.y;
    }
    public float getEndX() {
        return bounds.x + bounds.width;
    }
    public float getEndY() {
        return bounds.y + bounds.height;
    }
    public ArrayList<FruitAndVegetable> getCrops() {
        return crops;
    }

    public void addCrop(FruitAndVegetable fruitAndVegetable){
        crops.add(fruitAndVegetable);
    }

    public boolean isInFarm(float x, float y) {
        return bounds.contains(x, y);
    }
    public boolean isOwner(Player player) {
        return  owner.contains(player.getUsername());
    }
    public void addOwner(Player player){
        owner.add(player.getUsername());
    }
    public HashMap<Food, Integer> getRefrigeratedFoods() {
        return refrigeratedFoods;
    }
    public void addRefrigeratedFood(Food food, int amount) {
        refrigeratedFoods.put(food, amount);
    }

    public void removeRefrigeratedFood(Food food, int amount) {
        if(!refrigeratedFoods.containsKey(food)) return;
        refrigeratedFoods.put(food, refrigeratedFoods.get(food) - amount);
    }

//    public boolean containsTile (int x, int y) {
//        return (x >= startX && x <= endX) && (y >= startY && y <= endY);
//    }
    public ShippingBin getShippingBin() {
        return shippingBin;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }
    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public static class ShippingBin {
        float startX, startY, endX, endY;

        public ShippingBin(float farmStartY, float farmEndX) {
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
