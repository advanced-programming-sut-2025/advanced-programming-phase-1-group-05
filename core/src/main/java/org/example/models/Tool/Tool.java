package org.example.models.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Enums.ItemLevel;
import org.example.models.Item;
import org.example.models.Result;

import java.util.HashMap;
import java.util.Map;

public interface Tool<T extends Enum<T>> extends Item {
    Result use(Map.Entry<Integer, Integer> coordinates);
    boolean reduceEnergy(int amount);
    T getLevel();
    void upgradeLevel();
    TextureRegion getTexture();
}
