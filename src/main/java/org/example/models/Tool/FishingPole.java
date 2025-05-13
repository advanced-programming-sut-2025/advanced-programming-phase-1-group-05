package org.example.models.Tool;

import org.example.models.Enums.FishingPoleType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Game;
import org.example.models.GameMap;
import org.example.models.GameTile;
import org.example.models.Result;
import org.example.models.Skills.Fishing;

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
        Fishing fishing = Game.getCurrentPlayer().getFishingSkill();
        int energyUsage = level.getEnergyUsage();

        if(tile.getTileType() == TileType.Water){
            if(fishing.isMaxLevel()) energyUsage --;
            if(!reduceEnergy(energyUsage))
                return new Result(false, "You don't have enough energy");
            Game.getCurrentPlayer().getFishingSkill().fishing(tile, this);
            //TODO implement fishing
        } else {
            if(fishing.isMaxLevel()) energyUsage --;
            if(!reduceEnergy(energyUsage))
                return new Result(false, "You don't have enough energy");
            return new Result(false, "Wrong tile! what are you trying to fish?");
        }
        return new Result(true, "");
    }

    @Override
    public boolean reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        if(Game.getCurrentPlayer().getEnergy() - amount < 0)return false;
        Game.getCurrentPlayer().increaseEnergy(-amount);
        return true;
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
}
