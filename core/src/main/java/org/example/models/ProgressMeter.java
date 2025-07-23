package org.example.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ProgressMeter extends Actor {
    private float progress = 0;
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.GRAY);
//        shapeRenderer.rect(50, 20, 200, 20);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(50, 20, 2 * progress, 20);
        shapeRenderer.end();
    }
}

