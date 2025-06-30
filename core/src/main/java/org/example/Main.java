package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.views.LoginMenu;
import org.example.views.LoginMenu;

public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new LoginMenu(this)); // صفحه شروع: منوی ورود
    }

    @Override
    public void render() {
        super.render(); // این کار باعث میشه Screen فعلی خودش render بشه
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
