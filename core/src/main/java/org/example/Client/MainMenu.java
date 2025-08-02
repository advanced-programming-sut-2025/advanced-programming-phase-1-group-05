package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Server.controllers.RegisterMenuController;
import org.example.Common.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private Texture avatarTexture;

    public MainMenu(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        User currentUser = RegisterMenuController.currentUser;
        String nickname = currentUser != null ? currentUser.getNickName() : "Guest";
//        String avatarPath = "assets/NPCs/sebastian/avatar.png";
//        if (currentUser.getGender().equals("Male")) {
//            avatarPath = "assets/NPCs/sebastian/avatar.png";
//        } else {
//            avatarPath = "assets/NPCs/sebastian/avatar.png";
//        }
        String path = "NPCs/sebastian/avatar.png";
        if (Gdx.files.internal(path).exists()) {
            avatarTexture = new Texture(Gdx.files.internal(path));
        } else {
            avatarTexture = new Texture(Gdx.files.internal("default.png"));
        }


        avatarTexture = new Texture(Gdx.files.internal(path));
        Image avatarImage = new Image(avatarTexture);
        Label nicknameLabel = new Label(nickname, skin);

        Label title = new Label("Main Menu", skin);
        TextButton profileButton = new TextButton("Profile", skin);
        TextButton gameButton = new TextButton("Game", skin);
        TextButton avatarButton = new TextButton("Avatar", skin);
        TextButton logoutButton = new TextButton("Logout", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        table.add(avatarImage).size(80, 80).padBottom(10).row();
        table.add(nicknameLabel).padBottom(20).row();
        table.add(title).padBottom(20).row();
        table.add(profileButton).pad(5).width(200).row();
        table.add(gameButton).pad(5).width(200).row();
        table.add(avatarButton).pad(5).width(200).row();
        table.add(logoutButton).padTop(15).width(200).row();
        table.add(exitButton).padTop(10).width(200).row();

        stage.clear();
        stage.addActor(table);

        profileButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showProfileMenu();
            }
        });

        gameButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showGameMenu();
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
                try {
                    clearCurrentUserFile();
                    RegisterMenuController.currentUser = null;
                    MenuNavigator.showLoginMenu();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
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
