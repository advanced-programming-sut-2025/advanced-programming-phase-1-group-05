package org.example.models;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ProgressMeter extends Actor {
    private float progress = 0;

    public boolean isComplete() {
        return progress >= 100;
    }
}
