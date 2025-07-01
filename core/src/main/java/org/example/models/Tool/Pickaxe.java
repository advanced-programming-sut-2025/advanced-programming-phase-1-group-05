package org.example.models.Tool;

import org.example.models.*;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Skills.Mining;
import org.example.models.Skills.Skill;

import java.util.HashMap;

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
        GameMap map = MyGame.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        Skill mining = MyGame.getCurrentPlayer().getMiningSkill();
        int energyUsage = level.getEnergyUsage();

        Item item = tile.getItemOnTile();
        if(item == null && tile.getTileType() == TileType.Soil){
            if(mining.isMaxLevel()) energyUsage --;
            if(!reduceEnergy(energyUsage))
                return new Result(false, "You don't have enough energy");
            tile.setTileType(TileType.Flat);
        } else if(item == null && tile.getTileType() == TileType.Water){
            return new Result(false, "You can't use the pickaxe on this tile");
        } else if(item != null) {
            if(mining.isMaxLevel()) energyUsage --;
            if(!reduceEnergy(energyUsage))
                return new Result(false, "You don't have enough energy");
            if(item instanceof Mineral) {
                ((Mining) mining).mine(tile, this);
                return new Result(true, "Successfully mined a mineral");
            } else if(item instanceof Craft) {
                MyGame.getCurrentPlayer().getBackPack().addToInventory(tile.getItemOnTile(), 1);
                tile.setItemOnTile(null);
            }
            else MyGame.getCurrentPlayer().getForagingSkill().forageItem(tile);
        }

        return new Result(true, "");
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
