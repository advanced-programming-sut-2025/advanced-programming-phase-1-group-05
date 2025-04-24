package org.example.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Building.GreenHouse;
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
        //nothing from the greenhouse
        int groupOf16 = (plants.size() + trees.size()) / 16;
        Random random = new Random();
        for (int i = 0; i < groupOf16; i++) {
            if (random.nextDouble() < 0.25) {
                boolean targetPlant = random.nextBoolean();

                if (targetPlant && !plants.isEmpty()) {
                    int index = random.nextInt(plants.size());
                    FruitAndVegetable removed = plants.remove(index);
                    return new Result(true, "A crow destroyed your plant during the night");
                } else if (!trees.isEmpty()) {
                    int index = random.nextInt(trees.size());
                    Tree removed = trees.remove(index);
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
            if (tile != null && tile.getTileType() == TileType.Soil) {
                if (random.nextBoolean()) {
                    //tile.plantSeed();
                } else {
                    //tile.plantCrop();

                }
            }
        }
    }

    //spawn foraging minerals
    public void setForagingMinerals(){
        //in the mining area
    }

    //get fruit type that's planted
    public FruitAndVegetable getPlantedFruit(Map.Entry<Integer, Integer> coordinates) {
        for(FruitAndVegetable f: plants ){
            if(f.getCoordinates().getKey() == coordinates.getKey() &&
                    f.getCoordinates().getValue() == coordinates.getValue()){
                return f;
            }
        }
        return null;
    }

    public GameTile getTile(int x, int y) {
        return map[x][y];
    }

    public void placeItemOnTile(Item item, int x, int y) {}

    public void removeItemFromTile(int x, int y) {}
}