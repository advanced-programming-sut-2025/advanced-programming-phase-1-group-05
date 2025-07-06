package org.example.models.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.*;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.TileType;
import org.example.models.Skills.Skill;

import java.util.HashMap;

public class Axe implements Tool <ItemLevel>{
    ItemLevel level = ItemLevel.Normal;
    Database database = new Database();

    @Override
    public String getName() {
        return "Axe";
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public Result use(HashMap.Entry<Integer, Integer> coordinates){
        GameMap map = MyGame.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        Skill foraging = MyGame.getCurrentPlayer().getForagingSkill();
        int energyUsage = level.getEnergyUsage();

        Item item = tile.getItemOnTile();
        if(item == null) {
            if(foraging.isMaxLevel()) energyUsage --;
            if(!reduceEnergy(energyUsage -1)) {
                return new Result(false, "You don't have enough energy");
            }
            return new Result(false, "You can't use the axe on this tile");
        }
        if(tile.getTileType() == TileType.Flat || tile.getTileType() == TileType.Soil) {
            if (foraging.isMaxLevel()) energyUsage--;
            if (!reduceEnergy(energyUsage - 1)) {
                return new Result(false, "You don't have enough energy");
            }
            if (item instanceof Tree) {
                tile.setItemOnTile(null);
                Item wood = database.getItem("Wood");
                MyGame.getCurrentPlayer().getBackPack().addToInventory(wood, 8);
                Item sap = new BasicItem(((Tree) item).getName() + " sap", 0);
                MyGame.getCurrentPlayer().getBackPack().addToInventory(sap, 2);
                Result result = ((Tree)item).cutDownTree();
                return result;
            } else if (item.getName().equals("Wood")) {
                tile.setItemOnTile(null);
                MyGame.getCurrentPlayer().getBackPack().addToInventory(item, 1);
            } else {
                if (foraging.isMaxLevel()) energyUsage--;
                if (!reduceEnergy(energyUsage - 1)) {
                    return new Result(false, "You don't have enough energy");
                }
                return new Result(false, "You can't use the axe on this tile");
            }
        } else {
            if (foraging.isMaxLevel()) energyUsage--;
            if (!reduceEnergy(energyUsage - 1)) {
                return new Result(false, "You don't have enough energy");
            }
            return new Result(false, "You can't use the axe on this tile");
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
            System.out.println(getName() + " upgraded to " + level.getName());
        }
    }

    @Override
    public TextureRegion getTexture() {
       return level.getToolTextureRegion(this);
    }
}
