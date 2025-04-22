package models.Tool;

import models.Enums.ItemLevel;

import java.util.HashMap;

public class FishingPole implements Tool {
    ItemLevel level;
    @Override
    public String getName() {
        return "Shear";
    }
    @Override
    public int getPrice() {
        return 0;
    }
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
    public void upgradeLevel(){

    }
}
