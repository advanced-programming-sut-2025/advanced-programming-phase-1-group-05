package org.example.Server.models.Skills;

import org.example.Common.Enums.CookingRecipeType;
import org.example.Common.Enums.CraftType;
import org.example.Server.models.MyGame;
import org.example.Common.GameTile;
import org.example.Common.Item;

import java.io.Serializable;

public class Foraging implements Skill , Serializable {
    int level = 0;
    int capacity = 0;

    //TODO add tool
    public void forageItem(GameTile tile) {
        Item item = tile.getItemOnTile();
        MyGame.getCurrentPlayer().getBackPack().addToInventory(item, 1);
        tile.setItemOnTile(null);
        increaseCapacity();
    }

    public void cutDownTree(){
        increaseCapacity();
    }


    @Override
    public void setLevel(int level) {
        this.level = level;
    }
    @Override
    public void increaseLevel() {
        if(level < 4) this.level ++;
        handleLevelChangeTrophies(level);
    }
    @Override
    public boolean canGoToNextLevel() {
        if((level + 1) * 100 + 50 <= capacity) {
            capacity -= (level + 1)*100 + 50;
            increaseLevel();
            MyGame.getCurrentPlayer().updateUserStats(0, 0, 1);
            return true;
        }
        return false;
    }
    @Override
    public int getLevel(){
        return level;
    }
    @Override
    public void increaseCapacity() {
        this.capacity += 10;
    }
    @Override
    public boolean isMaxLevel() {
        return level == 10;
    }
    @Override
    public void handleLevelChangeTrophies(int level){
        switch (level) {
            case 1: {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.CharcoalKlin);
                break;
            }
            case 2: {
                MyGame.getCurrentPlayer().getBackPack().addLearntCookingRecipe(CookingRecipeType.VegetableMedley);
                break;
            }
            case 3: {
                MyGame.getCurrentPlayer().getBackPack().addLearntCookingRecipe(CookingRecipeType.SurvivalBurger);
                break;
            }
            case 4: {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.MysticTreeSeed);
                break;
            }
        }
    }
}
