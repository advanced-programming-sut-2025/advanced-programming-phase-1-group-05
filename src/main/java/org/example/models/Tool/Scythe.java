package org.example.models.Tool;

import org.example.models.Enums.FishingPoleType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Game;
import org.example.models.GameMap;
import org.example.models.GameTile;

import java.util.HashMap;
import java.util.Map;

public class Scythe implements Tool<ItemLevel> {
    ItemLevel level = ItemLevel.Normal;

    @Override
    public String getName() {
        return "Scythe";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public void use(HashMap.Entry<Integer, Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());

        if(tile.getTileType() == TileType.Plant) {
            //different for crops and grass
            Game.getCurrentPlayer().getFarmingSkill().harvestCrop(tile, this);
        } else {
            System.out.println("You can't use the scythe on this tile");
        }
        reduceEnergy(2);
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