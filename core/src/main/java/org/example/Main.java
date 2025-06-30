// Main.java
package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.views.MenuNavigator;

public class Main extends Game {
    public static SpriteBatch batch;
    private Skin skin;
    private static Main main;

    public static Main getMain() {
        return main;
    }

    public static void setBatch(SpriteBatch batch) {
        Main.batch = batch;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        MenuNavigator.init(this, skin);
        MenuNavigator.showLoginMenu();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        skin.dispose();
    }
}
