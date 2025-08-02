package org.example.Server.models.Skills;

public interface Skill {
    int getLevel();
    void setLevel(int level);
    void increaseLevel();
    boolean canGoToNextLevel();
    void increaseCapacity();
    boolean isMaxLevel();
    void handleLevelChangeTrophies(int level);

}
