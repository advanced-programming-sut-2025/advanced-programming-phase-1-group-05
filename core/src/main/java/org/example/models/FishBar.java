package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.PrimitiveIterator;

public class FishBar extends Actor {
    private float speed = 60;
    private float barY = 500;
    private float barHeight = 200;
    private final float barWidth = 81;
    private final Texture barTex = GameAssetManager.getInstance().getOrLoadTexture("Animals/Fish/bar_green.png");
    public FishBar() {
        setBounds(1280, 500, barWidth, barHeight);

    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            barY += speed * delta;
        } else {
            barY -= speed * delta;
        }

        barY = MathUtils.clamp(barY, 250, Gdx.graphics.getHeight() - 430);
        setY(barY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.GREEN);
        batch.draw(barTex, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Color.WHITE);
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
