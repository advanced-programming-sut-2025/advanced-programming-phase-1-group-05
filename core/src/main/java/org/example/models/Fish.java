package org.example.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.models.Enums.FishType;

public class Fish extends Actor {
    TextureRegion frame;
    FishType type;
    public Fish(FishType type, boolean sonarBobber) {
        this.type = type;
        if (sonarBobber) {
            frame = type.getTexture();
        }
        else {
            if (type.isLegendary())
                frame = new TextureRegion(GameAssetManager.getInstance().getOrLoadTexture("Animals/Fish/SardineLegendary.png"));
            else frame = FishType.Sardine.getTexture();
        }

    }
}
