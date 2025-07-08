package org.example.models.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.*;
import org.example.models.Enums.ItemLevel;

import java.util.HashMap;

public class MilkPail implements Tool<ItemLevel> {
    ItemLevel level = ItemLevel.Normal;

    @Override
    public String getName() {
        return "Milk Pail";
    }
    @Override
    public int getPrice() {
        return 0;
    }


    @Override
    public Result use(HashMap.Entry<Integer, Integer> coordinates){
        if(!reduceEnergy(4))
            return new Result(false, "You don't have enough energy");
        GameMap map = MyGame.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());

        Item item = tile.getItemOnTile();
        //TODO animal on tile

        if(item == null){
            return new Result(false, "No animal to milk!");
        } else if(item instanceof Animal){
            //TODO implement animals that can be milked
            MyGame.getCurrentPlayer().getAnimalCare().milkAnimal((Animal) item);
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
