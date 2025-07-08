package org.example.models.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.*;
import org.example.models.Enums.ItemLevel;

import java.util.HashMap;

public class TrashCan implements Tool <ItemLevel> {
    ItemLevel level = ItemLevel.Normal;

    public void removeFromInventory(String name, int quantity){
        Item itemToRemove = null;
        for(Item item : MyGame.getCurrentPlayer().getBackPack().getInventory().keySet()) {
            if(item.getName().equals(name)) {
                itemToRemove = item;
            }
        }
        MyGame.getCurrentPlayer().getBackPack().removeFromInventory(itemToRemove, quantity);
    }

    public void setLevel(ItemLevel level){
        this.level = level;
    }
    @Override
    public String getName() {
        return "Trash Can";
    }
    @Override
    public int getPrice() {
        return 1000;
    }
    @Override
    public Result use(HashMap.Entry<Integer, Integer> coordinates) {
        //nada
        return new Result(false, "");
    }
    @Override
    public boolean reduceEnergy(int amount){
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
