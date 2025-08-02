package org.example.Common.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Common.GameTile;
import org.example.Common.Item;
import org.example.Server.models.Result;

public interface Tool<T extends Enum<T>> extends Item {
    Result use(GameTile tile);
    boolean reduceEnergy(int amount);
    T getLevel();
    void upgradeLevel();
    TextureRegion getTexture();
}
