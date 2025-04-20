package models;

import models.Enums.BuildingType;
import models.Enums.TileType;

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
}
