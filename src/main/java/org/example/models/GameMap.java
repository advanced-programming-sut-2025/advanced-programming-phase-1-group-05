package org.example.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Building.GreenHouse;
import org.example.models.Enums.ForagingSeedType;
import org.example.models.Enums.ForagingTreeSourceType;
import org.example.models.Enums.Season;
import org.example.models.Enums.TileType;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GameMap {
    GameTile[][] map;
    // TODO initialize this!!
    ArrayList<FruitAndVegetable> plants = new ArrayList<>();
    ArrayList<Tree> trees = new ArrayList<>();
    GreenHouse greenHouse = new GreenHouse();

    public ArrayList<FruitAndVegetable> getPlants() {
        return plants;
    }
    public ArrayList<Tree> getTrees() {
        return trees;
    }
    public void addPlant(FruitAndVegetable fruitAndVegetable) {
        plants.add(fruitAndVegetable);
    }
    public void addTree(Tree tree) {
        trees.add(tree);
    }

    //crow damage during the night with 25% probability
    public Result crowDamage() {
        //TODO nothing from greenhouse
        int groupOf16 = (plants.size() + trees.size()) / 16;
        Random random = new Random();
        for (int i = 0; i < groupOf16; i++) {
            if (random.nextDouble() < 0.25) {
                boolean targetPlant = random.nextBoolean();

                if (targetPlant && !plants.isEmpty()) {
                    int index = random.nextInt(plants.size());
                    FruitAndVegetable fruitAndVegetable = plants.get(index);
                    if(fruitAndVegetable.isProtectedByScareCrow())
                        return new Result(false, "Your plant was protected by scare crow.");
                    GameTile tile = this.getTile(fruitAndVegetable.getCoordinates().getKey(),
                            fruitAndVegetable.getCoordinates().getValue());
                    tile.setItemOnTile(null);
                    plants.remove(index);
                    return new Result(true, "A crow destroyed your plant during the night");
                } else if (targetPlant && !trees.isEmpty()) {
                    int index = random.nextInt(trees.size());
                    Tree tree = trees.get(index);
                    if(tree.isProtectedByScareCrow()) return new Result(false,"The tree was protected by scare crow.");
                    tree.setFruitGrowthCounter(0);
                   return new Result(true, "A crow destroyed your tree during the night");
                }
            }
        }
        return new Result(false, "");
    }


    //set a random foraging item on some tiles after the end of each day
    public void setForagingItems() {
        int totalTiles = map.length * map[0].length;
        Random random = new Random();

        if (random.nextInt(100) == 0) { // 1% chance
            int chosen = random.nextInt(totalTiles);
            int row = chosen / map[0].length;
            int col = chosen % map[0].length;

            GameTile tile = map[row][col];
            Item item;
            if (tile != null && tile.getTileType() == TileType.Soil && tile.getItemOnTile() == null) {
                if (chosen % 2 == 0) {
                    item = new BasicItem(ForagingTreeSourceType
                            .getRandomForagingTreeType(TimeAndDate.getCurrentSeason()).getName(), 0);
                } else {
                    item = new BasicItem(ForagingSeedType
                            .getRandomForagingSeedType(TimeAndDate.getCurrentSeason()).getName(), 0);
                }
            }
        }
    }

    //spawn foraging minerals
    public void setForagingMinerals(){
        //TODO mining area
    }


    public GameTile getTile(int x, int y) {
        return map[x-1][y-1];
    }

    public void growPlants(){
        for(FruitAndVegetable f: plants){
            f.grow();
        }
        for(Tree t: trees){
            t.growTree();
        }
    }
}