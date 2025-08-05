package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.Item;
import org.example.Common.Player;
import org.example.Server.controllers.TradingController;

public class TradeScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final boolean isInitiator;

    private final Player fromPlayer;
    private final Player toPlayer;

    private final TradingController controller;

    private final Label titleLabel;
    private final TextButton confirmOfferButton, cancelButton;
    private final TextButton acceptButton, rejectButton;
    private TextField offerField, requestField;


    private Item selectedOfferItem = null;
    private Item selectedRequestItem = null;

    public TradeScreen(Player fromPlayer, Player toPlayer, boolean isInitiator, TradingController controller) {
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.isInitiator = isInitiator;
        this.controller = controller;

        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.getSkin();

        titleLabel = new Label("Trading with: " + toPlayer.getUsername(), skin);


        confirmOfferButton = new TextButton("Confirm Offer", skin);
        cancelButton = new TextButton("Cancel", skin);
        acceptButton = new TextButton("Accept Trade", skin);
        rejectButton = new TextButton("Reject Trade", skin);

        setupUI();
        setupListeners();
    }

    private void setupUI() {
        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(20);
        stage.addActor(root);

        root.add(titleLabel).colspan(2).padBottom(20).row();

        root.add(new Label("Request:", skin)).padRight(10);
        root.add(new Label("Offer:", skin)).row();

//        root.add(offerSlot).pad(10).size(64);
//        root.add(requestSlot).pad(10).size(64).row();

        if (isInitiator) {
            offerField = new TextField("", skin);
            requestField = new TextField("", skin);

            offerField.setMessageText("Enter your offer item");
            requestField.setMessageText("Enter requested item");

            root.add(requestField).padTop(10).fillX();
            root.add(offerField).padTop(10).fillX().row();

            root.add(confirmOfferButton).colspan(2).padTop(20).fillX().row();
        } else {
            root.add(acceptButton).padTop(20).fillX();
            root.add(rejectButton).padTop(20).fillX().row();
        }

        root.add(cancelButton).colspan(2).padTop(10).fillX();
    }


    private void setupListeners() {
        if (isInitiator) {
//            offerSlot.addListener(new ClickListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    //selectedOfferItem = controller.pickItemFromInventory(); // opens your inventory
//                    updateSlotImage(offerSlot, selectedOfferItem);
//                }
//            });
//
//            requestSlot.addListener(new ClickListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    //selectedRequestItem = controller.pickItemFromCatalog(); // pick what you want
//                    updateSlotImage(requestSlot, selectedRequestItem);
//                }
//            });

            confirmOfferButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (selectedOfferItem != null && selectedRequestItem != null) {
                        //controller.sendOffer(fromPlayer, toPlayer, selectedOfferItem, selectedRequestItem);
                    }
                }
            });
        } else {
            acceptButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                 //   controller.acceptTrade(fromPlayer, toPlayer);
                }
            });

            rejectButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                   // controller.rejectTrade(fromPlayer, toPlayer);
                }
            });
        }

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //controller.cancelTrade();
            }
        });
    }


    private void updateSlotImage(Image slot, Item item) {
        if (item != null) {
            slot.setDrawable(new TextureRegionDrawable(new TextureRegion(item.getTexture())));
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}

    @Override
    public void hide() {

    }
}

