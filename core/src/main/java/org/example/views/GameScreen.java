package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class GameScreen implements Screen {
    private static final int TILE_SIZE = 64;
    private static final int VIEW_WIDTH = 40;
    private static final int VIEW_HEIGHT = 24;
    private static final int MAP_WIDTH = 140;
    private static final int MAP_HEIGHT = 140;

    private SpriteBatch batch;
    private Texture soilTexture, waterTexture, flatTexture, treeTexture, buildingTexture, greenHouseTexture, playerTexture;
    private TileType[][] map;
    private int playerX = 75, playerY = 35; // در محدوده زمین خود بازیکن

    @Override
    public void show() {
        batch = new SpriteBatch();
        loadTextures();
        generateMap();
    }

    private void loadTextures() {
        flatTexture = new Texture("Flat.png");
        soilTexture = new Texture("Soil.png");
        waterTexture = new Texture("Water.png");
        treeTexture = new Texture("tree.png");
        buildingTexture = new Texture("Building.png");
        greenHouseTexture = new Texture("GreenHouse.png");
        playerTexture = new Texture("frame_0_2.png");
    }

    private void generateMap() {
        map = new TileType[MAP_WIDTH][MAP_HEIGHT];
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                map[x][y] = TileType.Flat;
            }
        }

        for (int x = 70; x < 140; x++) {
            for (int y = 0; y < 70; y++) {
                map[x][y] = TileType.Soil;
            }
        }

        // Add lakes
        for (int x = 72; x < 78; x++) {
            for (int y = 50; y < 55; y++) {
                map[x][y] = TileType.Water;
            }
        }

        for (int x = 130; x < 135; x++) {
            for (int y = 60; y < 65; y++) {
                map[x][y] = TileType.Water;
            }
        }

        map[75][10] = TileType.Building;
        map[78][12] = TileType.GreenHouse;
        map[90][25] = TileType.Tree;
    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        int startX = MathUtils.clamp(playerX - VIEW_WIDTH / 2, 0, MAP_WIDTH - VIEW_WIDTH);
        int startY = MathUtils.clamp(playerY - VIEW_HEIGHT / 2, 0, MAP_HEIGHT - VIEW_HEIGHT);

        for (int x = 0; x < VIEW_WIDTH; x++) {
            for (int y = 0; y < VIEW_HEIGHT; y++) {
                int mapX = startX + x;
                int mapY = startY + y;
                TileType tile = map[mapX][mapY];

                if (tile == TileType.Flat || tile == TileType.Soil || tile == TileType.Water) {
                    batch.draw(getTextureByType(tile), x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Draw special structures
        drawStructure(buildingTexture, 75, 10, 1024, 1536);
        drawStructure(greenHouseTexture, 78, 12, 218, 270);
        drawStructure(treeTexture, 90, 25, 150, 150);

        // Draw player
        batch.draw(playerTexture, (playerX - startX) * TILE_SIZE, (playerY - startY) * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        batch.end();
    }

    private void drawStructure(Texture texture, int mapX, int mapY, int pixelWidth, int pixelHeight) {
        int startX = MathUtils.clamp(playerX - VIEW_WIDTH / 2, 0, MAP_WIDTH - VIEW_WIDTH);
        int startY = MathUtils.clamp(playerY - VIEW_HEIGHT / 2, 0, MAP_HEIGHT - VIEW_HEIGHT);

        float scale = TILE_SIZE / 64f; // Adjust based on expected ratio
        float drawWidth = pixelWidth * scale;
        float drawHeight = pixelHeight * scale;

        float drawX = (mapX - startX) * TILE_SIZE;
        float drawY = (mapY - startY) * TILE_SIZE;

        batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) playerY++;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) playerY--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) playerX--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) playerX++;

        playerX = MathUtils.clamp(playerX, 70, 139);
        playerY = MathUtils.clamp(playerY, 0, 69);
    }

    private Texture getTextureByType(TileType type) {
        switch (type) {
            case Soil: return soilTexture;
            case Water: return waterTexture;
            default: return flatTexture;
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        flatTexture.dispose();
        soilTexture.dispose();
        waterTexture.dispose();
        treeTexture.dispose();
        buildingTexture.dispose();
        greenHouseTexture.dispose();
        playerTexture.dispose();
    }

    enum TileType {
        Flat, Soil, Water, Tree, Building, GreenHouse
    }
}
