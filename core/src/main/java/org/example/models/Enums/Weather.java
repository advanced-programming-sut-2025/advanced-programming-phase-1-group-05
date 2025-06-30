package org.example.models.Enums;

import org.example.models.*;

public enum Weather {
    Sunny(0, 1.5),
    Rain(1.5, 1.2),
    Storm(1.5, 4),
    Snow(2, 0);

    private final double energyCoefficient;
    private final double fishingCoefficient;
    Weather(double energyCoefficient, double fishingCoefficient) {
        this.energyCoefficient = energyCoefficient;
        this.fishingCoefficient = fishingCoefficient;
    }

    public Result thunder(int x, int y){
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(x, y);
        Item item = tile.getItemOnTile();
        if(item instanceof Tree) {
            ((Tree) item).thunderEffect(tile);
        }
        return null;
    }

//    @Override
//    public String toString() {
//        return super.toString();
//    }
    @Override
    public String toString() {
        String result = "";
        switch (this) {
            case Sunny: {
                result = "Sunny ☀️";
                break;
            }
            case Rain :{
                result = "Rain 🌧️";
                break;
            }
            case Storm :{
                result = "Storm ⛈️";
                break;
            }
            case Snow :{
                result = "Snow ❄️";
                break;
            }
        }
        return result;
    }

    public double getFishingCoefficient() {
        return fishingCoefficient;
    }
}
