package org.example.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controllers.GameManager;
import org.example.controllers.GameMenuController;
import org.example.models.Building.GreenHouse;
import org.example.models.Enums.*;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GameMap {
    public static final int MAP_WIDTH = 100;
    public static final int MAP_HEIGHT = 100;
    private static GameTile[][] map = new GameTile[MAP_HEIGHT][MAP_WIDTH];


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
                    if(tile.getTileType().equals(TileType.GreenHouse))
                        return new Result(true, "Your plant was protected in the green house.");
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
                if (chosen % 4 == 0) {
                    ForagingTreeSourceType type = ForagingTreeSourceType.getRandomForagingTreeType(GameManager.getSeason());
                    item = new ForagingItem(type, type.getName(), type.getPrice());
                    tile.setItemOnTile(item);
                } else if(chosen % 4 == 1) {
                    ForagingSeedType type = ForagingSeedType.getRandomForagingSeedType(GameManager.getSeason());
                    item = new ForagingItem(type, type.getName(), type.getPrice());
                    tile.setItemOnTile(item);
                } else if(chosen % 4 == 2) {
                    item = Game.getDatabase().getItem("Wood");
                    tile.setItemOnTile(item);
                } else {
                    item = Game.getDatabase().getItem("Fiber");
                    tile.setItemOnTile(item);
                }
            }
        }
    }

    //spawn foraging minerals
    public void setForagingMinerals() {
        Random random = new Random();

        if (random.nextInt(25) == 0) { // 4% chance
            List<GameTile> mineralTiles = new ArrayList<>();

            for (int row = 0; row < map.length; row++) {
                for (int col = 0; col < map[0].length; col++) {
                    GameTile tile = map[row][col];
                    if (tile != null && tile.getTileType() == TileType.Mine && tile.getItemOnTile() == null) {
                        mineralTiles.add(tile);
                    }
                }
            }

            if (!mineralTiles.isEmpty()) {
                GameTile chosenTile = mineralTiles.get(random.nextInt(mineralTiles.size()));
                MineralType mineralType = MineralType.getRandomMineralType();
                chosenTile.setItemOnTile(new Mineral(mineralType));
            }
        }
    }

    public void growPlants(){
        for(FruitAndVegetable f: plants){
            if(f.isAlive()) f.grow();
            else {
                //remove dead plants
                GameTile tile = Game.getGameMap().getTile(f.getCoordinates().getKey(), f.getCoordinates().getValue());
                tile.setItemOnTile(null);
                Game.getGameMap().getPlants().remove(f);
            }
        }
        for(Tree t: trees){
            t.growTree();
        }
    }
    public GameMap() {
//        Player currentPlayer = Game.getCurrentPlayer();
//        int farmNum = currentPlayer.getMapNum();
        initEmptyMap(TileType.Flat);
//        generatePlaceOfPlayer(farmNum);
        generateFarm(0, 0, 30, 30, 1);          // Farm A
        generateFarm(0, 70, 30, 30, 2);         // Farm B
        generateFarm(70, 0, 30, 30, 3);         // Farm C
        generateFarm(70, 70, 30, 30, 4);        // Farm D
        generateVillageCenter();

    }

    public static void generatePlaceOfPlayer(int farmNum) {
        System.out.println(farmNum);
        int startX, startY;

        switch (farmNum) {
            case 1:
                startX = 5;
                startY = 5;
                break;
            case 2:
                startX = 95;
                startY = 5;
                break;
            case 3:
                startX = 5;
                startY = 95;
                break;
            case 4:
                startX = 95;
                startY = 95;
                break;
            default:
                // Default position if farm number is invalid
                startX = 50;
                startY = 50;
                break;
        }

        // Set player coordinates
        Game.getCurrentPlayer().setCoordinate(startX, startY);

        // Mark the tile as occupied
        // TODO ; HELLo
        GameTile playerTile = getTile(startX, startY);
        if (playerTile != null) {
            playerTile.occupy();
        }
    }

    private void generateVillageCenter() {
        int centerX = MAP_HEIGHT / 2;
        int centerY = MAP_WIDTH / 2;
        for (int i = centerX - 5; i <= centerX + 5; i++) {
            for (int j = centerY - 5; j <= centerY + 5; j++) {
                setTile(i, j, new GameTile(i, j, TileType.Soil));
            }
        }
    }
    private void placeRandomDecorations(int startX, int startY, int width, int height,
                                        int cropCount, int stoneCount,
                                        List<Rectangle> occupiedAreas, Random random) {
        int placedTrees = 0;
        int placedStones = 0;
        int maxAttempts = (cropCount + stoneCount) * 2;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            if (placedTrees >= cropCount && placedStones >= stoneCount) {
                break;
            }

            int x = startX + random.nextInt(height);
            int y = startY + random.nextInt(width);

            if (!isInBounds(x, y)) {
                continue;
            }

            GameTile tile = getTile(x, y);
            if (tile == null || tile.getTileType() != TileType.Soil) {
                continue;
            }

            Rectangle point = new Rectangle(x, y, 1, 1);
            if (isAreaOccupied(point, occupiedAreas)) {
                continue;
            }
            setRandomDecoration(tile);
        }

    }

    public void setRandomDecoration(GameTile tile){
        Random random = new Random();
        int x = random.nextInt(100);
        if(tile.getTileType() == TileType.Mine && tile.getItemOnTile() == null) {
            tile.setItemOnTile(new Mineral(MineralType.getRandomMineralType()));
        } else if (tile.getTileType() == TileType.Soil && tile.getItemOnTile() == null) {
            ForagingCrop type = ForagingCrop.getRandomForagingCrop(GameManager.getSeason());
            TreeType type1 = TreeType.getRandomTreeType(GameManager.getSeason());
            Tree newTree = new Tree(type1);
            MineralType mineralType = MineralType.getRandomMineralType();
            newTree.setFullyGrown();
            if(x%2 == 0) tile.setItemOnTile(new ForagingItem(type, type.getName(), type.getPrice()));
            else tile.setItemOnTile(newTree);
        }
    }

    private void placeRandomMine(int startX, int startY, int width, int height,
                                 List<Rectangle> occupiedAreas, Random random) {
        int maxAttempts = 100;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = startX + random.nextInt(height - 3);
            int y = startY + random.nextInt(width - 1);

            Rectangle mineArea = new Rectangle(x, y, 2, 4);

            if (!isAreaOccupied(mineArea, occupiedAreas) && isAreaValid(mineArea, TileType.Mine)) {
                setTile(x, y, new GameTile(x, y, TileType.Mine));
                setTile(x, y+1, new GameTile(x, y+1, TileType.Mine));
                setTile(x+1, y, new GameTile(x+1, y, TileType.Mine));
                setTile(x+1, y+1, new GameTile(x+1, y+1, TileType.Mine));
                setTile(x+2, y, new GameTile(x+2, y, TileType.Mine));
                setTile(x+3, y, new GameTile(x+3, y, TileType.Mine));

                occupiedAreas.add(mineArea);
                return;
            }
        }
        System.out.println("Warning: Could not place mine");
    }

    private boolean isAreaOccupied(Rectangle area, List<Rectangle> occupiedAreas) {
        for (Rectangle rect : occupiedAreas) {
            if (area.intersects(rect)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAreaValid(Rectangle area, TileType type) {
        for (int i = area.x; i < area.x + area.height; i++) {
            for (int j = area.y; j < area.y + area.width; j++) {
                GameTile tile = getTile(i, j);
                if (tile == null || tile.getTileType() != TileType.Soil) {
                    return false;
                }
            }
        }
        return true;
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
//TODO HElloooo
    public static GameTile getTile(int x, int y) {
        if (isInBounds(x, y)) {
            return map[x - 1][y - 1];
        }
        return null;
    }

    public void setTile(int x, int y, GameTile tile) {
        if (isInBounds(x, y)) {
            map[x][y] = tile;
        }
    }

    public static boolean isInBounds(int x, int y) {
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
    public void printMapSection1(int centerX, int centerY, int size) {
        int half = size / 2;
        for (int i = 0; i < centerX - half; i++) {
            for (int j = 0; j < centerY - half; j++) {
                if (isInBounds(i, j)) {
                    System.out.print(getTile(i, j).toString() + " ");
                } else {
                    System.out.print("⬛ ");
                }
            }
            System.out.println();
        }
    }
    public void printMapSection2(int centerX, int centerY, int size) {
        int half = size / 2;
        for (int i = centerX - half; i <= centerX + half; i++) {
            for (int j = 0; j < centerY - half; j++) {
                if (isInBounds(i, j)) {
                    System.out.print(getTile(i, j).toString() + " ");
                } else {
                    System.out.print("⬛ ");
                }
            }
            System.out.println();
        }
    }
    public void printMapSection3(int centerX, int centerY, int size) {
        int half = size / 2;
        for (int i = 0; i < centerX - half; i++) {
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

    public void printMapSection4(int centerX, int centerY, int size) {
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

    private void generateFarm(int startX, int startY, int width, int height, int farmType) {
        for (int i = startX; i < startX + height; i++) {
            for (int j = startY; j < startY + width; j++) {
                if (isInBounds(i, j)) {
                    setTile(i, j, new GameTile(i, j, TileType.Soil));
                }
            }
        }


        switch (farmType) {
            case 1: // مزرعه نوع 1
                placeFixedFeature(startX+20, startY+15, 6, 5, TileType.GreenHouse, "greenhouse");
                placeFixedFeature(startX+1, startY+1, 3, 2, TileType.Mine, "mine");
                placeFixedFeature(startX+5, startY+5, 3, 2, TileType.Water, "lake");
                placeFixedFeature(startX+10, startY+10, 4, 4, TileType.Building, "builing");
                break;

            case 2: // مزرعه نوع 2
                placeFixedFeature(startX+14, startY+15, 6, 5, TileType.GreenHouse, "greenhouse");
                placeFixedFeature(startX+20, startY+21, 3, 2, TileType.Water, "lake");
                placeFixedFeature(startX+4, startY+4, 3, 2, TileType.Mine, "mine");
                placeFixedFeature(startX+10, startY+10, 4, 4, TileType.Building, "builing");
                break;

            case 3: // مزرعه نوع 3
                placeFixedFeature(startX+20, startY+15, 6, 5, TileType.GreenHouse, "greenhouse");
                placeFixedFeature(startX+2, startY+2, 3, 2, TileType.Mine, "mine");
                placeFixedFeature(startX+10, startY+11, 3, 2, TileType.Water, "lake");
                placeFixedFeature(startX+15, startY+20, 4, 4, TileType.Building, "builing");
                break;

            case 4: // مزرعه نوع 4
                placeFixedFeature(startX+10, startY+5, 6, 5, TileType.GreenHouse, "greenhouse");
                placeFixedFeature(startX+4, startY+2, 3, 2, TileType.Mine, "mine");
                placeFixedFeature(startX+15, startY+11, 3, 2, TileType.Water, "lake");
                placeFixedFeature(startX+19, startY+20, 4, 4, TileType.Building, "builing");
                break;
        }


        placeRandomDecorations(startX, startY, width, height, 15, 10, new ArrayList<>(), new Random());
    }

    private void placeFixedFeature(int startX, int startY, int width, int height,
                                   TileType type, String featureName) {
        for (int i = startX; i < startX + height; i++) {
            for (int j = startY; j < startY + width; j++) {
                if (isInBounds(i, j)) {
                    setTile(i, j, new GameTile(i, j, type));
                }
            }
        }
    }

    public void setPlayerCoordinates(){
        for(int i = 1; i < MAP_HEIGHT; i ++) {
            for(int j = 1; j < MAP_WIDTH; j ++) {
                GameTile tile = getTile(i, j);
                if(tile.getTileType().equals(TileType.Building)) {
                    Game.getCurrentPlayer().setCoordinate(i, j);
                }
            }
        }
    }

    public String whereAmI() {
        String location ="Home";
        //TODO implement
        return location;
    }



}