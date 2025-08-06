package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.Network.*;
import org.example.Common.Lobby;
import org.example.Common.Player;
import org.example.Main;
import org.example.Server.controllers.RegisterMenuController;

import java.util.List;

public class LobbyMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private Table lobbyTable;
    private TextField lobbyNameField, passwordField, searchField;
    private CheckBox privateBox, visibleBox;

    public LobbyMenu(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // ==== لابی ساختن ====
        lobbyNameField = new TextField("", skin);
        lobbyNameField.setMessageText("Lobby name");

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Password (optional)");

        privateBox = new CheckBox(" Private", skin);
        visibleBox = new CheckBox(" Visible", skin);
        visibleBox.setChecked(true);

        TextButton createButton = new TextButton("Create Lobby", skin);
        createButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isPrivate = privateBox.isChecked();
                boolean visible = visibleBox.isChecked();
                String name = lobbyNameField.getText();
                String pass = passwordField.getText();

                Player player = new Player(Main.currentUser);
                CreateLobbyRequest request = new CreateLobbyRequest(name, isPrivate, pass, visible, player);
                Main.getMain().getNetworkManager().sendTCP(request);
            }
        });

        // ==== لیست لابی‌ها ====
        lobbyTable = new Table();
        ScrollPane lobbyScroll = new ScrollPane(lobbyTable, skin);

        TextButton refreshButton = new TextButton("Refresh", skin);
        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().getNetworkManager().sendTCP(new GetLobbiesRequest());
            }
        });

        searchField = new TextField("", skin);
        searchField.setMessageText("Enter lobby ID to join");

        TextButton joinByIdButton = new TextButton("Join by ID", skin);
        joinByIdButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String id = searchField.getText();
                Player player = new Player(RegisterMenuController.currentUser);
                JoinLobbyRequest join = new JoinLobbyRequest(id, player, null);
                Main.getMain().getNetworkManager().sendTCP(join);
            }
        });

        // ==== چیدمان ====
        root.top().pad(20);
        root.add(new Label("Create a Lobby", skin)).colspan(2).padBottom(10).row();
        root.add(lobbyNameField).width(200).pad(5);
        root.add(passwordField).width(200).pad(5).row();
        root.add(privateBox).pad(5);
        root.add(visibleBox).pad(5).row();
        root.add(createButton).colspan(2).pad(10).row();

        root.add(new Label("Available Lobbies:", skin)).colspan(2).padTop(20).row();
        root.add(lobbyScroll).colspan(2).height(300).width(450).padBottom(10).row();

        root.add(refreshButton).pad(5);
        root.add(joinByIdButton).pad(5).row();
        root.add(searchField).colspan(2).width(300).padTop(10);
    }

    public void updateLobbyList(List<Lobby> lobbies) {
        lobbyTable.clear();
        for (Lobby lobby : lobbies) {
            String name = lobby.getName();
            int count = lobby.getPlayers().size();
            String admin = lobby.getAdmin().getUsername();
            String id = lobby.getId();

            Label lobbyInfo = new Label(name + " | ID: " + id + " | Players: " + count + " | Admin: " + admin, skin);
            TextButton joinBtn = new TextButton("Join", skin);

            joinBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Player player = new Player(Main.currentUser);

                    if (lobby.isPrivate()) {
                        // پنجره وارد کردن رمز
                        Dialog passwordDialog = new Dialog("Enter Password", skin) {
                            @Override
                            protected void result(Object object) {
                                if (object instanceof Boolean && (Boolean) object) {
                                    String inputPassword = passwordField.getText();
                                    JoinLobbyRequest join = new JoinLobbyRequest(id, player, inputPassword);
                                    Main.getMain().getNetworkManager().sendTCP(join);
                                    this.hide();
                                } else {
                                    this.hide();
                                }
                            }
                        };

                        TextField passwordField = new TextField("", skin);
                        passwordField.setMessageText("Password");

                        passwordDialog.getContentTable().add(passwordField).width(200).pad(10);
                        passwordDialog.button("Join", true);
                        passwordDialog.button("Cancel", false);
                        passwordDialog.show(stage);
                    } else {
                        // لابی public
                        JoinLobbyRequest join = new JoinLobbyRequest(id, player, null);
                        Main.getMain().getNetworkManager().sendTCP(join);
                    }
                }
            });


            lobbyTable.add(lobbyInfo).left().pad(5);
            lobbyTable.add(joinBtn).right().pad(5).row();
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
