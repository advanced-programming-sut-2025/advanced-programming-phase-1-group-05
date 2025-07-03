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
    private Map<TileType, Texture> textureMap;
    private Random random = new Random();

    public TileMapRenderer() {
        textureMap = new HashMap<>();
        for (TileType type : TileType.values()) {
            textureMap.put(type, new Texture(Gdx.files.internal(type.getImagePath())));
        }

        map = new TileType[MAP_HEIGHT][MAP_WIDTH];

        // پر کردن کل نقشه با چمن
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                map[y][x] = TileType.Flat;
            }
        }

        // ساخت هر چهار ربع نقشه با سازه‌های اختصاصی
        for (int playerId = 0; playerId < 4; playerId++) {
            int offsetX = (playerId % 2) * PLAYER_FARM_WIDTH;
            int offsetY = (playerId / 2) * PLAYER_FARM_HEIGHT;

            // خاک کردن وسط مزرعه
            for (int y = offsetY + 10; y < offsetY + 60; y++) {
                for (int x = offsetX + 10; x < offsetX + 60; x++) {
                    map[y][x] = TileType.Soil;
                }
            }

            // قرار دادن خانه در نقطه مشخص
            placeStructure(offsetX + 12, offsetY + 48, TileType.House);

            // گلخانه
            placeStructure(offsetX + 18, offsetY + 48, TileType.GreenHouse);

            // دو دریاچه
            fillArea(offsetX + 20, offsetY + 20, 5, 6, TileType.Water);
            fillArea(offsetX + 45, offsetY + 30, 3, 7, TileType.Water);

            // درخت تصادفی
            for (int i = 0; i < 15; i++) {
                int tx = offsetX + 10 + random.nextInt(50);
                int ty = offsetY + 10 + random.nextInt(50);
                if (map[ty][tx] == TileType.Soil) {
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
        map[y][x] = type;
        int width = textureMap.get(type).getWidth() / TILE_SIZE;
        int height = textureMap.get(type).getHeight() / TILE_SIZE;

        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (nx < MAP_WIDTH && ny < MAP_HEIGHT) {
                    map[ny][nx] = TileType.Flat;
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
                Texture tex = textureMap.get(type);

                if (type.isLargeStructure() && !isTopLeftCorner(x, y, type)) {
                    continue;
                }

                int drawWidth = (type.isLargeStructure()) ? tex.getWidth() : TILE_SIZE;
                int drawHeight = (type.isLargeStructure()) ? tex.getHeight() : TILE_SIZE;

                batch.draw(tex, x * TILE_SIZE, y * TILE_SIZE, drawWidth, drawHeight);
            }
        }
    }

    private boolean isTopLeftCorner(int x, int y, TileType type) {
        return map[y][x] == type;
    }

    public void dispose() {
        for (Texture tex : textureMap.values()) {
            tex.dispose();
        }
    }
}
