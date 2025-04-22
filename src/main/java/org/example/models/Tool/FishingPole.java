package org.example.models.Tool;

import org.example.models.Enums.FishingPoleType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Game;
import org.example.models.GameMap;
import org.example.models.GameTile;

import java.util.HashMap;

public class FishingPole implements Tool<FishingPoleType> {
    FishingPoleType level = FishingPoleType.Normal;

    @Override
    public String getName() {
        return "Fishing Pole";
    }
    @Override
    public int getPrice() {
        return level.getPrice();
    }
    @Override
    public void use(HashMap.Entry<Integer, Integer> coordinates){
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        ItemLevel fishingLevel = Game.getCurrentPlayer().getFishingSkill().getLevel();
        int energyUsage = level.getEnergyUsage();

        if(tile.getTileType() == TileType.Water){
            if(fishingLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage);
            Game.getCurrentPlayer().getFishingSkill().fishing(tile, this);
            //fishing complications for later im tired dude
        } else {
            if(fishingLevel.isMaxLevel()) energyUsage --;
            reduceEnergy(energyUsage -1);
            System.out.println("Wrong tile! what are you trying to fish?");
        }
    }
    @Override
    public void reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        Game.getCurrentPlayer().addEnergy(-amount);
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
