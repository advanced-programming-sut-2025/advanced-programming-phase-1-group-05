package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import org.example.controllers.GameMenuController;
import org.example.models.App;
import org.example.models.Enums.GameMenuCommands;
import org.example.models.GameAssetManager;

import java.util.regex.Matcher;

public class CheatCodeWindow implements InputProcessor {
    private final BitmapFont font;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    private final Array<String> history = new Array<>();
    private final StringBuilder currentInput = new StringBuilder();

    private boolean isTerminalOpen = false;
    private boolean cursorVisible = true;
    private float cursorTimer = 0f;

    private final int maxLines = 10;
    private final float lineHeight = 20f;
    private final int padding = 10;

    private final int height = 250;

    public CheatCodeWindow(SpriteBatch batch) {
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = GameAssetManager.getSkin().getFont("subtitle");
        this.font.setColor(Color.GREEN);
    }

    public void update(float delta) {
        if(!isTerminalOpen) return;

        cursorTimer += delta;
        if (cursorTimer > 0.5) {
            cursorVisible = !cursorVisible; // :)
            cursorTimer = 0f;
        }
    }

    public void render() {
        if (!isTerminalOpen) return;

        OrthographicCamera uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.setToOrtho(false);

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        batch.setProjectionMatrix(uiCamera.combined);

        float windowWidth = 700;
        float windowHeight = height + 100;

        float x = (Gdx.graphics.getWidth() - windowWidth) / 2f;
        float y = (Gdx.graphics.getHeight() - windowHeight) / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, windowWidth, windowHeight);
        shapeRenderer.end();

        batch.begin();

        BitmapFont titleFont = GameAssetManager.getSkin().getFont("subtitle");
        titleFont.setColor(Color.WHITE);
        float titleY = y + windowHeight - padding;
        titleFont.draw(batch, "Cheat Code Window", x + padding, titleY);

        font.setColor(Color.WHITE);
        float inputY = y + padding + font.getLineHeight();
        String cursor = cursorVisible ? "_" : " ";
        font.draw(batch, "> " + currentInput + cursor, x + padding, inputY);

        float lineSpacing = font.getLineHeight() * 1.5f;
        float availableHeight = titleY - inputY - lineSpacing;
        int maxVisibleLines = (int)(availableHeight / lineSpacing);
        int start = Math.max(0, history.size - maxVisibleLines);

        float lineY = inputY + lineSpacing;
        font.setColor(Color.GREEN);
        for (int i = start; i < history.size; i++) {
            font.draw(batch, history.get(i), x + padding, lineY);
            lineY += lineSpacing;
        }

        batch.end();
    }
    
    private void command(String input) {
        history.add("> " + input);
        processCommand(input);
        currentInput.setLength(0);
    }

    private void processCommand(String input) {
        Matcher matcher = null;
        GameMenuController controller = new GameMenuController(App.currentUser); //what happened to game??
        if((matcher = GameMenuCommands.EnergySetCC.getMatcher(input)) != null) {
            controller.setEnergy(Integer.parseInt(matcher.group("value")));
        } else if((matcher = GameMenuCommands.EnergyUnlimitedCC.getMatcher(input)) != null) {
            controller.unlimitedEnergy();
        } else if((matcher = GameMenuCommands.AddItemCC.getMatcher(input)) != null) {
            controller.addItemCheatCode(matcher.group("itemName"), Integer.parseInt(matcher.group("count")));
        } else if((matcher = GameMenuCommands.SetFriendshipCC.getMatcher(input)) != null) {
            controller.cheatSetFriendship(matcher);
        } else if((matcher = GameMenuCommands.FriendshipPointsCC.getMatcher(input)) != null) {
            controller.cheatAddFriendshipPoints(matcher);
        } else if((matcher = GameMenuCommands.CheatAddMoney.getMatcher(input)) != null) {
            controller.cheatAddMoney(Integer.parseInt(matcher.group("count")));
        } else {
            //TODO implement invalid command + other cheat codes
        }
    }


    @Override
    public boolean keyDown(int i) {
        //using shortcut "C" to open cheat code window
        if (i == Input.Keys.C) {
            isTerminalOpen = !isTerminalOpen;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        if (!isTerminalOpen) return false;

        if (c == '\b') {
            if (currentInput.length() > 0)
                currentInput.deleteCharAt(currentInput.length() - 1);
        } else if (c == '\r' || c == '\n') {
            command(currentInput.toString());
        } else if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c) || c == '.' || c == '_') {
            currentInput.append(c);
        }
        return true;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public boolean isTerminalOpen(){
        return isTerminalOpen;
    }
}
