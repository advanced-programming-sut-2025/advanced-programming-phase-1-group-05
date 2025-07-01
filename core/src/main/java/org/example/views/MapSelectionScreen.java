package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.GameMenuController;
import org.example.models.Result;

import java.util.HashMap;
import java.util.List; // ✅ اضافه کردن import درست
import java.util.Map;

public class MapSelectionScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final GameMenuController controller;
    private final List<String> usernames;  // حالا درست شده
    private final Map<String, SelectBox<String>> playerMapSelections = new HashMap<>();
    private final Label resultLabel;

    public MapSelectionScreen(List<String> usernames, Skin skin, GameMenuController controller) {
        this.usernames = usernames;
        this.skin = skin;
        this.controller = controller;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Select Map for Each Player", skin, "title");
        table.add(titleLabel).colspan(2).padBottom(15).row();

        for (String username : usernames) {
            table.add(new Label(username + "'s Map:", skin)).pad(5);
            SelectBox<String> mapBox = new SelectBox<>(skin);
            mapBox.setItems("1", "2", "3", "4"); // اصلاح نام نقشه‌ها بر اساس منطق کد backend
            playerMapSelections.put(username, mapBox);
            table.add(mapBox).width(200).pad(5).row();
        }

        TextButton confirmBtn = new TextButton("Start Game", skin);
        resultLabel = new Label("", skin);
        resultLabel.setWrap(true);

        table.add(confirmBtn).colspan(2).pad(10).row();
        table.add(resultLabel).colspan(2).width(400).pad(5).row();

        confirmBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Map<String, String> chosenMaps = new HashMap<>();
                for (String username : usernames) {
                    String selectedMap = playerMapSelections.get(username).getSelected();

                    if (chosenMaps.containsValue(selectedMap)) {
                        resultLabel.setText("Duplicate map selected! Each player must choose a different map.");
                        return;
                    }

                    chosenMaps.put(username, selectedMap);
                }

                resultLabel.setText("Game started!");
                // MenuNavigator.showGameUI();  // فعال کن وقتی صفحه بازی آماده شد
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
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
