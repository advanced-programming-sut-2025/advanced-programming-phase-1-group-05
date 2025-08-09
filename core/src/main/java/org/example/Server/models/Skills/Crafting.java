package org.example.Server.models.Skills;

import org.example.Server.models.MyGame;

import java.io.Serializable;

public class Crafting implements Skill, Serializable {
    int level = 0;
    int capacity = 0;

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
            MyGame.getCurrentPlayer().getUser().updateUserInfo(1, "skill");
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
