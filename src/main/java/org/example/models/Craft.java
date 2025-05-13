package org.example.models;

import org.example.models.Enums.CraftType;
import org.example.models.Enums.Material;
import org.example.models.Enums.MineralType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Craft extends BasicItem implements Item {
    private Map.Entry<Integer,Integer> coordinates;
    private final CraftType type;

    public Craft(CraftType type) {
        super(type.getName(), type.getPrice());
        this.type = type;
    }

    public Map<Material, Integer> getIngredients() {
        return type.getIngredients();
    }

    public String getSource() {
        return type.getSource();
    }

    public Map.Entry<Integer,Integer> getCoordinates() {
        return coordinates;
    }

    //build craft
    public boolean buildCraft() {
        Map<Material, Integer> ingredients = type.getIngredients();
        HashMap<Item, Integer> inventory = Game.getCurrentPlayer().getBackPack().getInventory();
        for(Item item : inventory.keySet()) {
            if(ingredients.containsKey(((BasicItem)item).getMaterial())) {
                if(inventory.get(item) < ingredients.get(((BasicItem)item).getMaterial())) {
                    return false;
                }
            }
        }
        Game.getCurrentPlayer().increaseEnergy(-2);
        for(Item item : inventory.keySet()) {
            if(ingredients.containsKey(((BasicItem)item).getMaterial())) {
                Game.getCurrentPlayer().getBackPack().removeFromInventory(item, ingredients.get(((BasicItem)item).getMaterial()));
            }
        }
        return true;

    }

    private ArrayList<GameTile> getTiles(GameTile currentTile) {
        int x = currentTile.getX();
        int y = currentTile.getY();
        ArrayList<GameTile> tiles = new ArrayList<>();
        GameMap map = Game.getGameMap();

        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < GameMap.MAP_WIDTH &&
                        ny >= 0 && ny < GameMap.MAP_HEIGHT) {
                    tiles.add(map.getTile(nx, ny));
                }
            }
        }

        return tiles;
    }
    
    public Result useCraft(GameTile currentTile) {
        ArrayList<GameTile> adjacentTiles = getTiles(currentTile);
        if(type.getName().contains("Bomb")){
            int MAX_TILES = 0;
            int tilesDestroyed = 0;
            if(type.getName().equals("Cherry Bomb")) MAX_TILES = 3;
            else if(type.getName().equals("Bomb")) MAX_TILES = 5;
            else if(type.getName().equals("Mega Bomb")) MAX_TILES = 7;
            for(GameTile tile : adjacentTiles) {
                if(tile.getItemOnTile() != null && tilesDestroyed < MAX_TILES) {
                    tile.setItemOnTile(null);
                    tilesDestroyed++;
                }
            }
            return new Result(true, "The bomb destroyed " + tilesDestroyed + " tiles");
        } else if(type.getName().contains("Sprinkler")) {
            int MAX_TILES = 0;
            int watered = 0;
            if(type.getName().equals("Sprinkler")) MAX_TILES = 4;
            else if(type.getName().equals("Quality Sprinkler")) MAX_TILES = 8;
            else if(type.getName().equals("Iridium Sprinkler")) MAX_TILES = 24;
            for(GameTile tile : adjacentTiles) {
                Item item = tile.getItemOnTile();
                if(item != null && watered < MAX_TILES ) {
                    if(item instanceof FruitAndVegetable) ((FruitAndVegetable)item).waterCrop();
                    watered++;
                }
            }
            return new Result(true, "The Sprinkler watered " + watered + " crops");
        } else if(type.getName().equals("Charcoal Klin")) {
            Item wood = Game.getDatabase().getItem("Wood");
            if(Game.getCurrentPlayer().getBackPack().getInventory().containsKey(wood)) {
                if (Game.getCurrentPlayer().getBackPack().getInventory().get(wood) < 10)
                    return new Result(false, "Not enough wood");
                else Game.getCurrentPlayer().getBackPack().removeFromInventory(wood, 10);
                BasicItem coal = new BasicItem("Coal", MineralType.Coal.getPrice());
                coal.setMaterial(MineralType.Coal);
                currentTile.setItemOnTile(coal);
                return new Result(true, "Wood turned to coal");
            }
        } else if(type.getName().equals("Furnace")) {
            Item item = currentTile.getItemOnTile();
            if(MineralType.isMineral(((BasicItem)item).getMaterial())) {
                ((BasicItem)item).setMaterial(MineralType.Gold);
            }
            return new Result(true, "Mineral turned to gold");
        } else if(type.getName().contains("Scarecrow")) {
            int MAX_TILES = 0;
            int protectedTiles = 0;
            if(type.getName().equals("Scarecrow")) MAX_TILES = 8;
            else if(type.getName().equals("Deluxe Scarecrow")) MAX_TILES = 12;
            for(GameTile tile : adjacentTiles) {
                Item item = tile.getItemOnTile();
                if(item != null && protectedTiles < MAX_TILES ) {
                    if(item instanceof FruitAndVegetable) {
                        ((FruitAndVegetable)item).setProtectedByScareCrow(true);
                        protectedTiles++;
                    } else if(item instanceof Tree) {
                        ((Tree)item).setProtectedByScareCrow(true);
                        protectedTiles++;
                    }
                }
            }
            return new Result(true, "The Scarecrow now protects " + protectedTiles + " tiles");
        }
        return null;
    }
    
    @Override
    public String getName() {
        return type.getName();
    }
    @Override
    public int getPrice() {
        return type.getPrice();
    }


}
