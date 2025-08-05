package org.example.Common.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Client.GameAssetManager;
import org.example.Common.GameMap;
import org.example.Common.GameTile;
import org.example.Server.models.*;
import org.example.Common.Enums.ItemLevel;
import org.example.Common.Enums.TileType;

import java.io.Serializable;

public class Hoe implements Tool <ItemLevel> , Serializable {
    ItemLevel level = ItemLevel.Normal;

    @Override
    public String getName() {
        return "Hoe";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public Result use(GameTile tile){
        GameAssetManager.playSfx("use hoe");
        if(!reduceEnergy(level.getEnergyUsage()))
            return new Result(false, "You don't have enough energy.");
        GameMap map = MyGame.getGameMap();
        if(tile.getTileType() == TileType.FarmFlat) {
            tile.setTileType(TileType.Soil);
        } else {
            return new Result(false, "You can't use the hoe on this tile");
        }
        return new Result(true, "Tile plowed successfully.");
    }
    @Override
    public boolean reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        if(MyGame.getCurrentPlayer().getEnergy() - amount < 0)return false;
        MyGame.getCurrentPlayer().increaseEnergy(-amount);
        return true;
    }
    @Override
    public ItemLevel getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.upgradeLevel();
        }
    }

    @Override
    public TextureRegion getTexture() {
        return level.getToolTextureRegion(this);
    }

}
