package models.Tool;

import models.Enums.ItemLevel;
import models.Item;

import java.util.HashMap;

public interface Tool extends Item {
    void use(HashMap.Entry<Integer, Integer> coordinates);
    void reduceEnergy();
    ItemLevel getLevel();
    void upgradeLevel();
}
