package models.Skills;

import models.Enums.Seed;
import models.Tool.Axe;
import models.Tool.Pickaxe;

public class Foraging implements Skill{
    int level;
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
}
