package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.example.Server.controllers.MenuController;
import org.example.Server.controllers.TradingController;

public class TradeMenu extends Window {

    private final TradingController tradingController;
    private final MenuController menuController;

    private TextField usernameField, itemField, amountField;
    private SelectBox<String> tradeTypeSelect;
    private Label resultLabel;

    public TradeMenu(TradingController tradingController, MenuController menuController, Skin skin) {
        super("Trade Menu", skin);
        Texture bgTexture = new Texture(Gdx.files.internal("Animals/MenuBackground2.png"));
        Drawable background = new TextureRegionDrawable(new TextureRegion(bgTexture));
        this.setBackground(background);
        this.tradingController = tradingController;
        this.menuController = menuController;

        setMovable(true);
        setResizable(false);
        pad(20);
        setupUI(skin);
    }

    private void setupUI(Skin skin) {
        usernameField = new TextField("", skin);
        itemField = new TextField("", skin);
        amountField = new TextField("", skin);

        tradeTypeSelect = new SelectBox<>(skin);
        tradeTypeSelect.setItems("offer", "request");

        TextButton tradeButton = new TextButton("Trade", skin);
        TextButton listButton = new TextButton("List Trades", skin);
        TextButton historyButton = new TextButton("Trade History", skin);
        TextButton closeButton = new TextButton("Close", skin);

        resultLabel = new Label("", skin);
        resultLabel.setWrap(true);


        add(new Label("Username:", skin)).left();
        add(usernameField).width(150).row();

        add(new Label("Item:", skin)).left();
        add(itemField).width(150).row();

        add(new Label("Amount:", skin)).left();
        add(amountField).width(150).row();

        add(new Label("Type:", skin)).left();
        add(tradeTypeSelect).width(150).row();

        add(tradeButton).colspan(2).padTop(10).row();
        add(listButton).colspan(2).padTop(5).row();
        add(historyButton).colspan(2).padTop(5).row();
        add(closeButton).colspan(2).padTop(10).row();
        add(resultLabel).colspan(2).padTop(15).width(250).row();

        setupListeners(tradeButton, listButton, historyButton, closeButton);
        pack();
    }

    private void setupListeners(TextButton tradeButton, TextButton listButton,
                                TextButton historyButton, TextButton closeButton) {
        tradeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String input = String.format("trade -u %s -t %s -i %s -a %s",
                    usernameField.getText(),
                    tradeTypeSelect.getSelected(),
                    itemField.getText(),
                    amountField.getText());
                resultLabel.setText(tradingController.trade(input).getMessage());
            }
        });

        listButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
               // resultLabel.setText(tradingController.showTradeList());
            }
        });

        historyButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //resultLabel.setText(tradingController.printTradeHistory());
            }
        });

        closeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }
}
