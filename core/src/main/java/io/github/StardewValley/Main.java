package io.github.StardewValley;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.StardewValley.views.LoginScreen;

public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new LoginScreen(this)); // صفحه شروع: منوی ورود
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
