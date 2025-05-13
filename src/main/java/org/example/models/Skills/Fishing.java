package org.example.models.Skills;

import org.example.models.Enums.ItemLevel;
import org.example.models.GameTile;
import org.example.models.Tool.FishingPole;

public class Fishing implements Skill {
    int level = 0;
    int capacity = 0;

    public void fishing (GameTile tile, FishingPole pole) {
        //TODO implement fishing
        //if successful
        increaseCapacity();
    }

    @Override
    public int getLevel() {
        return level;
    }
    @Override
    public boolean isMaxLevel() {
        return level == 10;
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
        this.capacity += 10;
    }
    @Override
    public void handleLevelChangeTrophies(int level){

    }
}