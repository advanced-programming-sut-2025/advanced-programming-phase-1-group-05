package org.example.models.Tool;

import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.MyGame;
import org.example.models.GameMap;
import org.example.models.GameTile;
import org.example.models.Result;

import java.util.HashMap;

public class Hoe implements Tool <ItemLevel> {
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
    public Result use(HashMap.Entry<Integer, Integer> coordinates){
        if(!reduceEnergy(level.getEnergyUsage()))
            return new Result(false, "You don't have enough energy.");
        GameMap map = MyGame.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        if(tile.getTileType() == TileType.Flat) {
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

}
