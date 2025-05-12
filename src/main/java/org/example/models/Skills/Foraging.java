package org.example.models.Skills;

import org.example.models.Game;
import org.example.models.GameTile;
import org.example.models.Item;
import org.example.models.Tool.Axe;

public class Foraging implements Skill{
    int level = 0;
    int capacity = 0;

    //TODO add tool
    public void forageItem(GameTile tile) {
        Item item = tile.getItemOnTile();
        Game.getCurrentPlayer().getBackPack().addToInventory(item, 1);
        increaseCapacity();
    }

    public void chopTree(String treeType, Axe axe) {}

    //public Seed getRandomSeedFromWildTree() { return null; }

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
    public int getLevel(){
        return level;
    }
    @Override
    public void increaseCapacity() {
        this.capacity += 10;
    }
    @Override
    public boolean isMaxLevel() {
        return level == 10;
    }
}