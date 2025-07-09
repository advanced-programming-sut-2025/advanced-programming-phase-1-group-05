package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.GameManager;
import org.example.controllers.GameMenuController;
import org.example.models.*;
import org.example.models.Enums.Season;

import java.util.ArrayList;

public class TestScreen implements Screen {
    private ShapeRenderer shapeRenderer;
    Stage stage;
    Table missionListTable;
    Skin skin;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TileMapRenderer mapRenderer;
    private Player player;

    private Texture energyBarBg, energyBarFill, overlay, blackOverlay;
    private BitmapFont font;

    static final int TILE_SIZE = 64;
    private static final int VIEW_WIDTH = 20;
    private static final int VIEW_HEIGHT = 15;

    private float timeAccumulator = 0f;
    private boolean overviewMode = false;
    private Season currentSeason;
    private ArrayList<Rectangle> allowedArea = new ArrayList<>();
    private ArrayList<Player> players;

    private CheatCodeWindow cheatCodeWindow;

    public TestScreen() {
        skin = GameAssetManager.getInstance().getSkin();
        camera = new OrthographicCamera(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        cheatCodeWindow = new CheatCodeWindow(batch);

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(cheatCodeWindow);
        multiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(multiplexer);
        System.out.println("InputMultiplexer set with cheatCodeWindow");

        missionListTable = new Table();
        missionListTable.setVisible(false);
        missionListTable.setFillParent(true);
        stage.addActor(missionListTable);
        showMissionList(MyGame.getNPCByName("Leah"));

    }

    @Override
    public void render(float v) {

        Gdx.gl.glClearColor(0.6f, 0.8f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    private void showMissionList(NPC npc) {
        missionListTable.clear();
        missionListTable.setVisible(true);

        Table innerPanel = new Table(skin);
        Texture questLogTexture = new Texture(Gdx.files.internal("NPCs/questLog.png"));
        Drawable questLogDrawable = new TextureRegionDrawable(new TextureRegion(questLogTexture));
        innerPanel.setBackground(questLogDrawable);
        innerPanel.pad(30);

        for (Mission mission : npc.getMissions()) {
            Label label = new Label(mission.getTitle(), skin);
            Image icon = new Image(getStatusDrawable(mission));

            Table row = new Table();
            label.setColor(86f/225f, 22f/225f, 12f/225f,1);
            if (npc.getMissions().indexOf(mission) == 0) {
                row.add(label).padTop(45).padBottom(5).padRight(10).padLeft(10);
                row.add(icon).size(32).pad(5).padTop(45).padBottom(5).padRight(10);
            }
            else if (npc.getMissions().indexOf(mission) == 2) {
                row.add(label).padBottom(70).padRight(10);
                row.add(icon).size(32).pad(5).padBottom(70).padRight(10);
            }
            else {
                row.add(label).padBottom(10).padRight(10);
                row.add(icon).size(32).pad(5).padTop(10).padBottom(10).padRight(10);
            }

            innerPanel.add(row).padBottom(10).row();
        }

        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                missionListTable.setVisible(false);
            }
        });

        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        missionListTable.add(innerPanel).center();
    }

    private Drawable getStatusDrawable(Mission mission) {
        Mission.Status status = mission.getStatus();
        if (!mission.getPlayerUsername().isEmpty() &&
            !mission.getPlayerUsername().equals(MyGame.getCurrentPlayer().getUsername()))
            status = Mission.Status.DONE_BY_OTHER;
        switch (status) {
            case COMPLETED: {
                Texture texture = new Texture(Gdx.files.internal("missionStatus/icon_completed.png"));
                return new TextureRegionDrawable(new TextureRegion(texture));
            }
            case LOCKED: {
                Texture texture = new Texture(Gdx.files.internal("missionStatus/icon_locked.png"));
                return new TextureRegionDrawable(new TextureRegion(texture));
            }
            case DONE_BY_OTHER: {
                Texture texture = new Texture(Gdx.files.internal("missionStatus/icon_x.png"));
                return new TextureRegionDrawable(new TextureRegion(texture));
            }
            default: {
                Texture texture = new Texture(Gdx.files.internal("missionStatus/icon_available.png"));
                return new TextureRegionDrawable(new TextureRegion(texture));
            }
        }
    }


}
