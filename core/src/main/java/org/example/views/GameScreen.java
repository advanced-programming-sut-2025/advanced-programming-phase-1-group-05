package org.example.views;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import org.example.models.Player;
import org.example.models.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import org.example.controllers.GameManager;
import org.example.models.MyGame;


public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TileMapRenderer mapRenderer;
    private Player player;
    private boolean overviewMode = false;
    private static final int PLAYER_FARM_WIDTH = 70;
    private static final int PLAYER_FARM_HEIGHT = 70;
    private int playerId = 0;
    private Texture energyBarBg;
    private Texture energyBarFill;
    private BitmapFont font;
    private float timeAccumulator = 0f;
    private boolean isNightTransition = false;
    private float nightTransitionTimer = 0f;
    private boolean isDarkOverlay = false;
    private static final float NIGHT_DURATION = 2f; // 2 ثانیه شب
    Texture overlay;
    private boolean isDayAdvanced = false;
    private Texture blackOverlay;
    private boolean showBlackOverlay = false;



    private static final int TILE_SIZE = 64;
    private static final int MAP_WIDTH = 140;
    private static final int MAP_HEIGHT = 140;
    private static final int VIEW_WIDTH = 20;
    private static final int VIEW_HEIGHT = 15;



    public GameScreen() {
        camera = new OrthographicCamera(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);
        camera.setToOrtho(false);

        float farmStartX = (playerId % 2) * PLAYER_FARM_WIDTH * TILE_SIZE;
        float farmStartY = (playerId / 2) * PLAYER_FARM_HEIGHT * TILE_SIZE;
        camera.position.set(farmStartX + VIEW_WIDTH * TILE_SIZE / 2f,
            farmStartY + VIEW_HEIGHT * TILE_SIZE / 2f, 0);

        camera.update();

        batch = new SpriteBatch();
        mapRenderer = new TileMapRenderer();

        player = new Player(
            farmStartX, farmStartY, TILE_SIZE, TILE_SIZE);
        energyBarBg = new Texture(Gdx.files.internal("ui/energy_bar_bg.png"));
        energyBarFill = new Texture(Gdx.files.internal("ui/energy_bar_fill.png"));
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.moveUp(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.moveDown(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.moveLeft(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.moveRight(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            overviewMode = !overviewMode;
            if (overviewMode) {
                camera.viewportWidth = MAP_WIDTH * TILE_SIZE;
                camera.viewportHeight = MAP_HEIGHT * TILE_SIZE;
                camera.position.set(MAP_WIDTH * TILE_SIZE / 2f, MAP_HEIGHT * TILE_SIZE / 2f, 0);
            } else {
                camera.viewportWidth = VIEW_WIDTH * TILE_SIZE;
                camera.viewportHeight = VIEW_HEIGHT * TILE_SIZE;
                float farmStartX = (playerId % 2) * PLAYER_FARM_WIDTH * TILE_SIZE;
                float farmStartY = (playerId / 2) * PLAYER_FARM_HEIGHT * TILE_SIZE;
                camera.position.set(farmStartX + VIEW_WIDTH * TILE_SIZE / 2f,
                    farmStartY + VIEW_HEIGHT * TILE_SIZE / 2f, 0);
            }
            camera.update();
        }

        if (!overviewMode) {
            float minX = (playerId % 2) * PLAYER_FARM_WIDTH * TILE_SIZE;
            float minY = (playerId / 2) * PLAYER_FARM_HEIGHT * TILE_SIZE;
            float maxX = minX + PLAYER_FARM_WIDTH * TILE_SIZE - TILE_SIZE;
            float maxY = minY + PLAYER_FARM_HEIGHT * TILE_SIZE - TILE_SIZE;

            player.clampPosition(minX, minY, maxX, maxY);

            camera.position.set(player.getXX() + player.getWidth() / 2,
                player.getYY() + player.getHeight() / 2, 0);

            float halfW = camera.viewportWidth / 2;
            float halfH = camera.viewportHeight / 2;
            camera.position.x = MathUtils.clamp(camera.position.x, minX + halfW, maxX - halfW);
            camera.position.y = MathUtils.clamp(camera.position.y, minY + halfH, maxY - halfH);
        }

        timeAccumulator += delta;
        if (timeAccumulator >= 5f) {
            GameManager.getGameClock().advanceTime(60);
            timeAccumulator = 0f;
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        mapRenderer.render(batch, camera);
        player.draw(batch);
        drawEnergyBar(batch);

        String time = String.format("%02d:%02d", GameManager.getCurrentHour(), GameManager.getGameClock().getMinute());
        String season = GameManager.getSeason().toString();
        String day = GameManager.getDayOfTheWeek() + ", Day " + GameManager.getDay();
        int gold = MyGame.getCurrentPlayer().getGold();

        float x = camera.position.x + camera.viewportWidth / 2 - 300;
        float y = camera.position.y + camera.viewportHeight / 2 - 20;

        font.draw(batch, "Gold: " + gold, x, y);
        font.draw(batch, "Time: " + time, x, y - 30);
        font.draw(batch, "Season: " + season, x, y - 60);
        font.draw(batch, day, x, y - 90);

        int hour = GameManager.getGameClock().getHour();
        int minute = GameManager.getGameClock().getMinute();

        // Gray overlay for dusk
        if (hour >= 18 && hour < 22) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setColor(0.3f, 0.3f, 0.3f, 0.4f);
            batch.draw(new Texture("white.png"), camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight);
            batch.setColor(1, 1, 1, 1);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if ((hour == 21 && minute >= 30) || hour >= 22) {
            showBlackOverlay = true;
        }

        if (hour == 9 && minute == 0) {
            showBlackOverlay = false;
        }

        if (showBlackOverlay) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setColor(0f, 0f, 0f, 1f); // مشکی کامل
            batch.draw(blackOverlay,
                camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth,
                camera.viewportHeight);
            batch.setColor(1, 1, 1, 1);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        // Black screen transition
        if (hour >= 22 && !isNightTransition) {
            isNightTransition = true;
            isDarkOverlay = true;
            nightTransitionTimer = 0f;
            isDayAdvanced = false;
        }

        if (isNightTransition) {
            nightTransitionTimer += delta;

            // کشیدن صفحه سیاه با blend
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setColor(0f, 0f, 0f, 1f);
            batch.draw(overlay, camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight);
            batch.setColor(1, 1, 1, 1);
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // بعد از ۲ ثانیه، روز بعد رو شروع کن
            if (nightTransitionTimer >= NIGHT_DURATION && !isDayAdvanced) {
                GameManager.getGameClock().advanceDay();
                isDayAdvanced = true;
            }

            // بعد از تموم شدن فاز شب، برگرد به حالت عادی
            if (nightTransitionTimer >= NIGHT_DURATION + 0.5f) {
                isNightTransition = false;
                nightTransitionTimer = 0f;
            }
        }

        if (isNightTransition && !isDarkOverlay) {
            isNightTransition = false;
        }

        batch.end();
    }

//    public void render(float delta) {
//        Gdx.gl.glClearColor(0.6f, 0.8f, 0.5f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
//            player.moveUp(delta);
//        }
//        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
//            player.moveDown(delta);
//        }
//        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
//            player.moveLeft(delta);
//        }
//        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
//            player.moveRight(delta);
//        }
//
//        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.M)) {
//            overviewMode = !overviewMode;
//            if (overviewMode) {
//                camera.viewportWidth = MAP_WIDTH * TILE_SIZE;
//                camera.viewportHeight = MAP_HEIGHT * TILE_SIZE;
//                camera.position.set(MAP_WIDTH * TILE_SIZE / 2f, MAP_HEIGHT * TILE_SIZE / 2f, 0);
//            } else {
//                camera.viewportWidth = VIEW_WIDTH * TILE_SIZE;
//                camera.viewportHeight = VIEW_HEIGHT * TILE_SIZE;
//
//                float farmStartX = (playerId % 2) * PLAYER_FARM_WIDTH * TILE_SIZE;
//                float farmStartY = (playerId / 2) * PLAYER_FARM_HEIGHT * TILE_SIZE;
//
//                camera.position.set(farmStartX + VIEW_WIDTH * TILE_SIZE / 2f,
//                    farmStartY + VIEW_HEIGHT * TILE_SIZE / 2f, 0);
//            }
//            camera.update();
//        }
//
//        if (!overviewMode) {
//            float minX = (playerId % 2) * PLAYER_FARM_WIDTH * TILE_SIZE;
//            float minY = (playerId / 2) * PLAYER_FARM_HEIGHT * TILE_SIZE;
//            float maxX = minX + PLAYER_FARM_WIDTH * TILE_SIZE - TILE_SIZE;
//            float maxY = minY + PLAYER_FARM_HEIGHT * TILE_SIZE - TILE_SIZE;
//            player.clampPosition(minX, minY, maxX, maxY);
//
//            camera.position.set(player.getXX() + player.getWidth() / 2,
//                player.getYY() + player.getHeight() / 2, 0);
//
//            float halfW = camera.viewportWidth / 2;
//            float halfH = camera.viewportHeight / 2;
//            camera.position.x = MathUtils.clamp(camera.position.x, minX + halfW, maxX - halfW);
//            camera.position.y = MathUtils.clamp(camera.position.y, minY + halfH, maxY - halfH);
//        }
//
//        timeAccumulator += delta;
//        //todo set realy time to ghange time
//        if (timeAccumulator >= 5f) {
//            GameManager.getGameClock().advanceTime(60);
//            timeAccumulator = 0f;
//        }
//        camera.update();
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//        mapRenderer.render(batch, camera);
//        player.draw(batch);
//        drawEnergyBar(batch);
//        String time = String.format("%02d:%02d", GameManager.getCurrentHour(), GameManager.getGameClock().getMinute());
//        String season = GameManager.getSeason().toString();
//        String day = GameManager.getDayOfTheWeek() + ", Day " + GameManager.getDay();
//        int gold = MyGame.getCurrentPlayer().getGold();
//
//        float x = camera.position.x + camera.viewportWidth / 2 - 300;
//        float y = camera.position.y + camera.viewportHeight / 2 - 20;
//
//        font.draw(batch, "Gold: " + gold, x, y);
//        font.draw(batch, "Time: " + time, x, y - 30);
//        font.draw(batch, "Season: " + season, x, y - 60);
//        font.draw(batch, day, x, y - 90);
//        int hour = GameManager.getGameClock().getHour();
//
//        if (hour >= 18 && hour < 22) {
//            Gdx.gl.glEnable(GL20.GL_BLEND);
//            batch.begin();
//            batch.setColor(0.3f, 0.3f, 0.3f, 0.4f);
//            batch.draw(new Texture("white.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//            batch.setColor(1, 1, 1, 1);
//            batch.end();
//            Gdx.gl.glDisable(GL20.GL_BLEND);
//        }
//
//        if (hour >= 22 && !isNightTransition) {
//            isNightTransition = true;
//            isDarkOverlay = true;
//            nightTransitionTimer = 0f;
//        }
//
//        if (isNightTransition && isDarkOverlay) {
//            Gdx.gl.glEnable(GL20.GL_BLEND);
//            batch.begin();
//            batch.setColor(0f, 0f, 0f, 1f);
//            batch.draw(new Texture("white.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//            batch.setColor(1, 1, 1, 1);
//            batch.end();
//            Gdx.gl.glDisable(GL20.GL_BLEND);
//
//            nightTransitionTimer += delta;
//            if (nightTransitionTimer >= NIGHT_DURATION) {
//                isDarkOverlay = false;
//            }
//        }
//
//        if (isNightTransition && !isDarkOverlay) {
//            isNightTransition = false;
//        }
//
//        batch.end();
//    }

    private void drawEnergyBar(SpriteBatch batch) {
        float barWidth = 30;
        float barHeight = 150;
        float x = camera.position.x + camera.viewportWidth / 2 - barWidth - 10;
        float y = camera.position.y - camera.viewportHeight / 2 + 10;

        batch.draw(energyBarBg, x, y, barWidth, barHeight);

        float energyPercent = player.getEnergy() / 200f;
        float fillHeight = barHeight * energyPercent;
        batch.draw(energyBarFill, x, y, barWidth, fillHeight);
    }


    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {
        overlay = new Texture("white.png");
        blackOverlay = new Texture("black.png");
    }
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        mapRenderer.dispose();
        player.dispose();
        font.dispose();
        overlay.dispose();
        blackOverlay.dispose();
    }
}
