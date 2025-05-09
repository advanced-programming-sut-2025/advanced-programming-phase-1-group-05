package org.example.models.Skills;

import org.example.models.*;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Tool.Hoe;
import org.example.models.Tool.Scythe;
import org.example.models.Tool.WateringCan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Farming implements Skill{
    int level = 0;
    int capacity = 0;

    //شخم زدن
    public void plowTile(GameTile tile, Hoe hoe) {}
    //کاشتن دانه
    public boolean plantSeed(Seed seed, GameTile tile) {
        if(getSeedType(seed) == null) return false;
        else if(getSeedType(seed) == "crop") {
            Game.getGameMap().addPlant(getFruitType(seed));
        } else if(getSeedType(seed) == "tree") {
            Game.getGameMap().addTree(getTreeType(seed));
        }
        increaseCapacity();
        return true;
    }

    //fertilize crop
    public boolean fertilizeCrop(Map.Entry<Integer, Integer> coordinantes, String fertilizer) {
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getBackPack().getInventory();
        for(Item item : items.keySet()) {
            if(item.getName().equals(fertilizer)) {
                FruitAndVegetable fruit = Game.getGameMap().getPlantedFruit(coordinantes);
                // idk the details
                fruit.fertilize();
                return true;
            }
        }
        return false;
    }

    //water crop
    public void waterCrop(GameTile tile) {
        GameMap map = Game.getGameMap();
        for(FruitAndVegetable f : map.getPlants()) {
            if(map.getTile(f.getCoordinates().getKey(), f.getCoordinates().getValue()) == tile) {
                f.waterCrop();
            }
        }
    }

    //harvest crop
    public void harvestCrop(GameTile tile) {
        GameMap map = Game.getGameMap();
        for(FruitAndVegetable f : map.getPlants()) {
            if(map.getTile(f.getCoordinates().getKey(), f.getCoordinates().getValue()) == tile) {
                Game.getCurrentPlayer().getBackPack().addToInventory(f, 1);
                map.getPlants().remove(f);
            }
        }
    }

    //seed type
    public String getSeedType(Seed seed){
        for(FruitAndVegetable f: Game.getDatabase().getFruitAndVegetables()){
            if(f.getSeed().equals(seed.getName())){
                return "crop";
            }
        }
        for (Tree tree: Game.getDatabase().getTreeDatabase()) {
            if(tree.getTreeType().getSeed().equals(seed.getName())){
                return "tree";
            }
        }
        return null;
    }

    //get fruit type from database
    public FruitAndVegetable getFruitType(Seed seed) {
        for(FruitAndVegetable f: Game.getDatabase().getFruitAndVegetables()){
            if(f.getSeed().equals(seed.getName())){
                return f;
            }
        }
        return null;
    }

    //get tree type from database
    public Tree getTreeType(Seed seed) {
        for(Tree tree: Game.getDatabase().getTreeDatabase()){
            if(tree.getTreeType().getSeed().equals(seed.getName())){
                return tree;
            }
        }
        return null;
    }

    @Override
    public ItemLevel getLevel() {
        return null;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }
    @Override
    public void increaseLevel() {
        if(level < 4) this.level ++;
    }
    @Override
    public boolean canGoToNextLevel() {
        if((level + 1) * 100 + 50 <= capacity) {
            capacity -= (level + 1)*100 + 50;
            increaseLevel();
            return true;
        }
        return false;
    }
    @Override
    public void increaseCapacity() {
        this.capacity += 5;
    }
}