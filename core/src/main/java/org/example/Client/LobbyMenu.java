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
import org.example.Main;
import org.example.Server.models.MyGame;

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

        // ==== Create Lobby ====
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

                SimplePlayer simple = new SimplePlayer(MyGame.getCurrentPlayer());
                CreateLobbyRequest request = new CreateLobbyRequest(name, isPrivate, pass, visible, simple);
                Main.getMain().getNetworkManager().sendTCP(request);
                Main.getMain().getNetworkManager().sendTCP(new GetLobbiesRequest(simple));
            }
        });

        // ==== Lobby List ====
        lobbyTable = new Table();
        ScrollPane lobbyScroll = new ScrollPane(lobbyTable, skin);

        TextButton refreshButton = new TextButton("Refresh", skin);
        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SimplePlayer simple = new SimplePlayer(MyGame.getCurrentPlayer());
                Main.getMain().getNetworkManager().sendTCP(new GetLobbiesRequest(simple));
            }
        });

        searchField = new TextField("", skin);
        searchField.setMessageText("Enter lobby ID to join");

        TextButton joinByIdButton = new TextButton("Join by ID", skin);
        joinByIdButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String id = searchField.getText();
                SimplePlayer simple = new SimplePlayer(MyGame.getCurrentPlayer());
                JoinLobbyRequest join = new JoinLobbyRequest(id, simple, null);
                Main.getMain().getNetworkManager().sendTCP(join);
            }
        });

        root.top().pad(20);
        root.add(new Label("Create a Lobby", skin)).colspan(2).padBottom(10).row();
        root.add(lobbyNameField).width(200).pad(5);
        root.add(passwordField).width(200).pad(5).row();
        root.add(privateBox).pad(5);
        root.add(visibleBox).pad(5).row();
        root.add(createButton).colspan(2).pad(10).row();

        root.add(new Label("Available Lobbies:", skin)).colspan(2).padTop(20).row();
        root.add(lobbyScroll).colspan(2).height(300).width(600).padBottom(10).row();

        root.add(refreshButton).pad(5);
        root.add(joinByIdButton).pad(5).row();
        root.add(searchField).colspan(2).width(300).padTop(10);
    }

    public void updateLobbyList(List<Lobby> lobbies) {
        lobbyTable.clear();
        SimplePlayer current = new SimplePlayer(MyGame.getCurrentPlayer());

        for (Lobby lobby : lobbies) {
            String name = lobby.getName();
            int count = lobby.getPlayers().size();
            String admin = lobby.getAdmin().username;
            String id = lobby.getId();

            boolean isMember = lobby.getPlayers().stream().anyMatch(p -> p.username.equals(current.username));
            boolean isAdmin = admin.equals(current.username);

            Label lobbyInfo = new Label(name + " | ID: " + id + " | Players: " + count + " | Admin: " + admin, skin);
            lobbyInfo.setWrap(true);
            lobbyInfo.setWidth(300);

            TextButton joinBtn = new TextButton(isMember ? "Joined" : "Join", skin);
            joinBtn.setDisabled(isMember);
            joinBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!isMember) {
                        if (lobby.isPrivate()) {
                            showPasswordDialog(id, current);
                        } else {
                            JoinLobbyRequest join = new JoinLobbyRequest(id, current, null);
                            Main.getMain().getNetworkManager().sendTCP(join);
                        }
                    }
                }
            });

            TextButton playersBtn = new TextButton("Players", skin);
            playersBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Dialog playersDialog = new Dialog("Players in " + lobby.getName(), skin);
                    Table content = playersDialog.getContentTable();
                    for (SimplePlayer p : lobby.getPlayers()) {
                        content.add(new Label(p.nickname + (p.username.equals(admin) ? " (Admin)" : ""), skin)).row();
                    }
                    playersDialog.button("Close");
                    playersDialog.show(stage);
                }
            });

            TextButton outBtn = new TextButton("Out", skin);
            outBtn.setDisabled(!isMember);
            outBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (isMember) {
                        LeaveLobbyRequest leave = new LeaveLobbyRequest(current);
                        Main.getMain().getNetworkManager().sendTCP(leave);
                    }
                }
            });

            lobbyTable.add(lobbyInfo).width(300).left().pad(5);
            lobbyTable.add(joinBtn).pad(5);
            lobbyTable.add(playersBtn).pad(5);
            lobbyTable.add(outBtn).pad(5).row();
        }
    }

    private void showPasswordDialog(String lobbyId, SimplePlayer player) {
        Dialog passwordDialog = new Dialog("Enter Password", skin) {
            @Override
            protected void result(Object object) {
                if (object instanceof Boolean && (Boolean) object) {
                    String inputPassword = ((TextField) getContentTable().getCells().first().getActor()).getText();
                    JoinLobbyRequest join = new JoinLobbyRequest(lobbyId, player, inputPassword);
                    Main.getMain().getNetworkManager().sendTCP(join);
                }
            }
        };

        TextField pwField = new TextField("", skin);
        pwField.setMessageText("Password");

        passwordDialog.getContentTable().add(pwField).width(200).pad(10);
        passwordDialog.button("Join", true);
        passwordDialog.button("Cancel", false);
        passwordDialog.show(stage);
    }

    @Override
    public void render(float delta) {
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
