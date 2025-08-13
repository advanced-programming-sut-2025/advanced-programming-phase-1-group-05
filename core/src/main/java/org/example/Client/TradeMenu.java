package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.example.Common.Player;
import org.example.Common.Request.TradeMessage;
import org.example.Common.Trade;
import org.example.Main;
import org.example.Server.controllers.MenuController;
import org.example.Server.controllers.TradingController;
import org.example.Server.models.MyGame;
import org.example.Server.models.Result;

import java.util.Map;

public class TradeMenu extends Table {

    private final TradingController tradingController;
    private final MenuController menuController;
    private final ClientNetworkManager clientNetworkManager;
    private final Label tradingMenuLabel, startTradingLabel, tradeHistoryLabel, closeLabel;

    public TradeMenu(TradingController tradingController, MenuController menuController, Skin skin) {
        super(skin);
        this.tradingController = tradingController;
        this.menuController = menuController;
        this.clientNetworkManager = Main.getMain().getNetworkManager();

        Texture bgTexture = new Texture(Gdx.files.internal("Animals/MenuBackground2.png"));
        Drawable background = new TextureRegionDrawable(new TextureRegion(bgTexture));
        this.setBackground(background);

        tradingMenuLabel = new Label("Trade Menu", skin, "title");
        startTradingLabel = new Label("Start Trading", skin, "subtitle");
        tradeHistoryLabel = new Label("Trade History", skin, "subtitle");
        closeLabel = new Label("Close", skin, "subtitle");

        pad(30);
        defaults().pad(10).center().fillX().expandX();

        setupUI();

        setupListeners();
    }

    private void setupUI() {
        this.add(tradingMenuLabel).row();
        this.add(startTradingLabel).row();
        this.add(tradeHistoryLabel).row();
        this.add(closeLabel).row();
    }

    private void setupListeners() {
        addHoverEffect(startTradingLabel, Color.GREEN);
        addHoverEffect(tradeHistoryLabel, Color.GREEN);
        addHoverEffect(closeLabel, Color.GREEN);

        startTradingLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clear();

                Label enterNameLabel = new Label("Enter other player's username:", getSkin());
                TextField usernameField = new TextField("", getSkin());

                Label confirmButton = new Label("Send Trade Request", getSkin(), "subtitle");
                confirmButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        TradeMessage msg = new TradeMessage();
                        Result result;
                        msg.type = TradeMessage.MessageType.REQUEST;
                        msg.fromPlayer = MyGame.getCurrentPlayer().getUsername();
                        String otherPlayerUsername = usernameField.getText();
                        Player otherPlayer = MyGame.getPlayer(otherPlayerUsername);
                        if(otherPlayer == null) {
                            result = new Result(false, "Player not found");
                            MyGame.getGameScreen().latestResult = result;
                            MyGame.getGameScreen().showResult = true;
                            return;
                        }
                        msg.toPlayer = otherPlayer.getUsername();

                        clientNetworkManager.sendTradeRequest(msg);

                    }
                });
                addHoverEffect(confirmButton, Color.GREEN);

                add(enterNameLabel).center().pad(10).row();
                add(usernameField).center().width(200).pad(10).row();
                add(confirmButton).center().pad(10).row();
            }
        });


        tradeHistoryLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clear();

                Map<String, Trade> tradeHistory = MyGame.getCurrentPlayer().getTrades();

                Skin skin = getSkin();
                for (Map.Entry<String, Trade> entry : tradeHistory.entrySet()) {
                    String otherPlayerName = entry.getKey();
                    Trade trade = entry.getValue();

                    Label tradeWithLabel = new Label("Trade With: " + otherPlayerName, skin);
                    tradeWithLabel.setColor(Color.RED);
                    tradeWithLabel.setScale(1.5f);

                    String itemName = trade.item;
                    String targetItemName = trade.targetItem;
                    int amount = trade.amount;

                    Label offeredItemLabel = new Label("Offered Item: " + itemName + " x" + amount, skin);
                    Label requestedItemLabel = new Label("Requested Item: " + targetItemName +  " x" + amount, skin);

                    offeredItemLabel.setColor(Color.BLACK);
                    requestedItemLabel.setColor(Color.BLACK);

                    add(tradeWithLabel).left().row();
                    add(offeredItemLabel).left().padLeft(10).row();
                    add(requestedItemLabel).left().padLeft(10).row();
                    row().row();
                }
            }
        });

        closeLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }

    private void addHoverEffect(final Label label, final Color hoverColor) {
        final Color originalColor = label.getColor().cpy();
        label.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                label.setColor(hoverColor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                label.setColor(originalColor);
            }
        });
    }
}
