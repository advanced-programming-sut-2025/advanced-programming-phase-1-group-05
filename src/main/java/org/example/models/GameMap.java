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
    ArrayList<FruitAndVegetable> plantDatabase = new ArrayList<>();

    //start when the game starts
    //put this somewhere where everything is initialized
    public void initializePlantDatabase(){
        try {
            Gson gson = new Gson();

            FileReader reader = new FileReader("plants.json");

            Type plantListType = new TypeToken<ArrayList<FruitAndVegetable>>(){}.getType();

            ArrayList<FruitAndVegetable> plantList = gson.fromJson(reader, plantListType);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public FruitAndVegetable getFruitAndVegetable(String name){
        FruitAndVegetable plant = null;
        for(FruitAndVegetable f : plantDatabase){
            if(f.getName().equals(name)){
                plant = f;
            }
        }
        return plant;
    }

    public ArrayList<FruitAndVegetable> getPlantDatabase(){
        return plantDatabase;
    }
    public GameTile getTile(int x, int y) {
        return map[x][y];
    }

    public void placeItemOnTile(Item item, int x, int y) {}

    public void removeItemFromTile(int x, int y) {}
}
