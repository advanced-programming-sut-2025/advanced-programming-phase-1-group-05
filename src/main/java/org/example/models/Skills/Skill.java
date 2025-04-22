package org.example.models.Skills;

import org.example.models.Enums.ItemLevel;

public interface Skill {
    ItemLevel getLevel();
    void setLevel(int level);
    void increaseLevel();
    boolean canGoToNextLevel();

}