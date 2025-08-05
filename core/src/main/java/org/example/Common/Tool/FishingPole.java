package org.example.Common.Tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Common.Enums.FishingPoleType;
import org.example.Server.models.MyGame;
import org.example.Common.GameTile;
import org.example.Server.models.Result;

import java.io.Serializable;

public class FishingPole implements Tool<FishingPoleType> , Serializable {
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
