package org.example.models.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.*;
import org.example.models.Enums.ItemLevel;

import java.util.HashMap;

public class Scythe implements Tool<ItemLevel> {
    ItemLevel level = ItemLevel.Normal;
    Database database = new Database();
    @Override
    public String getName() {
        return "Scythe";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public Result use(HashMap.Entry<Integer, Integer> coordinates) {
        if(!reduceEnergy(2))
            return new Result(false, "You don't have enough energy");
        GameMap map = MyGame.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());

        Item item = tile.getItemOnTile();

        if(item == null){
            return new Result(false, "You can't use the scythe on this tile");
        } else {
            if(item instanceof FruitAndVegetable){
                if(!((FruitAndVegetable) item).isFullyGrown())
                    return new Result(false, "The crop isn't ready for harvest");
                MyGame.getCurrentPlayer().getFarmingSkill().harvestCrop(tile);
                return new Result(true, item.getName() + " harvested successfully");
            } else if(item == database.getItem("Fiber")) {
                MyGame.getCurrentPlayer().getBackPack().addToInventory(tile.getItemOnTile(), 1);
                tile.setItemOnTile(null);
                return new Result(true, item.getName() + " harvested successfully");
            } else if(item instanceof ForagingItem) {
                MyGame.getCurrentPlayer().getBackPack().addToInventory(tile.getItemOnTile(), 1);
                tile.setItemOnTile(null);
                return new Result(true, item.getName() + " harvested successfully");
            } else if(item instanceof Tree) {
                Result result = ((Tree)item).harvestFruit();
                if(result.isSuccess()) {
                    ForagingItem fruit = ((Tree) item).getHarvestedFruit();
                    MyGame.getCurrentPlayer().getBackPack().addToInventory(fruit, 1);
                }
                return result;
            }
        }
        return new Result(true, "");
    }
    @Override
    public boolean reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        if(MyGame.getCurrentPlayer().getEnergy() - amount < 0)return false;
        MyGame.getCurrentPlayer().increaseEnergy(-amount);
        return true;
    }
    @Override
    public ItemLevel getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.upgradeLevel();
        }
    }
    @Override
    public TextureRegion getTexture() {
        return level.getToolTextureRegion(this);
    }
}
