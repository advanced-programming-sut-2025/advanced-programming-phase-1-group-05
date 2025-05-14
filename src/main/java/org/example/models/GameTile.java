package org.example.models;

import org.example.models.Enums.BuildingType;
import org.example.models.Enums.MineralType;
import org.example.models.Enums.TileType;

public class GameTile {
    private int x, y;
    private TileType tileType;
    private BuildingType building;
    private boolean occupied = false;
    private Item itemOnTile = null;
    private boolean hasBeenFertilized = false;
    private String fertilizer = "";
    private boolean isOccupied = false;
    public static boolean greenHouseBuilt = false;
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

    public void fertilze(String fertilizer) {
        this.fertilizer = fertilizer;
        hasBeenFertilized = true;
    }

    public boolean hasBeenFertilized() {
        return hasBeenFertilized;
    }
    public String getFertilizer() {
        return fertilizer;
    }
    public boolean isOccupied() {
        return isOccupied;
    }
    public void occupy() {
        isOccupied = true;
    }

    @Override
    public String toString() {
        if (isOccupied) return "♥️";
        if (itemOnTile != null) {
            if(itemOnTile instanceof FruitAndVegetable) return "🍎";
            else if(itemOnTile instanceof Tree) return "\uD83C\uDF33";
            else if(itemOnTile instanceof Mineral) return "\uD83D\uDC8E";
            else if(itemOnTile instanceof ForagingItem) return "🍎";
            else if(itemOnTile.getName().equals("Wood")) return "\uD83E\uDEB5";
        }
        if (tileType == TileType.Building) return "🏠";
        if (tileType == TileType.Water) return "🌊";
        if (tileType == TileType.Soil) return "🟫";
        if (tileType == TileType.Flat) return "🟩";
        if (tileType == TileType.Mine) return "⛰\uFE0F";
        if (tileType == TileType.GreenHouse) return "\uD83C\uDF38";
        if (tileType == TileType.CheatThor) return "O";
        return "🟫";
    }

}