package org.example.models.Tool;

import org.example.models.Enums.ItemLevel;

import java.util.HashMap;

public class WateringCan implements Tool {
    ItemLevel level;

    @Override
    public void use(HashMap.Entry<Integer, Integer> coordinates){


    }
    @Override
    public void reduceEnergy(){

    }

    @Override
    public ItemLevel getLevel() {
        return level;
    }

    @Override
    public void upgradeLevel() {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getPrice() {
        return 0;
    }
}