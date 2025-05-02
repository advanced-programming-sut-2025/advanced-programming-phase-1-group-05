package org.example.models.Skills;

import org.example.models.Enums.ItemLevel;
import org.example.models.GameTile;
import org.example.models.Tool.FishingPole;

public class Mining implements Skill{
    int level = 0;
    int capacity = 0;

    //TODO implement functions

    @Override
    public ItemLevel getLevel() {
        return null;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void increaseLevel() {
        if (level < 4) this.level++;
    }

    @Override
    public boolean canGoToNextLevel() {
        if ((level + 1) * 100 + 50 <= capacity) {
            capacity -= (level + 1) * 100 + 50;
            increaseLevel();
            return true;
        }
        return false;
    }
    @Override
    public void increaseCapacity() {
        this.capacity += 10;
    }
}
