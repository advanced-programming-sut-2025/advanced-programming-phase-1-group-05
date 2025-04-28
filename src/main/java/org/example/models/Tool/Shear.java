package org.example.models.Tool;

import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Game;
import org.example.models.GameMap;
import org.example.models.GameTile;

import java.util.HashMap;
import java.util.Map;

public class Shear implements Tool<ItemLevel> {
    ItemLevel level;

    @Override
    public String getName() {
        return "Shear";
    }
    @Override
    public int getPrice() {
        return 1000;
    }

    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {

    }

    @Override
    public Map.Entry<Integer, Integer> getCoordinates() {
        return null;
    }

    @Override
    public void use(HashMap.Entry<Integer, Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());

        //if there's sheep on the tile
        if(true) {
            Game.getCurrentPlayer().getAnimalCare().shaveAnimal(tile);
        } else {
            System.out.println("No sheep on this tile!");
        }
        reduceEnergy(4);
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
}