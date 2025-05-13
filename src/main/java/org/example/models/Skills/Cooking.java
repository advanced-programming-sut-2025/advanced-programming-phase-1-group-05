package org.example.models.Skills;

import org.example.models.BasicItem;
import org.example.models.Enums.CookingRecipeType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.Material;
import org.example.models.Food;
import org.example.models.Game;
import org.example.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cooking implements Skill{
    int level = 0;
    int capacity = 0;


    public void cookFood(Food food) {
        for(Material f : food.getIngredients().keySet()) {
            Game.getCurrentPlayer().getBackPack().removeFromInventory
                    (new BasicItem(f.getName(), f.getPrice()), Game.getCurrentPlayer().getBackPack().getInventory().get(f));
        }
        Game.getCurrentPlayer().increaseEnergy(-3);
        Game.getCurrentPlayer().getBackPack().addToInventory(food, 1);
    }


    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }
    @Override
    public void increaseLevel() {
        if(level < 4) this.level ++;
    }
    @Override
    public boolean canGoToNextLevel() {
        if((level + 1) * 100 + 50 <= capacity) {
            capacity -= (level + 1)*100 + 50;
            increaseLevel();
            return true;
        }
        return false;
    }
    @Override
    public void increaseCapacity() {

    }

    @Override
    public boolean isMaxLevel() {
        return level == 4;
    }
    @Override
    public void handleLevelChangeTrophies(int level){

    }
}