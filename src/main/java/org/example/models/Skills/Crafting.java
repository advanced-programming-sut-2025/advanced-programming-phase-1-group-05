package org.example.models.Skills;

import org.example.models.Enums.ItemLevel;
import org.example.models.GameTile;
import org.example.models.Result;

public class Crafting implements Skill{
    int level = 0;
    int capacity = 0;

//    public Result craftItem(String itemType, Inventory inventory) {}

    public void placeItemOnGround(String item, GameTile tile) {}

    @Override
    public int getLevel() {
        return level;
    }

    //    public void addItemToInventory(String item, Inventory inventory) {}
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

}