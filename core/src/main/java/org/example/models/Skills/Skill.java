package org.example.models.Skills;

import org.example.models.Enums.ItemLevel;

public interface Skill {
    int getLevel();
    void setLevel(int level);
    void increaseLevel();
    boolean canGoToNextLevel();
    void increaseCapacity();
    boolean isMaxLevel();
    void handleLevelChangeTrophies(int level);

}