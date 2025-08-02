package org.example.Server.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import org.example.Common.Enums.*;
import org.example.Common.GameMap;
import org.example.Common.GameTile;
import org.example.Common.Item;
import org.example.Server.controllers.GameManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TileMapRenderer {
    private static final int TILE_SIZE = 64;
    private static final int MAP_WIDTH = 200;
    private static final int MAP_HEIGHT = 200;
    private static final int PLAYER_FARM_WIDTH = 70;
    private static final int PLAYER_FARM_HEIGHT = 70;

    private TileType[][] map;
    private boolean[][] isTopLeft;
    private Map<TileType, Texture> textureMap;
    private Season currentSeason;
    private Random random = new Random();

    public TileMapRenderer() {
        map = new TileType[MAP_HEIGHT][MAP_WIDTH];
        //initialize map
        isTopLeft = new boolean[MAP_HEIGHT][MAP_WIDTH];
        textureMap = new HashMap<>();

        currentSeason = GameManager.getSeason();

        loadTextures();

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                GameTile tile = GameMap.getTile(x, y);
                if(tile == null) continue;
                boolean isBorder = x < 5 || x >= MAP_WIDTH - 5 || y < 5 || y >= MAP_HEIGHT - 5;

                if (isBorder) {
                    tile.setTileType(TileType.Water); // Border = water
                } else {
                    tile.setTileType(TileType.Flat);  // Interior = flat land
                }
                isTopLeft[y][x] = false;
            }
        }

        for (int playerId = 0; playerId < 4; playerId++) {
            int offsetX = (playerId % 2) * PLAYER_FARM_WIDTH;
            int offsetY = (playerId / 2) * PLAYER_FARM_HEIGHT;

            for (int y = offsetY + 10; y < offsetY + 60; y++) {
                for (int x = offsetX + 10; x < offsetX + 60; x++) {
                    //map[y][x] = TileType.FarmFlat;
                    GameTile tile = GameMap.getTile(x, y);
                    if(tile == null) continue;
                    tile.setTileType(TileType.FarmFlat);
                }
            }
//
            placeHouse(offsetX + 10, offsetY + PLAYER_FARM_HEIGHT - 14);
            placeGreenHouse(offsetX + 18, offsetY + 48);

            fillArea(offsetX + 20, offsetY + 20, 5, 6, TileType.Water);
            fillArea(offsetX + 45, offsetY + 30, 3, 7, TileType.Water);
            placeRandomDecorations();
//            //jesus T-T
////            for (int i = 0; i < 15; i++) {
////                int tx = offsetX + 10 + random.nextInt(50);
////                int ty = offsetY + 10 + random.nextInt(50);
////                if (map[ty][tx] == TileType.Soil || map[ty][tx] == TileType.Flat) {
////                    placeStructure(tx, ty, TileType.Tree);
////                }
////            }
////            for (int i = 0; i < 15; i++) {
////                int tx = offsetX + 10 + random.nextInt(50);
////                int ty = offsetY + 10 + random.nextInt(50);
////                if (map[ty][tx] == TileType.Soil || map[ty][tx] == TileType.Flat) {
////                    placeStructure(tx, ty, TileType.Stone);
////                }
////            }
////            for (int i = 0; i < 15; i++) {
////                int tx = offsetX + 10 + random.nextInt(50);
////                int ty = offsetY + 10 + random.nextInt(50);
////                if (map[ty][tx] == TileType.Soil || map[ty][tx] == TileType.Flat) {
////                    placeStructure(tx, ty, TileType.Wood);
////                }
////            }
//
//
        }
    }

    public void setSeason(Season season) {
        this.currentSeason = season;
        reloadTextures();
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    private void loadTextures() {
        for (TileType type : TileType.values()) {
            textureMap.put(type, new Texture(Gdx.files.internal(getSeasonalTexturePath(type))));
        }
    }

    public void reloadTextures() {
        for (TileType type : TileType.values()) {
            Texture old = textureMap.get(type);
            if (old != null) old.dispose();
            textureMap.put(type, new Texture(Gdx.files.internal(getSeasonalTexturePath(type))));
        }
    }

    private String getSeasonalTexturePath(TileType type) {
        switch (type) {
            case Tree:
                return currentSeason.getTreeTexture();
            case Soil:
                return currentSeason.getSoilTexture();
            default:
                return type.getImagePath();
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
                GameTile tile = GameMap.getTile(x, y);
                if (tile == null) continue;
                TileType type = tile.getTileType();
                if (type == null) continue;

                if (type.isLargeStructure()
                    && !type.name().startsWith("HOUSE_")
                    && !type.name().startsWith("GREENHOUSE_")
                    && !isTopLeft[y][x]) continue;

                if (type == TileType.House
                    || type == TileType.GreenHouse || type.name().startsWith("HOUSE_")
                    || type.name().startsWith("GREENHOUSE_")) {
                    Texture bgTex = TileType.FarmFlat.getTexture();
                    if (bgTex != null)
                        batch.draw(bgTex, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

                Texture tex = textureMap.get(type);
                if (tex != null) {
                    int drawWidth = (type.isLargeStructure()
                        && !type.name().startsWith("HOUSE_")
                        && !type.name().startsWith("GREENHOUSE_")) ? tex.getWidth() : TILE_SIZE;
                    int drawHeight = (type.isLargeStructure()
                        && !type.name().startsWith("HOUSE_")
                        && !type.name().startsWith("GREENHOUSE_")) ? tex.getHeight() : TILE_SIZE;

                    batch.draw(tex, x * TILE_SIZE, y * TILE_SIZE, drawWidth, drawHeight);
                }

                Item itemOnTile = tile.getItemOnTile();
                if (itemOnTile != null) {
                    Texture itemTex = itemOnTile.getTexture().getTexture();

                    float texWidth = itemTex.getWidth();
                    float texHeight = itemTex.getHeight();

                    float scale = TILE_SIZE / Math.max(texWidth, texHeight);

                    float drawWidth = texWidth * scale;
                    float drawHeight = texHeight * scale;

                    float drawX = x * TILE_SIZE + (TILE_SIZE - drawWidth) / 2f;
                    float drawY = y * TILE_SIZE + (TILE_SIZE - drawHeight) / 2f;

                    batch.draw(itemTex, drawX, drawY, drawWidth, drawHeight);
                }
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

    private void fillArea(int startX, int startY, int width, int height, TileType type) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int nx = startX + x;
                int ny = startY + y;
                if (nx < MAP_WIDTH && ny < MAP_HEIGHT) {
                    map[ny][nx] = type;
                    GameTile tile = GameMap.getTile(nx, ny);
                    if (tile == null) {
                        System.out.println("shit's null");
                        continue;
                    }
                    tile.setTileType(type);
                }
            }
        }
    }

    //TODO fix the house position & texture
    private void placeHouse(int startX, int startY) {
        int index = 1;
        for (int dy = 0; dy < 4; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                int tileX = startX + dx;
                int tileY = startY + (3 - dy);
                TileType tile = TileType.valueOf("HOUSE_" + index);
                map[tileY][tileX] = tile;
                GameTile gameTile = GameMap.getTile(tileX, tileY);
                if(gameTile == null) {
                    System.out.println("shit's null");
                    continue;
                }
                gameTile.setTileType(tile);
                isTopLeft[tileY][tileX] = false;
                index++;
            }
        }
    }

    private void placeGreenHouse(int startX, int startY) {
        int index = 1;
        for (int dy = 0; dy < 4; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                int tileX = startX + dx;
                int tileY = startY + (3 - dy);
                TileType tile = TileType.valueOf("GREENHOUSE_" + index);
                map[tileY][tileX] = tile;
                GameTile gameTile = GameMap.getTile(tileX, tileY);
                if(gameTile == null) {
                    continue;
                }
                gameTile.setTileType(tile);
                isTopLeft[tileY][tileX] = false;
                index++;
            }
        }
    }


    public void dispose() {
        for (Texture tex : textureMap.values()) {
            tex.dispose();
        }
    }

    private void placeRandomDecorations() {
        int cropCount = 0; //TODO fix this
        int stoneCount = 50;
        int woodCount = 50;
        int fiberCount = 50;
        int maxAttempts = (cropCount + stoneCount + woodCount + fiberCount) * 2;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {


            int x = random.nextInt(MAP_WIDTH);
            int y = random.nextInt(MAP_HEIGHT);


            GameTile tile = GameMap.getTile(x, y);
            if (tile == null || tile.getTileType() != TileType.FarmFlat) {
                continue;
            }
            if(tile.getItemOnTile() != null) continue;

            setRandomDecoration(tile);
        }

    }

    public void setRandomDecoration(GameTile tile){
        Random random = new Random();
        int x = random.nextInt(100);
        if(tile.getTileType() == TileType.Mine && tile.getItemOnTile() == null) {
            tile.setItemOnTile(new Mineral(MineralType.getRandomMineralType()));
        } else if (tile.getItemOnTile() == null) {
            ForagingCrop type = ForagingCrop.getRandomForagingCrop(GameManager.getSeason());
            TreeType type1 = TreeType.getRandomTreeType(GameManager.getSeason());
            Tree newTree = new Tree(type1);
            newTree.setFullyGrown();
            newTree.setFullyGrown();
            if(x%5 == 0) {
                //tile.setItemOnTile(new ForagingItem(type, type.getName(), type.getPrice()));
            }
            else if(x%5 == 1) {
                //tile.setItemOnTile(newTree);
            }
            else if(x%5 == 2) tile.setItemOnTile(MineralType.Wood);
            else if(x%5 == 3) tile.setItemOnTile(MineralType.Stone);
            else tile.setItemOnTile(MineralType.Fiber);
        }
    }
}
