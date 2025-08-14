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
import org.example.Common.Player;
import org.example.Main;
import org.example.Server.controllers.RegisterMenuController;
import org.example.Common.User;
import org.example.Server.models.MyGame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private Texture avatarTexture;
    private static final List<Player> onlinePlayers = new ArrayList<>();
    private TextButton onlineButton;
    private TextButton scoreboardButton;

    public MainMenu(Skin skin) {
        MyGame.setMainMenu(this);
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
        onlineButton = new TextButton("Online Players", skin);
        scoreboardButton = new TextButton("Scoreboard", skin);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        User currentUser = RegisterMenuController.currentUser;
        String nickname = currentUser != null ? currentUser.getNickName() : "Guest";
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
        TextButton lobbyButton = new TextButton("Lobby", skin);
        TextButton avatarButton = new TextButton("Avatar", skin);
        TextButton logoutButton = new TextButton("Logout", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        table.add(avatarImage).size(80, 80).padBottom(12).colspan(2).row();
        table.add(nicknameLabel).padBottom(8).colspan(2).row();
        table.add(title).padBottom(18).colspan(2).row();

        table.add(profileButton).pad(5).width(300);
        table.add(gameButton).pad(5).width(300).row();

        table.add(onlineButton).pad(5).width(300);
        table.add(lobbyButton).pad(5).width(300).row();

        table.add(scoreboardButton).pad(5).width(300);
        table.add(avatarButton).pad(5).width(300).colspan(2).row();

        table.add(logoutButton).padTop(15).width(300);
        table.add(exitButton).padTop(15).width(300).row();

        stage.clear();
        stage.addActor(table);

        profileButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showProfileMenu();
            }
        });

        scoreboardButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new ScoreboardView());
            }
        });

        gameButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showGameMenu();
            }
        });

        onlineButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                showOnlinePlayers();
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

        lobbyButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showLobbyMenu();
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

    private void showOnlinePlayers() {
        Dialog dialog = new Dialog("Online Players", skin);
        Table content = new Table();
        content.pad(10).top().left();

        if (onlinePlayers.isEmpty()) {
            content.add(new Label("No players online.", skin));
        } else {
            for (Player player : onlinePlayers) {
                Table row = new Table();

                Image avatar = new Image(player.getUser().getAvatarTexture());
                avatar.setSize(32, 32);

                Label nameLabel = new Label(player.getUsername(), skin);
//                 String lobbyName = (player.getCurrentLobby() != null) ? player.getCurrentLobby().getName() : "No Lobby";
//                 Label lobbyLabel = new Label("Lobby: " + lobbyName, skin);

                row.add(avatar).size(32).padRight(10);
                row.add(nameLabel).padRight(10);
//                 row.add(lobbyLabel).left();

                content.add(row).left().row();
            }
        }

        ScrollPane scrollPane = new ScrollPane(content, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollbarsVisible(true);

        dialog.getContentTable().add(scrollPane).width(400).height(300);
        dialog.button("Close");
        dialog.show(stage);
    }

    public void updateOnlinePlayers(List<Player> players) {
        onlinePlayers.clear();
        onlinePlayers.addAll(players);
        showOnlinePlayers();
    }


    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
