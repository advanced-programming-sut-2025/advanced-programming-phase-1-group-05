package org.example.models.Tool;

import org.example.models.Enums.FishingPoleType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Game;
import org.example.models.GameMap;
import org.example.models.GameTile;
import org.example.models.Result;

import java.util.HashMap;
import java.util.Map;

public class FishingPole implements Tool<FishingPoleType> {
    FishingPoleType level = FishingPoleType.Training;

    @Override
    public String getName() {
        return "Fishing Pole";
    }
    @Override
    public int getPrice() {
        return level.getPrice();
    }
    @Override
    public Result use(HashMap.Entry<Integer, Integer> coordinates){
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        ItemLevel fishingLevel = Game.getCurrentPlayer().getFishingSkill().getLevel();
        int energyUsage = level.getEnergyUsage();

        if(tile.getTileType() == TileType.Water){
            if(fishingLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage);
            Game.getCurrentPlayer().getFishingSkill().fishing(tile, this);
            //TODO implement fishing
        } else {
            if(fishingLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage -1);
            return new Result(false, "Wrong tile! what are you trying to fish?");
        }
        return new Result(true, "");
    }

    @Override
    public void reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        Game.getCurrentPlayer().increaseEnergy(-amount);
    }
    @Override
    public FishingPoleType getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
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
