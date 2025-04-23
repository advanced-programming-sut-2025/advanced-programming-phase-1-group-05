package org.example.models.Tool;

import org.example.models.Enums.ItemLevel;
import org.example.models.Item;

import java.util.HashMap;
import java.util.Map;

public interface Tool<T extends Enum<T>> extends Item {
    void use(Map.Entry<Integer, Integer> coordinates);
    void reduceEnergy(int amount);
    T getLevel();
    void upgradeLevel();
}