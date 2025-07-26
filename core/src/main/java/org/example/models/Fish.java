package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.models.Enums.FishType;

public class Fish extends Actor {
    TextureRegion frame;
    FishType type;
    private float targetY;
    private float speed = 100;
    private float currentY;
    private float fishSize = 75;
    private FishType.FishBehavior behavior;
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
        this.currentY = 550f;
        setBounds(1280, 550, fishSize, fishSize);
        randomizeTargetY();
        behavior = type.getBehavior();
    }

    private void randomizeTargetY() {
        float min = 280;
        float max = Gdx.graphics.getHeight() - 480;

        if (max < min) max = min + 1;

        targetY = MathUtils.random(min, max);
    }


    @Override
    public void act(float delta) {
        if (Math.abs(targetY - currentY) < 5) {
            randomizeTargetY();
        }
        float direction = Math.signum(targetY - currentY);
        float adjustedSpeed = speed;
        switch (behavior) {
            case MIXED:
                adjustedSpeed = speed;
                break;
            case SMOOTH:
                adjustedSpeed = speed * 0.5f;
                break;
            case SINKER:
                adjustedSpeed = direction > 0 ? speed : speed * 1.5f;
                break;
            case FLOATER:
                adjustedSpeed = direction < 0 ? speed : speed * 1.5f;
                break;
            case DART:
                adjustedSpeed = speed * MathUtils.random(1f, 1.6f);
                break;
        }
                currentY += direction * adjustedSpeed * delta;
                setY(currentY);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }


}
