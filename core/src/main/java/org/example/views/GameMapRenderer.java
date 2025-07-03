package org.example.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.GameMap;
import org.example.models.GameTile;
import org.example.models.MyGame;
import org.example.models.Player;
import org.example.models.Enums.TileType;

public class GameMapRenderer {
    private final GameMap map;
    private final Player player;

    private final Texture waterTexture = new Texture("Water.png");
    private final Texture soilTexture = new Texture("Soil.png");
    private final Texture flatTexture = new Texture("Flat.png");
    private final Texture treeTexture = new Texture("tree.png");
    private final Texture buildingTexture = new Texture("Building.png");
    private final Texture greenHouseTexture = new Texture("GreenHouse.png");

    private final int tileSize = 32;
    private final int visibleWidth = 20;
    private final int visibleHeight = 15;

    public GameMapRenderer(GameMap map, Player player) {
        this.map = map;
        this.player = player;
    }

    public void render(SpriteBatch batch) {
        int startX = Math.max(player.getX() - visibleWidth / 2, 0);
        int startY = Math.max(player.getY() - visibleHeight / 2, 0);

        for (int y = 0; y < visibleHeight; y++) {
            for (int x = 0; x < visibleWidth; x++) {
                int mapX = startX + x;
                int mapY = startY + y;

                GameTile tile = map.getTile(mapX, mapY);
                if (tile != null) {
                    Texture tileTexture = getTextureForTile(tile);
                    batch.draw(tileTexture, x * tileSize, y * tileSize);
                }
            }
        }

        player.render(batch, tileSize);
    }

    private Texture getTextureForTile(GameTile tile) {
        TileType type = tile.getTileType();
        return switch (type) {
            case Water -> waterTexture;
            case Soil -> soilTexture;
            case Flat -> flatTexture;
            case Tree -> treeTexture;
            case Building -> buildingTexture;
            case GreenHouse -> greenHouseTexture;
            default -> flatTexture;
        };
    }

    public void dispose() {
        waterTexture.dispose();
        soilTexture.dispose();
        flatTexture.dispose();
        treeTexture.dispose();
        buildingTexture.dispose();
        greenHouseTexture.dispose();
    }
}
