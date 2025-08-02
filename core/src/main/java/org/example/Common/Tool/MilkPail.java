package org.example.Common.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Common.GameMap;
import org.example.Common.GameTile;
import org.example.Common.Item;
import org.example.Server.models.*;
import org.example.Common.Enums.ItemLevel;

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
    public Result use(GameTile tile){
        if(!reduceEnergy(4))
            return new Result(false, "You don't have enough energy");
        GameMap map = MyGame.getGameMap();

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
