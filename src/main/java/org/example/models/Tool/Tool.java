package org.example.models.Tool;

import org.example.models.Enums.ItemLevel;
import org.example.models.Item;

import java.util.HashMap;

public interface Tool<T extends Enum<T>> extends Item {
    void use(HashMap.Entry<Integer, Integer> coordinates);
    void reduceEnergy(int amount);
    T getLevel();
    void upgradeLevel();
}