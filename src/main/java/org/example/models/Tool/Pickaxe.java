package org.example.models.Tool;

import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Game;
import org.example.models.GameMap;
import org.example.models.GameTile;
import org.example.models.Result;

import java.util.HashMap;
import java.util.Map;

public class Pickaxe implements Tool <ItemLevel> {
    ItemLevel level = ItemLevel.Normal;

    @Override
    public String getName() {
        return "Pickaxe";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public Result use(HashMap.Entry<Integer, Integer> coordinates){
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        ItemLevel miningLevel = Game.getCurrentPlayer().getMiningSkill().getLevel();
        int energyUsage = level.getEnergyUsage();
        //or just set to flat
        if(tile.getTileType() == TileType.Stone) {
            if(miningLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage);
            tile.setTileType(TileType.Flat);
        } else if(tile.getTileType() == TileType.Soil) {
            if(miningLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(level.getEnergyUsage());
            tile.setTileType(TileType.Flat);
        } else if(tile.getTileType() == TileType.Object) {
            if(miningLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(level.getEnergyUsage());
            tile.setTileType(TileType.Flat);
        } else {
            if(miningLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage - 1);
            return new Result(false, "You can't use the pickaxe on this tile");
        }
        return new Result(true, "");
    }

    @Override
    public void reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        Game.getCurrentPlayer().increaseEnergy(-amount);
    }
    @Override
    public ItemLevel getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.upgradeLevel();
            System.out.println(getName() + " upgraded to " + level.getName());
        }
    }
    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {
    }
    @Override
    public Map.Entry<Integer, Integer> getCoordinates() {
        return null;
    }

}