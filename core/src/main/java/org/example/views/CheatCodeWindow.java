package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
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

    private final float toggleKeyCooldown = 0.3f;
    private float toggleKeyTimer = 0f;

    private final int maxLines = 10;
    private final float lineHeight = 20f;
    private final int padding = 10;

    private final int height = 250;

    public CheatCodeWindow(SpriteBatch batch) {
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = GameAssetManager.getSkin().getFont("subtitle");
        this.font.setColor(Color.GREEN);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, Gdx.input.getInputProcessor()));
    }

    public void update(float delta) {
        if(!isTerminalOpen) return;

        cursorTimer += delta;
        if (cursorTimer > 0.5) {
            cursorVisible = !cursorVisible; // :)
            cursorTimer = 0f;
        }
        toggleKeyTimer += delta;
    }

    public void render() {
        if(!isTerminalOpen) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), height);
        shapeRenderer.end();

        batch.begin();

        int y = height - padding;
        int start = Math.max(0, Math.min(history.size, maxLines));
        for (int i = start; i < history.size; i++) {
            font.draw(batch, history.get(i), padding, y);
            y -= lineHeight;
        }

        String cursor = "";
        if(cursorVisible) cursor = "_";
        else cursor = " ";
        font.draw(batch, "> " + currentInput + cursor, padding, y);
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
            //TODO implement invalid command
        }
    }


    @Override
    public boolean keyDown(int i) {
        //using shortcut "C" to open cheat code window
        if (i == Input.Keys.C && toggleKeyTimer > toggleKeyCooldown) {
            isTerminalOpen = !isTerminalOpen;
            toggleKeyTimer = 0f;
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
