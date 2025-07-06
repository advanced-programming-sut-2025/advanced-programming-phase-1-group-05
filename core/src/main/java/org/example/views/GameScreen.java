package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.example.controllers.GameManager;
import org.example.controllers.GameMenuController;
import org.example.models.Enums.Season;
import org.example.models.MyGame;
import org.example.models.Player;
import org.example.models.TileMapRenderer;

import java.awt.*;

public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TileMapRenderer mapRenderer;
    private Player player;

    private Texture energyBarBg, energyBarFill, overlay, blackOverlay;
    private BitmapFont font;

    private static final int TILE_SIZE = 64;
    private static final int VIEW_WIDTH = 20;
    private static final int VIEW_HEIGHT = 15;

    private float timeAccumulator = 0f;
    private boolean overviewMode = false;
    private Season currentSeason;
    private Rectangle allowedArea;

    public GameScreen() {
        camera = new OrthographicCamera(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);
        camera.setToOrtho(false);
        batch = new SpriteBatch();

        currentSeason = GameManager.getSeason();
        mapRenderer = new TileMapRenderer();
        mapRenderer.setSeason(currentSeason);

        Player currentPlayer = MyGame.getCurrentPlayer();
        String selectedMap = GameMenuController.getMapForPlayer(currentPlayer.getUsername());
        Vector2 spawnPosition = getInitialPositionForMap(selectedMap);
        allowedArea = getAllowedAreaForMap(selectedMap);


        player = new Player(spawnPosition.x, spawnPosition.y, TILE_SIZE, TILE_SIZE);

        camera.position.set(spawnPosition.x + player.getWidth() / 2f,
            spawnPosition.y + player.getHeight() / 2f, 0);
        camera.update();

        energyBarBg = new Texture(Gdx.files.internal("ui/energy_bar_bg.png"));
        energyBarFill = new Texture(Gdx.files.internal("ui/energy_bar_fill.png"));
        overlay = new Texture("white.png");
        blackOverlay = new Texture("black.png");

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
    }

    private Vector2 getInitialPositionForMap(String mapName) {
        switch (mapName.toLowerCase()) {
            case "map1": return new Vector2(10 * TILE_SIZE, 10 * TILE_SIZE);
            case "map2": return new Vector2(80 * TILE_SIZE, 10 * TILE_SIZE);
            case "map3": return new Vector2(10 * TILE_SIZE, 80 * TILE_SIZE);
            case "map4": return new Vector2(80 * TILE_SIZE, 80 * TILE_SIZE);
            default: return new Vector2(0, 0);
        }
    }

    private Rectangle getAllowedAreaForMap(String mapName) {
        switch (mapName.toLowerCase()) {
            case "map1": return new Rectangle(10 * TILE_SIZE, 10 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE);
            case "map2": return new Rectangle(80 * TILE_SIZE, 10 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE);
            case "map3": return new Rectangle(10 * TILE_SIZE, 80 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE);
            case "map4": return new Rectangle(80 * TILE_SIZE, 80 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE);
            default: return new Rectangle(0, 0, 0, 0);
        }
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput(delta);

        timeAccumulator += delta;
        if (timeAccumulator >= 5f) {
            GameManager.getGameClock().advanceTime(60);
            timeAccumulator = 0f;
        }

        Season newSeason = GameManager.getSeason();
        if (!newSeason.equals(currentSeason)) {
            currentSeason = newSeason;
            mapRenderer.setSeason(currentSeason);
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        mapRenderer.render(batch, camera);
        player.draw(batch);
        drawEnergyBar();
        drawHUD();

        applyLightingOverlay();

        batch.end();
    }

    private void handleInput(float delta) {
        Vector2 oldPos = new Vector2(player.getXX(), player.getYY());
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.moveUp(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.moveDown(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.moveLeft(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.moveRight(delta);

        // can't go to round area
        if (!allowedArea.contains(player.getXX(), player.getYY())) {
            player.setPosition(oldPos.x, oldPos.y);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            toggleOverviewMode();
        }

        if (!overviewMode) {
            camera.position.set(player.getXX() + player.getWidth() / 2f,
                player.getYY() + player.getHeight() / 2f, 0);
        }
    }

    private void toggleOverviewMode() {
        overviewMode = !overviewMode;
        if (overviewMode) {
            camera.viewportWidth = 140 * TILE_SIZE;
            camera.viewportHeight = 140 * TILE_SIZE;
            camera.position.set(140 * TILE_SIZE / 2f, 140 * TILE_SIZE / 2f, 0);
        } else {
            camera.viewportWidth = VIEW_WIDTH * TILE_SIZE;
            camera.viewportHeight = VIEW_HEIGHT * TILE_SIZE;

            Player currentPlayer = MyGame.getCurrentPlayer();
            String map = GameMenuController.getMapForPlayer(currentPlayer.getUsername());
            Vector2 pos = getInitialPositionForMap(map);

            camera.position.set(pos.x + player.getWidth() / 2f,
                pos.y + player.getHeight() / 2f, 0);
        }
    }

    private void drawEnergyBar() {
        float barWidth = 30;
        float barHeight = 150;
        float x = camera.position.x + camera.viewportWidth / 2 - barWidth - 10;
        float y = camera.position.y - camera.viewportHeight / 2 + 10;

        batch.draw(energyBarBg, x, y, barWidth, barHeight);
        float fill = barHeight * (player.getEnergy() / 200f);
        batch.draw(energyBarFill, x, y, barWidth, fill);
    }

    private void drawHUD() {
        float x = camera.position.x + camera.viewportWidth / 2 - 300;
        float y = camera.position.y + camera.viewportHeight / 2 - 20;

        font.draw(batch, "Gold: " + MyGame.getCurrentPlayer().getGold(), x, y);
        font.draw(batch, "Time: " + String.format("%02d:%02d", GameManager.getCurrentHour(), GameManager.getGameClock().getMinute()), x, y - 30);
        font.draw(batch, "Season: " + currentSeason.toString(), x, y - 60);
        font.draw(batch, GameManager.getDayOfTheWeek() + ", Day " + GameManager.getDay(), x, y - 90);
    }

    private void applyLightingOverlay() {
        int hour = GameManager.getCurrentHour();
        if (hour >= 18 && hour < 22) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setColor(0.3f, 0.3f, 0.3f, 0.4f);
            batch.draw(overlay, camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight);
            batch.setColor(1, 1, 1, 1);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        } else if (hour >= 22 && hour < 23) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setColor(0f, 0f, 0f, 1f);
            batch.draw(blackOverlay, camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight);
            batch.setColor(1, 1, 1, 1);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
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
