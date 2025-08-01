package org.example.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ProgressMeter extends Actor {
    private float progress = 10;
    private float progressRate = 30f;
    private float decayRate = 20f;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public ProgressMeter() {
        setPosition(100, 50);
    }
    public void update(boolean catching, float delta) {
        if (catching) {
            progress += progressRate * delta;
        } else {
            progress -= decayRate * delta;
        }
        progress = MathUtils.clamp(progress, 0, 100);
    }

    public boolean isComplete() {
        return progress >= 100;
    }

    public boolean isEmpty() {
        return progress <= 0;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (progress >= 70) shapeRenderer.setColor(Color.GREEN);
        else shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(1415, 240, 25, 9.8f * progress);
        shapeRenderer.end();
        batch.begin();
    }
}

