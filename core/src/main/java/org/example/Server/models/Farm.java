package org.example.Server.models;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.Client.GameAssetManager;
import org.example.Common.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Farm {
    private List<String> owner = new ArrayList<>();
    private HashMap<Food, Integer> refrigeratedFoods = new HashMap<>();
    private final ArrayList<FruitAndVegetable> crops = new ArrayList<>();
    private final ArrayList<Tree> trees = new ArrayList<>();
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

    public ShippingBin getShippingBin() {
        return shippingBin;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }
    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public static class ShippingBin extends Actor {
        float startX, startY;
        Texture tex = GameAssetManager.getInstance().getOrLoadTexture("Items/Shipping_bin.png");

        public ShippingBin(float farmStartY, float farmEndX) {
            startX = farmEndX + 300;
            startY = farmStartY - 200;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(tex, startX, startY);
        }
    }


}
