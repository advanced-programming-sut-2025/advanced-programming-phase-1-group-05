package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Server.controllers.GameMenuController;
import org.example.Server.models.MyGame;
import org.example.Server.models.Result;

public class CheatCodeWindow {

    private final Stage stage;
    private final TextField inputField;
    private boolean visible = false;
    private final BitmapFont font;
    private final Texture backgroundTexture;
    private OrthographicCamera camera;
    private GameMenuController gameMenuController;
    private InputProcessor previousInputProcessor;

    public CheatCodeWindow(OrthographicCamera camera, InputProcessor previousInputProcessor) {
        this.stage = new Stage(new ScreenViewport());
        this.previousInputProcessor = previousInputProcessor;
        gameMenuController = MyGame.getGameScreen().getController();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.7f);
        pixmap.fill();
        backgroundTexture = new Texture(pixmap);
        pixmap.dispose();
        this.camera = camera;

        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        font = new BitmapFont();
        font.getData().setScale(1.5f);
        TextFieldStyle style = new TextFieldStyle();
        style.font = font;
        style.fontColor = Color.GREEN;
        style.messageFont = font;
        style.messageFontColor = Color.DARK_GRAY;
        style.background = backgroundDrawable;

        inputField = new TextField("", style);
        inputField.setMessageText("Enter cheat code...");
        inputField.setSize(600, 40);
        inputField.setPosition(camera.position.x - 300f,camera.position.y - 20f);

        inputField.setTextFieldListener((field, c) -> {
            if (c == '\n' || c == '\r') {
                handleCheatCode(field.getText().trim());
                field.setText("");
                toggle();
            }
        });

        stage.addActor(inputField);
    }

    public void render(float delta) {
        if (!visible) return;

        float stageWidth = stage.getViewport().getWorldWidth();
        float stageHeight = stage.getViewport().getWorldHeight();
        inputField.setPosition(
            (stageWidth - inputField.getWidth()) / 2f,
            stageHeight - inputField.getHeight() - 10
        );

        stage.act(delta);
        stage.draw();
    }


    public void toggle() {
        visible = !visible;

        if (visible) {
            previousInputProcessor = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(stage);
            inputField.setText("");
            stage.setKeyboardFocus(inputField);
        } else {
            Gdx.input.setInputProcessor(previousInputProcessor);
        }
    }


    public boolean isVisible() {
        return visible;
    }

    private Result handleCheatCode(String command) {
        return gameMenuController.handleCheatCodes(command);
    }

    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        font.dispose();
    }
}
