package org.example.models.Skills;

import org.example.models.*;
import org.example.models.Enums.CookingRecipeType;
import org.example.models.Enums.CraftType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.MineralType;
import org.example.models.Tool.FishingPole;
import org.example.models.Tool.Pickaxe;

public class Mining implements Skill{
    int level = 0;
    int capacity = 0;

    public void mine(GameTile tile, Pickaxe pickaxe) {
        Item stone = tile.getItemOnTile();
        Game.getCurrentPlayer().getBackPack().addToInventory(stone, 1);
        tile.setItemOnTile(null);
        increaseCapacity();
        if(level >=2) {
            Item addedItem = MineralType.getRandomMineralType();
            Game.getCurrentPlayer().getBackPack().addToInventory(addedItem, 1);
        }
    }

    @Override
    public int getLevel() {
        return level;
    }
    @Override
    public boolean isMaxLevel() {
        return level == 10;
    }
    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void increaseLevel() {
        if (level < 4) this.level++;
        handleLevelChangeTrophies(level);
    }

    @Override
    public boolean canGoToNextLevel() {
        if ((level + 1) * 100 + 50 <= capacity) {
            capacity -= (level + 1) * 100 + 50;
            increaseLevel();
            return true;
        }
        return false;
    }
    @Override
    public void increaseCapacity() {
        this.capacity += 10;
    }
    @Override
    public void handleLevelChangeTrophies(int level){
        switch(level){
            case 1: {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.CherryBomb);
                Game.getCurrentPlayer().getBackPack().addLearntCookingRecipe(CookingRecipeType.MinersTreat);
                break;
            }
            case 2: {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.Bomb);
                break;
            }
            case 3: {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.MegaBomb);
                break;
            }
        }
    }
}
