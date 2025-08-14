package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.Item;
import org.example.Common.Player;
import org.example.Common.Request.TradeMessage;
import org.example.Common.Trade;
import org.example.Main;
import org.example.Server.controllers.GameMenuController;
import org.example.Server.controllers.TradingController;
import org.example.Server.models.MyGame;

public class TradeScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final boolean isInitiator;

    private final String fromPlayer;
    private final String toPlayer;

    private final TradingController controller;
    private final GameMenuController gameMenuController;

    private final Label titleLabel;
    private final TextButton confirmOfferButton, cancelButton;
    private final TextButton acceptButton, rejectButton;
    private TextField offerField, requestField;

    private final Image offerSlotBg, requestSlotBg;
    public final Image offerItemImage, requestItemImage;

    private final Label offerQuantityLabel, requestQuantityLabel;
    private Item selectedOfferItem = null;
    private Item selectedRequestItem = null;
    private int amount , targetAmount = 0;

    public TradeScreen(String fromPlayer, String toPlayer, boolean isInitiator, TradingController controller) {
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.isInitiator = isInitiator;
        this.controller = controller;
        this.controller.setTradeScreen(this);

        this.stage = new Stage(new ScreenViewport());
        this.skin = GameAssetManager.getSkin();
        this.gameMenuController = new GameMenuController();

        this.titleLabel = new Label("Trading with: " + toPlayer, skin);

        this.confirmOfferButton = new TextButton("Confirm Offer", skin);
        this.cancelButton = new TextButton("Cancel", skin);
        this.acceptButton = new TextButton("Accept Trade", skin);
        this.rejectButton = new TextButton("Reject Trade", skin);

        this.offerSlotBg = new Image(GameAssetManager.slotBg);
        this.requestSlotBg = new Image(GameAssetManager.slotBg);

        this.offerItemImage = new Image();
        this.requestItemImage = new Image();

        offerQuantityLabel = new Label("", skin);
        requestQuantityLabel = new Label("", skin);

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

        if (isInitiator) {
            offerField = new TextField("", skin);
            requestField = new TextField("", skin);

            offerField.setMessageText("Offered item");
            requestField.setMessageText("Requested item");

            root.add(requestField).padTop(10).fillX();
            root.add(offerField).padTop(10).fillX().row();

            Stack requestStack = createItemSlot(requestSlotBg, requestItemImage, requestQuantityLabel);
            Stack offerStack = createItemSlot(offerSlotBg, offerItemImage, offerQuantityLabel);


            root.add(requestStack).padTop(10).size(96, 96);
            root.add(offerStack).padTop(10).size(96, 96).row();

            root.add(confirmOfferButton).colspan(2).padTop(20).fillX().row();
        } else {
            Stack requestStack = createItemSlot(requestSlotBg, requestItemImage, requestQuantityLabel);
            Stack offerStack = createItemSlot(offerSlotBg, offerItemImage, offerQuantityLabel);


            root.add(requestStack).padTop(10).size(96);
            root.add(offerStack).padTop(10).size(96).row();

            root.add(acceptButton).padTop(20).fillX();
            root.add(rejectButton).padTop(20).fillX().row();
        }

        root.add(cancelButton).colspan(2).padTop(10).fillX();
    }

    private void setupListeners() {
        if (isInitiator) {

            offerField.setTextFieldListener((textField, c) -> updateSlotFromText(offerField.getText(), offerItemImage, true));
            requestField.setTextFieldListener((textField, c) -> updateSlotFromText(requestField.getText(), requestItemImage, false));

            confirmOfferButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println(selectedOfferItem);
                    System.out.println(selectedRequestItem);
                    if (selectedOfferItem != null && selectedRequestItem != null) {
                        TradeMessage msg = new TradeMessage();
                        msg.fromPlayer = fromPlayer;
                        msg.toPlayer = toPlayer;
                        msg.type = TradeMessage.MessageType.UPDATE;
                        Trade trade = new Trade();
                        trade.player = fromPlayer;
                        trade.targetPlayer = toPlayer;
                        trade.amount = amount;
                        trade.targetAmount = targetAmount;
                        trade.item = selectedOfferItem.getName();
                        trade.targetItem = selectedRequestItem.getName();
                        msg.trade = trade;
                        System.out.println(msg);
                        controller.sendUpdateMessage(msg);
                    }
                }
            });

        } else {
            acceptButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    TradeMessage msg = new TradeMessage();
                    msg.fromPlayer = fromPlayer;
                    msg.toPlayer = toPlayer;
                    Trade trade = new Trade();
                    trade.player = fromPlayer;
                    trade.targetPlayer = toPlayer;
                    trade.amount = amount;
                    trade.targetAmount = targetAmount;
                    trade.item = selectedOfferItem.getName();
                    trade.targetItem = selectedRequestItem.getName();
                    msg.trade = trade;
                    msg.type = TradeMessage.MessageType.ACCEPT;
                    controller.acceptTradeOffer(msg);
                    Main.getMain().setScreen(MenuNavigator.getGameScreen());
                }
            });

            rejectButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    controller.rejectTrade(fromPlayer, toPlayer);
                    Main.getMain().setScreen(MenuNavigator.getGameScreen());
                }
            });
        }

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(MenuNavigator.getGameScreen()); //??
            }
        });
    }

    public void updateSlotFromText(String input, Image itemImage, boolean isOffer) {
        if (input == null || input.isEmpty()) {
            itemImage.setDrawable(null);
            if (isOffer) {
                offerQuantityLabel.setText("");
            } else {
                requestQuantityLabel.setText("");
            }
            return;
        }

        String[] parts = input.trim().split("\\s*x\\s*");
        if (parts.length != 2) {
            itemImage.setDrawable(null);
            if (isOffer) {
                offerQuantityLabel.setText("");
            } else {
                requestQuantityLabel.setText("");
            }
            return;
        }

        String itemName = parts[0].trim().toLowerCase();
        int quantity;
        try {
            quantity = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            itemImage.setDrawable(null);
            if (isOffer) {
                offerQuantityLabel.setText("");
            } else {
                requestQuantityLabel.setText("");
            }
            return;
        }

        Item item = gameMenuController.getItemByName(itemName);
        if(isOffer) {
            selectedOfferItem = item;
        } else {
            selectedRequestItem = item;
        }
        if (item == null || quantity <= 0) {
            itemImage.setDrawable(null);
            if (isOffer) {
                offerQuantityLabel.setText("");
            } else {
                requestQuantityLabel.setText("");
            }
            return;
        }
        if(isOffer) selectedOfferItem = item;
        else selectedRequestItem = item;

        TextureRegion texture = item.getTexture();
        if (texture != null) {
            itemImage.setDrawable(new TextureRegionDrawable(texture));
        }

        if (isOffer) {
            amount = quantity;
            offerQuantityLabel.setText("x" + quantity);
        } else {
            targetAmount = quantity;
            requestQuantityLabel.setText("x" + quantity);
        }
    }


    private Stack createItemSlot(Image slotBackground, Image itemImage, Label quantityLabel) {
        Stack stack = new Stack();
        stack.setSize(96, 96);

        slotBackground.setSize(96, 96);
        slotBackground.setScaling(Scaling.stretch);

        itemImage.setScaling(Scaling.fit);
        itemImage.setAlign(Align.center);
        itemImage.setSize(48, 48);

        quantityLabel.setFontScale(0.8f);
        quantityLabel.setAlignment(Align.bottomRight);
        quantityLabel.setTouchable(null);

        Table labelTable = new Table();
        labelTable.setFillParent(true);
        labelTable.bottom().right().pad(4);
        labelTable.add(quantityLabel);

        stack.add(slotBackground);
        stack.add(itemImage);
        stack.add(labelTable);

        return stack;
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
    public void hide() {}
}
