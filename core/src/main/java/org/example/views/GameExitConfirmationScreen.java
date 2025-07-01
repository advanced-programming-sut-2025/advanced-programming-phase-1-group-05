package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.FileController;
import org.example.controllers.RegisterMenuController;
import org.example.models.MyGame;
import org.example.models.Player;

import java.io.File;
import java.util.*;

public class GameExitConfirmationScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final Map<String, CheckBox> checkBoxes = new HashMap<>();

    public GameExitConfirmationScreen(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Confirm Game Termination", skin, "title");
        table.add(title).colspan(2).padBottom(20).row();

        // ✅ استفاده از Set برای جلوگیری از تکرار اسامی
        Set<String> addedUsernames = new HashSet<>();
        for (Player player : MyGame.getAllPlayers()) {
            if (player == null) continue;
            String username = player.getUsername();
            if (addedUsernames.contains(username)) continue;
            addedUsernames.add(username);

            CheckBox checkBox = new CheckBox(" " + username, skin);
            checkBoxes.put(username, checkBox);
            table.add(checkBox).colspan(2).left().pad(5).row();
        }

        TextButton okBtn = new TextButton("OK", skin);
        TextButton backBtn = new TextButton("Back", skin);
        Label errorLabel = new Label("", skin);
        errorLabel.setColor(com.badlogic.gdx.graphics.Color.RED);

        table.add(okBtn).padTop(15).colspan(2).row();
        table.add(backBtn).pad(10).colspan(2).row();
        table.add(errorLabel).colspan(2).padTop(10).row();

        okBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (CheckBox cb : checkBoxes.values()) {
                    if (!cb.isChecked()) {
                        errorLabel.setText("All players must confirm.");
                        return;
                    }
                }

                // حذف بازی فقط اگر owner باشد
                String currentUsername = RegisterMenuController.currentUser.getUsername();
                File file = new File("game_state.json");
                if (file.exists()) {
                    if (MyGame.getCurrentPlayer() != null &&
                        MyGame.getCurrentPlayer().getUsername().equals(currentUsername)) {
                        if (file.delete()) {
                            errorLabel.setColor(com.badlogic.gdx.graphics.Color.GREEN);
                            errorLabel.setText("Game successfully deleted!");
                        } else {
                            errorLabel.setText("Failed to delete the game file.");
                        }
                    } else {
                        errorLabel.setText("Only the game owner can delete the game.");
                    }
                } else {
                    errorLabel.setText("No saved game found.");
                }
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showGameMenu();  // بازگشت به منوی بازی
            }
        });
    }

    @Override public void show() { Gdx.input.setInputProcessor(stage); }
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
