package org.example.models;

public class GameMap {
    GameTile[][] map;
    public GameTile getTile(int x, int y) {
        return map[x][y];
    }

    public void placeItemOnTile(Item item, int x, int y) {}

    public void removeItemFromTile(int x, int y) {}
}
