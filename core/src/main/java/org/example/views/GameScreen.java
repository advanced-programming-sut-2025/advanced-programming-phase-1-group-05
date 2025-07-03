package org.example.views;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import org.example.models.Player;
import org.example.models.TileMapRenderer;

public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TileMapRenderer mapRenderer;
    private Player player;
    private boolean overviewMode = false;
    private static final int PLAYER_FARM_WIDTH = 70;
    private static final int PLAYER_FARM_HEIGHT = 70;
    private int playerId = 0;

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

        player = new Player(new com.badlogic.gdx.graphics.Texture(Gdx.files.internal("frame_0_2.png")),
            farmStartX, farmStartY, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            player.moveUp(delta);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            player.moveDown(delta);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            player.moveLeft(delta);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            player.moveRight(delta);
        }

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.M)) {
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

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mapRenderer.render(batch, camera);
        player.draw(batch);
        batch.end();
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
    }
}
