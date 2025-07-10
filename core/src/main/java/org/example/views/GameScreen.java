package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.example.controllers.GameManager;
import org.example.controllers.GameMenuController;
import org.example.models.*;
import org.example.models.Enums.Season;

import java.util.ArrayList;
import java.util.Map;

public class GameScreen implements Screen {
    private ShapeRenderer shapeRenderer;
    Stage stage;
    Table missionListTable;
    Skin skin;

    Viewport viewport;
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

    private boolean isInvenotryOpen = false;
    public static Array<Rectangle> farms = new Array<>();
    Array<NpcActor> NPCs = new Array<>();

    //inventory stuff
    private ArrayList<InventorySlot> slots = new ArrayList<>();
    private float SLOT_SIZE = 64;
    private float INVENTORY_X = 0;
    private float INVENTORY_Y = 0;
    private Item draggedItem = null;
    private InventorySlot selectedSlot = null;
    private boolean trashcanOpen = false;
    private Rectangle trashcan = new Rectangle();


    public GameScreen(ArrayList<Player> playerList) {
        skin = GameAssetManager.getInstance().getSkin();
        camera = new OrthographicCamera(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        cheatCodeWindow = new CheatCodeWindow(batch);
        players = playerList;

        shapeRenderer = new ShapeRenderer();

        currentSeason = GameManager.getSeason();
        mapRenderer = new TileMapRenderer();
        mapRenderer.setSeason(currentSeason);

        Player currentPlayer = MyGame.getCurrentPlayer();
        String selectedMap = GameMenuController.getMapForPlayer(currentPlayer.getUsername());
        Vector2 spawnPosition = getInitialPositionForMap(selectedMap);
        allowedArea.add(getAllowedAreaForMap(selectedMap));
        initializeFarmArea();

        player = MyGame.getCurrentPlayer();
        player.setPosition(spawnPosition.x, spawnPosition.y);

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

    private void initializeFarmArea() {
        for (Player player : players) {
            String map = GameMenuController.getMapForPlayer(player.getUsername());
            farms.add(getAllowedAreaForMap(map));
        }

        for (NPC npc : MyGame.getAllNPCs()) {
            NPCs.add(new NpcActor(npc));
        }
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
            case "map1": return new Rectangle(10f * TILE_SIZE, 10f * TILE_SIZE, 50f * TILE_SIZE, 50f * TILE_SIZE);
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
        cheatCodeWindow.update(delta);


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

//        stage.act(delta);
//        stage.draw();
        batch.end();
        cheatCodeWindow.render();
        showInventory(batch);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isInvenotryOpen = !isInvenotryOpen;
            if (isInvenotryOpen) {
                TextureRegion inventory = MyGame.getCurrentPlayer().getBackPack().getLevel().getInventoryTexture();
                float scale = 0.5f;
                float scaledWidth = inventory.getRegionWidth() * scale;
                float scaledHeight = inventory.getRegionHeight() * scale;
                INVENTORY_X = camera.position.x - scaledWidth / 2f;
                INVENTORY_Y = camera.position.y - scaledHeight / 2f;

                updateInventorySlots();
            }
        }
        stage.act(delta);
        stage.draw();

    }

    private void handleInput(float delta) {
        Vector2 oldPos = new Vector2(player.getXX(), player.getYY());
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.moveUp(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.moveDown(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.moveLeft(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.moveRight(delta);


        // can't go to round area
//        if (!allowedArea.contains(player.getXX(), player.getYY())) {
//            player.setPosition(oldPos.x, oldPos.y);
//        }
        float px = player.getXX() + player.getWidth() / 2f;
        float py = player.getYY() + player.getHeight() / 2f;
        if (!canWalk(px, py)) {
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
        } else if (hour == 22) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            batch.setColor(0f, 0f, 0f, 1f);
            batch.draw(blackOverlay, camera.position.x - camera.viewportWidth / 2,
                camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight);
            batch.setColor(1, 1, 1, 1);
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }


    public void showInventory(SpriteBatch batch) {
        if (!isInvenotryOpen) return;

        TextureRegion inventory = MyGame.getCurrentPlayer().getBackPack().getLevel().getInventoryTexture();

        float scale = 0.5f;
        float originalWidth = inventory.getRegionWidth();
        float originalHeight = inventory.getRegionHeight();

        float scaledWidth = originalWidth * scale;
        float scaledHeight = originalHeight * scale;

        INVENTORY_X = camera.position.x - scaledWidth / 2f;
        INVENTORY_Y = camera.position.y - scaledHeight / 2f;

        batch.begin();
        batch.draw(inventory, INVENTORY_X, INVENTORY_Y, scaledWidth, scaledHeight);

        for (InventorySlot slot : slots) {
            if (slot.item != null) {
                //making sure images aren't blurred
                TextureRegion texture = slot.item.getTexture();
                float texWidth = texture.getRegionWidth();
                float texHeight = texture.getRegionHeight();

                float scale1 = Math.min(SLOT_SIZE / texWidth, SLOT_SIZE / texHeight);
                float drawWidth = texWidth * scale1;
                float drawHeight = texHeight * scale1;

                float drawX = slot.x + (SLOT_SIZE - drawWidth) / 2f;
                float drawY = slot.y + (SLOT_SIZE - drawHeight) / 2f;

                batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
            }
        }

        if (draggedItem != null) {
            Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            batch.draw(draggedItem.getTexture(), mouse.x - SLOT_SIZE / 2f, mouse.y - SLOT_SIZE / 2f, SLOT_SIZE, SLOT_SIZE);
        }

        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float trashTargetSize = 128;
        float aspectRatio = (float) GameAssetManager.trashcanClosed.getRegionWidth() /
            GameAssetManager.trashcanClosed.getRegionHeight();
        float trashWidth = trashTargetSize * aspectRatio;
        float trashHeight = trashTargetSize;
        float trashX = INVENTORY_X + scaledWidth - trashWidth + 100;
        float trashY = INVENTORY_Y + 70;
        trashcan.set(trashX, trashY, trashWidth, trashHeight);

        trashcanOpen = trashcan.contains(mouse.x, mouse.y);
        batch.draw(trashcanOpen ? GameAssetManager.trashcanOpen : GameAssetManager.trashcanClosed,
            trashX, trashY, trashWidth, trashHeight);
        batch.end();

        //outline the slot mouse is on
        InventorySlot hoveredSlot = null;

        for (InventorySlot slot : slots) {
            if (mouse.x >= slot.x && mouse.x <= slot.x + SLOT_SIZE &&
                mouse.y >= slot.y && mouse.y <= slot.y + SLOT_SIZE) {
                hoveredSlot = slot;
                break;
            }
        }

        if (hoveredSlot != null) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            Gdx.gl.glLineWidth(5f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(hoveredSlot.x, hoveredSlot.y, SLOT_SIZE, SLOT_SIZE);
            shapeRenderer.end();
        }
    }

    @Override public void resize(int width, int height) {
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {
        viewport = new FitViewport(320, 180, camera);
        viewport.apply();

        stage = new Stage(viewport, batch);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(cheatCodeWindow);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InventoryInputHandler());

        Gdx.input.setInputProcessor(multiplexer);
        System.out.println("InputMultiplexer set with cheatCodeWindow");

        missionListTable = new Table();
        missionListTable.setVisible(false);
        missionListTable.setFillParent(true);
        stage.addActor(missionListTable);

        for (NpcActor npc : NPCs) {
            stage.addActor(npc);
        }
        forceViewportReset();
    }
    private void forceViewportReset() {
        toggleOverviewMode();
        toggleOverviewMode();
    }
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

    private boolean canWalk(float x, float y) {
        for (Player player : players) {
            if (player.getFarm() != null) {
                if (player.getFarm().isInFarm(x, y) && !player.getFarm().isOwner(MyGame.getCurrentPlayer())) {
                    return false;
                }
            }
        }

        return true;
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

    public void updateInventorySlots() {
        slots.clear();

        float slotPadding = 1f;
        float leftOffset = 35f;
        float topOffset = 485;
        int cols = 12;
        int rows = 3;

        ArrayList<Item> items = new ArrayList<>(MyGame.getCurrentPlayer().getBackPack().getInventory().keySet());
        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                InventorySlot slot = new InventorySlot();
                slot.x = INVENTORY_X + leftOffset + col * (SLOT_SIZE + slotPadding) + 5f;
                slot.y = INVENTORY_Y + (3 - row - 1) * (SLOT_SIZE + 10f) + topOffset - 150f;

                if (index < items.size()) {
                    Item item = items.get(index++);
                    slot.item = item;
                    slot.count = MyGame.getCurrentPlayer().getBackPack().getInventory().get(item);
                }

                slots.add(slot);
            }
        }
    }


    private void syncBackPackFromSlots() {
        MyGame.getCurrentPlayer().getBackPack().getInventory().clear();
        for (InventorySlot slot : slots) {
            if (slot.item != null) {
                MyGame.getCurrentPlayer().getBackPack().addToInventory(slot.item, 1);
            }
        }
    }

    private class InventoryInputHandler extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (!isInvenotryOpen) return false;

            Vector3 world = camera.unproject(new Vector3(screenX, screenY, 0));

            //check trash can
            if (draggedItem != null && trashcan.contains(world.x, world.y)) {
                MyGame.getCurrentPlayer().getBackPack().removeFromInventory(draggedItem, 1);
                draggedItem = null;
                selectedSlot = null;
                updateInventorySlots();
                syncBackPackFromSlots();
                return true;
            }
            //check the slots
            for (InventorySlot slot : slots) {
                if (world.x >= slot.x && world.x <= slot.x + SLOT_SIZE &&
                    world.y >= slot.y && world.y <= slot.y + SLOT_SIZE) {

                    if (draggedItem == null && slot.item != null) {
                        draggedItem = slot.item;
                        selectedSlot = slot;
                        slot.item = null;
                        MyGame.getCurrentPlayer().setCurrentItem(slot.item);
                        return true;
                    } else if (draggedItem != null && slot.item == null) {
                        slot.item = draggedItem;
                        draggedItem = null;
                        selectedSlot = null;
                        MyGame.getCurrentPlayer().setCurrentItem(null);
                        syncBackPackFromSlots();
                        return true;
                    } else if (draggedItem != null && slot.item != null) {
                        Item temp = slot.item;
                        slot.item = draggedItem;
                        draggedItem = temp;
                        selectedSlot = slot;
                        MyGame.getCurrentPlayer().setCurrentItem(slot.item);
                        syncBackPackFromSlots();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    }
