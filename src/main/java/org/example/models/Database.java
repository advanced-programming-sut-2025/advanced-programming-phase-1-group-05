package org.example.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Database {
    ArrayList<FruitAndVegetable> plantDatabase = new ArrayList<>();
    ArrayList<Item> itemDatabase = new ArrayList<>();
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

    public void initializeAllItems(){
        itemDatabase.add(new basicItem("friendshipPoints", 0));
        // this item will only be used as a gift for incrementing the friendship points between two players or an npc
        //can't be bought or added to inventory
    }

    public Item getItem(String name){
        for(Item i : itemDatabase){
            if(i.getName().equals(name)){
                return i;
            }
        }
        return null;
    }
}