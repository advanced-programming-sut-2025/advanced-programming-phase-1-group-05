package org.example.models.Skills;

import org.example.models.Enums.Seed;
import org.example.models.GameTile;
import org.example.models.Result;
import org.example.models.Tool.Hoe;
import org.example.models.Tool.Scythe;
import org.example.models.Tool.WateringCan;

public class Farming implements Skill{
    int level = 0;
    int capacity = 0;

    //شخم زدن
    public void plowTile(GameTile tile, Hoe hoe) {}
    //کاشتن دانه
    public void plantSeed(Seed seed, GameTile tile) {}
    //کود دادن
    public void fertilizeCrop(GameTile tile, String fertilizer) {}
    //آبیاری
    public Result waterCrop(GameTile tile, WateringCan wateringCan) {return null;}
    //درو کردن
    public void harvestCrop(GameTile tile, Scythe scythe) {}

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