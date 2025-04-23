package org.example.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Database {
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

    public ArrayList<FruitAndVegetable> getFruitAndVegetables(){
        return plantDatabase;
    }
}
