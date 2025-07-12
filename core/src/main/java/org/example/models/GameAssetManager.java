package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import org.example.models.Enums.Direction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameAssetManager {
    private static GameAssetManager instance;

    private static final Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

    private final Map<String, Sound> sfxMap = new HashMap<>();
    private Music music;
    private static final Map<String, Texture> textureCache = new HashMap<>();
    public static final TextureRegion trashcanOpen = new TextureRegion(new Texture("trashcan/open trashcan.png"));
    public static final TextureRegion trashcanClosed = new TextureRegion(new Texture("trashcan/closed trashcan.png"));
    public static final TextureRegion skillSetPage = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/skills view.png"));
    public static final TextureRegion infoPage = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/info opener.png"));
    public static final TextureRegion toolSelection = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/tool selection.png"));
    private GameAssetManager() {
        // Load skin from assets/skin/
        try {
            Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("Stardew_Valley_Images-main/extra/cursor.png"));
            Cursor cursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
            Gdx.graphics.setCursor(cursor);
            cursorPixmap.dispose();
        } catch (Exception e) {
            Gdx.app.log("GameAssetManager", "Cursor not loaded: " + e.getMessage());
        }
        loadNPCAvatars();
    }

    public static GameAssetManager getInstance() {
        if (instance == null)
            instance = new GameAssetManager();
        return instance;
    }

    public static Skin getSkin() {
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
        for (Texture tex : textureCache.values()) {
            tex.dispose();
        }
        textureCache.clear();
    }

    public Animation<TextureRegion> getNPCWalkingAnimation(NPC npc, Direction direction) {
        Array<TextureRegion> frames = new Array<>();

        String baseName = npc.getName().toLowerCase(Locale.ROOT);
        String dir = direction.toString().toLowerCase(Locale.ROOT);

        for (int i = 1; i < 4; i++) {
            String path = "NPCs/" + baseName + "/walk" + dir + i + ".png";

            Texture texture = getOrLoadTexture(path);
            TextureRegion region = new TextureRegion(texture);
            frames.add(region);
        }

        return new Animation<>(0.2f, frames, Animation.PlayMode.LOOP);
    }

    private void loadNPCAvatars() {
        String[] names = {"abigail", "harvey", "leah", "robin", "sebastian"};
        for (String name : names) {
            String path = "NPCs/" + name + "/avatar.png";
            getOrLoadTexture(path);
        }
    }

    public Texture getOrLoadTexture(String path) {
        if (!textureCache.containsKey(path)) {
            textureCache.put(path, new Texture(Gdx.files.internal(path)));
        }
        return textureCache.get(path);
    }



}
