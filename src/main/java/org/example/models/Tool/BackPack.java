package org.example.models.Tool;

import org.example.models.Enums.BackPackType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Game;
import org.example.models.Tool.Tool;

import java.util.HashMap;

public class BackPack implements Tool <BackPackType>{
    BackPackType level = BackPackType.Normal;

    @Override
    public String getName() {
        return "BackPack";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public void use(HashMap.Entry<Integer, Integer> coordinates){


    }
    @Override
    public void reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        Game.getCurrentPlayer().addEnergy(-amount);
    }
    @Override
    public BackPackType getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.nextLevel();
            System.out.println(getName() + " upgraded to " + level.getName());
        }
    }
}