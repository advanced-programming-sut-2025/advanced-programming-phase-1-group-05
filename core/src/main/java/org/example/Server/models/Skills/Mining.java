package org.example.Server.models.Skills;

import org.example.Common.GameTile;
import org.example.Common.Item;
import org.example.Server.models.MyGame;
import org.example.Common.Enums.CookingRecipeType;
import org.example.Common.Enums.CraftType;
import org.example.Common.Enums.MineralType;
import org.example.Common.Tool.Pickaxe;

import java.io.Serializable;

public class Mining implements Skill , Serializable {
    int level = 0;
    int capacity = 0;

    public void mine(GameTile tile, Pickaxe pickaxe) {
        Item stone = tile.getItemOnTile();
        MyGame.getCurrentPlayer().getBackPack().addToInventory(stone, 1);
        tile.setItemOnTile(null);
        increaseCapacity();
        if(level >=2) {
            Item addedItem = MineralType.getRandomMineralType();
            MyGame.getCurrentPlayer().getBackPack().addToInventory(addedItem, 1);
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
            MyGame.getCurrentPlayer().getUser().updateUserInfo(1, "skill");
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
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.CherryBomb);
                MyGame.getCurrentPlayer().getBackPack().addLearntCookingRecipe(CookingRecipeType.MinersTreat);
                break;
            }
            case 2: {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.Bomb);
                break;
            }
            case 3: {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.MegaBomb);
                break;
            }
        }
    }
}
