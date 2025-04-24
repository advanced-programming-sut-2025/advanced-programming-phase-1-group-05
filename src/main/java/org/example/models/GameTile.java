package org.example.models;

import org.example.models.Enums.BuildingType;
import org.example.models.Enums.TileType;

public class GameTile {
    private int x, y;
    private TileType tileType;
    private BuildingType building;

    public GameTile(int x, int y, TileType tileType) {
        this.x = x;
        this.y = y;
    }
    public TileType getTileType() {
        return tileType;
    }
    //for changing tile type
    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }
    public boolean isTileValidForPlanting(){
        for(FruitAndVegetable f: Game.getGameMap().getPlants()) {
            if(f.getCoordinates().getKey() == x && f.getCoordinates().getValue() == y) {
                return true;
            }
        }
        for(Tree tree: Game.getGameMap().getTrees()) {
            if(tree.getCoordinates().getKey() == x && tree.getCoordinates().getValue() == y) {
                return true;
            }
        }
        return false;
    }

}