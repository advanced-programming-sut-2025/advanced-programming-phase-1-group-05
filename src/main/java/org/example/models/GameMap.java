package org.example.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Enums.Season;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
    GameTile[][] map;
    ArrayList<FruitAndVegetable> plants = new ArrayList<>();

    public GameTile getTile(int x, int y) {
        return map[x][y];
    }

    public void placeItemOnTile(Item item, int x, int y) {}

    public void removeItemFromTile(int x, int y) {}

    //every day
    public void addForagingItems(){
        
    }
}
