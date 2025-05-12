package org.example.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controllers.GameManager;
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
    public static final int MAP_WIDTH = 100;
    public static final int MAP_HEIGHT = 100;
    private GameTile[][] map = new GameTile[MAP_HEIGHT][MAP_WIDTH];

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
                            .getRandomForagingTreeType(GameManager.getSeason()).getName(), 0);
                } else {
                    item = new BasicItem(ForagingSeedType
                            .getRandomForagingSeedType(GameManager.getSeason()).getName(), 0);
                }
            }
        }
    }

    //spawn foraging minerals
    public void setForagingMinerals(){
        //TODO mining area
    }


//    public GameTile getTile(int x, int y) {
//        return map[x-1][y-1];
//    }

    public void growPlants(){
        for(FruitAndVegetable f: plants){
            f.grow();
        }
        for(Tree t: trees){
            t.growTree();
        }
    }
    public GameMap() {
        initEmptyMap(TileType.Flat);
        generateFarm(0, 0, 30, 30, 1);          // Farm A
        generateFarm(0, 70, 30, 30, 2);         // Farm B
        generateFarm(70, 0, 30, 30, 3);         // Farm C
        generateFarm(70, 70, 30, 30, 4);        // Farm D
        generateVillageCenter();               // وسط

        spreadRandomItemsInFarm(0, 0, 30, 30);
        spreadRandomItemsInFarm(0, 70, 30, 30);
        spreadRandomItemsInFarm(70, 0, 30, 30);
        spreadRandomItemsInFarm(70, 70, 30, 30);
    }

    private void generateVillageCenter() {
        int centerX = MAP_HEIGHT / 2;
        int centerY = MAP_WIDTH / 2;
        for (int i = centerX - 5; i <= centerX + 5; i++) {
            for (int j = centerY - 5; j <= centerY + 5; j++) {
                setTile(i, j, new GameTile(i, j, TileType.Building));
            }
        }
    }

    private void generateFarm(int startX, int startY, int width, int height, int farmType) {
        for (int i = startX; i < startX + height; i++) {
            for (int j = startY; j < startY + width; j++) {
                setTile(i, j, new GameTile(i, j, TileType.Soil)); // پیش‌فرض خاک

                // بسته به نوع مزرعه، آیتم خاصی بذار
                if (farmType == 1 && insideRect(i, j, startX+5, startY+5, 10, 6)) {
                    setTile(i, j, new GameTile(i, j, TileType.Water)); // دریاچه
                } else if (farmType == 2 && insideRect(i, j, startX+2, startY+2, 8, 5)) {
                    setTile(i, j, new GameTile(i, j, TileType.Stone)); // معدن
                }
                // و ... بقیه موارد ثابت مثل کلبه (4x4) یا گلخانه (5x6)
            }
        }
    }
    public void spreadRandomItemsInFarm(int startX, int startY, int width, int height) {
        Random random = new Random();
        int count = 50;

        for (int k = 0; k < count; k++) {
            int i = startX + random.nextInt(height);
            int j = startY + random.nextInt(width);
            TileType[] candidates = {TileType.Tree, TileType.Stone, TileType.Building};
            TileType randomType = candidates[random.nextInt(candidates.length)];
            setTile(i, j, new GameTile(i, j, randomType));
        }
    }

    private boolean insideRect(int i, int j, int x, int y, int w, int h) {
        return i >= x && i < x + h && j >= y && j < y + w;
    }






    public void initEmptyMap(TileType defaultType) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = new GameTile(i + 1, j + 1, defaultType);
            }
        }
    }

    public GameTile getTile(int x, int y) {
        if (isInBounds(x, y)) {
            return map[x - 1][y - 1];
        }
        return null;
    }

    public void setTile(int x, int y, GameTile tile) {
        if (isInBounds(x, y)) {
            map[x - 1][y - 1] = tile;
        }
    }

    public boolean isInBounds(int x, int y) {
        return x > 0 && y > 0 && x <= map.length && y <= map[0].length;
    }
    public void printFullMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j].toString() + " ");
            }
            System.out.println();
        }
    }
    public void printMapSection(int centerX, int centerY, int size) {
        int half = size / 2;
        for (int i = centerX - half; i <= centerX + half; i++) {
            for (int j = centerY - half; j <= centerY + half; j++) {
                if (isInBounds(i, j)) {
                    System.out.print(getTile(i, j).toString() + " ");
                } else {
                    System.out.print("⬛ ");
                }
            }
            System.out.println();
        }
    }



}