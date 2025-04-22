package models.Tool;

import models.Enums.ItemLevel;
import models.Enums.TileType;
import models.Game;
import models.GameMap;
import models.GameTile;

import java.util.HashMap;

public class Hoe implements Tool {
    ItemLevel level = ItemLevel.Normal;

    @Override
    public String getName() {
        return "Hoe";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public void use(HashMap.Entry<Integer, Integer> coordinates){
        reduceEnergy(level.getEnergyUsage());
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        if(tile.getTileType() == TileType.Flat) {
            tile.setTileType(TileType.Soil);
        } else {
            System.out.println("You can't use the hoe on this tile");
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
