package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;
import java.util.Map;

public class GameAssetManager {
    private static GameAssetManager instance;

    private final Skin skin;
    private final Map<String, Sound> sfxMap = new HashMap<>();
    private Music music;

    private GameAssetManager() {
        // Load skin from assets/skin/
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        // Optional: load cursor
        try {
            Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("Images/Texture2D/T_CursorSprite.png"));
            Cursor cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
            Gdx.graphics.setCursor(cursor);
            cursorPixmap.dispose();
        } catch (Exception e) {
            Gdx.app.log("GameAssetManager", "Cursor not loaded: " + e.getMessage());
        }

        // Optional: preload sounds (example)
        // sfxMap.put("click", Gdx.audio.newSound(Gdx.files.internal("AudioClip/click.wav")));
    }

    public static GameAssetManager getInstance() {
        if (instance == null)
            instance = new GameAssetManager();
        return instance;
    }

    public Skin getSkin() {
        return skin;
    }

    public void playSound(String name) {
        Sound sfx = sfxMap.get(name);
        if (sfx != null)
            sfx.play(1.0f);
    }

    public void dispose() {
        if (skin != null) skin.dispose();
        if (music != null) music.dispose();
        for (Sound sfx : sfxMap.values()) {
            sfx.dispose();
        }
    }
}
