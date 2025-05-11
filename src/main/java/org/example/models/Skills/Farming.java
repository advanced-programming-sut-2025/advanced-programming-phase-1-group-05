package org.example.models.Skills;

import org.example.models.*;
import org.example.models.Enums.CropType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Enums.TreeType;
import org.example.models.Tool.Hoe;
import org.example.models.Tool.Scythe;
import org.example.models.Tool.WateringCan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Farming implements Skill{
    int level = 0;
    int capacity = 0;

    public void plowTile(GameTile tile, Hoe hoe) {}

    //plant seed
    public boolean plantSeed(String seed, GameTile tile) {
        CropType cropType = CropType.getSeedType(seed);
        TreeType treeType = TreeType.getSeedType(seed);
        if(cropType != null) {
            FruitAndVegetable crop = new FruitAndVegetable(cropType);
            tile.setItemOnTile(crop);
            if(crop.canBecomeGiant(Game.getCurrentPlayer().getFarm().getCrops())) {
                crop.expandToGiant(Game.getCurrentPlayer().getFarm().getCrops());
            }
            return true;
        } else if (treeType != null) {
            tile.setItemOnTile(new Tree(treeType));
            return true;
        }
        return false;
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
    public void waterCrop(FruitAndVegetable crop) {
        crop.waterCrop();
    }

    //harvest crop
    public void harvestCrop(GameTile tile) {
        Item crop = tile.getItemOnTile();
        Game.getCurrentPlayer().getBackPack().addToInventory(tile.getItemOnTile(), 1);
        if(((FruitAndVegetable) crop).isOneTime()) tile.setItemOnTile(null);
        else {
            ((FruitAndVegetable) crop).setRegrowth();
        }
        increaseCapacity();
    }


    @Override
    public int getLevel() {
        return level;
    }
    @Override
    public boolean isMaxLevel() {
        return level == 10;
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