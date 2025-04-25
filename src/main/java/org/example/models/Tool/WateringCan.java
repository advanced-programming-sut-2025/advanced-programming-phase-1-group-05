package org.example.models.Tool;

import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Game;
import org.example.models.GameMap;
import org.example.models.GameTile;

import java.util.HashMap;
import java.util.Map;

public class WateringCan implements Tool<ItemLevel> {
    ItemLevel level = ItemLevel.Normal;
    int waterlevel = level.getWateringcanCapacity();

    @Override
    public void use(Map.Entry<Integer, Integer> coordinates){
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        ItemLevel farmingLevel = Game.getCurrentPlayer().getFarmingSkill().getLevel();
        int energyUsage = level.getEnergyUsage();

        if(tile.getTileType() == TileType.Plant) {
            if(farmingLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage);
            //water crop
            Game.getCurrentPlayer().getFarmingSkill().waterCrop(tile, this);
            if(waterlevel - 1 < 9) waterlevel = 0;
            else waterlevel--;
        } else if(tile.getTileType() == TileType.Water) {
            if(farmingLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage);
            waterlevel = level.getWateringcanCapacity();
        } else {
            if(farmingLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage - 1);
            System.out.println("You can't use the watering can on this tile");
        }

    }


    public int getWaterlevel() {
        return waterlevel;
    }
    public int getCapacity() {
        return level.getWateringcanCapacity();
    }
    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getPrice() {
        return 0;
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
        //only in blacksmith
        if (!level.isMaxLevel()) {
            level = level.nextLevel();
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