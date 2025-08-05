package org.example.Common.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Client.GameAssetManager;
import org.example.Common.GameMap;
import org.example.Common.GameTile;
import org.example.Common.Item;
import org.example.Server.models.*;
import org.example.Common.Enums.ItemLevel;
import org.example.Common.Enums.TileType;
import org.example.Server.models.Skills.Farming;

import java.io.Serializable;

public class WateringCan implements Tool<ItemLevel> , Serializable {
    ItemLevel level = ItemLevel.Normal;
    int waterlevel = level.getWateringcanCapacity();

    @Override
    public Result use(GameTile tile){
        GameAssetManager.playSfx("use watering can");
        GameMap map = MyGame.getGameMap();
        Farming farming = MyGame.getCurrentPlayer().getFarmingSkill();
        int energyUsage = level.getEnergyUsage();

        Item item = tile.getItemOnTile();
        if(item == null){
            if(tile.getTileType() == TileType.Water){
                if(farming.isMaxLevel()) energyUsage --;
                if(!reduceEnergy(energyUsage))
                    return new Result(false, "You don't have enough energy");
                waterlevel = level.getWateringcanCapacity();
                return new Result(true, "Watering can successfully filled up!");
            } else {
                if (tile.getTileType() == TileType.Soil) tile.setTileType(TileType.WateredSoil);
                return new Result(false, "Nothing to water!");
            }
        } else {
            if(item instanceof FruitAndVegetable) {
                if(farming.isMaxLevel()) energyUsage --;
                if(!reduceEnergy(energyUsage))
                    return new Result(false, "You don't have enough energy");
                MyGame.getCurrentPlayer().getFarmingSkill().waterCrop((FruitAndVegetable)item);
                if(waterlevel - 1 < 9) waterlevel = 0;
                else waterlevel--;
                tile.setTileType(TileType.WateredSoil);
                return new Result(true, "Crop was successfully watered!");
            }
        }
        return new Result(false, "Nothing to water!");
    }


    public int getWaterlevel() {
        return waterlevel;
    }
    public int getCapacity() {
        return level.getWateringcanCapacity();
    }
    @Override
    public String getName() {
        return "Watering Can";
    }

    @Override
    public int getPrice() {
        return 0;
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
