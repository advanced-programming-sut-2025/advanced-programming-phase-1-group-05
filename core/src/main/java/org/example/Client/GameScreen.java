package org.example.Client;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.example.Common.*;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.DataTransferObjects.PrivateChatMessage;
import org.example.Common.Enums.*;
import org.example.Common.Request.TradeMessage;
import org.example.Main;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResponse;
import org.example.Server.controllers.*;
import org.example.Server.models.*;
import org.example.Server.models.Building.AnimalHouse;
import org.example.Common.Tool.BackPack;
import org.example.Common.Tool.FishingPole;
import org.example.Common.Tool.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.Common.GameMap.MAP_HEIGHT;
import static org.example.Common.GameMap.MAP_WIDTH;

public class GameScreen implements Screen {
    Table dialogueTable;
    private ShapeRenderer shapeRenderer;
    GameMenuController controller;
    HomeMenuController homeMenuController;
    Stage stage;
    Table missionListTable, animalMenuTable, notificationTable, giftMenuTable, giftHistoryTable, rateTable,
        playerMenuTable, artisanMenuTable, npcMenuTable, friendshipMenuTable;
    Skin skin;
    ImageButton notificationButton, friendshipButton, tradingButton;
    Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TileMapRenderer mapRenderer;

    private boolean turnJustChanged = false;
    private boolean showGreenhouseMessage = false;
    private float greenhouseMessageTimer = 0f;
    private boolean buildGreenHouseMessage = false;
    InputMultiplexer multiplexer;

    Stage uiStage;
    private Texture energyBarBg, energyBarFill, overlay, blackOverlay;
    private BitmapFont font;

    static final int TILE_SIZE = 64;
    private static final int VIEW_WIDTH = 20;
    private static final int VIEW_HEIGHT = 15;

    private float timeAccumulator = 0f;
    private boolean overviewMode = false, messageMode;
    private Season currentSeason;
    private final ArrayList<Player> players;

    private boolean isInvenotryOpen = false;
    public static Array<Rectangle> farms = new Array<>();
    Array<NpcActor> NPCs = new Array<>();

    // lightning effect
    private boolean lightningEffectActive = false;
    private float lightningTimer = 0f;
    private float lightningDuration = 1f;


    //inventory stuff
    private ArrayList<InventorySlot> slots = new ArrayList<>();
    private float SLOT_SIZE = 64;
    private float INVENTORY_X = 0;
    private float INVENTORY_Y = 0;
    private Item draggedItem = null, selectedItem = null;
    private InventorySlot selectedSlot = null;
    private boolean trashcanOpen = false;
    private Rectangle trashcan = new Rectangle();
    private Rectangle inventoryBounds = new Rectangle();

    //skill set stuff
    private boolean isSkillSetOpen = false;
    private float SKILL_X = 0;
    private float SKILL_Y = 0;
    private Rectangle skillSetBounds = new Rectangle();
    private final HashMap<SkillSetInfo, Rectangle> skillHitboxes = new HashMap<>();

    //tool selection stuff
    private boolean isToolSelectionOpen = false;
    private ArrayList<InventorySlot> toolSlots = new ArrayList<>();
    private float TOOL_X = 0;
    private float TOOL_Y = 0;

    //craft stuff
    private boolean isCraftOpen = false;
    private float CRAFT_X = 0;
    private float CRAFT_Y = 0;
    private int craftPageIndex;
    private CraftType hoveredCraftType;

    //cooking stuff
    private boolean isCookingOpen = false;
    private float COOKING_X = 0;
    private float COOKING_Y = 0;
    private CookingRecipeType hoveredCookingRecipeType;

    //NPC/friendship stuff
    private boolean giftMode = false;
    private NpcActor lastNPC = null;
    private Player lastPlayer = null;
    private boolean hugMode = false;
    private Player playerA = null, playerB = null;
    private float hugTimer = 0f;
    private static final float HUG_DURATION = 2f;
    private static final float BOUNCE_HEIGHT = 10f;
    private static final float BOUNCE_SPEED = 10f;

    //Animal stuff
    private boolean isInBuildMode = false;
    private Texture buildingPreviewTexture;
    InputProcessor buildInputProcessor;
    private EnclosureType lastType;
    private AnimalHouseLevel lastLevel;

    // Artisan
    private boolean artisanInputMode = false;
    private List<InventorySlot> selectedSlots = new ArrayList<>();
    private ArtisanMachine lastArtisan;
    private boolean artisanMode = false;
    private Texture artisanPreviewTexture;
    private ArtisanType lastArtisanType;


    //result stuff
    private TextureRegion resultBg = GameAssetManager.resultTexture;
    private float resultTime = 0;
    private float resultDuration = 2f;
    public boolean showResult = false;
    public Result latestResult;
    private GlyphLayout layout = new GlyphLayout();

    //food buff stuff
    private float buffTime = 0;
    private float buffDuration = 2f;
    private boolean buffStarted = false;

    //cheat code window
    private CheatCodeWindow cheatCodeWindow;

    //journal stuff
    private TextureRegion journalBg = GameAssetManager.journalBg;
    private boolean isJournalOpen = false;
    private float JOURNAL_X = 0;
    private float JOURNAL_Y = 0;

    //trading stuff
    private TradeMenu tradeMenu;
    public boolean tradeMenuOpen = false;
    ImageButton accept, reject;
    boolean acceptedRequest;

    //chat
    private Table chatTable;
    private TextField chatInputField;
    private ScrollPane chatScrollPane;
    private VerticalGroup chatMessagesGroup;
    private boolean isChatOpen = false;
    private String currentPrivateTarget = null;
    private Table privateChatTable;

    Player player;


    public GameScreen(ArrayList<Player> playerList) {
        player = MyGame.getCurrentPlayer();
        MyGame.setGameScreen(this);
        skin = GameAssetManager.getSkin();
        camera = new OrthographicCamera(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        players = playerList;
        controller = new GameMenuController();
        controller = new GameMenuController(Main.currentUser , Main.getMain().getNetworkManager());
        homeMenuController = new HomeMenuController();
        cheatCodeWindow = new CheatCodeWindow(camera, Gdx.input.getInputProcessor());
        shapeRenderer = new ShapeRenderer();
        currentSeason = GameManager.getSeason();
        mapRenderer = new TileMapRenderer();
        mapRenderer.setSeason(currentSeason);

        Vector2 spawnPos = null;
        for (Player player : players) {
            String selectedMap = GameMenuController.getMapForPlayer(player.getUsername());
            Vector2 spawnPosition = getInitialPositionForMap(selectedMap);
            if (players.indexOf(player) == 0) spawnPos = spawnPosition;
            player.setPosition(spawnPosition.x, spawnPosition.y);


        }
        initializeFarmArea();
        Player player = MyGame.getCurrentPlayer();
        camera.position.set(spawnPos.x + player.getWidth() / 2f,
            spawnPos.y + player.getHeight() / 2f, 0);
        camera.update();


        energyBarBg = new Texture(Gdx.files.internal("ui/energy_bar_bg.png"));
        energyBarFill = new Texture(Gdx.files.internal("ui/energy_bar_fill.png"));
        overlay = new Texture("white.png");
        blackOverlay = new Texture("black.png");

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);

    }

    private void tileOutline(Vector3 mouseWorld) {
        int tileX = (int) (mouseWorld.x / TILE_SIZE);
        int tileY = (int) (mouseWorld.y / TILE_SIZE);

        if (tileX >= 0 && tileX < MAP_WIDTH &&
            tileY >= 0 && tileY < MAP_HEIGHT &&
            MyGame.getCurrentPlayer().getCurrentItem() != null) {

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(
                tileX * TILE_SIZE,
                tileY * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE
            );
            shapeRenderer.end();
        }

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
            case "map1":
                return new Vector2(10 * TILE_SIZE, 10 * TILE_SIZE);
            case "map2":
                return new Vector2(80 * TILE_SIZE, 10 * TILE_SIZE);
            case "map3":
                return new Vector2(10 * TILE_SIZE, 80 * TILE_SIZE);
            case "map4":
                return new Vector2(80 * TILE_SIZE, 80 * TILE_SIZE);
            default:
                return new Vector2(0, 0);
        }
    }

    private Rectangle getAllowedAreaForMap(String mapName) {
        switch (mapName.toLowerCase()) {
            case "map1":
                return new Rectangle(10f * TILE_SIZE, 10f * TILE_SIZE, 50f * TILE_SIZE, 50f * TILE_SIZE);
            case "map2":
                return new Rectangle(80 * TILE_SIZE, 10 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE);
            case "map3":
                return new Rectangle(10 * TILE_SIZE, 80 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE);
            case "map4":
                return new Rectangle(80 * TILE_SIZE, 80 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE);
            default:
                return new Rectangle(0, 0, 0, 0);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 136 / 255f, 199 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        if (!cheatCodeWindow.isVisible() && !messageMode) handleInput(delta);


        if (Main.getMain().networkManager != null) {
           // Main.networkManager.sendPlayerPosition(player.getUsername(), player.getXX(), player.getYY());
        }
        Season newSeason = GameManager.getSeason();
        if (!newSeason.equals(currentSeason)) {
            currentSeason = newSeason;
            mapRenderer.setSeason(currentSeason);
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (isInvenotryOpen) {
            updateInventorySlots(INVENTORY_X, INVENTORY_Y);
        }
        if (isCraftOpen) {
            updateInventorySlots(CRAFT_X, CRAFT_Y);
        }
        if (isCookingOpen) {
            updateInventorySlots(COOKING_X, COOKING_Y);
        }

        float bounceOffset = 0;
        if (hugMode) {
            hugTimer += delta;

            bounceOffset = MathUtils.sin(hugTimer * BOUNCE_SPEED) * BOUNCE_HEIGHT;

            if (hugTimer >= HUG_DURATION) {
                hugMode = false;
                playerA = null;
                playerB = null;
            }
        }

        mapRenderer.render(batch, camera);
        for (Player player : players) {
            if (player.equals(playerA) || player.equals(playerB))
                player.draw(batch, bounceOffset);
            else player.draw(batch, 0);
            for (AnimalHouse house : player.getCoopsAndBarns()) {
                house.draw(batch);
            }
        }
        if (isInBuildMode && buildingPreviewTexture != null) {
            Vector2 mousse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector3 worldCoords = camera.unproject(new Vector3(mousse.x, mousse.y, 0));
            batch.setColor(1f, 1f, 1f, 0.5f);
            batch.draw(buildingPreviewTexture, worldCoords.x, worldCoords.y);
            batch.setColor(1f, 1f, 1f, 1f);
        }
        if (artisanMode && artisanPreviewTexture != null) {
            Vector2 mousse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector3 worldCoords = camera.unproject(new Vector3(mousse.x, mousse.y, 0));
            batch.setColor(1f, 1f, 1f, 0.5f);
            batch.draw(artisanPreviewTexture, worldCoords.x, worldCoords. y);
            batch.setColor(1f, 1f, 1f, 1f);
        }
        drawEnergyBar();
        drawHUD();

        applyLightingOverlay();

        if (showGreenhouseMessage) {
            greenhouseMessageTimer -= delta;
            if (greenhouseMessageTimer <= 0) {
                showGreenhouseMessage = false;
            } else {
                font.setColor(Color.WHITE);
                font.draw(
                    batch,
                    "Press Q to build a Greenhouse\n (Cost: 1000 coins & 500 wood)",
                    camera.position.x - 350,
                    camera.position.y + camera.viewportHeight / 2 - 20
                );
            }
        }

        if (buildGreenHouseMessage) {
            greenhouseMessageTimer -= delta;
            if (greenhouseMessageTimer <= 0) {
                buildGreenHouseMessage = false;
            } else {
                MyGame.getCurrentPlayer().setBuildGreenHouse(true);
                font.setColor(Color.WHITE);
                font.draw(
                    batch,
                    "Green house built, " + MyGame.getCurrentPlayer().getGold() + " gold",
                    camera.position.x - 350,
                    camera.position.y + camera.viewportHeight / 2 - 20
                );
            }
        }


        if (lightningEffectActive) {
            lightningTimer += delta;

            float stage = (lightningTimer / lightningDuration) * 3;

            if (stage < 1) {
                batch.setColor(0f, 0f, 0f, 0.9f);
            } else if (stage < 2) {
                batch.setColor(0.7f, 0.7f, 0.7f, 0.7f);
            } else {
                batch.setColor(1f, 1f, 1f, 0f);
            }

            if (stage < 2) {
                batch.draw(blackOverlay,
                    camera.position.x - camera.viewportWidth / 2,
                    camera.position.y - camera.viewportHeight / 2,
                    camera.viewportWidth, camera.viewportHeight);
            }

            batch.setColor(1, 1, 1, 1);

            if (lightningTimer >= lightningDuration) {
                lightningEffectActive = false;
            }
        }

        batch.end();
        tileOutline(mouse);
        stage.act(delta);
        stage.draw();
        if (isInvenotryOpen) {
            for (NpcActor npc : NPCs) {
                npc.setVisible(false);
            }
        } else {
            for (NpcActor npc : NPCs) {
                npc.setVisible(true);
            }
        }
        showInventory(batch);
        showSkillSet(batch);
        showCraftPage(batch);
        showToolSelection(batch);
        showCookingPage(batch);
        showJournalPage(batch);
        showFoodBuff(batch, delta);
        updateToolSelectionSlots();
        checkGifting();
        checkArtisanInput();
        if (showResult) showResult(batch, latestResult, delta);
//        stage.act(delta);
//        stage.draw();
        cheatCodeWindow.render(delta);
        uiStage.act(delta);
        uiStage.draw();
    }

    private void closeAllPages() {
        isInvenotryOpen = false;
        isSkillSetOpen = false;
        isCraftOpen = false;
        isToolSelectionOpen = false;
    }

    public void triggerLightningEffect() {
        lightningEffectActive = true;
        lightningTimer = 0f;
        GameAssetManager.playSfx("thor");
    }

    private void checkGifting() {
        updateInventorySlots(INVENTORY_X, INVENTORY_Y);
        if (isInvenotryOpen && giftMode && Gdx.input.justTouched()) {
            TextureRegion inventory = MyGame.getCurrentPlayer().getBackPack()
                .getLevel().getInventoryTexture();
            float scale = 0.5f;
            float scaledWidth = inventory.getRegionWidth() * scale;
            float scaledHeight = inventory.getRegionHeight() * scale;
            INVENTORY_X = camera.position.x - scaledWidth / 2f;
            INVENTORY_Y = camera.position.y - scaledHeight / 2f;

            skillSetBounds.set(INVENTORY_X + 20f, INVENTORY_Y, 64, 64);

            Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            Player player = MyGame.getCurrentPlayer();
            Vector3 playerPos = camera.project(new Vector3(player.getXX(), player.getYY(), 0));

            for (InventorySlot slot : slots) {
                if (mouse.x >= slot.x && mouse.x <= slot.x + SLOT_SIZE &&
                    mouse.y >= slot.y && mouse.y <= slot.y + SLOT_SIZE) {

                    if (slot.item != null) {
                        Item giftedItem = slot.item;
                        Vector3 receiverPos = null;

                        if (lastNPC != null) {
                            latestResult = controller.giftNPC(lastNPC.getNpc(), giftedItem);
                            showResult = true;
                            receiverPos = camera.project(new Vector3(lastNPC.getX(), lastNPC.getY(), 0));
                        } else if (lastPlayer != null) {
                            latestResult = controller.giftPlayer(lastPlayer, giftedItem, 1);
                            showResult = true;
                            receiverPos = camera.project(new Vector3(lastPlayer.getXX(), lastPlayer.getYY(), 0));
                        }
                        if (receiverPos != null && latestResult.isSuccess()) {
                            slot.item = null;
                            TextureRegionDrawable drawable = new TextureRegionDrawable(giftedItem.getTexture());
                            Image flyingGift = new Image(drawable);
                            Vector2 startPos = new Vector2(player.getXX(), player.getYY());
                            Vector2 endPos = lastNPC != null
                                ? new Vector2(lastNPC.getX(), lastNPC.getY())
                                : new Vector2(lastPlayer.getXX(), lastPlayer.getYY());

                            flyingGift.setSize(32, 32);
                            flyingGift.setColor(Color.WHITE); // In case alpha is weird
                            flyingGift.setPosition(playerPos.x, playerPos.y);
                            stage.addActor(flyingGift);
                            isInvenotryOpen = false;
                            flyingGift.setPosition(startPos.x, startPos.y);
                            stage.addActor(flyingGift);

                            flyingGift.addAction(Actions.sequence(
                                Actions.moveTo(endPos.x, endPos.y, 1f, Interpolation.sine),
                                Actions.fadeOut(0.2f),
                                Actions.run(() -> {
                                    flyingGift.remove();
                                    if (lastNPC != null) lastNPC.setWalking(true);
                                })
                            ));

                        }
                        giftMode = false;
                        isInvenotryOpen = false;

                        break;
                    }
                }
            }
        }
    }

    private void checkArtisanInput() {
        if (!isInvenotryOpen || !artisanInputMode) return;

        if (Gdx.input.justTouched()) {
            Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            for (InventorySlot slot : slots) {
                if (mouse.x >= slot.x && mouse.x <= slot.x + SLOT_SIZE &&
                    mouse.y >= slot.y && mouse.y <= slot.y + SLOT_SIZE) {

                    if (slot.item != null) {
                        if (selectedSlots.contains(slot)) {
                            selectedSlots.remove(slot); // deselect
                        } else {
                            selectedSlots.add(slot); // select
                        }
                        break;
                    }
                }
            }
        }

        // Confirm selection with Enter key
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            List<String> selectedItems = new ArrayList<>();

            latestResult = lastArtisan.insertItem(selectedItems);
            if (latestResult.isSuccess()){
                for (InventorySlot slot : selectedSlots) {
                    selectedItems.add(slot.item.getName());
                    MyGame.getCurrentPlayer().getBackPack().removeFromInventory(slot.item, 1);
                }
            }
            artisanInputMode = false;
            isInvenotryOpen = false;
            selectedSlots.clear();
            showResult = true;
            lastArtisan.setVisible(true);
        }
    }

    private void handleInput(float delta) {
        Player player = MyGame.getCurrentPlayer();
        Vector2 oldPos = new Vector2(player.getXX(), player.getYY());
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.moveUp(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) player.moveDown(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.moveLeft(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.moveRight(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            cheatCodeWindow.toggle();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            MyGame.getCurrentPlayer().addGold(100000);
            Main.getMain().setScreen(new FishingMiniGame(this, new FishingPole(), FishType.CrimsonFish));
        }
        if (Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector3 click = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            for (Player p : players) {
                if (!p.equals(MyGame.getCurrentPlayer())) {
                    Rectangle bounds = new Rectangle(p.getXX(), p.getYY(), p.getWidth(), p.getHeight());
                    if (bounds.contains(click.x, click.y)) {
                        showPlayerMenu(p);
                        break;
                    }
                }
            }
        }
        if (Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 click = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (click.x / TILE_SIZE);
            int tileY = (int) (click.y / TILE_SIZE);

            GameTile clickedTile = GameMap.getTile(tileX, tileY);
            if (clickedTile != null) {
                TileType type = clickedTile.getTileType();

                if (type.name().startsWith("GREENHOUSE_")) {
                    showGreenhouseMessage = true;
                    greenhouseMessageTimer = 3f;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Item wood = MyGame.getDatabase().getItem("wood");

            if (MyGame.getCurrentPlayer().getGold() >= 1000 &&
                MyGame.getCurrentPlayer().getBackPack().howManyOfItem(wood) >= 500) {

                MyGame.getCurrentPlayer().addGold(-1000);
                MyGame.getCurrentPlayer().getBackPack().removeFromInventory(wood, 500);

                buildGreenHouseMessage = true;
                greenhouseMessageTimer = 3f;
            } else {
                latestResult = Result.error("Not enough gold or wood!");
                showResult = true;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            triggerLightningEffect();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            Result result = controller.nextTurn();
            if (result.isSuccess()) {
                Player currentPlayer = MyGame.getCurrentPlayer();
                String map = GameMenuController.getMapForPlayer(currentPlayer.getUsername());

                farms.clear();
                farms.add(getAllowedAreaForMap(map));

                Vector2 pos = getInitialPositionForMap(map);
                camera.viewportWidth = VIEW_WIDTH * TILE_SIZE;
                camera.viewportHeight = VIEW_HEIGHT * TILE_SIZE;
                camera.position.set(
                    pos.x + currentPlayer.getWidth() / 2f,
                    pos.y + currentPlayer.getHeight() / 2f,
                    0
                );
                camera.update();

                turnJustChanged = true;
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            GameManager.getGameClock().advanceDay();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            latestResult = controller.eatFood(player.getCurrentItem());
            if (latestResult.isSuccess()) {
                player.setEating(true);
            }
            showResult = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (dialogueTable.isVisible()) {
                dialogueTable.setVisible(false);
            }
            else {
                GameAssetManager.playSfx("open page");
                isInvenotryOpen = !isInvenotryOpen;
                isCraftOpen = false;
                isSkillSetOpen = false;
                isToolSelectionOpen = false;
                isCookingOpen = false;
                isJournalOpen = false;
                if (isInvenotryOpen) {
                    TextureRegion inventory = MyGame.getCurrentPlayer().getBackPack()
                        .getLevel().getInventoryTexture();
                    float scale = 0.5f;
                    float scaledWidth = inventory.getRegionWidth() * scale;
                    float scaledHeight = inventory.getRegionHeight() * scale;
                    INVENTORY_X = camera.position.x - scaledWidth / 2f;
                    INVENTORY_Y = camera.position.y - scaledHeight / 2f;

                    skillSetBounds.set(INVENTORY_X + 20f, INVENTORY_Y, 64, 64);

                    updateInventorySlots(INVENTORY_X, INVENTORY_Y);
                }
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            GameAssetManager.playSfx("open page");
            isSkillSetOpen = !isSkillSetOpen;
            isCraftOpen = false;
            isInvenotryOpen = false;
            isToolSelectionOpen = false;
            isCookingOpen = false;
            isJournalOpen = false;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            GameAssetManager.playSfx("open page");
            isToolSelectionOpen = !isToolSelectionOpen;
            isCraftOpen = false;
            isSkillSetOpen = false;
            isInvenotryOpen = false;
            isCookingOpen = false;
            isJournalOpen = false;
            updateToolSelectionSlots();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            GameAssetManager.playSfx("open page");
            isCraftOpen = !isCraftOpen;
            isToolSelectionOpen = false;
            isSkillSetOpen = false;
            isInvenotryOpen = false;
            isCookingOpen = false;
            isJournalOpen = false;
            updateInventorySlots(CRAFT_X, CRAFT_Y);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            GameAssetManager.playSfx("open page");
            isCookingOpen = !isCookingOpen;
            isToolSelectionOpen = false;
            isCraftOpen = false;
            isSkillSetOpen = false;
            isInvenotryOpen = false;
            isJournalOpen = false;
            updateInventorySlots(COOKING_X, COOKING_Y);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            GameAssetManager.playSfx("open page");
            isJournalOpen = !isJournalOpen;
            isCraftOpen = false;
            isToolSelectionOpen = false;
            isCraftOpen = false;
            isSkillSetOpen = false;
            isInvenotryOpen = false;
        }
        float px = player.getXX() + player.getWidth() / 2f;
        float py = player.getYY() + player.getHeight() / 2f;
        Store stoore = null;
        Rectangle playerRect = new Rectangle(player.getXX(), player.getYY(), player.getWidth(), player.getHeight());
        for (Store store : MyGame.getDatabase().getStores()) {
            if (store.getBoundingRectangle().overlaps(playerRect)) {
                stoore = store;
                player.setPosition(oldPos.x, oldPos.y);
            }
        }

        if (stoore == null) {
            if (!canWalk(px, py)) {
                player.setPosition(oldPos.x, oldPos.y);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                toggleOverviewMode();
            }

//            if (!overviewMode) {
//                camera.position.set(player.getXX() + player.getWidth() / 2f,
//                    player.getYY() + player.getHeight() / 2f, 0);
//            }
            if (!overviewMode && !turnJustChanged) {
                camera.position.set(
                    player.getXX() + player.getWidth() / 2f,
                    player.getYY() + player.getHeight() / 2f,
                    0
                );
            }

        } else {
            if (stoore.isOpen(GameManager.getCurrentHour()))
                Main.getMain().setScreen(new StoreView(stoore, this));
            else {
                latestResult = Result.error(stoore.getStoreName() + " is closed right now");
                showResult = true;
            }
        }
        turnJustChanged = false;
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

            Player player = MyGame.getCurrentPlayer();
            camera.position.set(pos.x + player.getWidth() / 2f,
                pos.y + player.getHeight() / 2f, 0);
        }
    }

    private void drawEnergyBar() {
        float barWidth = 60;
        float barHeight = 300;
        float x = camera.position.x + camera.viewportWidth / 2 - barWidth - 10;
        float y = camera.position.y - camera.viewportHeight / 2 + 10;

        batch.draw(energyBarBg, x, y, barWidth, barHeight);
        Player player = MyGame.getCurrentPlayer();
        float fill = (barHeight - 70f) * (player.getEnergy() / 200f);
        batch.draw(energyBarFill, x + 10f, y + 5f, barWidth - 20f, fill);
    }

    private void drawHUD() {
        float baseX = camera.position.x + camera.viewportWidth / 2 - 300;
        float baseY = camera.position.y + camera.viewportHeight / 2 - 150;

        Player player = MyGame.getCurrentPlayer();
        int hour = GameManager.getCurrentHour();
        int minute = GameManager.getGameClock().getMinute();
        int day = GameManager.getDay();
        String dayOfWeek = GameManager.getDayOfTheWeek();

        Season currentSeason = GameManager.getSeason();
        Weather currentWeather = MyGame.currentWeather;

        TextureRegion clockFace = GameAssetManager.clockTexture;
        TextureRegion seasonIcon = GameAssetManager.getSeasonIcon(currentSeason);
        TextureRegion weatherIcon = GameAssetManager.getWeatherIcon(currentWeather);

        float seasonIconWidth = seasonIcon.getRegionWidth();
        float seasonIconHeight = seasonIcon.getRegionHeight();
        float weatherIconWidth = weatherIcon.getRegionWidth();
        float weatherIconHeight = weatherIcon.getRegionHeight();

        font.setColor(92 / 255f, 64 / 255f, 33 / 255f, 1f);
        batch.draw(clockFace, baseX, baseY - 100f, clockFace.getRegionWidth()*4, clockFace.getRegionHeight()*4);

        font.draw(batch,  String.valueOf(player.getGold()), baseX + 70, baseY - 60f);

        font.draw(batch, String.format("%02d:%02d", hour, minute), baseX + 140f, baseY + 20f);

        batch.draw(seasonIcon, baseX + 115f, baseY + 33f, seasonIconWidth*4, seasonIconHeight*4);
        batch.draw(weatherIcon, baseX + 205f, baseY + 33f, weatherIconWidth*4, weatherIconHeight*4);
        font.draw(batch, dayOfWeek + " ." + day, baseX + 110f, baseY + 115f);
        font.setColor(Color.BLACK);
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

    public void showToolSelection(SpriteBatch batch) {
        if (!isToolSelectionOpen) return;

        TextureRegion toolSelection = GameAssetManager.toolSelection;
        float scale = 0.5f;
        float scaledWidth = toolSelection.getRegionWidth() * scale;
        float scaledHeight = toolSelection.getRegionHeight() * scale;

        TOOL_X = camera.position.x - scaledWidth / 2f;
        TOOL_Y = camera.position.y - scaledHeight / 2f;

        float x = camera.position.x - camera.viewportWidth / 2f + (camera.viewportWidth - scaledWidth) / 2f;
        float y = camera.position.y - camera.viewportHeight / 2f + 20f;
        batch.begin();
        batch.draw(toolSelection, x, y, scaledWidth, scaledHeight);
        for (InventorySlot slot : toolSlots) {
            if (slot.item != null) {
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

        batch.end();

        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        InventorySlot hoveredSlot = null;

        for (InventorySlot slot : toolSlots) {
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

    public void showSkillSet(SpriteBatch batch) {
        if (!isSkillSetOpen) return;

        TextureRegion skillSet = GameAssetManager.skillSetPage;
        float scale = 0.5f;
        float textWidth = skillSet.getRegionWidth();
        float textHeight = skillSet.getRegionHeight();

        float scaledWidth = textWidth * scale;
        float scaledHeight = textHeight * scale;

        SKILL_X = camera.position.x - scaledWidth / 2f;
        SKILL_Y = camera.position.y - scaledHeight / 2f;

        initSkillHitboxes(SKILL_X, SKILL_Y);

        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        batch.begin();
        batch.draw(skillSet, SKILL_X, SKILL_Y, scaledWidth, scaledHeight);

        for (Map.Entry<SkillSetInfo, Rectangle> entry : skillHitboxes.entrySet()) {
            if (entry.getValue().contains(mouse.x, mouse.y)) {
                String name = entry.getKey().getSkillName();
                String description = entry.getKey().getSkillDescription();
                StringBuilder sb = new StringBuilder();
                sb.append(entry.getKey().getSkillName()).append("\n\n").
                    append(entry.getKey().getSkillDescription());

                GlyphLayout layout = new GlyphLayout(font, sb.toString());
                float padding = 100f;
                TextureRegion infoPage = GameAssetManager.infoPage;
                float bgWidth = layout.width + padding * 2;
                float bgHeight = layout.height + padding * 2 + 50f;
                float tooltipX = mouse.x + 15;
                float tooltipY = mouse.y - 40;

                batch.draw(infoPage, tooltipX, tooltipY, bgWidth, bgHeight);
                BitmapFont boldFont = GameAssetManager.getSkin().getFont("subtitle");
                boldFont.setColor(Color.WHITE);
                boldFont.draw(batch, name, tooltipX + padding, tooltipY + bgHeight - padding);
                font.draw(batch, description, tooltipX + padding, tooltipY + bgHeight - padding - 50f);
            }
        }

        batch.end();
    }

    public void showJournalPage(SpriteBatch batch) {
        if (!isJournalOpen) return;

        float scale = 0.5f;
        float drawWidth = journalBg.getRegionWidth() * scale;
        float drawHeight = journalBg.getRegionHeight() * scale;

        JOURNAL_X = camera.position.x - drawWidth / 2f;
        JOURNAL_Y = camera.position.y - drawHeight / 2f;

        batch.begin();
        batch.draw(journalBg, JOURNAL_X, JOURNAL_Y, drawWidth, drawHeight);

        Map<Mission, NPC> quests = new HashMap<>();
        Player player = MyGame.getCurrentPlayer();
        for (NpcActor npcActor : NPCs) {
            NPC npc = npcActor.getNpc();
            for (Mission mission : npc.getMissions()) {
                if (mission.hasClaimed(player)) {
                    quests.put(mission, npc);
                }
            }
        }

        float padding = 20f;
        float lineHeight = font.getLineHeight() + 6f;
        float entryHeight = lineHeight * 2 + padding;
        float textX = JOURNAL_X + 30f;
        float startY = JOURNAL_Y + drawHeight - 100f;

        int index = 0;
        for (Map.Entry<Mission, NPC> quest : quests.entrySet()) {
            float entryY = startY - index * entryHeight;

            font.draw(batch, "* " + quest.getKey().getTitle(), textX, entryY);
            font.draw(batch, quest.getValue().getName(), textX + 20f, entryY - lineHeight);

            index++;
        }

        batch.end();
    }

    public void showCookingPage(SpriteBatch batch) {
        if (!isCookingOpen) return;

        TextureRegion cookingPage = GameAssetManager.getCookingTexture();
        float scale = 0.5f;
        float textWidth = cookingPage.getRegionWidth();
        float textHeight = cookingPage.getRegionHeight();

        float scaledWidth = textWidth * scale;
        float scaledHeight = textHeight * scale;

        COOKING_X = camera.position.x - scaledWidth / 2f;
        COOKING_Y = camera.position.y - scaledHeight / 2f;

        batch.begin();
        batch.draw(cookingPage, COOKING_X, COOKING_Y, scaledWidth, scaledHeight);

        //show craft recipes
        ArrayList<CookingRecipeType> learnedRecipes = MyGame.getCurrentPlayer().getBackPack().getLearntCookingRecipe();

        int recipesPerRow = 15;
        int maxRows = 4;
        int recipesPerPage = recipesPerRow * maxRows;

        //pagination
        int currentPage = craftPageIndex; //do we want pagination?
        int startIndex = currentPage * recipesPerPage;
        int endIndex = Math.min(startIndex + recipesPerPage, learnedRecipes.size());

        float padding = 8f;
        float iconSize = 64f;
        float startX = COOKING_X + 50f;
        float startY = COOKING_Y + scaledHeight - iconSize;

        for (int i = startIndex; i < endIndex; i++) {
            CookingRecipeType recipe = learnedRecipes.get(i);
            boolean canCook = homeMenuController.cookingIngredientCheck(recipe);

            int pageIndex = i - startIndex;
            int row = pageIndex / recipesPerRow;
            int col = pageIndex % recipesPerRow;

            float x = startX + col * (iconSize + padding);
            float y = startY - row * (iconSize + padding);

            TextureRegion text = recipe.getTexture();
            float texWidth = text.getRegionWidth();
            float texHeight = text.getRegionHeight();

            float maxIconSize = 64f;
            float craftScale = Math.min(maxIconSize / texWidth, maxIconSize / texHeight);

            float drawWidth = texWidth * craftScale;
            float drawHeight = texHeight * craftScale;

            float drawX = x + (iconSize - drawWidth) / 2f - 20f;
            float drawY = y + (iconSize - drawHeight) / 2f - 50f;

            float alpha = canCook ? 1f : 0.4f;

            batch.setColor(1, 1, 1, alpha);
            batch.draw(text, drawX, drawY, drawWidth, drawHeight);
            batch.setColor(1, 1, 1, 1);

        }

        //show inventory items
        for (InventorySlot slot : slots) {
            if (slot.item != null) {
                TextureRegion texture = slot.item.getTexture();
                float texWidth = texture.getRegionWidth();
                float texHeight = texture.getRegionHeight();

                float scale1 = Math.min(SLOT_SIZE / texWidth, SLOT_SIZE / texHeight);
                float drawWidth = texWidth * scale1;
                float drawHeight = texHeight * scale1;

                float drawX = slot.x + (SLOT_SIZE - drawWidth) / 2f;
                float drawY = slot.y + (SLOT_SIZE - drawHeight) / 2f - 310f;

                batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
                if (slot.count > 1) font.draw(batch, String.valueOf(slot.count), drawX + drawWidth - 15f, drawY + 10f);
            }
        }

        if (hoveredCookingRecipeType != null) {
            //info background
            Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            TextureRegion infoBg = GameAssetManager.infoPage;

            float boxWidth = infoBg.getRegionWidth() * 13f;
            float boxHeight = infoBg.getRegionHeight() * 13f + hoveredCookingRecipeType.getIngredients().size() * 12f;

            float boxX = mouse.x + 30f;
            float boxY = mouse.y - 30f;

            batch.draw(infoBg, boxX, boxY, boxWidth, boxHeight);

            //cooking recipe info
            float textX = boxX + 45f;
            float textY = boxY + boxHeight - 50f;
            font.draw(batch, hoveredCookingRecipeType.getName(), textX, textY);

            Map<Item, Integer> ingredients = hoveredCookingRecipeType.getIngredients();

            float ingredientIconSize = 32f;
            float ingredientPadding = 10f;

            float iconX = boxX + 15f;
            float iconY = boxY + boxHeight - 50f;

            for (Map.Entry<Item, Integer> entry : ingredients.entrySet()) {
                Item item = entry.getKey();
                int count = entry.getValue();

                TextureRegion textureRegion = item.getTexture();
                float texWidth = textureRegion.getRegionWidth();
                float texHeight = textureRegion.getRegionHeight();

                float ingredientScale = Math.min(ingredientIconSize / texWidth, ingredientIconSize / texHeight);
                float drawWidth = texWidth * ingredientScale;
                float drawHeight = texHeight * ingredientScale;

                batch.draw(textureRegion, iconX + 10f, iconY - drawHeight - 50f, drawWidth, drawHeight);

                font.draw(batch, "x" + count + " " + item.getName(), iconX + drawWidth + 15f, iconY - 55f);

                iconY -= drawHeight + ingredientPadding;
            }

        }

        batch.end();

    }

    public void showCraftPage(SpriteBatch batch) {
        if (!isCraftOpen) return;

        TextureRegion craftPage = GameAssetManager.getCraftTexture();
        float scale = 0.5f;
        float textWidth = craftPage.getRegionWidth();
        float textHeight = craftPage.getRegionHeight();

        float scaledWidth = textWidth * scale;
        float scaledHeight = textHeight * scale;

        CRAFT_X = camera.position.x - scaledWidth / 2f;
        CRAFT_Y = camera.position.y - scaledHeight / 2f;

        batch.begin();
        batch.draw(craftPage, CRAFT_X, CRAFT_Y, scaledWidth, scaledHeight);

        //show craft recipes
        ArrayList<CraftType> learnedRecipes = MyGame.getCurrentPlayer().getBackPack().getLearntRecipes();

        int recipesPerRow = 12;
        int maxRows = 4;
        int recipesPerPage = recipesPerRow * maxRows;

        //pagination
        int currentPage = craftPageIndex;
        int startIndex = currentPage * recipesPerPage;
        int endIndex = Math.min(startIndex + recipesPerPage, learnedRecipes.size());

        float padding = 3f;
        float iconSize = 74f;
        float startX = CRAFT_X + 50f;
        float startY = CRAFT_Y + scaledHeight - iconSize - 40f;

        for (int i = startIndex; i < endIndex; i++) {
            CraftType craftType = learnedRecipes.get(i);
            boolean canCraft = homeMenuController.craftIngredientCheck(craftType);

            int pageIndex = i - startIndex;
            int row = pageIndex / recipesPerRow;
            int col = pageIndex % recipesPerRow;

            float x = startX + col * (iconSize + padding);
            float y = startY - row * (iconSize + padding);

            TextureRegion text = craftType.getTexture();
            float texWidth = text.getRegionWidth();
            float texHeight = text.getRegionHeight();

            float maxIconSize = 74f;
            float craftScale = Math.min(maxIconSize / texWidth, maxIconSize / texHeight);

            float drawWidth = texWidth * craftScale;
            float drawHeight = texHeight * craftScale;

            float drawX = x + (iconSize - drawWidth) / 2f - 20f;
            float drawY = y + (iconSize - drawHeight) / 2f - 50f;

            float alpha = canCraft ? 1f : 0.4f;

            batch.setColor(1, 1, 1, alpha);
            batch.draw(text, drawX, drawY, drawWidth, drawHeight);
            batch.setColor(1, 1, 1, 1);

        }

        //show inventory items
        for (InventorySlot slot : slots) {
            if (slot.item != null) {
                TextureRegion texture = slot.item.getTexture();
                float texWidth = texture.getRegionWidth();
                float texHeight = texture.getRegionHeight();

                float scale1 = Math.min(SLOT_SIZE / texWidth, SLOT_SIZE / texHeight);
                float drawWidth = texWidth * scale1;
                float drawHeight = texHeight * scale1;

                float drawX = slot.x + (SLOT_SIZE - drawWidth) / 2f;
                float drawY = slot.y + (SLOT_SIZE - drawHeight) / 2f - 310f;

                batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
                if (slot.count > 1) font.draw(batch, String.valueOf(slot.count), drawX + drawWidth - 15f, drawY + 10f);
            }
        }

        if (hoveredCraftType != null) {
            //info background
            Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            TextureRegion infoBg = GameAssetManager.infoPage;

            float boxWidth = infoBg.getRegionWidth() * 13f;
            float boxHeight = infoBg.getRegionHeight() * 13f + hoveredCraftType.getIngredients().size() * 12f;

            float boxX = mouse.x + 30f;
            float boxY = mouse.y - 30f;

            batch.draw(infoBg, boxX, boxY, boxWidth, boxHeight);

            //craft info
            float textX = boxX + 45f;
            float textY = boxY + boxHeight - 50f;
            font.draw(batch, hoveredCraftType.getName(), textX, textY);

            Map<Item, Integer> ingredients = hoveredCraftType.getIngredients();

            float ingredientIconSize = 32f;
            float ingredientPadding = 10f;

            float iconX = boxX + 15f;
            float iconY = boxY + boxHeight - 50f;

            for (Map.Entry<Item, Integer> entry : ingredients.entrySet()) {
                Item item = entry.getKey();
                int count = entry.getValue();

                TextureRegion textureRegion = item.getTexture();
                float texWidth = textureRegion.getRegionWidth();
                float texHeight = textureRegion.getRegionHeight();

                float ingredientScale = Math.min(ingredientIconSize / texWidth, ingredientIconSize / texHeight);
                float drawWidth = texWidth * ingredientScale;
                float drawHeight = texHeight * ingredientScale;

                batch.draw(textureRegion, iconX + 10f, iconY - drawHeight - 50f, drawWidth, drawHeight);

                font.draw(batch, "x" + count + " " + item.getName(), iconX + drawWidth + 15f, iconY - 55f);

                iconY -= drawHeight + ingredientPadding;
            }

        }

        batch.end();

    }

    private void initSkillHitboxes(float skillX, float skillY) {
        skillHitboxes.clear();
        float width = 120;
        float height = 30;

        skillHitboxes.put(SkillSetInfo.Farming, new Rectangle(skillX + 200, skillY + 500, width, height));
        skillHitboxes.put(SkillSetInfo.Mining, new Rectangle(skillX + 200, skillY + 450, width, height));
        skillHitboxes.put(SkillSetInfo.Foraging, new Rectangle(skillX + 200, skillY + 400, width, height));
        skillHitboxes.put(SkillSetInfo.Fishing, new Rectangle(skillX + 200, skillY + 350, width, height));
        skillHitboxes.put(SkillSetInfo.Combat, new Rectangle(skillX + 200, skillY + 300, width, height));

    }

    public void showCheatCodeWindow(SpriteBatch spriteBatch) {

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
                if (slot.count > 1) font.draw(batch, String.valueOf(slot.count), drawX + drawWidth - 15f, drawY + 10f);
            }
            if (selectedSlots.contains(slot)) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.YELLOW);
                shapeRenderer.rect(slot.x, slot.y, SLOT_SIZE, SLOT_SIZE);
                shapeRenderer.end();
            }
        }

        if (!giftMode && draggedItem != null) {
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

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        viewport = new FitViewport(320, 180, camera);
        viewport.apply();

        uiStage = new Stage(new ScreenViewport(), batch);
        stage = new Stage(viewport, batch);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InventoryInputHandler(this));
        multiplexer.addProcessor(uiStage);
        if (isInBuildMode || artisanMode) multiplexer.addProcessor(0, buildInputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        System.out.println("InputMultiplexer set with cheatCodeWindow");

        buildInputProcessor = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (isInBuildMode && button == Input.Buttons.LEFT) {
                    Vector3 worldPos = camera.unproject(new Vector3(screenX, screenY, 0));
                    placeBuilding(worldPos.x, worldPos.y, lastType, lastLevel);
                    isInBuildMode = false;
                    multiplexer.removeProcessor(buildInputProcessor);
                } else if (artisanMode && button == Input.Buttons.LEFT) {
                    Vector3 worldPos = camera.unproject(new Vector3(screenX, screenY, 0));
                    Player player = MyGame.getCurrentPlayer();
                    addArtisanMachine(new ArtisanMachine(lastArtisanType, player, worldPos.x, worldPos.y));
                    artisanMode = false;
                    multiplexer.removeProcessor(buildInputProcessor);
                }
                return true;
            }
        };


        missionListTable = new Table();
        missionListTable.setVisible(false);
        missionListTable.setFillParent(true);
        uiStage.addActor(missionListTable);
        animalMenuTable = new Table();
        animalMenuTable.setVisible(false);
        animalMenuTable.setFillParent(true);
        uiStage.addActor(animalMenuTable);
        notificationTable = new Table();
        notificationTable.setVisible(false);
        notificationTable.setFillParent(true);
        uiStage.addActor(notificationTable);
        giftMenuTable = new Table();
        giftMenuTable.setVisible(false);
        giftMenuTable.setFillParent(true);
        uiStage.addActor(giftMenuTable);
        giftHistoryTable = new Table();
        giftHistoryTable.setVisible(true);
        giftHistoryTable.setFillParent(true);
        uiStage.addActor(giftHistoryTable);
        rateTable = new Table();
        rateTable.setVisible(false);
        rateTable.setFillParent(true);
        uiStage.addActor(rateTable);
        friendshipMenuTable = new Table();
        friendshipMenuTable.setVisible(false);
        friendshipMenuTable.setFillParent(true);
        uiStage.addActor(friendshipMenuTable);
        playerMenuTable = new Table();
        playerMenuTable.setVisible(false);
        playerMenuTable.setFillParent(true);
        uiStage.addActor(playerMenuTable);
        artisanMenuTable = new Table();
        artisanMenuTable.setVisible(false);
        artisanMenuTable.setFillParent(true);
        uiStage.addActor(artisanMenuTable);
        npcMenuTable = new Table();
        npcMenuTable.setVisible(false);
        npcMenuTable.setFillParent(true);
        uiStage.addActor(npcMenuTable);
        dialogueTable = new Table();
        dialogueTable.setFillParent(true);
        uiStage.addActor(dialogueTable);

        chatTable = new Table();
        chatTable.setVisible(false);
        chatTable.setFillParent(true);
        uiStage.addActor(chatTable);

        Table innerPanel = new Table(skin);
        Texture chatBgTex = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");
        Drawable chatBg = new TextureRegionDrawable(new TextureRegion(chatBgTex));
        innerPanel.setBackground(chatBg);
        innerPanel.pad(30);

        chatMessagesGroup = new VerticalGroup();
        chatMessagesGroup.top().left().columnAlign(Align.left).space(10);
        chatMessagesGroup.setFillParent(true);
        chatMessagesGroup.wrap(true);

        chatScrollPane = new ScrollPane(chatMessagesGroup, skin);
        chatScrollPane.setFadeScrollBars(false);
        chatScrollPane.setScrollingDisabled(true, false);

        chatInputField = new TextField("", skin);
        chatInputField.setMessageText("Type your message...");
        TextButton sendButton = new TextButton("Send", skin);
        TextButton backButton = new TextButton("Back", skin);
        TextButton privateChatButton = new TextButton("Private", skin);

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String content = chatInputField.getText().trim();
                if (!content.isEmpty()) {
                    ChatMessage msg = new ChatMessage(MyGame.getCurrentPlayer().getUsername(), content);
                    GameClient.client.sendTCP(msg);
                    chatInputField.setText("");
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chatTable.setVisible(false);
                isChatOpen = false;
            }
        });

        privateChatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPrivateChatPopup(); // تابعی که پایین تعریف می‌کنیم
            }
        });
        chatTable.add(privateChatButton).pad(10);

        innerPanel.add(chatScrollPane).height(400).width(600).colspan(2).padBottom(20).row();
        innerPanel.add(chatInputField).width(400).padRight(10);
        innerPanel.add(sendButton).width(100).row();
        innerPanel.add(backButton).colspan(2).padTop(20);

        chatTable.add(innerPanel).center();


        for (NpcActor npc : NPCs) {
            stage.addActor(npc);
            npc.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.RIGHT) {
                        showNpcMenu(npc);
                        return true;
                    }
                    else if (button == Input.Buttons.LEFT) {
                        if (npc.isDialogueReady()) {
                            showNpcDialogue(npc);
                            System.out.println("clicked");
                            return true;
                        }
                    }
                    return false;
                }
            });
            npc.toBack();
        }
        for (Store store : MyGame.getDatabase().getStores()) {
            stage.addActor(store);
        }
        for (Player player : players) {
            stage.addActor(player.getFarm().getShippingBin());
        }

        Texture mail = GameAssetManager.getInstance().getOrLoadTexture("ui/mailSign.png");
        Drawable mailDrawable = new TextureRegionDrawable(new TextureRegion(mail));
        notificationButton = new ImageButton(mailDrawable);
        float padding = 70;
        float screenHeight = uiStage.getViewport().getScreenHeight();
        float screenWidth = uiStage.getViewport().getScreenWidth();

        float x = padding - 10f;
        float y = screenHeight - notificationButton.getHeight() - padding;


        notificationButton.setPosition(x, y);
        notificationButton.getImageCell().size(72, 56);
        notificationButton.setSize(72, 56);
        notificationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNotifications();
            }
        });
        uiStage.addActor(notificationButton);
        Texture heart = GameAssetManager.getInstance().getOrLoadTexture("ui/heart.png");
        Drawable heartDrawable = new TextureRegionDrawable(new TextureRegion(heart));
        friendshipButton = new ImageButton(heartDrawable);
        friendshipButton.setPosition(x, y - 80);
        friendshipButton.getImageCell().size(70, 60);
        friendshipButton.setSize(70, 60);
        friendshipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendshipMenu();
            }
        });
        uiStage.addActor(friendshipButton);

        Texture chatTex = GameAssetManager.getInstance().getOrLoadTexture("ui/chat.png");
        Drawable chatDrawable = new TextureRegionDrawable(new TextureRegion(chatTex));
        ImageButton chatButton = new ImageButton(chatDrawable);
        chatButton.setPosition(x, y - 250f);
        chatButton.getImageCell().size(72, 56);
        chatButton.setSize(72, 56);

        chatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chatTable.setVisible(true);
                isChatOpen = true;
            }
        });

        uiStage.addActor(chatButton);


        Texture tradingTexture = GameAssetManager.tradingButton;
        Drawable tradingDrawable = new TextureRegionDrawable(new TextureRegion(tradingTexture));
        tradingButton = new ImageButton(tradingDrawable);
        tradingButton.setPosition(x - 10f, y - 190f);
        tradingButton.getImageCell().size(100, 90);
        tradingButton.setSize(100, 90);
        tradingButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!tradeMenuOpen) {
                    tradeMenu = new TradeMenu(new TradingController(), new MenuController(), skin);
                    tradeMenu.pack();
                    tradeMenu.setPosition(camera.position.x - tradeMenu.getWidth() / 2,
                        camera.position.y - tradeMenu.getHeight() / 2);
//                    tradeMenu.setPosition();
                    stage.addActor(tradeMenu);
                    tradeMenuOpen = true;

                    // window removal
                    tradeMenu.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            tradeMenuOpen = false;
                        }
                    });
                }
            }
        });

        Texture rejectTex = new Texture("closeButton.png");
        Texture acceptTex = new Texture("ui/checkMark.png");
        Drawable rejectDrawable = new TextureRegionDrawable(new TextureRegion(rejectTex));
        Drawable acceptDrawable = new TextureRegionDrawable(new TextureRegion(acceptTex));
        accept = new ImageButton(acceptDrawable);
        accept.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                acceptedRequest = true;
            }
        });
        reject = new ImageButton(rejectDrawable);
        reject.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                acceptedRequest = false;
            }
        });

        uiStage.addActor(tradingButton);

        forceViewportReset();
//        Player player = MyGame.getCurrentPlayer();
//        AnimalHouse house  = new AnimalHouse(EnclosureType.COOP, AnimalHouseLevel.Big, 500, 500, GameAssetManager.getInstance().getItemTexture("Coop"));
//        player.addAnimalHouse(house);
//        Animal animal = new Animal("parastoo", AnimalType.CHICKEN, player);
//        house.addAnimal(animal);
//        addAnimalActor(new AnimalActor(animal));

    }

    public void receivePrivateMessage(String sender, String message) {
        Label msgLabel = new Label(sender + ": " + message, skin);
        msgLabel.setColor(Color.GOLD);
        msgLabel.setAlignment(Align.left);
        chatMessagesGroup.addActor(msgLabel);
    }


    public void receiveChatMessage(String sender, String message) {
        // چک کردن تگ شدن
        String currentUsername = MyGame.getCurrentPlayer().getUsername();
        boolean isTagged = message.contains("@" + currentUsername);

        // نمایش pop-up اگر تگ شد
        if (isTagged) {
            showTagPopup(sender, message);
        }

        // ساخت پیام چت با رنگ برای @
        Label.LabelStyle style = skin.get(Label.LabelStyle.class);
        Label msgLabel = new Label(formatTaggedMessage(sender, message, currentUsername), style);
        msgLabel.setColor(Color.BLACK);
        msgLabel.setAlignment(Align.left);
        chatMessagesGroup.addActor(msgLabel);
        chatScrollPane.layout();
        chatScrollPane.setScrollPercentY(100);
    }

    private String formatTaggedMessage(String sender, String message, String currentUsername) {
        String taggedUser = "@" + currentUsername;

        if (message.contains(taggedUser)) {
            message = message.replace(taggedUser,  "**" + taggedUser + "**");
        }

        return sender + ": " + message;
    }

    private void showTagPopup(String sender, String message) {
        Dialog tagDialog = new Dialog("You've been tagged!", skin);
        Label label = new Label(sender + ": " + message, skin);
        label.setWrap(true);
        tagDialog.getContentTable().add(label).width(300).pad(10);
        tagDialog.button("Close", true);
        tagDialog.show(uiStage);
    }

//    public void showPrivateChatPopup(String sender, String content) {
//        Dialog privateDialog = new Dialog("Private Chat with " + sender, skin);
//
//        Label message = new Label(sender + ": " + content, skin);
//        message.setWrap(true);
//
//        TextField replyField = new TextField("", skin);
//        TextButton sendButton = new TextButton("Send", skin);
//        TextButton finishButton = new TextButton("Finish", skin);
//
//        Table body = privateDialog.getContentTable();
//        body.add(message).width(300).padBottom(10).row();
//        body.add(replyField).width(300).padBottom(10).row();
//        body.add(sendButton).padRight(10);
//        body.add(finishButton).row();
//
//        sendButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                String reply = replyField.getText();
//                if (!reply.isEmpty()) {
//                    PrivateChatMessage replyMsg = new PrivateChatMessage(
//                        MyGame.getCurrentPlayer().getUsername(),
//                        sender,
//                        reply
//                    );
//                    GameClient.client.sendTCP(replyMsg);
//                    replyField.setText("");
//                }
//            }
//        });
//
//        finishButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                privateDialog.hide();
//            }
//        });
//
//        privateDialog.show(uiStage);
//    }


//    private void showPrivateChatPopup() {
//        final TextField usernameField = new TextField("", skin);
//
//        Dialog dialog = new Dialog("Start Private Chat", skin) {
//            @Override
//            protected void result(Object obj) {
//                if ((boolean) obj) {
//                    String targetUsername = usernameField.getText().trim();
//                    if (!targetUsername.isEmpty() &&
//                        !targetUsername.equals(MyGame.getCurrentPlayer().getUsername())) {
//                        openPrivateChat(targetUsername);
//                    }
//                }
//            }
//        };
//
//        usernameField.setMessageText("Enter username...");
//        dialog.getContentTable().add(usernameField).width(300).pad(10).row();
//
//        dialog.button("Start", true);
//        dialog.button("Cancel", false);
//
//        dialog.show(uiStage);
//    }

    private void showPrivateChatPopup() {
        Dialog dialog = new Dialog("Private Message", skin);
        dialog.pad(20);

        TextField recipientField = new TextField("", skin);
        recipientField.setMessageText("Enter recipient username");

        TextField messageField = new TextField("", skin);
        messageField.setMessageText("Type your message here");

        TextButton sendBtn = new TextButton("Send", skin);
        TextButton cancelBtn = new TextButton("Cancel", skin);

        sendBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String recipient = recipientField.getText().trim();
                String content = messageField.getText().trim();
                String sender = MyGame.getCurrentPlayer().getUsername();

                if (!recipient.isEmpty() && !content.isEmpty()) {
                    ChatMessage msg = new ChatMessage(sender, recipient, content);
                    GameClient.client.sendTCP(msg);

                    // نمایش پیام محلی هم می‌تونه اضافه بشه:
                    receivePrivateMessage("To " + recipient, content);
                }

                dialog.hide();
            }
        });

        cancelBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        Table table = new Table();
        table.add(new Label("To:", skin)).padRight(10);
        table.add(recipientField).width(300).row();
        table.add(new Label("Message:", skin)).padTop(10).padRight(10);
        table.add(messageField).width(300).row();
        table.add(sendBtn).padTop(20);
        table.add(cancelBtn).padTop(20);

        dialog.getContentTable().add(table);
        dialog.button("Close", false);
        dialog.show(uiStage);
    }


    private void openPrivateChat(String targetUsername) {
        currentPrivateTarget = targetUsername;

        chatTable.setVisible(false);

        privateChatTable = new Table();
        privateChatTable.setFillParent(true);
        privateChatTable.top().left().pad(20);

        Label title = new Label("Private chat with @" + targetUsername, skin);
        privateChatTable.add(title).colspan(2).padBottom(10).row();

        ScrollPane privateScroll = new ScrollPane(chatMessagesGroup = new VerticalGroup(), skin);
        privateScroll.setFadeScrollBars(false);
        privateChatTable.add(privateScroll).colspan(2).width(800).height(400).row();

        TextField messageField = new TextField("", skin);
        privateChatTable.add(messageField).width(600).pad(10);

        TextButton sendBtn = new TextButton("Send", skin);
        sendBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String msg = messageField.getText().trim();
                if (!msg.isEmpty()) {
                    ChatMessage chat = new ChatMessage(MyGame.getCurrentPlayer().getUsername(), targetUsername, msg);
                    GameClient.client.sendTCP(chat);


                    Main.getMain().gameClient.sendChat(chat);
                    messageField.setText("");
                    receivePrivateMessage(chat.sender, chat.content);
                }
            }
        });

        privateChatTable.add(sendBtn).width(100).pad(10);

        TextButton finishBtn = new TextButton("Finish", skin);
        finishBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                uiStage.getActors().removeValue(privateChatTable, true);
                chatTable.setVisible(true);
                currentPrivateTarget = null;
            }
        });
        privateChatTable.add(finishBtn).colspan(2).padTop(20).row();

        uiStage.addActor(privateChatTable);
    }



    private void forceViewportReset() {
        toggleOverviewMode();
        toggleOverviewMode();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapRenderer.dispose();

        for (Player player : players) {
            player.dispose();
        }
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

        int tileX = (int) (x / TILE_SIZE);
        int tileY = (int) (y / TILE_SIZE);

        if (tileX >= 0 && tileX < MAP_WIDTH &&
            tileY >= 0 && tileY < MAP_HEIGHT) {

            GameTile tile = GameMap.getTile(tileX, tileY);
            return tile == null || tile.getTileType() != TileType.Water; // can't walk on water
        }

        return true;
    }

    private void showNpcDialogue(NpcActor npcActor) {
        Texture dialogueBoxTexture = GameAssetManager.getInstance().getOrLoadTexture("NPCs/dialogueTemplate.png");
        dialogueBoxTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch ninePatch = new NinePatch(dialogueBoxTexture, 16, 16, 16, 16);
        NinePatchDrawable dialogueBackground = new NinePatchDrawable(ninePatch);

        Skin skin = GameAssetManager.getSkin();
        String message = npcActor.getMessage();
        if (message == null) return;
        dialogueTable.clear();
        dialogueTable.setVisible(true);
        dialogueTable.pad(20);
        dialogueTable.setBackground(dialogueBackground);
        Label dialogueText = new Label(message, skin);
        dialogueText.setWrap(true);
        dialogueText.setFontScale(2f);
        dialogueText.setColor(86f / 225f, 22f / 225f, 12f / 225f, 1);
        dialogueText.setWidth(8000);

        String NpcName = npcActor.getNpc().getName();
        Texture tex = GameAssetManager.getInstance().getOrLoadTexture("NPCs/" + NpcName + "/avatar.png");
        TextureRegionDrawable avatarDrawable = new TextureRegionDrawable(new TextureRegion(tex));
        Image npcAvatar = new Image(avatarDrawable);


        Label npcNameLabel = new Label(NpcName, skin);
        npcNameLabel.setFontScale(2f);
        npcNameLabel.setColor(86f / 225f, 22f / 225f, 12f / 225f, 1);
        VerticalGroup rightGroup = new VerticalGroup();
        rightGroup.space(100);
        npcAvatar.setScaling(Scaling.stretch);

        Container<Image> avatarContainer = new Container<>(npcAvatar);
        avatarContainer.size(400, 400);
        avatarContainer.fill();

        rightGroup.addActor(avatarContainer);
        rightGroup.addActor(npcNameLabel);
        rightGroup.center();


        dialogueTable.add(dialogueText).expand().left().width(1500).padLeft(200);
        dialogueTable.add(rightGroup).padTop(650).padRight(100).width(1000).height(1000);
    }
    private void showNotifications() {
        notificationTable.clear();
        notificationTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground1.png");
        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);
        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));
        String notifications = MyGame.getCurrentPlayer().getNotifications();
        Label notificationsLabel = new Label(notifications, skin);
        notificationsLabel.setWrap(true);
        notificationsLabel.setAlignment(Align.topLeft);
        innerPanel.add(notificationsLabel).width(300).pad(10).left().top();
        notificationsLabel.setColor(86f / 225f, 22f / 225f, 12f / 225f, 1);
        innerPanel.row();
        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                notificationTable.setVisible(false);
            }
        });
        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        notificationTable.add(innerPanel).center();
    }

    private void showFriendshipMenu() {
        friendshipMenuTable.clear();
        friendshipMenuTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground1.png");
        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);
        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        for (Player player : players) {
            if (!player.equals(MyGame.getCurrentPlayer())) {
                innerPanel.add(buildRow(player)).padBottom(50).row();
            }
        }

        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                friendshipMenuTable.setVisible(false);
            }
        });

        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        friendshipMenuTable.add(innerPanel).center();
    }

    private Table buildRow(Player player) {
        Table row = new Table();
        Label name = new Label(player.getName(), skin);
        name.setColor(86f / 255, 22f / 255, 12f / 255, 1);
        name.setFontScale(1.5f);
        row.add(name).padRight(10);
        Texture giftTexture = GameAssetManager.getInstance().getOrLoadTexture("ui/gift.png");
        Drawable giftDrawable = new TextureRegionDrawable(new TextureRegion(giftTexture));
        ImageButton giftButton = new ImageButton(giftDrawable);
        giftButton.getImageCell().size(32, 32);

        giftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftMode = true;
                lastPlayer = player;
                lastNPC = null;
                isInvenotryOpen = true;
                friendshipMenuTable.setVisible(false);
            }
        });
        row.add(giftButton).size(32, 32).padRight(10);
        Texture fullHeartTexture = GameAssetManager.getInstance().getOrLoadTexture("ui/heart.png");
        Texture greyHeartTexture = GameAssetManager.getInstance().getOrLoadTexture("ui/greyHeart.png");
        int friendshipLevel = MyGame.getCurrentPlayer().getFriendshipLevel(player);
        for (int i = 0; i < friendshipLevel; i++) {
            Image heart = new Image(fullHeartTexture);
            heart.setSize(30, 30);
            row.add(heart).size(30).pad(10);
        }
        for (int i = friendshipLevel; i < 4; i++) {
            Image heart = new Image(greyHeartTexture);
            heart.setSize(30, 30);
            row.add(heart).size(30).pad(10);
        }
        return row;
    }

    private void showGiftHistory(Player player) {
        giftHistoryTable.clear();
        giftHistoryTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground1.png");
        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);
        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        Label receivedGifts = new Label("received gifts", skin);
        receivedGifts.setFontScale(1.5f);
        receivedGifts.setColor(86f / 225f, 22f / 225f, 12f / 225f, 1);
        innerPanel.add(receivedGifts).padBottom(30).row();
        for (Gift gift : controller.getReceivedGifts(player)) {
            innerPanel.add(buildRow(gift, true)).padBottom(50).row();
        }
        Label sentGifts = new Label("sent gifts", skin);
        sentGifts.setFontScale(1.5f);
        sentGifts.setColor(86f / 225f, 22f / 225f, 12f / 225f, 1);
        innerPanel.add(sentGifts).padBottom(30).row();
        for (Gift gift : controller.getSentGifts(player)) {
            innerPanel.add(buildRow(gift, false)).padBottom(50).row();
        }
        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftHistoryTable.setVisible(false);
            }
        });

        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        giftHistoryTable.add(innerPanel).center();
    }

    private Table buildRow(Gift gift, boolean ratable) {
        Item item = gift.getItem();
        Table row = new Table();
        Image itemIcon = new Image(item.getTexture());
        Label nameLabel = new Label(item.getName(), skin);
        itemIcon.setSize(itemIcon.getWidth(), itemIcon.getHeight());
        nameLabel.setFontScale(1.5f);
        nameLabel.setColor(86f / 255, 22f / 255, 12f / 255, 1);
        row.add(itemIcon).size(itemIcon.getWidth() * 1.5f, itemIcon.getHeight() * 1.5f).padRight(10);
        row.add(nameLabel).padRight(10);
        Texture starTexture = GameAssetManager.getInstance().getOrLoadTexture("ui/rateSign.png");
        if (!gift.hasBeenRated() && ratable) {
            ImageButton starButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(starTexture)));
            starButton.getImageCell().size(30, 30);
            starButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    giftHistoryTable.setVisible(false);
                    showRateTable(gift);
                }
            });
            row.add(starButton).size(30);
        } else {
            starTexture = GameAssetManager.getInstance().getOrLoadTexture("ui/purpleStar.png");
            for (int i = 0; i < gift.getRating(); i++) {
                Image star = new Image(starTexture);
                star.setSize(30, 30);
                row.add(star).size(30).pad(10);
            }
        }
        return row;
    }

    private void showRateTable(Gift gift) {
        rateTable.clear();
        rateTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");
        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);

        TextField enterRating = new TextField("", skin);
        enterRating.setWidth(500);
        enterRating.setMessageText("enter your rating (1-5)");
        TextField.TextFieldStyle style = enterRating.getStyle();
        style.fontColor = Color.WHITE;
        style.disabledFontColor = Color.GRAY;
        enterRating.setStyle(style);
        style.background = skin.newDrawable("white", new Color(0.1f, 0.1f, 0.2f, 1f));
        innerPanel.add(enterRating).width(500).center().row();

        Label errorLabel = new Label("", skin);
        errorLabel.setColor(Color.PINK);
        Texture checkTexture = new Texture(Gdx.files.internal("ui/checkMark.png"));
        Drawable checkDrawable = new TextureRegionDrawable(new TextureRegion(checkTexture));
        ImageButton done = new ImageButton(checkDrawable);
        innerPanel.add(done).size(48, 48).padTop(20).colspan(2).pad(20);
        done.getImageCell().size(48, 48);
        innerPanel.add(errorLabel).pad(20);
        done.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int rating;
                try {
                    rating = Integer.parseInt(enterRating.getText());
                    Result result = controller.rateTheGift(gift, rating);
                    if (!result.isSuccess()) {
                        errorLabel.setText(result.getMessage());
                    } else {
                        errorLabel.setColor(Color.GREEN);
                        errorLabel.setText(result.getMessage());
                    }
                } catch (Exception e) {
                    errorLabel.setText("invalid rating!");
                }

            }
        });
        rateTable.add(innerPanel).center();
        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));
        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rateTable.setVisible(false);
                showGiftHistory(gift.getSender());
            }
        });

        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).pad(20);
        closeButton.getImageCell().size(48, 48);
        rateTable.add(innerPanel).left();

    }

    public void startFoodBuff(){
        buffStarted = true;
    }

    public void showFoodBuff(SpriteBatch batch ,float delta) {
       if(!buffStarted) return;

       buffTime += delta;
       if(buffTime >= buffDuration) {
           buffTime = 0f;
           buffStarted = false;
       }

       TextureRegion buff = CookingRecipeType.getBuffTexture();
       float x = camera.position.x - 400f;
       float y = camera.position.y + 400f;

       batch.begin();
       batch.draw(buff, x, y);
       font.setColor(Color.WHITE);
       font.draw(batch,"food buff",x,y - 10f);
       font.setColor(Color.BLACK);
       batch.end();
    }
    private void showPlayerMenu(Player player) {
        Player currentPlayer = MyGame.getCurrentPlayer();
        if (currentPlayer.equals(player)) return;
        playerMenuTable.clear();
        playerMenuTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");

        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);

        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        TextButton talkButton = new TextButton("talk to " + player.getUsername(), skin);
        innerPanel.add(talkButton).fillX();
        innerPanel.row();
        talkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Window window = new Window("Enter Message", skin);
                TextField messageField = new TextField("", skin);
                TextButton submitButton = new TextButton("Submit", skin);
                messageMode = true;
                window.add(messageField).width(200).pad(10);
                window.row();
                window.add(submitButton).pad(10);
                window.pack();
                window.setPosition(
                    (Gdx.graphics.getWidth() - window.getWidth()) / 2f,
                    (Gdx.graphics.getHeight() - window.getHeight()) / 2f
                );

                uiStage.addActor(window);

                submitButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        String message = messageField.getText();
                        latestResult = controller.talkToPlayer(player, message);
                        if (!latestResult.isSuccess()) showResult = true;
                        playerMenuTable.setVisible(false);
                        messageMode = false;
                        window.remove();
                    }
                });
            }
        });


        TextButton giveBouquet = new TextButton("give a bouquet", skin);
        innerPanel.add(giveBouquet).fillX();
        innerPanel.row();
        giveBouquet.setDisabled(!MyGame.getCurrentPlayer().canGiveBouquet(player));

        if (!MyGame.getCurrentPlayer().canGiveBouquet(player)) {
            giveBouquet.setTouchable(Touchable.disabled);
            giveBouquet.setColor(Color.DARK_GRAY);
        } else giveBouquet.setTouchable(Touchable.enabled);
        ImageButton closeButton = new ImageButton(closeDrawable);
        giveBouquet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                latestResult = controller.giveBouquet(player);
                showResult = true;
                playerMenuTable.setVisible(false);
            }
        });
        TextButton hugButton = new TextButton("hug " + player.getName(), skin);
        innerPanel.add(hugButton).fillX();
        innerPanel.row();
        //hugButton.setDisabled(!MyGame.getCurrentPlayer().canHug(player));
        if (hugButton.isDisabled()) {
            hugButton.setTouchable(Touchable.disabled);
            hugButton.setColor(Color.DARK_GRAY);
        } else hugButton.setTouchable(Touchable.enabled);
        hugButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playersAreClose(currentPlayer, player)) {
                    hugMode = true;
                    playerA = currentPlayer;
                    playerB = player;
                    hugTimer = 0f;
                    faceEachOther(currentPlayer, player);
                    moveToCenter(currentPlayer, player);
                }
                playerMenuTable.setVisible(false);
            }
        });
        TextButton proposeMarriage = new TextButton("propose marriage", skin);
        innerPanel.add(proposeMarriage).fillX();
        innerPanel.row();
        proposeMarriage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                latestResult = controller.askMarriage(player);
                showResult = true;
                playerMenuTable.setVisible(false);
            }
        });
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerMenuTable.setVisible(false);
            }
        });
        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        playerMenuTable.add(innerPanel).center();
    }

    private boolean playersAreClose(Player player1, Player player2) {
        Vector2 pos1 = new Vector2(player1.getXX(), player1.getYY());
        Vector2 pos2 = new Vector2(player2.getXX(), player2.getYY());
        float distance = pos1.dst(pos2);
        return distance < 100f;
    }

    private void faceEachOther(Player a, Player b) {
        if (a.getXX() < b.getXX()) {
            a.setFacingRight(true);
            b.setFacingRight(false);
        } else {
            a.setFacingRight(false);
            b.setFacingRight(true);
        }
    }

    private void moveToCenter(Player a, Player b) {
        float centerX = (a.getXX() + b.getXX()) / 2f;
        a.setX(centerX - 25);
        b.setX(centerX + 25);
    }

    private void showGiftMenu(Player player) {
        giftMenuTable.clear();
        giftMenuTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");
        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);

        TextButton giftButton = new TextButton("gift " + player.getName(), skin);
        innerPanel.add(giftButton).fillX();
        innerPanel.row();
        giftButton.setColor(1, 210f / 255, 132f / 255, 1);
        giftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftMode = true;
                isInvenotryOpen = true;
                lastNPC = null;
                lastPlayer = player;
            }
        });

        TextButton giftHistory = new TextButton("see gift history", skin);
        innerPanel.add(giftHistory).fillX();
        innerPanel.row();
        giftHistory.setColor(1, 210f / 255, 132f / 255, 1);
        giftHistory.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftMenuTable.setVisible(false);
                showGiftHistory(player);
            }
        });


        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));
        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuTable.setVisible(false);
            }
        });

        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        giftMenuTable.add(innerPanel).center();
    }

    private void showArtisanMenu(ArtisanMachine machine) {
        artisanMenuTable.clear();
        artisanMenuTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");

        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);

        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        TextButton finishNowButton = new TextButton("Finish Now", skin);
        innerPanel.add(finishNowButton).fillX();
        innerPanel.row();
        finishNowButton.setColor(1, 210f / 255, 132f / 255, 1);
        finishNowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machine.finish();
                latestResult = Result.success("you can now collect the artisan product!");
                showResult = true;
                artisanMenuTable.setVisible(false);
            }
        });

        TextButton cancelButton = new TextButton("cancel", skin);
        innerPanel.add(cancelButton).fillX();
        innerPanel.row();
        cancelButton.setColor(1, 210f / 255, 132f / 255, 1);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (machine.isWorking() || machine.isReady()) {
                    machine.reset();
                    latestResult = Result.success("canceled successfully");
                    showResult = true;
                }
                artisanMenuTable.setVisible(false);
                latestResult = Result.error("nothing to cancel");
                showResult = true;
            }
        });
        TextButton collectProductButton = new TextButton("collect product", skin);
        if (machine.isReady()) innerPanel.add(collectProductButton).fillX();
        innerPanel.row();
        collectProductButton.setColor(1, 210f / 255, 132f / 255, 1);
        collectProductButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ArtisanProduct product = machine.getProduct();
                Player player = MyGame.getCurrentPlayer();
                player.getBackPack().addToInventory(product, 1);
                latestResult  = Result.success("artisan product added to your inventory!");
                showResult = true;
                artisanMenuTable.setVisible(false);
            }
        });
        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                artisanMenuTable.setVisible(false);
            }
        });

        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        artisanMenuTable.add(innerPanel).center();
    }

    private void showAnimalMenu(AnimalActor animalActor) {
        Animal animal = animalActor.getAnimal();
        animalMenuTable.clear();
        animalMenuTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");

        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);

        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        String name = animal.getName();
        TextButton feedButton = new TextButton("feed " + name, skin);
        innerPanel.add(feedButton).fillX();
        innerPanel.row();
        feedButton.setColor(1, 210f / 255, 132f / 255, 1);
        feedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                latestResult = controller.feedHay(animalActor.getAnimal());
                feedAnimal(animalActor);
                animalMenuTable.setVisible(false);
            }
        });
        TextButton petButton = new TextButton("pet " + name, skin);
        innerPanel.add(petButton).fillX();
        innerPanel.row();
        petButton.setColor(1, 210f / 255, 132f / 255, 1);
        TextButton shepherdAnimal = new TextButton("shepherd " + name, skin);
        innerPanel.add(shepherdAnimal).fillX();
        innerPanel.row();
        shepherdAnimal.setColor(1, 210f / 255, 132f / 255, 1);
        TextButton collectProduceButton = new TextButton("collect produce", skin);
        innerPanel.add(collectProduceButton).fillX();
        innerPanel.row();
        collectProduceButton.setColor(1, 210f / 255, 132f / 255, 1);
        collectProduceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                latestResult = controller.collectProduce(animal);
                showResult = true;
                animalMenuTable.setVisible(false);
            }
        });
        TextButton sellAnimal = new TextButton("sell " + name, skin);
        innerPanel.add(sellAnimal).fillX();
        innerPanel.row();
        sellAnimal.setColor(1, 210f / 255, 132f / 255, 1);
        sellAnimal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                latestResult = controller.sellAnimal(animalActor);
                showResult = true;
                animalMenuTable.setVisible(false);
            }
        });

        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuTable.setVisible(false);
            }
        });

        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        animalMenuTable.add(innerPanel).center();
    }

    private void showNpcMenu(NpcActor npcActor) {
        NPC npc = npcActor.getNpc();
        npcMenuTable.clear();
        npcMenuTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");

        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);

        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        ImageButton closeButton = new ImageButton(closeDrawable);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                npcMenuTable.setVisible(false);
            }
        });
        TextButton gift = new TextButton("gift " + npc.getName(), skin);
        gift.setColor(1, 210f / 255, 132f / 255, 1);
        gift.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftMode = true;
                isInvenotryOpen = true;
                lastNPC = npcActor;
                lastPlayer = null;
                npcMenuTable.setVisible(false);
                npcActor.setWalking(false);
            }
        });
        innerPanel.add(gift).fillX().row();
        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        npcMenuTable.add(innerPanel).center();

    }

    public void feedAnimal(AnimalActor animal) {
        Texture hayTexture = GameAssetManager.getInstance().getOrLoadTexture("Items/Hay.png");
        Image hayImage = new Image(hayTexture);

        float animalX = animal.getX();
        float animalY = animal.getY();


        float hayX = animalX;
        float hayY = animalY - 50;

        hayImage.setPosition(hayX, hayY);
        stage.addActor(hayImage);

        animal.setState(Animal.State.EATING);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                hayImage.remove();
                animal.setState(Animal.State.IDLE);
            }
        }, 5);
    }

    private void showMissionList(NPC npc) {
        missionListTable.clear();
        missionListTable.setVisible(true);

        Table innerPanel = new Table(skin);
        Texture questLogTexture = GameAssetManager.getInstance().getOrLoadTexture("NPCs/questLog.png");
        Drawable questLogDrawable = new TextureRegionDrawable(new TextureRegion(questLogTexture));
        innerPanel.setBackground(questLogDrawable);
        innerPanel.pad(30);

        for (Mission mission : npc.getMissions()) {
            Label label = new Label(mission.getTitle(), skin);
            Image icon = new Image(getStatusDrawable(mission));

            Table row = new Table();
            label.setColor(86f / 225f, 22f / 225f, 12f / 225f, 1);
            if (npc.getMissions().indexOf(mission) == 0) {
                row.add(label).padTop(45).padBottom(5).padRight(10).padLeft(10);
                row.add(icon).size(32).pad(5).padTop(45).padBottom(5).padRight(10);
            } else if (npc.getMissions().indexOf(mission) == 2) {
                row.add(label).padBottom(70).padRight(10);
                row.add(icon).size(32).pad(5).padBottom(70).padRight(10);
            } else {
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

    public void updateToolSelectionSlots() {
        toolSlots.clear();

        float slotPadding = 0.5f;
        float leftOffset = 35f;
        float topOffset = 485;
        int cols = 12;
        int rows = 1;

        TextureRegion toolSelection = GameAssetManager.toolSelection;
        float scale = 0.5f;
        float scaledWidth = toolSelection.getRegionWidth() * scale;
        float scaledHeight = toolSelection.getRegionHeight() * scale;

        TOOL_X = camera.position.x - scaledWidth / 2f;
        TOOL_Y = camera.position.y - scaledHeight / 2f;

        ArrayList<Item> items = new ArrayList<>();
        for (Item item : MyGame.getCurrentPlayer().getBackPack().getInventory().keySet()) {
            if (item instanceof Tool) {
                items.add(item);
            }
        }
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                InventorySlot slot = new InventorySlot();
                slot.x = TOOL_X + leftOffset + col * (SLOT_SIZE + slotPadding) - 20f;
                slot.y = TOOL_Y + (-row) * (SLOT_SIZE + 10f) - 395f;


                if (index < items.size()) {
                    Item item = items.get(index++);
                    slot.item = item;
                    slot.count = MyGame.getCurrentPlayer().getBackPack().getInventory().get(item);
                }

                toolSlots.add(slot);
            }
        }

    }

    public void updateInventorySlots(float x, float y) {
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
                slot.x = x + leftOffset + col * (SLOT_SIZE + slotPadding) + 5f;
                slot.y = y + (3 - row - 1) * (SLOT_SIZE + 10f) + topOffset - 150f;

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

    private void showResult(SpriteBatch batch, Result result, float delta) {
        resultTime += delta;
        if (resultTime > resultDuration) {
            resultTime = 0;
            showResult = false;
            return;
        }

        String message = result.toString();
        if (message.isEmpty()) return;
        layout.setText(font, message);

        float padding = 60f;
        float bgWidth = layout.width + padding;
        float bgHeight = resultBg.getRegionHeight() * 5f;

        float x = camera.position.x - bgWidth / 2f;
        float y = camera.position.y + camera.viewportHeight / 2f - bgHeight - 10f;

        float textX = x + (bgWidth - layout.width) / 2f;
        float textY = y + (bgHeight + layout.height) / 2f;

        batch.begin();
        batch.draw(resultBg, x, y, bgWidth, bgHeight);

        font.setColor(result.isSuccess() ? Color.GREEN : Color.RED);
        font.draw(batch, message, textX, textY);
        font.setColor(Color.BLACK);
        batch.end();
    }

    private class InventoryInputHandler extends InputAdapter {
        GameScreen screen;

        public InventoryInputHandler(GameScreen screen) {
            this.screen = screen;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {

            Vector3 world = camera.unproject(new Vector3(screenX, screenY, 0));

            if (isInvenotryOpen) {
                if (artisanInputMode) return true;
                //check trash can
                if (draggedItem != null && trashcan.contains(world.x, world.y)) {
                    MyGame.getCurrentPlayer().getBackPack().removeFromInventory(draggedItem, 1);
                    draggedItem = null;
                    selectedSlot = null;
                    updateInventorySlots(INVENTORY_X, INVENTORY_Y);
                    syncBackPackFromSlots();
                    return true;
                }
                //check the slots
                for (InventorySlot slot : slots) {
                    if (world.x >= slot.x && world.x <= slot.x + SLOT_SIZE &&
                        world.y >= slot.y && world.y <= slot.y + SLOT_SIZE) {
                            if(draggedItem == null && slot.item == null) {
                            MyGame.getCurrentPlayer().setCurrentItem(null);
                            }
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
                        if (!giftMode) MyGame.getCurrentPlayer().setCurrentItem(slot.item);
                    }
                }
            } else if (isToolSelectionOpen) {
                for (InventorySlot slot : toolSlots) {
                    if (world.x >= slot.x && world.x <= slot.x + SLOT_SIZE &&
                        world.y >= slot.y && world.y <= slot.y + SLOT_SIZE) {
                        if (slot.item != null) {
                            MyGame.getCurrentPlayer().setCurrentItem(slot.item);
                            return true;
                        }
                    }
                }
            } else if (isCraftOpen) { //craft click mechanism
                CraftType selectedCraft = null;
                BackPack backPack = MyGame.getCurrentPlayer().getBackPack();
                int recipesPerRow = 12;
                int maxRows = 4;
                int recipesPerPage = recipesPerRow * maxRows;

                int currentPage = craftPageIndex;
                int startIndex = currentPage * recipesPerPage;
                int endIndex = Math.min(startIndex + recipesPerPage, backPack.getLearntRecipes().size());

                float padding = 3f;
                float iconSize = 74f;
                float startX = CRAFT_X + 50f;
                float startY = CRAFT_Y + (GameAssetManager.getCraftTexture().getRegionHeight() * 0.5f) - iconSize - 40f;

                for (int i = startIndex; i < endIndex; i++) {
                    int index = i - startIndex;
                    int row = index / recipesPerRow;
                    int col = index % recipesPerRow;

                    float x = startX + col * (iconSize + padding);
                    float y = startY - row * (iconSize + padding) - 20f;

                    if (world.x >= x && world.x <= x + iconSize &&
                        world.y >= y && world.y <= y + iconSize) {

                        selectedCraft = backPack.getLearntRecipes().get(i);
                        Result result = homeMenuController.craftItem(selectedCraft.getName());
                        latestResult = result;
                        showResult = true;
                        if (result.isSuccess()) GameAssetManager.playSfx("crafted");
                        updateInventorySlots(CRAFT_X, CRAFT_Y);
                        break;
                    }
                }

            } else if (isCookingOpen) {
                CookingRecipeType selectedRecipe = null;

                BackPack backPack = MyGame.getCurrentPlayer().getBackPack();

                int recipesPerRow = 12;
                int maxRows = 4;
                int recipesPerPage = recipesPerRow * maxRows;

                int currentPage = craftPageIndex;
                int startIndex = currentPage * recipesPerPage;
                int endIndex = Math.min(startIndex + recipesPerPage, backPack.getLearntCookingRecipe().size());

                float padding = 3f;
                float iconSize = 74f;
                float startX = COOKING_X + 50f;
                float startY = COOKING_Y + (GameAssetManager.getCookingTexture().getRegionHeight() * 0.5f) - iconSize;

                for (int i = startIndex; i < endIndex; i++) {
                    int index = i - startIndex;
                    int row = index / recipesPerRow;
                    int col = index % recipesPerRow;

                    float x = startX + col * (iconSize + padding);
                    float y = startY - row * (iconSize + padding) - 20f;

                    if (world.x >= x && world.x <= x + iconSize &&
                        world.y >= y && world.y <= y + iconSize) {

                        selectedRecipe = backPack.getLearntCookingRecipe().get(i);
                        Result result = homeMenuController.prepareFood(selectedRecipe.getName());
                        latestResult = result;
                        showResult = true;
                        break;
                    }
                }
            }

            if (MyGame.getCurrentPlayer().getCurrentItem() != null &&
                !isCraftOpen && !isInvenotryOpen && !isCookingOpen) {
                int tileX = (int) (world.x / TILE_SIZE);
                int tileY = (int) (world.y / TILE_SIZE);

                if (tileX >= 0 && tileX < MAP_WIDTH &&
                    tileY >= 0 && tileY < MAP_HEIGHT) {

                    GameTile tile = GameMap.getTile(tileX, tileY);
                    if (tile != null) {
                        if (MyGame.getCurrentPlayer().getCurrentItem() instanceof Tool) {
                            Item currentItem = MyGame.getCurrentPlayer().getCurrentItem();
                            if (currentItem instanceof FishingPole && tile.getTileType() == TileType.Water) {
                                FishingPole pole = (FishingPole) currentItem;
                                FishType fish = controller.getRandomFish((FishingPole) currentItem);
                                Main.getMain().setScreen(new FishingMiniGame(screen, pole, fish));
                            } else MyGame.getCurrentPlayer().useTool();
                            Result result = ((Tool) currentItem).use(tile);
                            latestResult = result;
                            showResult = true;
                        } else {
                            Item currentItem = MyGame.getCurrentPlayer().getCurrentItem();
                            Result result = null;
                            if (!(result = controller.plantSeed(currentItem, tile)).getMessage().startsWith("That's not a valid seed")) {
                                showResult = true;
                                latestResult = result;
                                MyGame.getCurrentPlayer().getBackPack().removeFromInventory(currentItem, 1);
                            } else if (currentItem.getName().equals("Speed-Gro") || currentItem.getName().equals("Retaining-Soil")) {
                                latestResult = controller.fertilizeCrop(currentItem.getName(), tile);
                                showResult = true;
                                MyGame.getCurrentPlayer().getBackPack().removeFromInventory(currentItem, 1);
                            } else if (currentItem instanceof Craft && ArtisanType.getArtisan(((Craft) currentItem).getType()) != null) {
                                lastArtisanType = ArtisanType.getArtisan(((Craft) currentItem).getType());
                                artisanMode = true;
                                MyGame.getCurrentPlayer().getBackPack().removeFromInventory(currentItem, 1);
                                multiplexer.addProcessor(0, buildInputProcessor);
                                String texturePath = "ArtisanMachines/" + lastArtisanType.name().toLowerCase() + "_ready.png";
                                artisanPreviewTexture = GameAssetManager.getInstance().getOrLoadTexture(texturePath);
                            } else {
                                GameAssetManager.playSfx("place item");
                                latestResult = controller.placeItem(currentItem, tile);
                                showResult = true;
                            }
                            Player player = MyGame.getCurrentPlayer();
                            if (player.getBackPack().howManyOfItem(currentItem) == 0)
                                MyGame.getCurrentPlayer().setCurrentItem(null);
                        }
                    }
                }

            }

            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            if (!isCraftOpen && !isCookingOpen) {
                hoveredCraftType = null;
                hoveredCookingRecipeType = null;
                return false;
            }
            Vector3 world = camera.unproject(new Vector3(screenX, screenY, 0));
            if (isCraftOpen) {
                hoveredCraftType = null;

                ArrayList<CraftType> learnedRecipes = MyGame.getCurrentPlayer().getBackPack().getLearntRecipes();

                int recipesPerRow = 12;
                int maxRows = 4;
                int recipesPerPage = recipesPerRow * maxRows;

                int currentPage = craftPageIndex;
                int startIndex = currentPage * recipesPerPage;
                int endIndex = Math.min(startIndex + recipesPerPage, learnedRecipes.size());

                float padding = 3f;
                float iconSize = 74f;
                float startX = CRAFT_X + 50f;
                float startY = CRAFT_Y + (GameAssetManager.getCraftTexture().getRegionHeight() * 0.5f) - iconSize - 40f;

                for (int i = startIndex; i < endIndex; i++) {
                    int index = i - startIndex;
                    int row = index / recipesPerRow;
                    int col = index % recipesPerRow;

                    float x = startX + col * (iconSize + padding);
                    float y = startY - row * (iconSize + padding) - 20f;

                    if (world.x >= x && world.x <= x + iconSize &&
                        world.y >= y && world.y <= y + iconSize) {

                        hoveredCraftType = learnedRecipes.get(i);
                        break;
                    }
                }
            } else if (isCookingOpen) {
                hoveredCookingRecipeType = null;

                ArrayList<CookingRecipeType> learnedRecipes = MyGame.getCurrentPlayer().getBackPack().getLearntCookingRecipe();

                int recipesPerRow = 12;
                int maxRows = 4;
                int recipesPerPage = recipesPerRow * maxRows;

                int currentPage = craftPageIndex;
                int startIndex = currentPage * recipesPerPage;
                int endIndex = Math.min(startIndex + recipesPerPage, learnedRecipes.size());

                float padding = 3f;
                float iconSize = 74f;
                float startX = COOKING_X + 50f;
                float startY = COOKING_Y + (GameAssetManager.getCookingTexture().getRegionHeight() * 0.5f) - iconSize;

                for (int i = startIndex; i < endIndex; i++) {
                    int index = i - startIndex;
                    int row = index / recipesPerRow;
                    int col = index % recipesPerRow;

                    float x = startX + col * (iconSize + padding);
                    float y = startY - row * (iconSize + padding) - 20f;

                    if (world.x >= x && world.x <= x + iconSize &&
                        world.y >= y && world.y <= y + iconSize) {

                        hoveredCookingRecipeType = learnedRecipes.get(i);
                        break;
                    }
                }
            }
            return false;
        }
    }

    public void addAnimalActor(AnimalActor animalActor) {
        stage.addActor(animalActor);
        animalActor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                    showAnimalMenu(animalActor);
                    return true;
                }
                return false;
            }
        });
    }

    public void removeAnimalActor(AnimalActor animalActor) {
        animalActor.remove();
    }

    public void addArtisanMachine(ArtisanMachine machine) {
        stage.addActor(machine);
        machine.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                    if (machine.isWorking()) {
                        if (machine.getOwner().equals(MyGame.getCurrentPlayer()))
                            showArtisanMenu(machine);
                    }
                    else {
                        lastArtisan = machine;
                        artisanInputMode = true;
                        isInvenotryOpen = true;
                        machine.setVisible(false);
                        selectedSlots.clear();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public GameMenuController getController() {
        return controller;
    }

    public void enterBuildMode(Texture buildingTexture, EnclosureType type, AnimalHouseLevel level) {
        isInBuildMode = true;
        buildingPreviewTexture = buildingTexture;
        lastType = type;
        lastLevel = level;
        multiplexer.addProcessor(0, buildInputProcessor);
    }

    private void placeBuilding(float x, float y, EnclosureType type, AnimalHouseLevel level) {
        AnimalHouse building = new AnimalHouse(type, level, x, y, buildingPreviewTexture);
        Player player = MyGame.getCurrentPlayer();
        player.addAnimalHouse(building);
    }

    public boolean showTradingRequest(String playerName){
        Table tradeRequestTable = new Table();
        Label tradeLabel = new Label("Trade request by " + playerName, skin);
        tradeRequestTable.add(tradeLabel);
        tradeRequestTable.add(accept);
        tradeRequestTable.add(reject);
        uiStage.addActor(tradeRequestTable);
        return acceptedRequest; //implement better
    }

    public void showProposalPopup(Player fromPlayer) {
//        Dialog dialog = new Dialog("Marriage Proposal", skin);
//        dialog.text(fromPlayer + " wants to marry you 💍");
//
//        dialog.button("Accept", true);
//        dialog.button("Reject", false);
//
//        dialog.show(stage);
//        dialog.setResultListener(result -> {
//            MarriageProposalResponse resp = new MarriageProposalResponse();
//            resp.fromPlayer = MyGame.getCurrentPlayer().getUsername();
//            resp.toPlayer = fromPlayer;
//            resp.accepted = (Boolean) result;
//            MyGame.getClient().sendTCP(resp);
//        });

        Dialog dialog = new Dialog("Marriage Proposal", skin) {
            @Override
            protected void result(Object object) {
                boolean accepted = (Boolean) object;
                MarriageProposalResponse resp = new MarriageProposalResponse();
                resp.fromPlayer = MyGame.getCurrentPlayer();
                resp.toPlayer = fromPlayer;
                resp.accepted = accepted;
                Main.getMain().getNetworkManager().getClient().sendTCP(resp);
            }
        };

        dialog.text(fromPlayer + " wants to marry you 💍");
        dialog.button("Accept", true);
        dialog.button("Reject", false);
        dialog.show(stage);
    }



}
