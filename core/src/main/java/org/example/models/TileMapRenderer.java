package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import org.example.models.Enums.TileType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TileMapRenderer {
    private static final int TILE_SIZE = 64;
    private static final int MAP_WIDTH = 140;
    private static final int MAP_HEIGHT = 140;
    private static final int PLAYER_FARM_WIDTH = 70;
    private static final int PLAYER_FARM_HEIGHT = 70;

    private TileType[][] map;
    private boolean[][] isTopLeft;
    private Map<TileType, Texture> textureMap;
    private Random random = new Random();

    public TileMapRenderer() {
        textureMap = new HashMap<>();
        for (TileType type : TileType.values()) {
            textureMap.put(type, new Texture(Gdx.files.internal(type.getImagePath())));
        }

        map = new TileType[MAP_HEIGHT][MAP_WIDTH];
        isTopLeft = new boolean[MAP_HEIGHT][MAP_WIDTH];

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                map[y][x] = TileType.Flat;
                isTopLeft[y][x] = false;
            }
        }

        for (int playerId = 0; playerId < 4; playerId++) {
            int offsetX = (playerId % 2) * PLAYER_FARM_WIDTH;
            int offsetY = (playerId / 2) * PLAYER_FARM_HEIGHT;

            for (int y = offsetY + 10; y < offsetY + 60; y++) {
                for (int x = offsetX + 10; x < offsetX + 60; x++) {
                    map[y][x] = TileType.Soil;
                }
            }

            placeStructure(offsetX + 12, offsetY + 48, TileType.House);

            placeStructure(offsetX + 18, offsetY + 48, TileType.GreenHouse);

            fillArea(offsetX + 20, offsetY + 20, 5, 6, TileType.Water);
            fillArea(offsetX + 45, offsetY + 30, 3, 7, TileType.Water);

            for (int i = 0; i < 15; i++) {
                int tx = offsetX + 10 + random.nextInt(50);
                int ty = offsetY + 10 + random.nextInt(50);
                if (map[ty][tx] == TileType.Soil || map[ty][tx] == TileType.Flat) {
                    placeStructure(tx, ty, TileType.Tree);
                }
            }
        }
    }

    private void fillArea(int startX, int startY, int width, int height, TileType type) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int nx = startX + x;
                int ny = startY + y;
                if (nx < MAP_WIDTH && ny < MAP_HEIGHT) {
                    map[ny][nx] = type;
                }
            }
        }
    }

    private void placeStructure(int x, int y, TileType type) {
        Texture tex = textureMap.get(type);
        int width = tex.getWidth() / TILE_SIZE;
        int height = tex.getHeight() / TILE_SIZE;

        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx < MAP_WIDTH && ny < MAP_HEIGHT) {
                    if (dx == 0 && dy == 0) {
                        map[ny][nx] = type;
                        isTopLeft[ny][nx] = true;
                    } else {
                        isTopLeft[ny][nx] = false;
                    }
                }
            }
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float camX = camera.position.x;
        float camY = camera.position.y;
        float halfW = camera.viewportWidth / 2;
        float halfH = camera.viewportHeight / 2;
        int startX = MathUtils.floor((camX - halfW) / TILE_SIZE);
        int startY = MathUtils.floor((camY - halfH) / TILE_SIZE);
        int endX = MathUtils.ceil((camX + halfW) / TILE_SIZE);
        int endY = MathUtils.ceil((camY + halfH) / TILE_SIZE);

        startX = MathUtils.clamp(startX, 0, MAP_WIDTH - 1);
        startY = MathUtils.clamp(startY, 0, MAP_HEIGHT - 1);
        endX = MathUtils.clamp(endX, 0, MAP_WIDTH - 1);
        endY = MathUtils.clamp(endY, 0, MAP_HEIGHT - 1);

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TileType type = map[y][x];
                if (type == null) continue;

                if (type.isLargeStructure() && !isTopLeft[y][x]) continue;

                TileType background = TileType.Flat;
                if (type == TileType.Tree || type == TileType.House || type == TileType.GreenHouse) {
                    background = inferBackground(x, y);
                    Texture bgTex = textureMap.get(background);
                    batch.draw(bgTex, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

                Texture tex = textureMap.get(type);
                int drawWidth = (type.isLargeStructure()) ? tex.getWidth() : TILE_SIZE;
                int drawHeight = (type.isLargeStructure()) ? tex.getHeight() : TILE_SIZE;

                batch.draw(tex, x * TILE_SIZE, y * TILE_SIZE, drawWidth, drawHeight);
            }
        }
    }

    private TileType inferBackground(int x, int y) {
        for (int playerId = 0; playerId < 4; playerId++) {
            int offsetX = (playerId % 2) * PLAYER_FARM_WIDTH;
            int offsetY = (playerId / 2) * PLAYER_FARM_HEIGHT;
            if (x >= offsetX + 10 && x < offsetX + 60 && y >= offsetY + 10 && y < offsetY + 60) {
                return TileType.Soil;
            }
        }
        return TileType.Flat;
    }

    public void dispose() {
        for (Texture tex : textureMap.values()) {
            tex.dispose();
        }
    }
}
