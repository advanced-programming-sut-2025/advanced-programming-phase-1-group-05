package org.example.models.Tool;

import org.example.models.*;
import org.example.models.Enums.FishingPoleType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;

import java.util.HashMap;
import java.util.Map;

public class Scythe implements Tool<ItemLevel> {
    ItemLevel level = ItemLevel.Normal;
    Database database = new Database();
    @Override
    public String getName() {
        return "Scythe";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public Result use(HashMap.Entry<Integer, Integer> coordinates) {
        if(!reduceEnergy(2))
            return new Result(false, "You don't have enough energy");
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());

        Item item = tile.getItemOnTile();

        if(item == null){
            return new Result(false, "You can't use the scythe on this tile");
        } else {
            if(item instanceof FruitAndVegetable){
                Game.getCurrentPlayer().getFarmingSkill().harvestCrop(tile);
            } else if(item == database.getItem("Fiber")) {
                Game.getCurrentPlayer().getBackPack().addToInventory(tile.getItemOnTile(), 1);
                tile.setItemOnTile(null);
            }
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
    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {
    }
    @Override
    public Map.Entry<Integer, Integer> getCoordinates() {
        return null;
    }
}