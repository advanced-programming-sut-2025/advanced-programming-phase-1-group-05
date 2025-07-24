package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.example.Main;
import org.example.controllers.GameManager;
import org.example.controllers.GameMenuController;
import org.example.controllers.HomeMenuController;
import org.example.models.*;
import org.example.models.Building.AnimalHouse;
import org.example.models.Enums.*;
import org.example.models.Tool.BackPack;
import org.example.models.Tool.FishingPole;
import org.example.models.Tool.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.models.GameMap.MAP_HEIGHT;
import static org.example.models.GameMap.MAP_WIDTH;

public class GameScreen implements Screen {
    private ShapeRenderer shapeRenderer;
    GameMenuController controller;
    HomeMenuController homeMenuController;
    Stage stage;
    Table missionListTable, animalMenuTable, notificationTable, giftMenuTable, giftHistoryTable, rateTable,
        playerMenuTable, artisanMenuTable;
    Skin skin;
    ImageButton notificationButton;
    Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TileMapRenderer mapRenderer;
    private Player player;
    Stage uiStage;
    private Texture energyBarBg, energyBarFill, overlay, blackOverlay;
    private BitmapFont font;

    static final int TILE_SIZE = 64;
    private static final int VIEW_WIDTH = 20;
    private static final int VIEW_HEIGHT = 15;

    private float timeAccumulator = 0f;
    private boolean overviewMode = false;
    private Season currentSeason;
    private final ArrayList<Player> players;

    private CheatCodeWindow cheatCodeWindow;

    private boolean isInvenotryOpen = false;
    public static Array<Rectangle> farms = new Array<>();
    Array<NpcActor> NPCs = new Array<>();

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
    private boolean giftMode =false;
    private NPC lastNPC = null;
    private Player lastPlayer = null;

    // Artisan
    private boolean artisanInputMode = false;
    private List<InventorySlot> selectedSlots = new ArrayList<>();
    private ArtisanMachine lastArtisan;

    //result stuff
    private TextureRegion resultBg = GameAssetManager.resultTexture;
    private float resultTime = 0;
    private float resultDuration = 2f;
    private boolean showResult = false;
    private Result latestResult;
    private GlyphLayout layout = new GlyphLayout();

    public GameScreen(ArrayList<Player> playerList) {
        skin = GameAssetManager.getSkin();
        camera = new OrthographicCamera(VIEW_WIDTH * TILE_SIZE, VIEW_HEIGHT * TILE_SIZE);
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        cheatCodeWindow = new CheatCodeWindow(batch);
        players = playerList;
        controller = new GameMenuController(this);
        homeMenuController = new HomeMenuController();

        shapeRenderer = new ShapeRenderer();

        currentSeason = GameManager.getSeason();
        mapRenderer = new TileMapRenderer();
        mapRenderer.setSeason(currentSeason);

        Player currentPlayer = MyGame.getCurrentPlayer();
        String selectedMap = GameMenuController.getMapForPlayer(currentPlayer.getUsername());
        Vector2 spawnPosition = getInitialPositionForMap(selectedMap);
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

    private void tileOutline(Vector3 mouseWorld) {
        int tileX = (int)(mouseWorld.x / TILE_SIZE);
        int tileY = (int)(mouseWorld.y / TILE_SIZE);

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
        Gdx.gl.glClearColor(0f, 136/255f, 199/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        handleInput(delta);
        //cheatCodeWindow.update(delta);

        timeAccumulator += delta;
        if (timeAccumulator >= 42f) {
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

        batch.end();
        tileOutline(mouse);
        cheatCodeWindow.render();
        showInventory(batch);
        showSkillSet(batch);
        showCraftPage(batch);
        showToolSelection(batch);
        showCookingPage(batch);
        updateToolSelectionSlots();
        checkGifting();
        checkArtisanInput();
        if(showResult) showResult(batch,latestResult,delta);
        if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            GameManager.getGameClock().advanceDay();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            latestResult = controller.eatFood(player.getCurrentItem());
            if(latestResult.isSuccess()) {
                player.setEating(true);
            }
            showResult = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            GameAssetManager.playSfx("open page");
            isInvenotryOpen = !isInvenotryOpen;
            isCraftOpen = false;
            isSkillSetOpen = false;
            isToolSelectionOpen = false;
            isCookingOpen = false;
            if (isInvenotryOpen) {
                TextureRegion inventory = MyGame.getCurrentPlayer().getBackPack()
                    .getLevel().getInventoryTexture();
                float scale = 0.5f;
                float scaledWidth = inventory.getRegionWidth() * scale;
                float scaledHeight = inventory.getRegionHeight() * scale;
                INVENTORY_X = camera.position.x - scaledWidth / 2f;
                INVENTORY_Y = camera.position.y - scaledHeight / 2f;

                skillSetBounds.set(INVENTORY_X + 20f, INVENTORY_Y, 64, 64);

                updateInventorySlots();
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            GameAssetManager.playSfx("open page");
            isSkillSetOpen = !isSkillSetOpen;
            isCraftOpen = false;
            isInvenotryOpen = false;
            isToolSelectionOpen = false;
            isCookingOpen = false;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            GameAssetManager.playSfx("open page");
            isToolSelectionOpen = !isToolSelectionOpen;
            isCraftOpen = false;
            isSkillSetOpen = false;
            isInvenotryOpen = false;
            isCookingOpen = false;
            updateToolSelectionSlots();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            GameAssetManager.playSfx("open page");
            isCraftOpen = !isCraftOpen;
            isToolSelectionOpen = false;
            isSkillSetOpen = false;
            isInvenotryOpen = false;
            isCookingOpen = false;
            updateInventorySlots();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            GameAssetManager.playSfx("open page");
            isCookingOpen = !isCookingOpen;
            isToolSelectionOpen = false;
            isCraftOpen = false;
            isSkillSetOpen = false;
            isInvenotryOpen = false;
        }
        stage.act(delta);
        stage.draw();
        uiStage.act(delta);
        uiStage.draw();
    }

    private void closeAllPages(){
        isInvenotryOpen = false;
        isSkillSetOpen = false;
        isCraftOpen = false;
        isToolSelectionOpen = false;
    }

    private void checkGifting() {
        if (isInvenotryOpen && giftMode && Gdx.input.justTouched()) {
            Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            for (InventorySlot slot : slots) {
                if (mouse.x >= slot.x && mouse.x <= slot.x + SLOT_SIZE &&
                    mouse.y >= slot.y && mouse.y <= slot.y + SLOT_SIZE) {

                    if (slot.item != null) {
                        Item giftedItem = slot.item;
                        slot.item = null;
                        if (lastNPC!= null)
                            controller.giftNPC(lastNPC, giftedItem);
                        else if (lastPlayer != null)
                            controller.giftPlayer(lastPlayer, giftedItem, 1);
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
            for (InventorySlot slot : selectedSlots) {
                selectedItems.add(slot.item.getName());
                MyGame.getCurrentPlayer().getBackPack().removeFromInventory(slot.item, 1);
                // TODO ?????
            }

            latestResult = lastArtisan.insertItem(selectedItems);
            artisanInputMode = false;
            isInvenotryOpen = false;
            selectedSlots.clear();
            showResult = true;
        }
    }

    private void handleInput(float delta) {
        Vector2 oldPos = new Vector2(player.getXX(), player.getYY());
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.moveUp(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) player.moveDown(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.moveLeft(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.moveRight(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            MyGame.getCurrentPlayer().addGold(100000);
            Main.getMain().setScreen(new FishingMiniGame(this, new FishingPole(), FishType.CrimsonFish));
        }
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

    public void showCookingPage(SpriteBatch batch) {
        if(!isCookingOpen) return;

        TextureRegion cookingPage = GameAssetManager.getCookingTexture();
        float scale = 0.5f;
        float textWidth =  cookingPage.getRegionWidth();
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

                float drawX = slot.x + (SLOT_SIZE - drawWidth) / 2f + 235f;
                float drawY = slot.y + (SLOT_SIZE - drawHeight) / 2f + 65f; //fix

                batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
                if(slot.count > 1) font.draw(batch,String.valueOf(slot.count), drawX + drawWidth - 15f,drawY + 10f);
            }
        }

        if(hoveredCookingRecipeType != null) {
            //info background
            Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            TextureRegion infoBg = GameAssetManager.infoPage;

            float boxWidth = infoBg.getRegionWidth() * 13f;
            float boxHeight = infoBg.getRegionHeight() * 13f + hoveredCookingRecipeType.getIngredients().size() * 12f;

            float boxX = mouse.x + 30f ;
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

            for(Map.Entry<Item, Integer> entry : ingredients.entrySet()) {
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
        if(!isCraftOpen) return;

        TextureRegion craftPage = GameAssetManager.getCraftTexture();
        float scale = 0.5f;
        float textWidth =  craftPage.getRegionWidth();
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

                float drawX = slot.x + (SLOT_SIZE - drawWidth) / 2f + 235f;
                float drawY = slot.y + (SLOT_SIZE - drawHeight) / 2f + 65f; //fix

                batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
                if(slot.count > 1) font.draw(batch,String.valueOf(slot.count), drawX + drawWidth - 15f,drawY + 10f);
            }
        }

        if(hoveredCraftType != null) {
            //info background
            Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            TextureRegion infoBg = GameAssetManager.infoPage;

            float boxWidth = infoBg.getRegionWidth() * 13f;
            float boxHeight = infoBg.getRegionHeight() * 13f + hoveredCraftType.getIngredients().size() * 12f;

            float boxX = mouse.x + 30f ;
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

            for(Map.Entry<Item, Integer> entry : ingredients.entrySet()) {
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
                if(slot.count > 1) font.draw(batch,String.valueOf(slot.count), drawX + drawWidth - 15f,drawY + 10f);
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
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(cheatCodeWindow);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InventoryInputHandler(this));
        multiplexer.addProcessor(uiStage);

        Gdx.input.setInputProcessor(multiplexer);
        System.out.println("InputMultiplexer set with cheatCodeWindow");

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
        playerMenuTable = new Table();
        playerMenuTable.setVisible(false);
        playerMenuTable.setFillParent(true);
        uiStage.addActor(playerMenuTable);
        artisanMenuTable = new Table();
        artisanMenuTable.setVisible(false);
        artisanMenuTable.setFillParent(true);
        uiStage.addActor(artisanMenuTable);

        for (NpcActor npc : NPCs) {
            stage.addActor(npc);
            // TODO add listeners
        }
        Texture mail = GameAssetManager.getInstance().getOrLoadTexture("ui/mailSign.png");
        Drawable mailDrawable = new TextureRegionDrawable(new TextureRegion(mail));
        notificationButton = new ImageButton(mailDrawable);
        float padding = 70;
        float screenWidth = uiStage.getViewport().getScreenWidth();
        float screenHeight = uiStage.getViewport().getScreenHeight();

        float x = screenWidth - notificationButton.getWidth() - padding;
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
        forceViewportReset();

        Player player1 = MyGame.getCurrentPlayer();
        AnimalHouse house = new AnimalHouse(EnclosureType.COOP, AnimalHouseLevel.Big);
        player1.addAnimalHouse(house);
        Animal animal = new Animal("chicko", AnimalType.CHICKEN, player1);
        house.addAnimal(animal);
        addAnimalActor(new AnimalActor(animal));
        player1.getBackPack().addToInventory(MyGame.getDatabase().getItem("Hay"), 10);

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

        int tileX = (int)(x / TILE_SIZE);
        int tileY = (int)(y / TILE_SIZE);

        if (tileX >= 0 && tileX < MAP_WIDTH &&
            tileY >= 0 && tileY < MAP_HEIGHT) {

            GameTile tile = GameMap.getTile(tileX, tileY);
            return tile == null || tile.getTileType() != TileType.Water; // can't walk on water
        }

        return true;
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

    private  void showGiftHistory(Player player) {
        giftHistoryTable.clear();
        giftHistoryTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground1.png");
        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);
        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        for (Gift  gift : controller.getReceivedGifts(player)) {
            innerPanel.add(buildRow(gift,true)).padBottom(50).row();
        }
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
        nameLabel.setColor(86f/255, 22f/255, 12f/255, 1);
        row.add(itemIcon).size(itemIcon.getWidth()*1.5f, itemIcon.getHeight()*1.5f).padRight(10);
        row.add(nameLabel).padRight(10);
        Texture starTexture = GameAssetManager.getInstance().getOrLoadTexture("ui/rateSign.png");
        if (!gift.hasBeenRated() && ratable){
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
        }
        else {
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
        Texture menuTexture =  GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");
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
                   rating =  Integer.parseInt(enterRating.getText());
                   Result result = controller.rateTheGift(gift, rating);
                   if (! result.isSuccess()) {
                       errorLabel.setText(result.getMessage());
                   }
                   else  {
                       errorLabel.setColor(Color.GREEN);
                       errorLabel.setText(result.getMessage());
                   }
                }
                catch (Exception e) {
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

    private void showPlayerMenu(Player player) {
        playerMenuTable.clear();
        playerMenuTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture = GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");

        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);

        Texture closeTexture = new Texture(Gdx.files.internal("closeButton.png"));
        Drawable closeDrawable = new TextureRegionDrawable(new TextureRegion(closeTexture));

        TextButton giveBouquet = new TextButton("give a bouquet", skin);
        innerPanel.add(giveBouquet).fillX();
        innerPanel.row();
        giveBouquet.setDisabled(!MyGame.getCurrentPlayer().canGiveBouquet(player));

        if (!MyGame.getCurrentPlayer().canGiveBouquet(player)) {
            giveBouquet.setTouchable(Touchable.disabled);
            giveBouquet.setColor(Color.DARK_GRAY);
        }
        else giveBouquet.setTouchable(Touchable.enabled);
        ImageButton closeButton = new ImageButton(closeDrawable);
        giveBouquet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                isInvenotryOpen = true;
//                giftMode = true;
//                lastNPC = npc;
//                lastPlayer = null;
                playerMenuTable.setVisible(false);
            }
        });

        innerPanel.add(closeButton).size(48, 48).padTop(20).colspan(2).center();
        closeButton.getImageCell().size(48, 48);
        playerMenuTable.add(innerPanel).center();
    }

    private void showGiftMenu(Player player) {
        giftMenuTable.clear();
        giftMenuTable.setVisible(true);
        Table innerPanel = new Table(skin);
        Texture menuTexture =  GameAssetManager.getInstance().getOrLoadTexture("Animals/MenuBackground2.png");
        Drawable menuDrawable = new TextureRegionDrawable(new TextureRegion(menuTexture));
        innerPanel.setBackground(menuDrawable);
        innerPanel.pad(30);

        TextButton giftButton = new TextButton("gift " + player.getName(), skin);
        innerPanel.add(giftButton).fillX();
        innerPanel.row();
        giftButton.setColor(1, 210f/255, 132f/255, 1);
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
        giftHistory.setColor(1, 210f/255, 132f/255, 1);
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
        finishNowButton.setColor(1, 210f/255, 132f/255, 1);
        finishNowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machine.finish();
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
        feedButton.setColor(1, 210f/255, 132f/255, 1);
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
        petButton.setColor(1, 210f/255, 132f/255, 1);
        TextButton shepherdAnimal = new TextButton("shepherd " + name, skin);
        innerPanel.add(shepherdAnimal).fillX();
        innerPanel.row();
        shepherdAnimal.setColor(1, 210f/255, 132f/255, 1);
        TextButton collectProduceButton = new TextButton("collect produce", skin);
        innerPanel.add(collectProduceButton).fillX();
        innerPanel.row();
        collectProduceButton.setColor(1, 210f/255, 132f/255, 1);
        TextButton sellAnimal = new TextButton("sell " + name, skin);
        innerPanel.add(sellAnimal).fillX();
        innerPanel.row();
        sellAnimal.setColor(1, 210f/255, 132f/255, 1);
        sellAnimal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               latestResult =  controller.sellAnimal(animalActor);
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
                slot.y = TOOL_Y + (-row) * (SLOT_SIZE + 10f) -395f;


                if (index < items.size()) {
                    Item item = items.get(index++);
                    slot.item = item;
                    slot.count = MyGame.getCurrentPlayer().getBackPack().getInventory().get(item);
                }

                toolSlots.add(slot);
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

    private void showResult(SpriteBatch batch, Result result, float delta) {
        resultTime += delta;
        if (resultTime > resultDuration) {
            resultTime = 0;
            showResult = false;
            return;
        }

        String message = result.toString();
        if(message.isEmpty()) return;
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
            //if (!isInvenotryOpen && !isToolSelectionOpen) return false;

            Vector3 world = camera.unproject(new Vector3(screenX, screenY, 0));

            if(isInvenotryOpen) {
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

                        MyGame.getCurrentPlayer().setCurrentItem(slot.item);
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
            } else if(isCraftOpen) { //craft click mechanism
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
                        if(result.isSuccess()) GameAssetManager.playSfx("crafted");
                        updateInventorySlots();
                        break;
                    }
                }

            } else if(isCookingOpen) {
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

            if(MyGame.getCurrentPlayer().getCurrentItem() != null &&
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
                            Result result = controller.plantSeed(currentItem, tile);
                            if(!result.getMessage().startsWith("That's not a valid seed")) {
                                showResult = true;
                                latestResult = result;
                            } else {
                                GameAssetManager.playSfx("place item");
                                latestResult = controller.placeItem(currentItem, tile);
                                Player player = MyGame.getCurrentPlayer();
                                if (player.getBackPack().howManyOfItem(currentItem) == 0)
                                    MyGame.getCurrentPlayer().setCurrentItem(null);
                                updateInventorySlots();
                                showResult = true;
                            }
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
            if(isCraftOpen) {
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
                    if (machine.isWorking())
                        showArtisanMenu(machine);
                    else {
                        artisanInputMode = true;
                        isInvenotryOpen = true;
                        selectedSlots.clear();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
