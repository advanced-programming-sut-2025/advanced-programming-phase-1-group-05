//package org.example.views;
//
//import com.badlogic.gdx.Game;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.FitViewport;
//import org.example.models.GameAssetManager;
//
//public class MainMenu implements Screen {
//    private Game game;
//    private Stage stage;
//    private Skin skin;
//    private Texture bgTexture;
//
//    public MainMenu(Game game) {
//        this.game = game;
//    }
//
//    @Override
//    public void show() {
//        stage = new Stage(new FitViewport(800, 600));
//        Gdx.input.setInputProcessor(stage);
//
//        skin = GameAssetManager.getInstance().getSkin();
//        bgTexture = new Texture(Gdx.files.internal("backgrounds/main_bg.png"));
//
//        Table table = new Table();
//        table.setFillParent(true);
//        table.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));
//        stage.addActor(table);
//
//        // عنوان MAIN MENU
//        Label title = new Label("MAIN MENU", skin, "title");
//        table.top().padTop(50).add(title).colspan(2).row();
//
//        // دکمه‌های منو
//        TextButton settingsButton = new TextButton("Settings", skin);
//        TextButton profileButton = new TextButton("Profile", skin);
//        TextButton gameButton = new TextButton("Game", skin);
//        TextButton avatarButton = new TextButton("Avatar", skin);
//        TextButton logoutButton = new TextButton("Logout", skin);
//
//        // چینش دکمه‌ها
//        table.add(settingsButton).width(200).pad(10);
//        table.add(profileButton).width(200).pad(10).row();
//        table.add(gameButton).width(200).pad(10);
//        table.add(avatarButton).width(200).pad(10).row();
//        table.add(logoutButton).colspan(2).width(420).padTop(20);
//
//        // لیسنر دکمه Settings
//        settingsButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
////                game.setScreen(new SettingsMenu(game));
//            }
//        });
//
//        // لیسنر دکمه Profile
//        profileButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
////                game.setScreen(new ProfileMenu(game));
//            }
//        });
//
//        // لیسنر دکمه Game
//        gameButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
////                game.setScreen(new GameMenu(game));
//            }
//        });
//
//        // لیسنر دکمه Avatar
//        avatarButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
////                game.setScreen(new AvatarMenu(game));
//            }
//        });
//
//        // لیسنر دکمه Logout
//        logoutButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                game.setScreen(new LoginMenu(game));
//            }
//        });
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        stage.act(delta);
//        stage.draw();
//    }
//
//    @Override public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//    }
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//    @Override public void dispose() {
//        stage.dispose();
//        bgTexture.dispose();
//    }
//}

// MainMenu.java
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
