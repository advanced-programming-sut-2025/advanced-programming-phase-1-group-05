package org.example.models.Skills;

import org.example.models.Enums.Seed;
import org.example.models.Tool.Axe;
import org.example.models.Tool.Pickaxe;

public class Foraging implements Skill{
    int level = 0;
    int capacity = 0;
    public void spawnDailyForagingItems() {}

    public void placeSeedsOnEmptyTiles() {}

    public void spawnMineralsInMine() {}

    public void chopTree(String treeType, Axe axe) {}

    public void mineRock(String rockType, Pickaxe pickaxe) {}

    public Seed getRandomSeedFromWildTree() { return null; }

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
}