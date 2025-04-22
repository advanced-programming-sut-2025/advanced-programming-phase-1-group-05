package models.Tool;

import models.Enums.ItemLevel;
import models.Enums.TileType;
import models.Game;
import models.GameMap;
import models.GameTile;

import java.util.HashMap;

public class Pickaxe implements Tool {
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
    public void use(HashMap.Entry<Integer, Integer> coordinates){
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        //mining skill level ??
        ItemLevel miningLevel = Game.getCurrentPlayer().getForagingSkill().getLevel();
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
            System.out.println("You can't use the pickaxe on this tile");
        }
    }

    @Override
    public void reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        Game.getCurrentPlayer().addEnergy(-amount);
    }
    @Override
    public ItemLevel getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.nextLevel();
            System.out.println(getName() + " upgraded to " + level.getName());
        }
    }

}
