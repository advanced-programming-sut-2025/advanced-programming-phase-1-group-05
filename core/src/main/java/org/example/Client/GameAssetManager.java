package org.example.Client;

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
import org.example.Server.models.AnimalAnimations;
import org.example.Common.Enums.AnimalType;
import org.example.Common.Enums.Direction;
import org.example.Common.Enums.Season;
import org.example.Common.Enums.Weather;
import org.example.Server.models.MyGame;
import org.example.Server.models.NPC;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameAssetManager {
    private static GameAssetManager instance;

    private static final Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

    private static Map<String, Sound> sfxMap = new HashMap<>();
    private Music music;
    private static final Map<String, Texture> textureCache = new HashMap<>();
    public static final TextureRegion trashcanOpen = new TextureRegion(new Texture("trashcan/open trashcan.png"));
    public static final TextureRegion trashcanClosed = new TextureRegion(new Texture("trashcan/closed trashcan.png"));
    public static final TextureRegion skillSetPage = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/skills view.png"));
    public static final TextureRegion infoPage = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/info opener.png"));
    public static final TextureRegion toolSelection = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/tool selection.png"));
    public static final TextureRegion resultTexture = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/result.png"));
    public static final TextureRegion journalBg = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/journal page.png"));
    private static final Texture clockSheet = new Texture("Stardew_Valley_Images-main/extra/PC Computer - Stardew Valley - Clock.png");
    public static final TextureRegion clockTexture = new TextureRegion(new Texture("Stardew_Valley_Images-main/extra/clock.png"));
    public static final Texture slotBg = new Texture("Stardew_Valley_Images-main/extra/slot.png");
    public static final Texture tradingButton = new Texture("ui/trading.png");
    public static final Texture menuBg = new Texture("Animals/MenuBackground2.png");
    public static final Texture backGround = new Texture("backgrounds/login_bg.png");
    private static final HashMap<Season, TextureRegion> seasonIcons = new HashMap<>();
    private static final HashMap<Weather, TextureRegion> weatherIcons = new HashMap<>();

    public Map<AnimalType, AnimalAnimations> animalAnimations = new HashMap<>();
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
        loadSfx();
        loadNPCAvatars();
        loadAnimalAnimations();
        loadClockSheet();
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

    public TextureRegion getIdle(AnimalType type) {
        return animalAnimations.get(type).walk_down.getKeyFrame(0);
    }
    public void loadAnimalAnimations() {
        Texture duckTextureSheet = getOrLoadTexture("Animals/duck/sheet.png");
        Texture chickenTextureSheet = getOrLoadTexture("Animals/chicken/sheet.png");
        Texture rabbitTextureSheet = getOrLoadTexture("Animals/rabbit/sheet.png");
        Texture dinosaurTextureSheet = getOrLoadTexture("Animals/dinosaur/sheet.png");
        Texture cowTextureSheet = getOrLoadTexture("Animals/cow/sheet.png");
        Texture goatTextureSheet = getOrLoadTexture("Animals/goat/sheet.png");
        Texture sheepTextureSheet = getOrLoadTexture("Animals/sheep/sheet.png");
        Texture pigTextureSheet = getOrLoadTexture("Animals/pig/sheet.png");

        animalAnimations.put(AnimalType.DUCK, buildDuckAnimation(duckTextureSheet));
        animalAnimations.put(AnimalType.CHICKEN, buildChickenAnimation(chickenTextureSheet));
        animalAnimations.put(AnimalType.RABBIT, buildRabbitAnimation(rabbitTextureSheet));
        animalAnimations.put(AnimalType.DINOSAUR, buildDinosaurAnimation(dinosaurTextureSheet));
        animalAnimations.put(AnimalType.COW, buildBarnAnimalAnimation(cowTextureSheet));
        animalAnimations.put(AnimalType.GOAT, buildBarnAnimalAnimation(goatTextureSheet));
        animalAnimations.put(AnimalType.SHEEP, buildBarnAnimalAnimation(sheepTextureSheet));
        animalAnimations.put(AnimalType.PIG, buildBarnAnimalAnimation(pigTextureSheet));

    }

    private AnimalAnimations buildDuckAnimation(Texture textureSheet) {
        TextureRegion[][] regions = TextureRegion.split(textureSheet, 16, 16);
        Animation<TextureRegion> walkLeft = new Animation<>(0.2f, regions[0]);
        Animation<TextureRegion> walkRight = new Animation<>(0.2f, regions[1]);
        Animation<TextureRegion> walkUp = new Animation<>(0.2f, regions[7]);
        Animation<TextureRegion> walkDown = new Animation<>(0.2f, regions[9]);
        Animation<TextureRegion> petting = new Animation<>(0.2f, regions[10]);
        Animation<TextureRegion> eating = new Animation<>(0.2f, regions[5]);
        return new AnimalAnimations(walkDown, walkRight, walkLeft, walkUp, petting, eating);
    }

    private AnimalAnimations buildChickenAnimation(Texture textureSheet) {
        TextureRegion[][] regions = TextureRegion.split(textureSheet, 16, 16);
        Animation<TextureRegion> walkDown = new Animation<>(0.2f, regions[0]);
        Animation<TextureRegion> walkRight = new Animation<>(0.2f, regions[1]);
        Animation<TextureRegion> walkUp = new Animation<>(0.2f, regions[2]);
        Animation<TextureRegion> walkLeft = new Animation<>(0.2f, regions[3]);
        Animation<TextureRegion> petting = new Animation<>(0.2f, regions[4]);
        // region[5]
        Animation<TextureRegion> eating = new Animation<>(0.2f, regions[6]);
        return new AnimalAnimations(walkDown, walkRight, walkUp, walkLeft, petting,eating);
    }

    private AnimalAnimations buildRabbitAnimation(Texture textureSheet) {
        TextureRegion[][] regions = TextureRegion.split(textureSheet, 16, 16);
        Animation<TextureRegion> walkDown = new Animation<>(0.2f, regions[0]);
        Animation<TextureRegion> walkRight = new Animation<>(0.2f, regions[1]);
        Animation<TextureRegion> walkUp = new Animation<>(0.2f, regions[2]);
        Animation<TextureRegion> walkLeft = new Animation<>(0.2f, regions[3]);
        TextureRegion[] pettingRegions = {regions[4][0], regions[4][1]};
        Animation<TextureRegion> petting = new Animation<>(0.2f, pettingRegions);
        Animation<TextureRegion> eating = new Animation<>(0.2f, regions[5]);
        return new AnimalAnimations(walkDown, walkRight, walkLeft, walkUp, petting, eating);
    }

    private AnimalAnimations buildBarnAnimalAnimation(Texture textureSheet) {
        TextureRegion[][] regions = TextureRegion.split(textureSheet, 32, 32);;
        Animation<TextureRegion> walkDown = new Animation<>(0.2f, regions[0]);
        Animation<TextureRegion> walkRight = new Animation<>(0.2f, regions[1]);
        Animation<TextureRegion> walkUp = new Animation<>(0.2f, regions[2]);
        Animation<TextureRegion> eating = new Animation<>(0.2f, regions[4]);
        TextureRegion[] rightFrames = walkRight.getKeyFrames();
        TextureRegion[] leftFrames = new TextureRegion[rightFrames.length];

        for (int i = 0; i < rightFrames.length; i++) {
            leftFrames[i] = new TextureRegion(rightFrames[i]);
            leftFrames[i].flip(true, false);
        }
        Animation<TextureRegion> walkLeft = new Animation<>(0.2f, leftFrames);
        return new AnimalAnimations(walkDown, walkRight, walkLeft, walkUp, walkDown, eating);
    }

    private AnimalAnimations buildDinosaurAnimation(Texture textureSheet) {
        TextureRegion[][] regions = TextureRegion.split(textureSheet, 16, 16);
        Animation<TextureRegion> walkDown = new Animation<>(0.2f, regions[0]);
        Animation<TextureRegion> walkRight = new Animation<>(0.2f, regions[1]);
        Animation<TextureRegion> walkUp = new Animation<>(0.2f, regions[2]);
        Animation<TextureRegion> walkLeft = new Animation<>(0.2f, regions[3]);
        TextureRegion[] pettingRegions = {regions[4][2], regions[4][3]};
        Animation<TextureRegion> petting = new Animation<>(0.2f, pettingRegions);
        Animation<TextureRegion> eating = new Animation<>(0.2f, regions[6]);

        return new AnimalAnimations(walkDown, walkRight, walkLeft, walkUp, walkDown, eating);
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

    public Texture getItemTexture(String itemName) {
        String path = "Items/" + itemName.replaceAll("\\s+", "_") + ".png";
        return getOrLoadTexture(path);
    }

    public Texture getOrLoadTexture(String path) {
        if (!textureCache.containsKey(path)) {
            textureCache.put(path, new Texture(Gdx.files.internal(path)));
        }
        return textureCache.get(path);
    }

    public static TextureRegion getCraftTexture() {
        return MyGame.getCurrentPlayer().getBackPack().getLevel().getCraftTexture();
    }

    public static TextureRegion getInventoryTexture() {
        return MyGame.getCurrentPlayer().getBackPack().getLevel().getInventoryTexture();
    }

    public static TextureRegion getCookingTexture() {
        return MyGame.getCurrentPlayer().getBackPack().getLevel().getCookingTexture();
    }

    private void loadSfx(){
        sfxMap.put("open page", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Interface/bigSelect.wav")));
        sfxMap.put("close page", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Interface/bigDeSelect.wav")));
        sfxMap.put("use axe", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/axchop.wav")));
        sfxMap.put("crafted", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/crafting.wav")));
        sfxMap.put("use hoe", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/hoeHit.wav")));
        sfxMap.put("sand step", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/sandyStep.wav")));
        sfxMap.put("grass step", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/grassyStep.wav")));
        sfxMap.put("place item", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/throwDownITem.wav")));
        sfxMap.put("thunder", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/thunder.wav")));
        sfxMap.put("trashcan", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/trashcan.wav")));
        sfxMap.put("use watering can", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/water_lap1.wav")));
        sfxMap.put("use pickaxe", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/hammer.wav")));
        sfxMap.put("eat", Gdx.audio.newSound(Gdx.files.internal("sfx & music/Miscellaneous/eat.wav")));
        sfxMap.put("thor", Gdx.audio.newSound(Gdx.files.internal("sfx & music/thor/thor.wav")));
    }

    public static void playSfx(String sfxName) {
        for(Map.Entry<String,Sound> entry : sfxMap.entrySet()){
            if(entry.getKey().equals(sfxName)){
                entry.getValue().play();
            }
        }
    }

    private void loadClockSheet() {
        TextureRegion[][] gridIcons = TextureRegion.split(clockSheet,13,9);
        seasonIcons.put(Season.SPRING, gridIcons[0][0]);
        seasonIcons.put(Season.SUMMER, gridIcons[0][1]);
        seasonIcons.put(Season.FALL, gridIcons[0][2]);
        seasonIcons.put(Season.WINTER, gridIcons[0][3]);

        weatherIcons.put(Weather.Sunny, gridIcons[1][3]);
        weatherIcons.put(Weather.Rain, gridIcons[1][2]);
        weatherIcons.put(Weather.Snow, gridIcons[2][1]);
        weatherIcons.put(Weather.Storm, gridIcons[2][3]);

    }

    public static TextureRegion getSeasonIcon(Season season) {
        return seasonIcons.get(season);
    }

    public static TextureRegion getWeatherIcon(Weather weather) {
        return weatherIcons.get(weather);
    }

}
