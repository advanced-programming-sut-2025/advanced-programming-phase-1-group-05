package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.RegisterMenuController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainMenu implements Screen {
    private final Stage stage;
    private final Skin skin;

    public MainMenu(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Label title = new Label("Main Menu", skin);
        TextButton profileButton = new TextButton("Profile", skin);
        TextButton gameButton = new TextButton("Game", skin);
        TextButton avatarButton = new TextButton("Avatar", skin);
        TextButton logoutButton = new TextButton("Logout", skin);

        table.add(title).padBottom(20).row();
        table.add(profileButton).pad(5).width(200).row();
        table.add(gameButton).pad(5).width(200).row();
        table.add(avatarButton).pad(5).width(200).row();
        table.add(logoutButton).padTop(15).width(200).row();

        stage.clear();
        stage.addActor(table);

        profileButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showProfileMenu();
            }
        });

        gameButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
//                MenuNavigator.showGameMenu();
            }
        });

        avatarButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
//                MenuNavigator.showAvatarMenu();
            }
        });

        logoutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clearCurrentUserFile();
                RegisterMenuController.currentUser = null;
                MenuNavigator.showLoginMenu();
            }
        });
    }

    private void clearCurrentUserFile() {
        File file = new File("currentuser.json");
        try {
            if (file.exists()) {
                new FileWriter(file, false).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
