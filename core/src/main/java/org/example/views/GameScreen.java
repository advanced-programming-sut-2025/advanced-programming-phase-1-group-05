package org.example.views;
// GameScreen.java
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class GameScreen implements Screen {
    private static final int TILE_SIZE = 64;
    private static final int VIEW_WIDTH = 10;
    private static final int VIEW_HEIGHT = 8;
    private static final int MAP_WIDTH = 140;
    private static final int MAP_HEIGHT = 140;

    private SpriteBatch batch;
    private Texture grassTexture, soilTexture, waterTexture, flatTexture, houseTexture, treeTexture, playerTexture, greenhouseTexture;

    private GameTile[][] map;
    private int playerX = 0, playerY = 0;

    @Override
    public void show() {
        batch = new SpriteBatch();
        loadTextures();
        generateMap();
        playerX = 0;
        playerY = 0;
    }

    private void loadTextures() {
        grassTexture = new Texture("Flat.png");
        soilTexture = new Texture("Soil.png");
        waterTexture = new Texture("Water.png");
        flatTexture = new Texture("Flat.png");
        houseTexture = new Texture("Building.png");
        treeTexture = new Texture("tree.png");
        playerTexture = new Texture("frame_0_2.png");
        greenhouseTexture = new Texture("GreenHouse.png");
    }

    private void generateMap() {
        map = new GameTile[MAP_WIDTH][MAP_HEIGHT];
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                map[x][y] = new GameTile(TileType.Flat);
            }
        }
        // Sample terrain for current farm area (example bottom-right corner)
        for (int x = 70; x < 140; x++) {
            for (int y = 0; y < 70; y++) {
                map[x][y].setType(TileType.Soil);
            }
        }
        map[75][10].setType(TileType.Building);
        map[78][12].setType(TileType.GreenHouse);
        map[80][15].setType(TileType.Tree);
        map[76][20].setType(TileType.Water);
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
                TileType type = map[mapX][mapY].getType();
                Texture texture = getTextureByType(type);
                batch.draw(texture, x * TILE_SIZE, y * TILE_SIZE);

                if (mapX == playerX && mapY == playerY) {
                    batch.draw(playerTexture, x * TILE_SIZE, y * TILE_SIZE);
                }
            }
        }
        batch.end();
    }

    private Texture getTextureByType(TileType type) {
        switch (type) {
            case Soil: return soilTexture;
            case Water: return waterTexture;
            case Tree: return treeTexture;
            case Building: return houseTexture;
            case GreenHouse: return greenhouseTexture;
            default: return grassTexture;
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) playerY++;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) playerY--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) playerX--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) playerX++;

        playerX = MathUtils.clamp(playerX, 70, 139);
        playerY = MathUtils.clamp(playerY, 0, 69);
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        grassTexture.dispose();
        soilTexture.dispose();
        waterTexture.dispose();
        houseTexture.dispose();
        treeTexture.dispose();
        playerTexture.dispose();
        greenhouseTexture.dispose();
    }

    static class GameTile {
        private TileType type;
        public GameTile(TileType type) {
            this.type = type;
        }
        public TileType getType() {
            return type;
        }
        public void setType(TileType type) {
            this.type = type;
        }
    }

    enum TileType {
        Flat, Soil, Water, Tree, Building, GreenHouse
    }
}
