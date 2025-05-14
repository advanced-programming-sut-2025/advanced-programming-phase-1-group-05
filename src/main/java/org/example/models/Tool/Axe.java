package org.example.models.Tool;

import org.example.models.*;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Skills.Skill;

import java.util.HashMap;
import java.util.Map;

public class Axe implements Tool <ItemLevel>{
    ItemLevel level = ItemLevel.Normal;
    Database database = new Database();

    @Override
    public String getName() {
        return "Axe";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public Result use(HashMap.Entry<Integer, Integer> coordinates){
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        Skill foraging = Game.getCurrentPlayer().getForagingSkill();
        int energyUsage = level.getEnergyUsage();

        Item item = tile.getItemOnTile();
        if(item == null) {
            if(foraging.isMaxLevel()) energyUsage --;
            if(!reduceEnergy(energyUsage -1)) {
                return new Result(false, "You don't have enough energy");
            }
            return new Result(false, "You can't use the axe on this tile");
        }
        if(tile.getTileType() == TileType.Flat || tile.getTileType() == TileType.Soil) {
            if (foraging.isMaxLevel()) energyUsage--;
            if (!reduceEnergy(energyUsage - 1)) {
                return new Result(false, "You don't have enough energy");
            }
            if (item instanceof Tree) {
                tile.setItemOnTile(null);
                Item wood = database.getItem("Wood");
                Game.getCurrentPlayer().getBackPack().addToInventory(wood, 8);
                Item sap = new BasicItem(((Tree) item).getName() + " sap", 0);
                Game.getCurrentPlayer().getBackPack().addToInventory(sap, 2);
                Result result = ((Tree)item).cutDownTree();
                return result;
            } else if (item.getName().equals("Wood")) {
                tile.setItemOnTile(null);
                Game.getCurrentPlayer().getBackPack().addToInventory(item, 1);
            } else {
                if (foraging.isMaxLevel()) energyUsage--;
                if (!reduceEnergy(energyUsage - 1)) {
                    return new Result(false, "You don't have enough energy");
                }
                return new Result(false, "You can't use the axe on this tile");
            }
        } else {
            if (foraging.isMaxLevel()) energyUsage--;
            if (!reduceEnergy(energyUsage - 1)) {
                return new Result(false, "You don't have enough energy");
            }
            return new Result(false, "You can't use the axe on this tile");
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
}