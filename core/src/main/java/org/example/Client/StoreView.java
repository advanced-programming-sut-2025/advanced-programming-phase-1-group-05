package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.Player;
import org.example.Common.Product;
import org.example.Main;
import org.example.Server.controllers.StoreController;
import org.example.Server.models.*;
import org.example.Common.Enums.AnimalHouseLevel;
import org.example.Common.Enums.AnimalType;
import org.example.Common.Enums.EnclosureType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreView implements Screen {
    private ShaderProgram grayscaleShader;
    private final GameScreen previousScreen;
    private Stage stage;
    private final Skin skin;
    private Map<Product, Integer> quantities = new HashMap<>();
    Store store;
    private boolean showOnlyAvailable = false;
    Table itemTable;
    ScrollPane scrollPane;
    MessageBanner banner;

    public StoreView(Store store, GameScreen game) {
        this.skin = GameAssetManager.getSkin();
        this.stage = new Stage(new ScreenViewport());
        this.store = store;
        previousScreen = game;
    }

    private void buildUI(List<Product> storeItems) {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        itemTable = new Table();

        scrollPane = new ScrollPane(itemTable, createScrollPaneStyle());
        scrollPane.setFadeScrollBars(false);
        rebuildItemList(storeItems);
        root.add(scrollPane).height(800).expandX().fillX().pad(20).row();

//        for (Product item : storeItems) {
//            if (item.getName().contains("Tool")) continue;
//            itemTable.add(createItemRow(item)).padBottom(50).row();
//        }




        Texture shoppingIcon = GameAssetManager.getInstance().getOrLoadTexture("stores/shoppingIcon.png");
        ImageButton finishButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(shoppingIcon)));
        TextButton filterButton = new TextButton("Only Available Products", skin);
        filterButton.setColor(1, 210f/255, 132f/255, 1);
        finishButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Map<Product, Integer> toPurchase = new HashMap<>(quantities);

                for (Map.Entry<Product, Integer> entry : toPurchase.entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    if (quantity <= 0) continue;

                    AnimalType animalType = AnimalType.fromString(product.getName());
                    if (animalType != null) {
                        showNameDialog(animalType, product);
                        return;
                    }
                }
                Result result = StoreController.getInstance().purchase(quantities, previousScreen);
                if (result.isSuccess()) banner.showMessage(result.getMessage(), Color.GREEN, 5);
                else banner.showMessage(result.getMessage(), Color.RED, 5);
                Main.getMain().setScreen(previousScreen);
            }
        });
        filterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showOnlyAvailable = !showOnlyAvailable;
                filterButton.setText(showOnlyAvailable ? "Show All Products" : "Only Available Products");
                List<Product> filtered = showOnlyAvailable
                    ? store.getProducts().stream()
                    .filter(Product::isAvailable)
                    .toList()
                    : store.getProducts();
                rebuildItemList(filtered);
            }
        });
        Table buttonRow = new Table();
        buttonRow.add(filterButton).padRight(20);
        buttonRow.add(finishButton).size(70, 70);
        root.add(buttonRow).padTop(20);
    }


    private void showNameDialog(AnimalType type, Product product) {
        TextField nameField = new TextField("", skin);
        Dialog dialog = new Dialog("Name Your Animal", skin) {
            protected void result(Object obj) {
                if ((boolean) obj) {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) return;

                    Animal animal = new Animal(name, type, MyGame.getCurrentPlayer());
                    Result result = StoreController.getInstance().buyAnimal(type, product, previousScreen, animal);

                    if (result.isSuccess()) {
                        removeActor(this);
                        quantities.remove(product);
                        banner.showMessage(result.getMessage(), Color.GREEN, 2f);
                    } else {
                        banner.showMessage(result.getMessage(), Color.RED, 2f);
                    }
                }
            }
        };


        dialog.getContentTable().add(new Label("Enter a name for your new animal:", skin)).padBottom(10).row();
        dialog.getContentTable().add(nameField).width(300).padBottom(20).row();

        dialog.button("Confirm", true);
        dialog.button("Cancel", false);
        dialog.key(Input.Keys.ENTER, true);
        dialog.key(Input.Keys.ESCAPE, false);

        stage.addActor(dialog);
        dialog.show(stage);
    }




    private void rebuildItemList(List<Product> products) {
        itemTable.clear();
        for (Product item : products) {
            if (item.getName().contains("Tool")) continue;
            itemTable.add(createItemRow(item)).padBottom(50).row();
        }
    }

    private ScrollPane.ScrollPaneStyle createScrollPaneStyle() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1, 212/255f, 130/255f, 1f));
        pixmap.fill();
        Texture backgroundTexture = new Texture(pixmap);
        pixmap.dispose();
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        style.background = backgroundDrawable;
        return style;
    }

    private Table createItemRow(Product item) {
        Table row = new Table();

        Image itemIcon;
        if (item.isAvailable())
            itemIcon = new Image(item.getTexture());
        else itemIcon = new Image(makeGrayscale(GameAssetManager.getInstance().getItemTexture(item.getName())));
        Label nameLabel = new Label(item.getName(), skin);
        Label priceLabel = new Label(item.getPrice() + "g", skin);
        Label quantityLabel = new Label("0", skin);

        itemIcon.setSize(itemIcon.getWidth(), itemIcon.getHeight());

        nameLabel.setFontScale(1.5f);
        nameLabel.setColor(86f/225f, 22f/225f, 12f/225f,1);
        priceLabel.setColor(86f/225f, 22f/225f, 12f/225f,1);
        Texture plusTexture = GameAssetManager.getInstance().getOrLoadTexture("stores/plus.png");
        Texture minusTexture = GameAssetManager.getInstance().getOrLoadTexture("stores/minus.png");
        ImageButton plus = new ImageButton(new TextureRegionDrawable(new TextureRegion(plusTexture)));
        ImageButton minus = new ImageButton(new TextureRegionDrawable(new TextureRegion(minusTexture)));
        plus.getImageCell().size(30, 30);
        minus.getImageCell().size(30, 30);;
        plus.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (item.isAvailable()){
                    int qty = Integer.parseInt(quantityLabel.getText().toString());
                    if (MyGame.getCurrentPlayer().getGold() < item.getPrice() * (qty + 1)) {
                        banner.showMessage("you don't have enough gold!", Color.RED, 5f);
                        return;
                    }

                    if (item.getName().contains("Barn")) {
                        Texture tex  = GameAssetManager.getInstance().getItemTexture(item.getName());
                        AnimalHouseLevel level = AnimalHouseLevel.fromString(item.getName());
                        previousScreen.enterBuildMode(tex, EnclosureType.BARN, level);
                        Player player = MyGame.getCurrentPlayer();
                        player.addGold(- item.getPrice());
                        Main.getMain().setScreen(previousScreen);
                    }

                    qty++;
                    if (item.getRemainingForToday() < qty && item.getRemainingForToday() > 0) {
                        banner.showMessage("No more of this item available.", Color.RED, 5f);
                    }
                    quantityLabel.setText(String.valueOf(qty));
                    quantities.put(item, qty);
                }
                else {
                    banner.showMessage("item not available!", Color.RED, 5f);
                }
            }
        });

        minus.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                int qty = Integer.parseInt(quantityLabel.getText().toString());
                if (qty > 0) qty--;
                quantityLabel.setText(String.valueOf(qty));
                quantities.put(item, qty);
            }
        });

        Image hoverRect = new Image(new Texture("white_pixel.png"));
        hoverRect.setColor(Color.BLACK);
        hoverRect.setSize(200, 200);
        hoverRect.setVisible(false);

        Label tooltipLabel = new Label(item.getDescription(), skin);
        tooltipLabel.setColor(Color.WHITE);
        tooltipLabel.setWrap(true);
        tooltipLabel.setWidth(200);
        tooltipLabel.setAlignment(Align.left);
        tooltipLabel.setVisible(false);
        itemIcon.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                hoverRect.setVisible(true);
                Vector2 iconPos = itemIcon.localToStageCoordinates(new Vector2(0, 0));
                hoverRect.setPosition(iconPos.x - itemIcon.getWidth() - 250, iconPos.y);
                tooltipLabel.toFront();
                tooltipLabel.setVisible(true);
                tooltipLabel.setPosition(iconPos.x- itemIcon.getWidth() -245 , iconPos.y + 75);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoverRect.setVisible(false);
                tooltipLabel.setVisible(false);
            }
        });

        stage.addActor(tooltipLabel);
        stage.addActor(hoverRect);
        row.add(itemIcon).size(itemIcon.getWidth()*1.5f, itemIcon.getHeight()*1.5f).padRight(10);
        row.add(nameLabel).padRight(10);
        row.add(priceLabel).padRight(10);
        row.add(minus).size(30);
        row.add(quantityLabel).padLeft(5).padRight(5);
        row.add(plus).size(30);

        return row;
    }
    @Override
    public void show() {
        Image background = new Image(new Texture("stores/"+ store.getStoreName().toLowerCase().replaceAll("\\s+", "") + "Interior.png"));
        background.setFillParent(true);
        stage.addActor(background);
        Gdx.input.setInputProcessor(stage);
        String vertexShader = Gdx.files.internal("shaders/default.vert").readString();
        String fragmentShader = Gdx.files.internal("shaders/grayscale.frag").readString();
        grayscaleShader = new ShaderProgram(vertexShader, fragmentShader);

        if (!grayscaleShader.isCompiled()) {
            Gdx.app.error("Shader", grayscaleShader.getLog());
        }
        banner = new MessageBanner(skin);
        banner.setPosition(stage.getWidth() / 2f - 200, stage.getHeight() - 100);
        stage.addActor(banner);

        buildUI(store.getProducts());
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(v);
        stage.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Main.getMain().setScreen(previousScreen);
        }
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
        stage.dispose();
    }
    private Texture makeGrayscale(Texture original) {
        TextureData textureData = original.getTextureData();

        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        Pixmap pixmap = textureData.consumePixmap();
        Pixmap grayPixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);

        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pixel = pixmap.getPixel(x, y);
                int r = (pixel >> 24) & 0xff;
                int g = (pixel >> 16) & 0xff;
                int b = (pixel >> 8) & 0xff;
                int a = pixel & 0xff;

                int gray = (int)(0.3 * r + 0.59 * g + 0.11 * b);
                grayPixmap.drawPixel(x, y, (gray << 24) | (gray << 16) | (gray << 8) | a);
            }
        }

        Texture grayscaleTexture = new Texture(grayPixmap);
        pixmap.dispose();
        grayPixmap.dispose();
        return grayscaleTexture;
    }

}
