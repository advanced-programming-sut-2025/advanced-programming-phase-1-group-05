package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.StoreController;
import org.example.models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreView implements Screen {
    //private final GameScreen previousScreen;
    private Stage stage;
    private final Skin skin;
    private Map<Product, Integer> quantities = new HashMap<>();
    Store store;

    public StoreView(Store store) {
//        this.previousScreen = previousScreen;
        this.skin = GameAssetManager.getSkin();
        this.stage = new Stage(new ScreenViewport());
        this.store = store;
        List<Product> products = store.getProducts();

    }

    private void buildUI(List<Product> storeItems) {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Table itemTable = new Table();

        for (Product item : storeItems) {
            if (item.getName().contains("Tool")) continue;
            itemTable.add(createItemRow(item)).padBottom(50).row();
        }

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(1, 212/255f, 130/255f, 1f));
        pixmap.fill();

        Texture backgroundTexture = new Texture(pixmap);
        pixmap.dispose();
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        style.background = backgroundDrawable;

        ScrollPane scrollPane = new ScrollPane(itemTable, style);
        scrollPane.setFadeScrollBars(false);
        root.add(scrollPane).height(800).expandX().fillX().pad(20).row();



        TextButton finishButton = new TextButton("Finish Shopping", skin);
        finishButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                StoreController.getInstance().purchase(quantities, store);
            }
        });

        root.add(finishButton).padTop(20);
    }

    private Table createItemRow(Product item) {
        Table row = new Table();

        Image itemIcon = new Image(item.getTexture());
        Label nameLabel = new Label(item.getName(), skin);
        Label priceLabel = new Label(item.getPrice() + "g", skin);
        Label quantityLabel = new Label("0", skin);

        itemIcon.setSize(itemIcon.getWidth(), itemIcon.getHeight());

        nameLabel.setFontScale(1.5f);
        nameLabel.setColor(86f/225f, 22f/225f, 12f/225f,1);
        TextButton plus = new TextButton("+", skin);
        TextButton minus = new TextButton("-", skin);

        plus.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                int qty = Integer.parseInt(quantityLabel.getText().toString());
                if (MyGame.getCurrentPlayer().getGold() < item.getPrice() * (qty + 1))
                    return;
                qty++;
                quantityLabel.setText(String.valueOf(qty));
                quantities.put(item, qty);
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
        Image background = new Image(new Texture("stores/"+ store.getStoreName().toLowerCase() + ".jpg"));
        background.setFillParent(true);
        stage.addActor(background);
        Gdx.input.setInputProcessor(stage);
        buildUI(store.getProducts());
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(v);
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
        stage.dispose();
    }
}
