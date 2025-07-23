package org.example.models.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Main;
import org.example.models.Enums.FishingPoleType;
import org.example.models.Enums.TileType;
import org.example.models.MyGame;
import org.example.models.GameMap;
import org.example.models.GameTile;
import org.example.models.Result;
import org.example.models.Skills.Fishing;
import org.example.views.GameScreen;

import java.util.HashMap;

public class FishingPole implements Tool<FishingPoleType> {
    FishingPoleType level = FishingPoleType.Training;

    @Override
    public String getName() {
        return "Fishing Pole";
    }
    @Override
    public int getPrice() {
        return level.getPrice();
    }
    @Override
    public Result use(GameTile tile) {
        return new Result(true, "fishing successful (or not)");
    }

    public void setFishingPoleType(FishingPoleType fishingPoleType) {
        this.level = fishingPoleType;
    }
    @Override
    public boolean reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        if(MyGame.getCurrentPlayer().getEnergy() - amount < 0)return false;
        MyGame.getCurrentPlayer().increaseEnergy(-amount);
        return true;
    }
    @Override
    public FishingPoleType getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.nextLevel();
        }
    }
    @Override
    public TextureRegion getTexture() {
        return level.getTexture();
    }
}
