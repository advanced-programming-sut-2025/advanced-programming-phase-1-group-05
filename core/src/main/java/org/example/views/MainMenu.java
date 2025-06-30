package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu implements Screen {
    private final Stage stage;
    private final Skin skin;

    public MainMenu(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton profileButton = new TextButton("Profile", skin);
        TextButton gameButton = new TextButton("Game", skin);
        TextButton avatarButton = new TextButton("Avatar", skin);
        TextButton logoutButton = new TextButton("Logout", skin);

        table.add(new Label("Main Menu", skin)).padBottom(20).row();
        table.add(settingsButton).pad(5).row();
        table.add(profileButton).pad(5).row();
        table.add(gameButton).pad(5).row();
        table.add(avatarButton).pad(5).row();
        table.add(logoutButton).padTop(15).row();

        stage.addActor(table);

        logoutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showMainMenu();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
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
