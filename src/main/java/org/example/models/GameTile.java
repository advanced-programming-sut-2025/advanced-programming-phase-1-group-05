package org.example.models;

import org.example.models.Enums.BuildingType;
import org.example.models.Enums.TileType;

public class GameTile {
    private int x, y;
    private TileType tileType;
    private BuildingType building;
    private boolean occupied = false;
    private Item itemOnTile = null;
    // can't be walked over if it's occupied

    public GameTile(int x, int y, TileType tileType) {
        this.x = x;
        this.y = y;
        this.tileType = tileType;
    }
    public Item getItemOnTile() {
        return itemOnTile;
    }
    public void setItemOnTile(Item itemOnTile) {
        this.itemOnTile = itemOnTile;
    }
    public TileType getTileType() {
        return tileType;
    }
    //for changing tile type
    public void setBuilding(BuildingType building) {
        this.building = building;
        this.occupied = true;
    }
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
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        if (itemOnTile != null) return "🍎";
        if (building != null) return "🏠";
        if (tileType == TileType.Water) return "🌊";
        if (tileType == TileType.Soil) return "🟫";
        if (tileType == TileType.Flat) return "🟩";
        return "⬛";
    }

}