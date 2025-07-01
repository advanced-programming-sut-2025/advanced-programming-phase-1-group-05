package org.example.models.Skills;

import org.example.models.Enums.CookingRecipeType;
import org.example.models.Enums.CraftType;
import org.example.models.MyGame;
import org.example.models.GameTile;
import org.example.models.Item;

public class Foraging implements Skill{
    int level = 0;
    int capacity = 0;

    //TODO add tool
    public void forageItem(GameTile tile) {
        Item item = tile.getItemOnTile();
        MyGame.getCurrentPlayer().getBackPack().addToInventory(item, 1);
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
