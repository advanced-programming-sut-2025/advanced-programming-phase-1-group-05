package models.Skills;

import models.Enums.ItemLevel;

public interface Skill {
    ItemLevel getLevel();
    void setLevel(int level);
    void increaseLevel();
    boolean canGoToNextLevel();

}
