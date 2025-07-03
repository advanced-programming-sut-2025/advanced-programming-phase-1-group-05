package org.example.views;

import com.badlogic.gdx.Game;

public class MyFarmGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
