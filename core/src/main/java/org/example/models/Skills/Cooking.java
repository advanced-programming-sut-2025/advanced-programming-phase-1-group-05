package org.example.models.Skills;

import org.example.models.*;

public class Cooking implements Skill{
    int level = 0;
    int capacity = 0;


    public void cookFood(Food food) {
        for(Item f : food.getIngredients().keySet()) {
            MyGame.getCurrentPlayer().getBackPack().removeFromInventory(f, food.getIngredients().get(f));
        }
        MyGame.getCurrentPlayer().increaseEnergy(-3);
        MyGame.getCurrentPlayer().getBackPack().addToInventory(food, 1);
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
