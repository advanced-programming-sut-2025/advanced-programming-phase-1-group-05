package org.example.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.GameMap;
import org.example.models.GameTile;
import org.example.models.MyGame;
import org.example.models.Player;
import org.example.models.Enums.TileType;

public class GameMapRenderer {
    private final int tileSize = 64;
    private Texture soilTexture = new Texture("Soil.png");
    private Texture waterTexture = new Texture("Water.png");
    private Texture flatTexture = new Texture("Flat.png");
    private Texture treeTexture = new Texture("tree.png");
    private Texture buildingTexture = new Texture("Building.png");
    private Texture greenHouseTexture = new Texture("GreenHouse.png");
    private Texture playerTexture = new Texture("frame_0_2.png");

    public void render(SpriteBatch batch) {
        Player player = MyGame.getCurrentPlayer();
        GameMap map = MyGame.getGameMap();

        if (player == null || map == null) return;

        int startX = player.getX() - 10;
        int startY = player.getY() - 6;
        int endX = player.getX() + 10;
        int endY = player.getY() + 6;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                GameTile tile = map.getTile(x, y);
                if (tile == null) continue;

                Texture tex;
                switch (tile.getTileType()) {
                    case Flat:
                        tex = flatTexture;
                        break;
                    case Soil:
                        tex = soilTexture;
                        break;
                    case Water:
                        tex = waterTexture;
                        break;
                    case Building:
                        tex = buildingTexture;
                        break;
                    case Tree:
                        tex = treeTexture;
                        break;
                    case GreenHouse:
                        tex = greenHouseTexture;
                        break;
                    default:
                        tex = flatTexture;
                        break;
                }

                batch.draw(tex, (x - startX) * tileSize, (y - startY) * tileSize, tileSize, tileSize);

                if (x == player.getX() && y == player.getY()) {
                    batch.draw(playerTexture, (x - startX) * tileSize, (y - startY) * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public void dispose() {
        soilTexture.dispose();
        waterTexture.dispose();
        flatTexture.dispose();
        treeTexture.dispose();
        buildingTexture.dispose();
        greenHouseTexture.dispose();
        playerTexture.dispose();
    }
}
