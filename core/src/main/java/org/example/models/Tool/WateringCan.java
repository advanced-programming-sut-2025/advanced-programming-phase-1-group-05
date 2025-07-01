package org.example.models.Tool;

import org.example.models.*;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Skills.Farming;

import java.util.Map;

public class WateringCan implements Tool<ItemLevel> {
    ItemLevel level = ItemLevel.Normal;
    int waterlevel = level.getWateringcanCapacity();

    @Override
    public Result use(Map.Entry<Integer, Integer> coordinates){
        GameMap map = MyGame.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        Farming farming = MyGame.getCurrentPlayer().getFarmingSkill();
        int energyUsage = level.getEnergyUsage();

        Item item = tile.getItemOnTile();
        if(item == null){
            if(tile.getTileType() == TileType.Water){
                if(farming.isMaxLevel()) energyUsage --;
                if(!reduceEnergy(energyUsage))
                    return new Result(false, "You don't have enough energy");
                waterlevel = level.getWateringcanCapacity();
                return new Result(true, "Watering can successfully filled up!");
            } else return new Result(false, "Nothing to water!");
        } else {
            if(item instanceof FruitAndVegetable) {
                if(farming.isMaxLevel()) energyUsage --;
                if(!reduceEnergy(energyUsage))
                    return new Result(false, "You don't have enough energy");
                MyGame.getCurrentPlayer().getFarmingSkill().waterCrop((FruitAndVegetable)item);
                if(waterlevel - 1 < 9) waterlevel = 0;
                else waterlevel--;
                return new Result(true, "Crop was successfully watered!");
            }
        }
        return new Result(false, "Nothing to water!");
    }


    public int getWaterlevel() {
        return waterlevel;
    }
    public int getCapacity() {
        return level.getWateringcanCapacity();
    }
    @Override
    public String getName() {
        return "Watering Can";
    }

    @Override
    public int getPrice() {
        return 0;
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
        //only in blacksmith
        if (!level.isMaxLevel()) {
            level = level.upgradeLevel();
        }
    }
}
